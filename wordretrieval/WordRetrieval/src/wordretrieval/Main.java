package wordretrieval;

import java.io.*;
import java.util.*;

class WordInfo implements Comparable
{
    public String theWord;
    public int count;
    public WordInfo(String theWord, int count)
    {
        this.count = count;
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
    public static void main(String[] args) throws IOException
    {
        //FIXME need to have dynbamic array for stopwords instead of fixed size
        Vector<String> stopWords = new Vector<String>();
        Scanner s = null;

        try
        {
            s = new Scanner(new BufferedReader(new FileReader("../../stopwordsbig.txt")));
            //s = new Scanner(new BufferedReader(new FileReader("D:\\tue\\WIS\\review1.txt")));
            while (s.hasNext())
                stopWords.add(s.next());
        }
        finally {
            if (s != null) {
                s.close();
            }
        }

        //Arrays.sort(stopWords);
        Collections.sort(stopWords);
        //FIXME need to have dynbamic array for comment words instead of fixed size
        //String[] revWords = new String[20000];
        Vector<String> revWords = new Vector<String>();
//        for(int i = 0; i < 20000; i++)
//            revWords.elementAt(i) = "zzzzzzzzzzzzzzzzzzzzzzzz";

        int k = 0;
        int rNum = 1;

        try
        {
            s = new Scanner(new BufferedReader(new FileReader("../../amazonereviews.txt")));
            //s = new Scanner(new BufferedReader(new FileReader("D:\\tue\\WIS\\review1.txt")));
            //s = new Scanner(new BufferedReader(new FileReader("D:\\tue\\WIS\\wordret\\revlogitechg5.txt")));

            while (s.hasNext()) 
            {
                String curW = s.next();
                curW = curW.toLowerCase();
                //FIXME are these all the characters that have to be removed
                curW = curW.replace(".", "");
                curW = curW.replace(",", "");
                curW = curW.replace("?", "");
                curW = curW.replace("!", "");
                curW = curW.replace("(", "");
                curW = curW.replace(")", "");
                curW = curW.replace("\"", "");
                curW = curW.replace(":", "");

                //int stopLoc = Arrays.binarySearch(stopWords, curW);
                int stopLoc = Collections.binarySearch(stopWords, curW);

                if(curW.equals("---"))
                    rNum++;
                else
                {
                    if(stopLoc < 0)
                    {
                    	String temp = curW.replace("-", "").concat("|" + rNum);
                    	revWords.set(k, temp);
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

        //Arrays.sort(revWords);
        Collections.sort(revWords);
        String curWord = "";
        WordInfo[] countedWords = new WordInfo[k];
        int j = -1;
        k = 0;

        String prevWord = "zzzzzzzzzzzzzzzzzzzzzzzz";
        int prevNum = 0;
        while(!revWords.get(k).equals("zzzzzzzzzzzzzzzzzzzzzzzz"))
        {
            int loc = revWords.get(k).indexOf("|");
            int num = Integer.parseInt(revWords.get(k).substring(loc+1));
            curWord = revWords.get(k).substring(0, loc);

            if(!curWord.equals(prevWord))
            {
                j++;
                countedWords[j] = new WordInfo("", 0);
                countedWords[j].theWord = curWord;
                countedWords[j].count = 1;
                prevWord = curWord;
                prevNum = 1;
                numWords++;
            }
            else
            {
                if(num > prevNum)
                    countedWords[j].count++;
                prevNum = num;
            }
            k++;
        }
        k = 0;


        WordInfo[] result = new WordInfo[numWords];
        for(int i=0;i<numWords;i++)
        {
            result[i] = new WordInfo("", 0);
            result[i].count = countedWords[i].count;
            result[i].theWord = countedWords[i].theWord;
        }

        Arrays.sort(result);
        for(int i=0;i<numWords;i++)
        {
            System.out.println("count:" + result[i].count + " " + result[i].theWord);

        }
    }



}
