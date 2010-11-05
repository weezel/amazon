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


def getNextPageURL(data):
    """
    @return None in case of fail, str for the link if success

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
    @return (linenumber, link) when succeed
    @return None in case of fail
    """
    # Introduction to re.compile line. Will find lines:
    # "See all 14 customer reviews" or
    # "See all 1,114 customer reviews"
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
    return -1 when failed, else comments count
    """
    nmbrPat = re.compile(r"all (\d|,)*\d* customer")

    if line is None or len(line) < 1:
        return -1

    tmp = re.search(nmbrPat, line)
    if tmp is not None:
        tmp = tmp.group(0).replace(",", "")
        return re.search(r"(\d+)", tmp).group(0)
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


def parseComments(data):
    """Collect comments from a page and return as a list"""
    global cmntCount
    n = 0
    tableTagsHit = 0

    while n < len(data):
        line = data[n]
        # Comment starts
        if "<!-- BOUNDARY -->" in line:
            cmntCount += 1
            comm = Comment()
            # Comment section ends with </table>
            line = data[n]
            if "<a name=\"" in line:
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
        amazonurl = "data2.html"
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
    revStarts = parseReviewsStartLine(data) # Returns (lineNmbr, link)
    commentsLineNro = int(revStarts[0])
    cmntTotal = int(parseCommentsTotal(data[commentsLineNro]))

    if cmntTotal is -1:
        print "Cannot parse comment count -> malfunction"
        exit(2)
    if revStarts is None:
        print "Cannot determine where the comments starts"
        exit(3)

    data = urlopener(revStarts[1])
    global cmntCount
    pageCount = 1
    pagesTotal = 0
    timePassed = time()
    while getNextPageURL(data):
        nextPage = getNextPageURL(data)
        #print getNextPageURL(data)
        data = urlopener(nextPage)
        if pageCount  == 1:
            pagesTotal = int(parsePagesTotal(data))
        # PARSE COMMENTS IN HERE
        #comments.extend(parseComments(data))
        parseComments(data)
        #cmntCount = len(comments)
        print "Comment: %s/%s   Page: %s/%s   [ETA: %d sec]\n" % (cmntCount, cmntTotal,\
                pageCount, pagesTotal, estimatedTimeOfArrival(timePassed,\
                        pageCount, pagesTotal)),
        stdout.flush()
        pageCount += 1
    # Check whether we have gone through all pages
    if pageCount < pagesTotal:
        print "Fetched data not in consistent state. Aborting"
        exit(5)


if __name__ == "__main__":
    main()
