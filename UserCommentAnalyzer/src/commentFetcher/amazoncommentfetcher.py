#!/usr/bin/env python
# -*- coding: utf-8 -*-

import re
import os
from sys import argv, exit, stderr, stdout
from time import time

from Comment import Comment
from strip_html import strip_ml_tags as stripHtmlTags
from urlopener import urlopener


comments = []
cmntCount = 0
fileName = "comments.txt"
fileOut = 0


# FIXME -*- approach comment parsing from the different angle: use reverse, luke!

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


def parseCommentsTotalCount(line):
    """
    return comment count, -1 when fail
    """
    nmbrPat = re.compile(r"all (\d|,)*\d* customer")

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
    return -1


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

    if data is None or len(data) < 1:
        return

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

    if data is None or data < 1:
        return

    i = beginEnd[0]
    while i < beginEnd[1]:
        if "<!-- BOUNDARY -->" in data[i]:
            linenumbers.append(i)
        i += 1
    return linenumbers


def parseComments(data, cboundaries):
    """
    return True on success, False on failure
    Parse comments and sets them to global vector 'comments'
    """
    cbidx = 1 # cbidx will be used to move inside the boundary values vector
    global comments
    comm = Comment()
    pHelpfull = r"(\d|,)*\d* of (\d|,)*\d* people found the following review helpful:"

    if data is None or len(data) < 1 or \
        cboundaries is None or len(cboundaries) < 1:
            return False

    i = cboundaries[0]
    while i <= cboundaries[cbidx]:
        ### Parse comment name/ID
        if "<a name=" in data[i] and len(comm.name) < 1:
            line = data[i]
            comm.name = line[line.find("\"")+1:line.rfind("\"")].rstrip("\r\n")

        ### Parse stars
        if "<span class=\"swSprite s_star_" in data[i] and len(comm.stars) < 1:
            tmp = stripHtmlTags(data[i]).rstrip(" \n").rstrip("\n")
            tmp = re.sub("^\s+", "", tmp)
            comm.stars = tmp

        ### Header has bolding
        if "<span style=\"vertical-align:middle;\"><b>" in data[i]:
            tmp = stripHtmlTags(data[i])
            comm.header = re.sub("^\s+", "", tmp).rstrip("\n")

            ### Get comments
            ii = i
            endingCmp = ">Help other customers find the most helpful reviews</b>"
            tmp = []

            ### Rewind to the correct position
            # FIXME -*- approach from the different angle: use reverse, luke!
            #while ii < cboundaries[cbidx]-1:
            #    if "<span class=\"cmtySprite s_BadgeRealName \">" in data[ii] or \
            #       ">What's this?</a>)</span>" in data[ii] or \
            #       ">See all my reviews</a></div>" in data[ii] or \
            #       "This is review from" in data[ii] or \
            #       "REAL NAME" in data[ii]:
            #        i = ii + 1
            #    ii += 1

            ### Parse comment
            while i+1 <= cboundaries[cbidx]:
                # This means that we can stop parsing comment
                if endingCmp in data[i]:
                    break
                try:
                    k = stripHtmlTags(data[i])
                    tmp.append(k)
                except:
                    pass
                i += 1
            comm.comment = cleanUpComment(tmp)

        # Comment boundary hit. New comment will start in the next line.
        if i >= cboundaries[cbidx]:
            comments.append(comm)
            ### Append to file and flush buffers
            writeToFile() # fileName and comments[] are global
            comm = Comment()
            ### end of flush
            i = cboundaries[cbidx]
            cbidx += 1
            # No more comments remaining
            if cbidx >= len(cboundaries):
                return True
        i += 1

    return False


def cleanUpComment(c):
    """ return cleaned up comment, '' if fail. """
    p = re.compile(r"\s+")
    r = 0

    if c is None or len(c) < 1:
    	return ''

    # Thanks Amazon for the nice mark up -> CLEAN IT UP!
    while r < len(c):
        # Multiple spaces to single space
        if re.search(p, c[r]) != None:
            c[r] = re.sub(p, " ", c[r])
            if re.search(r"^\s", c[r]) != None or c[r] is '':
                c.pop(r)
                continue
        if c[r] is "" or re.search(r"^\n", c[r]) != None:
            c.pop(r)
            continue
        if "---" in c[r]:
            c.pop(r)
            continue
        # FIXME -*- please use tag matcher instead of this glue
        if re.search(r"^\(REAL NAME\)", c[r]) != None:
            c[r] = re.sub(r"^\(REAL NAME\)", "", c[r])
            continue
        r += 1
    return c


def writeToFile():
    if fileName is None or len(fileName) < 0:
        print "Bogus filename '%s'" % fileName
        exit(100)
    if len(comments) < 1:
        return

    c  = comments[len(comments)-1]
    fileOut.write(c.__repr__()) # Write name, helpfulness etc.
    fileOut.writelines(c.comment)
    fileOut.write("\n---")


def estimatedTimeOfArrival(t, pagesProcessed, pageCount):
    timePassed = time() - t

    if pagesProcessed is 0:
        return 0
    avg = timePassed/pagesProcessed
    return (int(pageCount) - int(pagesProcessed)) * avg


# Main function that binds everything together
def main():
    cboundaries = [] # Comment boundaries
    cmntTotal = pageTotal = revStarts = 0
    global cmntCount, comments, fileName, fileOut
    pageCount = 1

    if len(argv) == 1:
        amazonurl = str(raw_input())
    if len(argv) == 2:
        amazonurl = argv[1]
    elif len(argv) >= 3:
        amazonurl = argv[1]
        fileName = argv[2]

    data = urlopener(amazonurl) # Read data
    if data is None:
        print "Zero data"
        exit(1)

    ### Determine where the reviews starts
    revStarts = parseReviewsStartLine(data) # Returns (lineNmbr, link)
    if revStarts is None:
    	print "\nAre you sure you gave the front page of the product?"
    	exit(10)
    ### Line number where the "See all %d comments" is
    commentsLineNro = int(revStarts[0])
    cmntTotal = int(parseCommentsTotalCount(data[commentsLineNro]))

    if cmntTotal < 0:
        print "No reviews"
        exit(0)
    if revStarts is None:
        print "Cannot determine where the comment area is"
        exit(3)

    data = urlopener(revStarts[1])
    timePassed = time()

    # FILEOPEN
    try:
        if os.path.isfile(fileName):
            os.remove(fileName)
        fileOut = open(fileName, "a")
    except:
        print "Cannot write to file =  %s" % fileName

    try:
        # Will do the following:
        #    o  cboundaries has linenumbers of where the comments starts
        #    o  tmpbndr includes begin and end linenumbers of the comment area
        nextPage = getNextPageURL(data)
        while True:
            if pageCount is 1:
                pagesTotal = parsePagesTotal(data)
                if nextPage is None:
                    pagesTotal = 1
            cboundaries = [] # Remember to flush
            tmpbndr = commentsStartStopLineNmbr(data)
            cboundaries.extend(commentsStartLines(data, tmpbndr))
            cboundaries.append(tmpbndr[1])
            # Parse comments
            if parseComments(data, cboundaries) is False:
                print "\nFAILURE! Cannot parse comments"
                exit(20)
            cmntCount = len(comments)
            # Append end line number of a comment area to comment boundaries
            cboundaries.append(tmpbndr[1])
            printable = "Comment: %s/%s   Page: %s/%s   [ETA: %d sec]\n" % (cmntCount, cmntTotal,\
                    pageCount, pagesTotal, estimatedTimeOfArrival(timePassed,\
                            pageCount-1, pagesTotal))
            stderr.write(printable)
            # Prepare to move on the next page
            nextPage = getNextPageURL(data)
            if nextPage is None:
                break
            data = urlopener(nextPage)
            pageCount += 1
    except:
        fileOut.close()
    finally:
        timePassed = time() - timePassed
        stderr.write("Operation took %.2d:%.2d minute(s)" % (timePassed/60, timePassed%60))
        fileOut.close()

    # Check whether we have gone through all pages
    if int(pageCount) != int(pagesTotal):
        print "\nPAY ATTENTION!"
        print "Fetched data not in consistent state. Pages fetched %s/%s" % (pageCount-1, pagesTotal)
        exit(5)

    return 0


if __name__ == "__main__":
    main()
