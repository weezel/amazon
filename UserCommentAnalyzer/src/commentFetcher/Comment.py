class Comment:
    def __init__(self):
        self.helpful = ""
        self.stars = ""
        self.header = ""
        self.comment = ""

    def printAll(self):
        print "Name: none\nHelpful: %s\nStars: %s\nHeader: %s\nComment:\n" %\
            (self.helpful, self.stars, self.header),
        for line in self.comment:
            print "%s" % line

    def getComment(self):
        return "".join(self.comment)

    def __repr__(self):
        return "Name: none\nHelpful: %s\nStars: %s\nHeader: %s\nComment:\n%s\n" %\
            (self.helpful, self.stars, self.header, self.getComment())

    def getonelinecomment(self):
        return self.comment.replace("\n", " ")

