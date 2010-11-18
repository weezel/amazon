#!/usr/bin/env python
# -*- coding: utf-8 -*-

import re
from sys import argv, exit, stderr, stdout
from time import time

from Comment import Comment
from strip_html import strip_ml_tags as stripHtmlTags
from urlopener import urlopener


comments = []
cmntCount = 0


### TODO
#   o  add &nbsp; remover to strip_html
#   o  make strip_html error less error prone
#



def getNextPageURL(data):
    """
    @return link as str, None in case of fail

    Page navigation looks like this:
    ------------------------------------------------------
    |  <Previous  |  1 ... 55 [56] 57 ... 152  |  Next>  |
    ------------------------------------------------------
    """
    for line in reversed(data):
        if "<span class=\"paging\">" in line:
            e = line.rfind("|")
            if e < 0:
                return None
            e += 1
            #print "getNextPageURL = %s" % line[e:len(line)-1]
            return parseLinkFromLine(line[e:len(line)-1])
    return None


def parseReviewsStartLine(data):
    """
    @return (linenumber, link) when succeed, None in case of fail
    """
    # Introduction to re.compile line. Will find lines:
    #   "See all 14 customer reviews" or
    #   "See all 1,114 customer reviews"
    links = {} # Use dictionary to avoid duplicates
    searchPat = re.compile(r"See all (\d|,)*\d* customer reviews")
    totalcomments = 0

    if data is None or len(data) < 1:
        return None

    for n, line in enumerate(data):
        if re.search(searchPat, line) is not None:
            tmp = parseLinkFromLine(line)
            #print "tmp [%s]" % tmp
            links[tmp] = (n, tmp) # (lineNro, URL)
    if len(links) is 1:
        return links.values()[0]
    # In case we got several links, correct one will be
    # the one that includes word "helpful"
    if len(links) > 1:
        for key, item in links.iteritems():
            if "helpful" in item[1]:
                return item
    return None


def parseCommentsTotal(line):
    """
    return comment count, -1 when fail
    """
    nmbrPat = re.compile(r"all \d+ customer")

    if line is None or len(line) < 1:
        return -1

    line = line.replace(",", "")
    tmp = re.search(nmbrPat, line)
    if tmp is not None:
        return re.search(r"\d+", tmp.group(0)).group(0)
    return -1


def parsePagesTotal(data):
    """
    Page navigation looks like this:
    ------------------------------------------------------
    |        <Previous  |  1  2  3  |  Next>             |
    ------------------------------------------------------
    or                              ^
             Match against this ---´
    ------------------------------------------------------
    |  <Previous  |  1 ... 55 [56] 57 ... 152  |  Next>  |
    ------------------------------------------------------
    """
    p = re.compile(r"(\d|,)*\d*")

    if data is None or len(data) < 1:
        return -1

    for line in reversed(data):
        if "<span class=\"paging\">" in line:
            i = line.rfind("|")
            j = 0
            tmp = 0
            while i > 1:
                if line[i] is '>':
                    j += 1
                    if j is 2:
                        tmp = line[i+1:line.rfind("|")] # Format now: 3</a>
                        # By using regexp we don't have to clean up the string
                        tmp = re.search(p, tmp)
                        if tmp is not None:
                            tmp = tmp.group(0)
                        else:
                            tmp = 0
                        break
                i -= 1
            return tmp
    return 0


def parseLinkFromLine(s):
    i = 0
    idx_start = 0

    if s is None or len(s) < 1:
        return None

    idx_start = s.find("http")
    if idx_start is -1:
        return None
    i = idx_start
    while i < len(s):
        if s[i] is '>' or s[i] is '"':
            return s[idx_start:i]
        i += 1
    return s[idx_start:i]


def commentsStartStopLineNmbr(data):
    """
    return (begin, end) tuple which contains linenumbers of start/stop of
    comment area.
    """
    begin = 0
    end = 0
    i = 0

    while i < len(data):
        if "<table class=\"CMheadingBar\"" in data[i]:
            if begin is 0:
                begin = i
            else:
                end = i
                break
        i += 1
    return (begin, end)


def commentsStartLines(data, beginEnd):
    """
    return a vector which includes linenumbers where comments starts
    """
    i = 0
    linenumbers = []

    i = beginEnd[0]
    while i < beginEnd[1]:
        if "<!-- BOUNDARY -->" in data[i]:
            linenumbers.append(i)
        i += 1
    return linenumbers


def parseComments(data, cboundaries):
    """
    Tries to parse comments and set them to global vector 'comments'
    """
    cbidx = 1
    global comments
    i = cboundaries[0]
    comm = Comment()

    if data is None or len(data) < 0 or \
        cboundaries is None or len(cboundaries) < 0:
            return

    while i < cboundaries[cbidx]:
        # Comment boundary hit. New comment will start in the next line.
        if i >= cboundaries[cbidx]-1:
            comments.append(comm)
            comm = Comment()
            #print "\n#########################################"
            # cbidx is used to move inside the boundary values vector
            cbidx += 1
            # No more comments remaining
            if cbidx >= len(cboundaries):
                break
            #print data[i]
        ### Parse comment name/ID
        if "<a name=" in data[i] and len(comm.name) < 1:
            line = data[i]
            comm.name = line[line.find("\"")+1:line.rfind("\"")].rstrip("\r\n")
        ### Parse helpfulness
        # FIXME make better tag matcher
        if "helpful" in data[i] and len(comm.helpful) < 1:
            tmp = data[i]
            tmp = re.sub("^\s+", "", tmp)
            comm.helpful = stripHtmlTags(tmp).rstrip("\r\n").rstrip(":")
            #print "comm.helpful = '%s'" % comm.helpful
        ### Parse stars
        if "<span class=\"swSprite s_star_" in data[i] and len(comm.stars) < 1:
            tmp = stripHtmlTags(data[i]).rstrip(" \n").rstrip("\n")
            tmp = re.sub("^\s+", "", tmp)
            #print "comm.stars = '%s'" % comm.stars
            comm.stars = tmp
        i += 1
        ### Header has bolding
        if "<span style=\"vertical-align:middle;\"><b>"  in data[i-1]:
            tmp = stripHtmlTags(data[i-1])
            comm.header = re.sub("^\s+", "", tmp).rstrip("\n")
            #print "comm.header = %s" % comm.header

            ### Get comments FIXME
            ii = i
            endingCmp = ">Help other customers find the most helpful reviews</b>"
            tmp = []
            # Rewind to the correct position
            while ii < cboundaries[cbidx]-1:
                if "<span class=\"cmtySprite s_BadgeRealName \">" in data[ii] or \
                   ">What's this?</a>)</span>" in data[ii] or \
                   ">See all my reviews</a></div>" in data[ii] or \
                   "This is review from" in data[ii] or \
                   "REAL NAME" in data[ii]:
                    i = ii + 1
                ii += 1
            # Parse comment
            while i < cboundaries[cbidx]-1:
                # This means that we can stop parsing comment
                if endingCmp in data[i]:
                    break
                try:
                    k = stripHtmlTags(data[i])
                    tmp.append(k)
                except:
                    pass
                i += 1
            # Thanks Amazon for the nice mark up -> CLEAN IT UP
            p = re.compile(r"\s+")
            r = 0
            while r < len(tmp):
                if re.search(p, tmp[r]) != None:
                    # Multiple spaces to single space
                    tmp[r] = re.sub(p, " ", tmp[r])
                    if re.search(r"^\s", tmp[r]) != None or tmp[r] is '':
                        tmp.pop(r)
                        continue
                if tmp[r] == "" or re.search(r"^\n", tmp[r]) != None:
                    tmp.pop(r)
                    continue
                r += 1
            comm.comment = tmp


def estimatedTimeOfArrival(t, pagesProcessed, pageCount):
    timePassed = time() - t

    if pagesProcessed is 0:
        return 0
    avg = timePassed/pagesProcessed
    return int((pageCount - pagesProcessed) * avg)


# Main function that binds everything together
def main():
    cboundaries = [] # Comment boundaries
    cmntTotal = 0
    global cmntCount
    global comments
    pageCount = 1
    pagesTotal = 0
    revStarts = 0

    if len(argv) < 2:
        amazonurl = "data2.html" # Example file
        #amazonurl = "http://www.amazon.com/Sennheiser-CX300-B-In-Ear-Stereo-Headphone/product-reviews/B000E6G9RI/ref=cm_cr_pr_link_prev_149?ie=UTF8&showViewpoints=0&pageNumber=150"
    else:
        amazonurl = argv[1]

    data = urlopener(amazonurl) # Read data
    if data is None:
        print "Zero data"
        exit(1)

    # Determine where the reviews start
    revStarts = parseReviewsStartLine(data) # Returns (lineNmbr, link)
    # Line number of where the "See all %d comments" is
    commentsLineNro = int(revStarts[0])
    cmntTotal = int(parseCommentsTotal(data[commentsLineNro]))

    if cmntTotal is -1:
        print "Cannot parse comment count -> malfunction"
        exit(2)
    if revStarts is None:
        print "Cannot determine where the comment area is"
        exit(3)

    data = urlopener(revStarts[1])
    timePassed = time()

    while getNextPageURL(data):
        nextPage = getNextPageURL(data)
        data = urlopener(nextPage)
        if pageCount  == 1:
            pagesTotal = int(parsePagesTotal(data))
        # PARSE COMMENTS IN HERE
        cboundaries = [] # Remember to flush
        tmpbndr = commentsStartStopLineNmbr(data)
        cboundaries.extend(commentsStartLines(data, tmpbndr))
        # Parse comments
        parseComments(data, cboundaries)
        cmntCount = len(comments)
        # Append end line number of a comment area to comment boundaries
        cboundaries.append(tmpbndr[1])
        printable = "Comment: %s/%s   Page: %s/%s   [ETA: %d sec]\n" % (cmntCount, cmntTotal,\
                pageCount, pagesTotal, estimatedTimeOfArrival(timePassed,\
                        pageCount, pagesTotal))
        stderr.write(printable)
        stdout.flush()
        pageCount += 1

    # Finally print all the comments
    for comment in comments:
        print "---",
        comment.printAll()
    # Check whether we have gone through all pages
    if pageCount != pagesTotal:
        print "PAY ATTENTION!"
        print "Fetched data not in consistent state. Pages fetched %d/%d" % (pageCount, pagesTotal)
        exit(5)


if __name__ == "__main__":
    main()
