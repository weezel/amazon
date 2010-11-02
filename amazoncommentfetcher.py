#!/usr/bin/env python
# -*- coding: utf-8 -*-

import re
from sys import argv, exit, stdout
from time import time

from Comment import Comment
from strip_html import strip_ml_tags as stripHtmlTags
from urlopener import urlopener


comments = []


def getNextPageURL(data):
    #<span class="paging">&lsaquo; Previous |    <span class="on">1</span> <a href="http://www.amazon.com/Introduction-Algorithms-Third-Thomas-Cormen/product-reviews/0262033844?pageNumber=2" >2</a>
    #| <a href="http://www.amazon.com/Introduction-Algorithms-Third-Thomas-Cormen/product-reviews/0262033844?pageNumber=2" >Next &rsaquo;</a> </span>
    r = None
    for line in reversed(data):
        if "<span class=\"paging\">" in line:
            line = line.split("|")
            line = line[::-1]
            try:
                r = parseLinkFromLine(line[0].split("Next")[0])
            except:
                r = None
    return r


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
        if re.search(searchPat, line) != None:
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


def parseCommentCount(line):
    nmbrPat = re.compile(r"all (\d|,)*\d* customer")

    if line is None or len(line) < 1:
        return -1

    tmp = re.search(nmbrPat, line)
    if tmp != None:
        tmp = tmp.group(0).replace(",", "")
        return re.search(r"(\d+)", tmp).group(0)
    return -1


def parsePageCount(data):
    if data is None or len(data) < 1:
        return -1

    try:
        for line in reversed(data):
            if "<span class=\"paging\">" in line:
                tmp = line.split("&hellip") # Split from '...'
                tmp = tmp[1].split(">") # Page number will be in tmp[1]
                tmp = tmp[1].split("<") # Remove "</a"
                return tmp[0].replace(",", "")
    except:
        return -1
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


def parseComments(data):
    """Collect comments from a page and return as a list"""
    comments = []
    n = 0
    tableTagsHit = 0

    while n < len(data):
        line = data[n]
        # Comment starts
        if "<!-- BOUNDARY -->" in line:
            comm = Comment()
            # Comment section ends with </table>
            while "</table>" not in line or n < len(data):
                line = data[n]
                if "<a name=\"" in line:
                    #print "Comment\nName: %s, %d" % (type(line), len(line))
                    comm.name = stripHtmlTags(line)
                    #print "Comment\nName: %s" % comm.name
                    comments.append(comm)
                # Actually there is a table and two tables inside it. Thus
                # 3rd </table> tag
                if "</table>" in line:
                    tableTagsHit += 1
                    if tableTagsHit >= 3:
                        tableTagsHit = 0
                        break
                n += 1
        n += 1
    return comments


def estimatedTimeOfArrival(t, pagesProcessed, pageCount):
    timePassed = time() - t

    if pagesProcessed is 0:
        return 0
    avg = timePassed/pagesProcessed
    return float((pageCount - pagesProcessed) * avg)


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
    cmntTotal = int(parseCommentCount(data[commentsLineNro]))

    if cmntTotal is -1:
        print "Cannot parse comment count -> malfunction"
        exit(2)
    if revStarts is None:
        print "Cannot determine where the comments starts"
        exit(3)

    data = urlopener(revStarts[1])
    comments = []
    cmntCount = 0
    pageCount = 0
    pagesTotal = 0
    timePassed = time()
    while getNextPageURL(data):
        nextPage = getNextPageURL(data)
        #print getNextPageURL(data)
        data = urlopener(nextPage)
        if pageCount  == 0:
            pagesTotal = int(parsePageCount(data))
        # PARSE COMMENTS IN HERE
        comments.extend(parseComments(data))
        cmntCount = len(comments)
        print "\rComment: %d/%d   Page: %d/%d   [ETA: %d sec]" % (cmntCount, cmntTotal,\
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
