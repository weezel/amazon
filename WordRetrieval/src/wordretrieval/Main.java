/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordretrieval;

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
import java.util.regex.*;

class WordInfo implements Comparable
{
    public String theWord;
    public int count;
    public float rating;
    public WordInfo(String theWord, int count, float rating)
    {
        this.count = count;
        this.rating = rating;
        this.theWord = theWord;
    }
    public int compareTo(Object obj)
    {
        WordInfo tmp = (WordInfo)obj;
        if(this.count > tmp.count)
        {
            /* instance lt received */
            return -1;
        }
        else if(this.count < tmp.count)
        {
            /* instance gt received */
            return 1;
        }
        /* instance == received */
        return 0;
     }
}


/**
 *
 * @author Arjen
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        //////////////////stopwords////////////////////////////////////
        //FIXME need to have dynbamic array for stopwords instead of fixed size
        ArrayList stopWords = new ArrayList();
        Scanner s = null;
        try
        {
            s = new Scanner(new BufferedReader(new FileReader("wordfiles/stopwordsbig.txt")));
            //s = new Scanner(new BufferedReader(new FileReader("D:\\tue\\WIS\\review1.txt")));
            int i=0;
            while (s.hasNext())
            {
                stopWords.add(s.next());
                i++;
            }
        }
        finally {
            if (s != null) {
                s.close();
            }
        }

        Collections.sort(stopWords);
        //FIXME need to have dynbamic array for comment words instead of fixed size

        //////////////////adjectives////////////////////////////////////
        ArrayList adjectives = new ArrayList();
        try
        {
            s = new Scanner(new BufferedReader(new FileReader("wordfiles/adjectives.txt")));
            //s = new Scanner(new BufferedReader(new FileReader("D:\\tue\\WIS\\review1.txt")));
            int i=0;
            while (s.hasNext())
            {
                adjectives.add(s.next());
                i++;
            }
        }
        finally {
            if (s != null) {
                s.close();
            }
        }
        Collections.sort(adjectives);

        //////////////////connection words////////////////////////////////////
        ArrayList connectionWords = new ArrayList();
        try
        {
            s = new Scanner(new BufferedReader(new FileReader("wordfiles/connectionwords.txt")));
            //s = new Scanner(new BufferedReader(new FileReader("D:\\tue\\WIS\\review1.txt")));
            int i=0;
            while (s.hasNext())
            {
                connectionWords.add(s.next());
                i++;
            }
        }
        finally {
            if (s != null) {
                s.close();
            }
        }
        Collections.sort(connectionWords);



        ///////the words from the comments///////////////////////////////
        ArrayList<String>[] commentWords = (ArrayList<String>[])new ArrayList[10];
        commentWords[0] = new ArrayList<String>();
        commentWords[0].add("jello");
        commentWords[0].add("wgero");

        commentWords[1] = new ArrayList<String>();
        commentWords[1].add("fefvfd");
        commentWords[1].add("dfdff");


        //////////////////comments////////////////////////////////////
        ArrayList revWords = new ArrayList();

        int k=0;
        int rNum=0;
        String curRating = "";

        try
        {
            //s = new Scanner(new BufferedReader(new FileReader("D:\\tue\\WIS\\wordret\\amazonereviews.txt")));
            //s = new Scanner(new BufferedReader(new FileReader("D:\\tue\\WIS\\wordret\\newmakeup.txt")));
            //s = new Scanner(new BufferedReader(new FileReader("D:\\tue\\WIS\\wordret\\revlogitechg5.txt")));
            s = new Scanner(new BufferedReader(new FileReader("wordfiles/comments.txt")));
            
            while (s.hasNext())
            {
                String curW = s.next();
                if(curW.equals("Name:"))
                {
                    while(!curW.equals("Stars:")){
                        curW = s.next();
                    }
                    curRating = s.next();
                    while(!curW.equals("Comment:"))
                        curW = s.next();
                    curW = s.next();
                }

                
                curW = curW.toLowerCase();
                curW = removeSpecialCharacters(curW);
                //FIXME are these all the characters that have to be removed


                int stopLoc = Collections.binarySearch(stopWords, curW);

                Pattern p = Pattern.compile("---");
                Matcher m = p.matcher(curW);

                if(m.matches())
                    rNum++;
                else
                {
                    if(stopLoc < 0)
                    {
                        curW = curW.replace("-", "");
                        ////adjectives code///
                        int adjLoc = Collections.binarySearch(adjectives, curW);
                        if (adjLoc >= 0)
                        {
                            String curWnext = s.next();

                            curWnext = curWnext.toLowerCase();
                            curWnext = removeSpecialCharacters(curWnext);
                            
                            ///connection words///
                            int connexLoc = Collections.binarySearch(connectionWords, curWnext);
                            if(connexLoc >= 0 && !curWnext.contains("---"))
                            {
                                String curWnext2 = s.next();

                                curWnext2 = curWnext2.toLowerCase();
                                curWnext2 = removeSpecialCharacters(curWnext2);

                                String result = curW + " " + curWnext + " " + curWnext2;
                                String temp = "|" + rNum + ":" + curRating;
                                result = result.concat(temp);

                                revWords.add(result);

                            }
                            else
                            {
                                if(!curWnext.contains("---"))
                                {
                                    String result = curW + " " + curWnext;
                                    String temp = "|" + rNum + ":" + curRating;
                                    result = result.concat(temp);

                                    revWords.add(result);
                                }
                            }

                        }
                        else
                        {
                        ////end adjectives code///

                        String temp = "|" + rNum + ":" + curRating;
                        curW = curW.concat(temp);
                        revWords.add(curW);
                        }
                    }

                }
                k = k + 1;
            }
        }
        finally {
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

        while(k != len)
        {
            curWord = revWords.get(k).toString();
            //store the comment number of the current keyword
            int loc = curWord.indexOf("|");
            int ratingLoc = curWord.indexOf(":");
            int num = Integer.parseInt(curWord.substring(loc+1, ratingLoc));

            //store the rating of the current keyword
            float rating = Float.parseFloat(curWord.substring(ratingLoc+1));

            curWord = curWord.substring(0, loc);

            if(!curWord.equals(prevWord))
            {
                j++;
                countedWords[j] = new WordInfo("", 0, 0);
                countedWords[j].theWord = curWord;
                countedWords[j].count = 1;
                countedWords[j].rating = rating;
                prevWord = curWord;
                prevNum = 1;
                numWords++;
            }
            else
            {
                if(num > prevNum)
                {
                    countedWords[j].count++;
                    countedWords[j].rating+=rating;
                }
                prevNum = num;
            }
            k++;


        }
        k = 0;


        WordInfo[] result = new WordInfo[numWords];
        for(int i=0;i<numWords;i++)
        {
            result[i] = new WordInfo("", 0, 0);
            result[i].count = countedWords[i].count;
            result[i].rating = countedWords[i].rating;
            result[i].theWord = countedWords[i].theWord;
        }

        Arrays.sort(result);
        for(int i=0;i<numWords;i++)
        {
            //if(result[i].theWord.contains(" "))
                System.out.println("count:" + result[i].count + " " + result[i].theWord + " " + result[i].rating/result[i].count);
                //System.out.println(result[i].rating/result[i].count);
                //System.out.println(result[i].count + ",'" + result[i].theWord + "'," + result[i].rating/result[i].count);

        }

        
    }

    public static String removeSpecialCharacters(String curW)
    {
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

