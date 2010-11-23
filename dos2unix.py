#!/usr/bin/env python
# -*- coding: utf-8 -*-

import os, shutil, sys

if len(sys.argv) < 1:
    print "Give directory as parameter"
    sys.exit(1)


for dpath, dirs, files in os.walk(sys.argv[1]):
    # Don't visit .git
    if ".git" in dirs:
        dirs.remove(".git")
    for f in files:
        # Do not touch binary files
        if ".png" in f or ".pyc" in f:
            continue

        if len(dpath) is 1:
            print "%s" % f
        else:
            strpd = dpath.replace("./", "")
            f = "%s/%s" % (strpd, f)
            changed = False
            tmp = []
            with open(f) as ff:
                for i in ff:
                    if "\r\n" in i or "\r\t\n" in i:
                        changed = True
                    tmp.append(i.replace("\r\n", "\n").replace("\r\t\n", "\n"))
                if changed:
                    try:
                        outfile = open(ff, "w")
                        outfile.close()
                        outfile = open(ff, "a")
                        for i in tmp:
                            outfile.write(i)
                    except:
                        print "Could not write to %s" % ff
                    finally:
                        if outfile is not None:
                            outfile.close()
