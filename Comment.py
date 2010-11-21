class Comment:
    def __init__(self):
        self.name = ""
        self.helpful = ""
        self.stars = ""
        self.header = ""
        self.comment = ""
    def printAll(self):
        print "\nName: %s\nHelpful: %s\nStars: %s\nHeader: %s\nComment:\n" %\
            (self.name, self.helpful, self.stars, self.header),
        for line in self.comment:
            print "%s" % line
    def __repr__(self):
        return "\nName: %s\nHelpful: %s\nStars: %s\nHeader: %s\nComment:\n" %\
            (self.name, self.helpful, self.stars, self.header)

