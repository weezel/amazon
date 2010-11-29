#!/usr/bin/env python
# -*- coding: utf-8 -*-

import mimetypes
import os
import shutil
import sys


if len(sys.argv) < 2:
    print "Give directory as parameter"
    sys.exit(1)


mimetypes.init()
# These extensions will be skipped
mimetypes.add_type("text/properties", ".properties")
mimetypes.add_type("text/swp", ".swp")
mimetypes.add_type("text/mf", ".mf")


def visitDir(args, dirname, filenames):
    global mimetypes

    # Do not process .git directory
    if ".git" in dirname:
        return
    for f in filenames:
        ff = os.path.join(dirname, f)
        m = mimetypes.guess_type(ff)[0]

        if os.path.isfile(ff) and \
                m is not None and \
                "text" in m:
            #print "%s  filetype: %s" % (ff, m)
            try:
                changed = False
                outfile = None
                tmp = []

                with open(ff) as fh:
                    for i in fh:
                        if "\r\n" in i or "\r\t\n" in i and changed is False:
                            changed = True
                        tmp.append(i.replace("\r\n", "\n").replace("\r\t\n", "\n"))
                if changed:
                    print "+  %s" % ff
                    outfile = open(ff, "w")
                    outfile.writelines(tmp)
                    outfile.close()
            except:
                print "Could not write file: %s" % os.path.join(dirname, f)


def main():
    sys.stderr.write("Modified files:\n")
    os.path.walk(sys.argv[1], visitDir, None)


if __name__ == '__main__':
    main()

