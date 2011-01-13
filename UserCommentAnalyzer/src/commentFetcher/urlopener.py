def urlopener(url):
    # TODO user agent randomizer
    if "http://" in url:
        import urllib

        class MyOpener(urllib.FancyURLopener):
            version = "Amazon Comment Crawler"
            #version = "Mozilla/5.0"
        myopener = MyOpener()
        try:
            f = myopener.open(url)
            contents = f.readlines()
            f.close()
            return contents
        except:
            print "Cannot fetch http data from %s" % url
            return None
    # Got file
    else:
        try:
            O = open(url, "r")
            contents = O.readlines()
            O.close()
            return contents
        except:
            print "Cannot open file %s" % str(url)
            return None

