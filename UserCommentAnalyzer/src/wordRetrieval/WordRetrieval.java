package wordRetrieval;

import gui.ProgressWindow;
import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * Extracts the keyword info from the text file with the fetched comments
 * of the products from Amazon.com.
 *
 * @author Arjen Meurers
 */
public class WordRetrieval {

    /**
     * File which contains the output comments after the fetching process.
     */
    public final static String OUTPUT_FILE = "src/commentFetcher/python/comments.txt";
    /**
     * File which contains the stop words.
     */
    public final static String STOP_WORDS_FILE = "src/wordRetrieval/resources/StopWords.txt";
    /**
     * File which contains the adjectives.
     */
    public final static String ADJECTIVES_FILE = "src/wordRetrieval/resources/Adjectives.txt";
    /**
     * File which contains the connection words.
     */
    public final static String CONNECTION_WORDS_FILE = "src/wordRetrieval/resources/ConnectionWords.txt";
    /**
     * Special characters to remove.
     */
    private static String[] _specialCharacters = {".", ",", "?", "!", "(", ")", "{", "}", "[", "]", "\"", ":", "|", "~", "/", "'", ";", "#"};
    /**
     * Dinamic array which contains the stop words used in the analysis.
     */
    private static ArrayList<String> _stopWords = new ArrayList<String>();
    /**
     * Dinamic array which contains the list of adjectives used in the analysis.
     */
    private static ArrayList<String> _adjectives = new ArrayList<String>();
    /**
     * Dinamic array which contains the connection words used in the analysis.
     */
    private static ArrayList<String> _connectionWords = new ArrayList<String>();

    /**
     * Main method of the class. Starts the process.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        //run(WordRetrievalFilter.SINGLE);
    }

    /**
     * Runs the process with the selected word retrieval process.
     * 
     * @param filter selected filter by the user in the main window.
     */
    public WordInfo[] run(WordRetrievalFilter filter) throws FileNotFoundException{

        ArrayList revWords = new ArrayList();

        int k = 0;
        int rNum = 0;
        String curRating = "";

        Scanner scanner = null;

        try {

            scanner = new Scanner(new BufferedReader(new FileReader(OUTPUT_FILE)));

            while (scanner.hasNext()) {

                String curW = scanner.next();

                if (curW.equals("Name:")) {

                    while (!curW.equals("Stars:")) {
                        curW = scanner.next();
                    }
                    curRating = scanner.next();
                    while (!curW.equals("Comment:")) {
                        curW = scanner.next();
                    }
                    curW = scanner.next();
                }


                curW = curW.toLowerCase();
                //curW = removeSpecialCharacters(curW);
                //FIXME are these all the characters that have to be removed

                int stopLoc = Collections.binarySearch(_stopWords, curW);

                Pattern p = Pattern.compile("---");
                Matcher m = p.matcher(curW);

                if (m.matches()) {
                    rNum++;
                } else {
                    if (stopLoc < 0) {
                        curW = curW.replace("-", "");
                        ////adjectives code///
                        int adjLoc = Collections.binarySearch(_adjectives, curW);
                        if (adjLoc >= 0) {
                            String curWnext = scanner.next();

                            curWnext = curWnext.toLowerCase();
                            curWnext = removeSpecialCharacters(curWnext);

                            ///connection words///
                            int connexLoc = Collections.binarySearch(_connectionWords, curWnext);
                            if (connexLoc >= 0 && !curWnext.contains("---")) {
                                String curWnext2 = scanner.next();

                                curWnext2 = curWnext2.toLowerCase();
                                curWnext2 = removeSpecialCharacters(curWnext2);

                                String result = curW + " " + curWnext + " " + curWnext2;
                                String temp = "|" + rNum + ":" + curRating;
                                result = result.concat(temp);

                                revWords.add(result);

                            } else {
                                if (!curWnext.contains("---")) {
                                    String result = curW + " " + curWnext;
                                    String temp = "|" + rNum + ":" + curRating;
                                    result = result.concat(temp);

                                    revWords.add(result);
                                }
                            }

                        } else {
                            ////end adjectives code///

                            String temp = "|" + rNum + ":" + curRating;
                            curW = curW.concat(temp);
                            revWords.add(curW);
                        }
                    }

                }
                k = k + 1;
            }
        } finally {
            if (scanner != null) {
                scanner.close();
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
            ProgressWindow.getInstance().updateProgressWindow(result[i].toString());
        }

        return result;
    }

    /**
     * Creates the dinamic array of stop words.
     * 
     * @throws FileNotFoundException
     */
    public void generateStopWords() throws FileNotFoundException{

        // Dynamic list to store the stop words
        _stopWords = new ArrayList<String>();

        Scanner scanner = null;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(STOP_WORDS_FILE)));
            int i = 0;
            while (scanner.hasNext()) {
                _stopWords.add(scanner.next());
                i++;
            }
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        Collections.sort(_stopWords);
    }

    /**
     * Generates the list of adjectives used in the analysis.
     * 
     * @throws FileNotFoundException
     */
    public void generateAdjectives() throws FileNotFoundException{

        // Dynamic list to store the adjectives
        _adjectives = new ArrayList<String>();

        Scanner scanner = null;

        try {

            scanner = new Scanner(new BufferedReader(new FileReader(ADJECTIVES_FILE)));
            int i = 0;
            while (scanner.hasNext()) {
                _adjectives.add(scanner.next());
                i++;
            }
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        Collections.sort(_adjectives);
    }

    /**
     * Generates the list of connection words used in the analysis.
     * 
     * @throws FileNotFoundException
     */
    public void generateConnectionWords() throws FileNotFoundException{

               // Dynamic list to store the connection words
        _connectionWords = new ArrayList<String>();

        Scanner scanner = null;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(CONNECTION_WORDS_FILE)));
            int i = 0;
            while (scanner.hasNext()) {
                _connectionWords.add(scanner.next());
                i++;
            }
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        Collections.sort(_connectionWords);
    }

    /**
     * Removes the special character from the list of special characters.
     *
     * @param currentWord current word to analyze.
     * @return the current word with the removed special characters.
     *
     */
    public static String removeSpecialCharacters(String currentWord) {

        for(int index = 0; index < _specialCharacters.length; index++)

            if(_specialCharacters[index].matches(currentWord))
                currentWord = currentWord.replace(_specialCharacters[index], "");
        
        return currentWord;
    }
}
