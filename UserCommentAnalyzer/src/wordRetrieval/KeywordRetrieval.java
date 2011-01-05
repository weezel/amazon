/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wordRetrieval;

/**
 *
 * @author Arjen
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
//import java.util.Scanner;
import java.util.*;

/**
 *
 * @author Arjen
 */
public class KeywordRetrieval {

    /**
     * @param filter filter to apply to the keyword retrieval part.
     * @param index index for the comments file.
     * @param isApplyAction indicates if the keyword retrieval has to be done
     * for the apply action or for the generating product.
     *
     * If it is true then it takes the info from the comments designed for the
     * index because the file already exists.
     * If it is false the it takes the info from the new file generated.
     */
    public WordInfo[] run(String filter, int index) throws IOException {

        //////////////////stopwords////////////////////////////////////
        //list of stopwords are read from the stopwords file
        //stopwords are stored in the stopwords array list
        ArrayList stopWords = new ArrayList();
        Scanner s = null;
        try {
            s = new Scanner(new BufferedReader(new FileReader("src/wordRetrieval/resources/StopWords.txt")));
            //s = new Scanner(new BufferedReader(new FileReader("D:\\tue\\WIS\\review1.txt")));
            int i = 0;
            while (s.hasNext()) {
                stopWords.add(s.next());
                i++;
            }
        } finally {
            if (s != null) {
                s.close();
            }
        }

        Collections.sort(stopWords);

        //////////////////adjectives////////////////////////////////////
        ArrayList adjectives = new ArrayList();
        try {
            s = new Scanner(new BufferedReader(new FileReader("src/wordRetrieval/resources/Adjectives.txt")));
            //s = new Scanner(new BufferedReader(new FileReader("D:\\tue\\WIS\\review1.txt")));
            int i = 0;
            while (s.hasNext()) {
                adjectives.add(s.next());
                i++;
            }
        } finally {
            if (s != null) {
                s.close();
            }
        }
        Collections.sort(adjectives);

        //////////////////connection words////////////////////////////////////
        ArrayList connectionWords = new ArrayList();
        try {
            s = new Scanner(new BufferedReader(new FileReader("src/wordRetrieval/resources/ConnectionWords.txt")));
            //s = new Scanner(new BufferedReader(new FileReader("D:\\tue\\WIS\\review1.txt")));
            int i = 0;
            while (s.hasNext()) {
                connectionWords.add(s.next());
                i++;
            }
        } finally {
            if (s != null) {
                s.close();
            }
        }
        Collections.sort(connectionWords);

        /////the custom regular expression///
        //We use a self defined regular expression to manipulate the result
        //The following keywords are defined:
        //[+] : any word but stopwords
        //[*] : any word
        //[a] : adjective
        //[c] : connection word
        //[;string] : any user define string
        ////////////////////////////////////
        //Example: we wish to find all occurences of an adjective + "quality"
        //[a][;quality]
        ///////////////////////
        //Any combination of two words
        //[*][*]
        String regExpression = filter;         //an adjective + "picture"


        int regExpressionLen = 0;
        int m = regExpression.indexOf("[", 0);
        while (m >= 0) {
            regExpressionLen++;
            m = regExpression.indexOf("[", m + 1);
        }


        String[] tokenArray;
        tokenArray = new String[regExpressionLen];

        //////////////////comments////////////////////////////////////
        ArrayList revWords = new ArrayList();

        int k = 0;
        int rNum = 0;
        String curRating = "";

        try {
            
            // Creates the file
            File file = new File("comments" + index + ".txt");

            // If the file exists then 
            if (file.exists()) {

                // Do the process
                s = new Scanner(new BufferedReader(new FileReader("comments" + index + ".txt")));

                while (s.hasNext()) {
                    rNum++;
                    //Walk through the header of a comment
                    //Store the rating information
                    String curW = s.next();
                    if (curW.equals("Name:")) {
                        while (!curW.equals("Stars:")) {
                            curW = s.next();
                        }
                        curRating = s.next();
                        while (!curW.equals("Comment:")) {
                            curW = s.next();
                        }
                        curW = s.next();
                    }

                    //Store the first word in the token array
                    curW = curW.toLowerCase();
                    curW = removeSpecialCharacters(curW);
                    tokenArray[0] = curW;

                    //Store the remaining n-1 words in the token array
                    for (int i = 1; i < regExpressionLen; i++) {
                        curW = s.next();
                        curW = curW.toLowerCase();
                        curW = removeSpecialCharacters(curW);
                        tokenArray[i] = curW;
                    }

                    //Walk through all words in the comment
                    while (!curW.equals("---")) {

                        int tPos = -1;
                        char curToken;
                        boolean match = true;
                        for (int i = 0; i < regExpressionLen; i++) {
                            tPos = regExpression.indexOf("[", tPos + 1);
                            curToken = regExpression.substring(tPos + 1, tPos + 2).charAt(0);

                            switch (curToken) {
                                case '+':
                                    int stopLoc = Collections.binarySearch(stopWords, tokenArray[i]);
                                    if (stopLoc >= 0) {
                                        match = false;
                                    }
                                    break;
                                case '*':
                                    break;
                                case 'a':
                                    int adjLoc = Collections.binarySearch(adjectives, tokenArray[i]);
                                    if (adjLoc < 0) {
                                        match = false;
                                    }
                                    break;
                                case 'c':
                                    int connLoc = Collections.binarySearch(connectionWords, tokenArray[i]);
                                    if (connLoc < 0) {
                                        match = false;
                                    }
                                    break;
                                case ';':
                                    String cWord;
                                    if (regExpression.indexOf("[", tPos + 1) > 0) {
                                        cWord = regExpression.substring(tPos + 2, regExpression.indexOf("[", tPos + 1) - 1);
                                    } else {
                                        cWord = regExpression.substring(tPos + 2, regExpression.length() - 1);
                                    }

                                    if (!cWord.equals(tokenArray[i])) {
                                        match = false;
                                    }
                                    break;
                            }
                        }

                        if (match) {
                            String result = "";// = tokenArray[0] + " " + curWnext + " " + curWnext2;
                            for (int i = 0; i < regExpressionLen - 1; i++) {
                                result = result + tokenArray[i] + " ";
                            }

                            result = result + tokenArray[regExpressionLen - 1];
                            String temp = "|" + rNum + ":" + curRating;
                            result = result.concat(temp);

                            revWords.add(result);
                            k = k + 1;
                        }


                        for (int i = 0; i < regExpressionLen - 1; i++) {
                            tokenArray[i] = tokenArray[i + 1];
                        }
                        curW = s.next();
                        curW = curW.toLowerCase();
                        curW = removeSpecialCharacters(curW);
                        tokenArray[regExpressionLen - 1] = curW;

                    }

                }
            }

        } finally {
            if (s != null) {
                s.close();
            }
        }


        int numWords = 0;

        System.out.println("number of reviews:" + rNum);

        Collections.sort(revWords);
        String curWord = "";
        WordInfo[] countedWords = new WordInfo[k];
        int j = -1;
        k = 0;

        String prevWord = "";
        int prevNum = 0;

        int len = revWords.size();

        while (k != len) {
            curWord = revWords.get(k).toString();
            //store the comment number of the current keyword
            int loc = curWord.indexOf("|");
            int ratingLoc = curWord.indexOf(":");
            int num = Integer.parseInt(curWord.substring(loc + 1, ratingLoc));

            //store the rating of the current keyword
            float rating = Float.parseFloat(curWord.substring(ratingLoc + 1));

            curWord = curWord.substring(0, loc);

            if (!curWord.equals(prevWord)) {
                j++;
                countedWords[j] = new WordInfo("", 0, 0);
                countedWords[j].setTheWord(curWord);
                countedWords[j].setCount(1);
                countedWords[j].setRating(rating);
                prevWord = curWord;
                prevNum = 1;
                numWords++;
            } else {
                if (num > prevNum) {
                    countedWords[j].setCount(countedWords[j].getCount() + 1);
                    countedWords[j].setRating(countedWords[j].getRating() + rating);
                }
                prevNum = num;
            }
            k++;


        }
        k = 0;


        WordInfo[] result = new WordInfo[numWords];
        for (int i = 0; i < numWords; i++) {
            result[i] = new WordInfo("", 0, 0);
            result[i].setCount(countedWords[i].getCount());
            result[i].setRating(countedWords[i].getRating());
            result[i].setTheWord(countedWords[i].getTheWord());
        }

        Arrays.sort(result);
        for (int i = 0; i < numWords; i++) {
            System.out.println("count:" + result[i].getCount() + " " + result[i].getTheWord() + " " + result[i].getRating() / result[i].getCount());
            //System.out.println(result[i].rating/result[i].count);
            //System.out.println(result[i].count + ",'" + result[i].theWord + "'," + result[i].rating/result[i].count);

        }

        return result;
    }

    public static String removeSpecialCharacters(String curW) {
        curW = curW.replace(".", "");
        curW = curW.replace(",", "");
        curW = curW.replace("?", "");
        curW = curW.replace("!", "");
        curW = curW.replace("(", "");
        curW = curW.replace(")", "");
        curW = curW.replace("{", "");
        curW = curW.replace("}", "");
        curW = curW.replace("[", "");
        curW = curW.replace("]", "");
        curW = curW.replace("\"", "");
        curW = curW.replace(":", "");
        curW = curW.replace("|", "");
        curW = curW.replace("~", "");
        curW = curW.replace("/", "");
        curW = curW.replace("'", "");
        curW = curW.replace(";", "");
        curW = curW.replace("#", "");

        return curW;
    }
}


