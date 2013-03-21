#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Copyright (c) 2013 Ville Valkonen <weezelding[at]gmail.com>

Permission to use, copy, modify, and distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
"""

import os
import re
import threading
import time
from Queue import Queue
from sys import argv, exit, stderr, stdout

from Comment import Comment
from strip_html import strip_ml_tags as stripHtmlTags
from urlopener import urlopener

comments = []
linklist = []
filename_out = "comments.txt"
fetchqueue = Queue()
MAX_THREADS = 30
cnt = 0

class Fetcher(threading.Thread):
    def __init__(self, q):
        threading.Thread.__init__(self)

        self.fetchqueue = q

    def run(self):
        global cnt

        while True:
            url = self.fetchqueue.get()
            data = urlopener(url)
            parseComments(data)
            self.fetchqueue.task_done()
            textout = "[%3d / %d] completed\n" % (cnt + 1, len(linklist))
            stderr.write(textout)
            cnt += 1

def parseCommentsTotalCount(data):
    """
    return comment count, -1 when fail
    """
    p = re.compile(r"\d+ Reviews")

    for line in data:
        line = line.replace(",", "")
        match = re.search(p, line)
        if match != None:
            getNmbr = match.group().split(" ")
            return int(getNmbr[0])
    return -1

def parsePagesTotal(data):
    """
    Page navigation looks like this:
    ------------------------------------------------------
    |        <Previous  |  1  2  3  |  Next>             |
    ------------------------------------------------------
    or
    ------------------------------------------------------
    |  <Previous  |  1 ... 55 [56] 57 ... 152  |  Next>  |
    ------------------------------------------------------
                                               ^
             Match against this --------------´

    @return tuple (pagestotal, baselink with &pageNumber=)
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
                        tmp = line[i + 1 : line.rfind("|")] # Format now: 3</a>
                        tmp = re.search(p, tmp)
                        if tmp is not None:
                            tmp = tmp.group(0)
                        else:
                            tmp = 0
                        break
                i -= 1
            link = line.split("\"")
            link = link[len(link) - 2]
            return (int(tmp), link)
    return -1

def generatePageNumberLink(link, pagenumber):
    p = re.compile("pageNumber=\d+")
    return re.sub(p, "pageNumber=%d" % pagenumber, link)

def commentsStartStopLineNmbr(data):
    """
    return (begin, end) tuple which contains linenumbers of start/stop of
    comment area.
    """
    begin = 0
    end = 0
    i = 0

    if data is None or len(data) < 1:
        return None

    while i < len(data):
        if "<table class=\"CMheadingBar\"" in data[i]:
            if begin is 0:
                begin = i
            else:
                end = i
                break
        i += 1
    return (int(begin), int(end))

def parseComments(data):
    """
    Parse comments from site
    """
    global comments
    reviewBegins = '<div style="margin-left:0.5em;">'
    reviewEnds = '<div style="padding-top: 10px; clear: both; width: 100%;">'
    stars_line = 'margin-right:5px;'
    stars = re.compile('\d+.\d+ out of 5 stars')
    header_line = '<span style="vertical-align:middle;"'
    helpful_line ='people found the following review helpful'
    helpful = re.compile('\d+ of \d+ people found the following review helpful')
    reviewText = '<span class="h3color tiny">' # Actual review

    boundaries = commentsStartStopLineNmbr(data)
    for i in range(boundaries[0], boundaries[1] + 1):
        if reviewBegins in data[i]:
            curcomment = Comment()
            while reviewEnds not in data[i]:
                # Parse stars
                if stars_line in data[i]:
                    stars_found = re.search(stars, data[i])
                    if stars_found != None:
                        curcomment.stars = stars_found.group()
                # Parse header
                elif header_line in data[i]:
                    line = data[i]
                    begin = line.find('<b>') + 3
                    end = line.find('</b>')
                    curcomment.header = line[begin : end]
                # Parse helpfulness
                elif helpful_line in data[i]:
                    helpful_found = data[i].replace(",", "")
                    helpful_found = re.search(helpful, helpful_found)
                    if helpful_found != None:
                        curcomment.helpful = helpful_found.group()
                # Parse body text
                elif reviewText in data[i]:
                    i += 3
                    if '<span class="small"' in data[i]: # Yep, dirty trick :(
                        i += 3
                    data[i] = stripHtmlTags(data[i])
                    curcomment.comment = re.sub("\s+", " ", data[i])
                i += 1
            #comments.append(curcomment.getonelinecomment())
            comments.append(curcomment.__repr__())

def estimatedTimeOfArrival(t, pagesProcessed, pageCount):
    timePassed = time.time() - t

    if pagesProcessed is 0:
        return 0
    avg = timePassed/pagesProcessed
    return (int(pageCount) - int(pagesProcessed)) * avg

def generatePageLinks(link, pagesTotal):
    """
    Generate a list of comment page links that can be feed for QueueWorker
    """
    return [generatePageNumberLink(link, cnt) for cnt in range(1, pagesTotal + 1)]


# Main function that binds everything together
def main(url):
    global comments, linklist

    data = urlopener(url)
    if data is None:
        print "Zero data"
        exit(1)

    commentcount = parseCommentsTotalCount(data)
    if commentcount == -1:
        print "No customer reviews available or not in the reviews page"
        print "(or an ugly malfunction)"
        exit(1)
    elif commentcount <= 10:
        commentarea = commentsStartStopLineNmbr(data) # returns (start, end) of comments area
        # Only one page
        if commentarea != None:
            parseComments(data)
            return

    totalcommPages, baseUrl = parsePagesTotal(data) # returns (pagecount, lastpageurl)
    if totalcommPages == -1:
        print "That link surely is a comment page?"
        exit(1)
    linklist = generatePageLinks(baseUrl, totalcommPages)

    # Feed links to our Fetcher daemon
    for i in range(MAX_THREADS):
        t = Fetcher(fetchqueue)
        t.setDaemon(True)
        t.start()
    for url in linklist:
        fetchqueue.put(url)
    fetchqueue.join()

    return 0

def write2File(comments):
    with open(filename_out, 'w') as f:
        for comment in comments:
            f.write(comment)
            f.write("---")

if __name__ == "__main__":
    if len(argv) == 1:
        amazonurl = str(raw_input('> '))
    if len(argv) == 2:
        amazonurl = argv[1]
    elif len(argv) >= 3:
        amazonurl = argv[1]

    # Don't show that silly banner, we are not going to use it anyway
    if "&showViewpoints=1" in amazonurl:
        amazonurl = amazonurl.replace("&showViewpoints=1", "&showViewpoints=0")

    starttime = time.time()
    main(amazonurl)
    time.sleep(0.5)
    textout = "It took %.2f sec to fetch %d comments (%d pages)\n" % \
            (time.time() - starttime, len(comments), len(linklist))
    stderr.write(textout)

    stderr.write("Writing comments to file %s\n" % (filename_out))
    write2File(comments)

