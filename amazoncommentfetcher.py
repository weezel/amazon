#!/usr/bin/env python
# -*- coding: utf-8 -*-

import re
from sys import argv, exit, stdout
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


def commentStartLines(data, beginEnd):
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


def printComment(data, cboundaries):
    """
    """
    # FIXME Check NULL values and boundaries more carefully
    cbidx = 1
    i = cboundaries[0]

    # FIXME Determine comment changes and clean HTML out
    comm = Comment()
    while i < cboundaries[cbidx]:
        # Comment boundary hit. New comment will start in the next line.
        if i >= cboundaries[cbidx]-1:
            comments.append(comm)
            comm = Comment()
            print "\n#########################################"
            # cbidx is used to move inside the boundary values vector
            cbidx += 1
            # No more comments remaining
            if cbidx >= len(cboundaries):
                break
        #print data[i]
        # Parse comment name/ID
        if "<a name=" in data[i] and len(comm.name) < 1:
            line = data[i]
            comm.name = line[line.find("\"")+1:line.rfind("\"")].rstrip("\r\n")
        # Parse helpfulness
        # FIXME make better tag matcher
        if "helpful" in data[i] and len(comm.helpful) < 1:
            tmp = data[i]
            tmp = re.sub("^\s+", "", tmp)
            comm.helpful = stripHtmlTags(tmp).rstrip("\r\n").rstrip(":")
            #print "comm.helpful = '%s'" % comm.helpful
        # Parse stars
        if "<span class=\"swSprite s_star_" in data[i] and len(comm.stars) < 1:
            tmp = stripHtmlTags(data[i]).rstrip(" \n").rstrip("\n")
            tmp = re.sub("^\s+", "", tmp)
            #print "comm.stars = '%s'" % comm.stars
            comm.stars = tmp
        i += 1
        # Header has bolding
        if "<span style=\"vertical-align:middle;\"><b>"  in data[i-1]:
            tmp = stripHtmlTags(data[i-1])
            comm.header = re.sub("^\s+", "", tmp).rstrip("\n")
            #print "comm.header = %s" % comm.header

            # Get comments FIXME
            tmp = []
            while i < cboundaries[cbidx]-1:
                if ">Report abuse</a>" in data[i]:
                    break
                try:
                    k = stripHtmlTags(data[i])
                    tmp.append(k)
                except:
                    pass
                i += 1
            comm.comment = tmp
            continue

    #XXX REMOVE BELOW
    for i in comments:
        print "#*#**##*#*#*#**",
        i.printAll()
    #XXX REMOVE ABOVE


def parseComments(data):
    # ------ THIS FUNCTION IS DEPRECATED AND WILL BE ABANDONED -----
    """Collect comments from a page and return as a list"""
    global cmntCount
    n = 0
    tableTagsHit = 0

    while n < len(data):
        line = data[n]
        # Comment starts
        if "<!-- BOUNDARY -->" in data[n]:
            cmntCount += 1
            comm = Comment()
            # Comment section ends with </table>
            # Comment ID
            print "line[%s]" % data[n]
            if "<a name=" in line:
                print "WE ARE HERE1"
            if "<a name=\"" in line and len(comm.comment) is 0:
                print "WE ARE HERE"
                #print "Comment\nName: %s, %d" % (type(line), len(line))
                comm.name = line[line.find("\"")+1:line.rfind("\"")]
                #print "line = %s" % line
                #print "comm.name = %s" % comm.name
                #comm.name = stripHtmlTags(line)
                #print "Comment\nName: %s" % comm.name
            if "helpful" in line:
                # FIXME get rid of from this:
                # comm.helpful = class="crVotingButtons">Was this review helpful to you?&nbsp;Yes
                comm.helpful = stripHtmlTags(line)
                #print "comm.helpful = %s" % comm.helpful,
            # Header has bolding
            if "</b> <br />" in line:
                comm.header = stripHtmlTags(line)
                cmntStarts = n+1
                # And then starts the comment
                print "data[%d] = %s" % (n, data[n])
                while "<div class>" not in data[n] or n < len(data):
                    n += 1
                comm.comment = data[cmntStarts:n]
            # Actually there is a table and two tables inside it. Thus
            # 3rd </table> tag
            if "</table>" in line:
                tableTagsHit += 1
                #if tableTagsHit is 2:
                #    comm.comment = data[n+3]
                if tableTagsHit >= 3:
                    comments.append(comm)
                    #print "len(comments) = %d" % len(comments)
                    tableTagsHit = 0
                    #print "COMM = %s" % comm
                    break
            #n += 1
        n += 1


def estimatedTimeOfArrival(t, pagesProcessed, pageCount):
    timePassed = time() - t

    if pagesProcessed is 0:
        return 0
    avg = timePassed/pagesProcessed
    return int((pageCount - pagesProcessed) * avg)


def main():
    #amazonurl = "http://www.amazon.com/Introduction-Algorithms-Third-Thomas-Cormen/dp/0262033844/ref=sr_1_1?s=books&ie=UTF8&qid=1287355312&sr=1-1"
    #url = "http://www.amazon.com/Introduction-Algorithms-Third-Thomas-Cormen/dp/0262033844/ref=sr_1_1?s=books&ie=UTF8&qid=1287355312&sr=1-1"
    #url = """<span class="small"><b><b class="h3color">&rsaquo;</b>&nbsp;<a href="http://www.amazon.com/Introduction-Algorithms-Third-Thomas-Cormen/product-reviews/0262033844/ref=cm_cr_dp_all_recent?ie=UTF8&showViewpoints=1&sortBy=bySubmissionDateDescending" >See all 15 customer reviews...</a></b></span>
    #"""
    if len(argv) < 2:
        #amazonurl = "data2.html"     XXX remove latter when done
        amazonurl = "comments.html"
    else:
        amazonurl = argv[1]

    data = urlopener(amazonurl)
    if data is None:
        print "Zero data"
        exit(1)

    # Where the comments starts
    # -> get URL
    # -> while getNextPageURL
    # -> parse comments
    # -> move to next page
    #XXX revStarts = parseReviewsStartLine(data) # Returns (lineNmbr, link)
    #XXX commentsLineNro = int(revStarts[0])
    #XXX cmntTotal = int(parseCommentsTotal(data[commentsLineNro]))

    #XXX if cmntTotal is -1:
    #XXX     print "Cannot parse comment count -> malfunction"
    #XXX     exit(2)
    #XXX if revStarts is None:
    #XXX     print "Cannot determine where the comments starts"
    #XXX     exit(3)

    #XXX data = urlopener(revStarts[1])
    global cmntCount
    pageCount = 1
    pagesTotal = 0
    timePassed = time()

    #parseComments(data)
    bzzt = commentsStartStopLineNmbr(data)
    print "commentsStartStopLineNmbr(data) = %s" % str(bzzt)
    boundaries = []
    #boundaries.append(bzzt[0])
    boundaries.extend(commentStartLines(data, bzzt))
    boundaries.append(bzzt[1])
    print "boundaries = %s" % boundaries
    
    printComment(data, boundaries)

    #XXX while getNextPageURL(data):
    #XXX     nextPage = getNextPageURL(data)
    #XXX     #print getNextPageURL(data)
    #XXX     data = urlopener(nextPage)
    #XXX     if pageCount  == 1:
    #XXX         pagesTotal = int(parsePagesTotal(data))
    #XXX     # PARSE COMMENTS IN HERE
    #XXX     #comments.extend(parseComments(data))
    #XXX     parseComments(data)
    #XXX     #cmntCount = len(comments)
    #XXX     print "Comment: %s/%s   Page: %s/%s   [ETA: %d sec]\n" % (cmntCount, cmntTotal,\
    #XXX             pageCount, pagesTotal, estimatedTimeOfArrival(timePassed,\
    #XXX                     pageCount, pagesTotal)),
    #XXX     stdout.flush()
    #XXX     pageCount += 1
    #XXX # Check whether we have gone through all pages
    #XXX if pageCount < pagesTotal:
    #XXX     print "Fetched data not in consistent state. Aborting"
    #XXX     exit(5)


if __name__ == "__main__":
    main()
