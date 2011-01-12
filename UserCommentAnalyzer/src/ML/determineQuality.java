package ML;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import wordRetrieval.KeywordRetrieval;


public class determineQuality
{
    /**
     * Tf-idf expresses normalized top words from the comments.
     * By counting good/bad ratios and intersecting these two sets
     * (tfidf-score and good/bad ratios) we will get only the good words
     * that have high weight.
     *
     * Collect all the keyword indexes that have equal or bigger Tf-Idf
     * value than TFBOUNDARY.
     * Collect all the keyword indexes that have equal or bigger bad/good
     * percentage than GOODNESS
     * Finally perform intersection between them.
     *
     * @param comments list of comments
     * @return index list of important good words
     */
    public static ArrayList quality(ArrayList comments)
    {
        final int TFBOUNDARY = 20;
        final int GOODNESS = 50;
        ArrayList<Integer> returnIndexes;
        Set goodSet = new HashSet();
        Set tfidfSet   = new HashSet();
        Set intersect  = null;

        for (int i=0; i < 10; i++) {
            String word = comments.get(i).toString();
            int p = word.indexOf("|");
            int sc = word.indexOf(";");
            double tfidf_score = Double.parseDouble(word.substring(sc, word.length()-1));
            double goodnesspercentage = KeywordRetrieval.negScoresArray[i] / KeywordRetrieval.posScoresArray[i];

            if (tfidf_score >= TFBOUNDARY)
                tfidfSet.add(i);
            if (KeywordRetrieval.posScoresArray[i] > 1 &&
                    KeywordRetrieval.posScoresArray[i] > 1 &&
                    goodnesspercentage >= GOODNESS)
                goodSet.add(i);
        }

        // Perform intersection so we can get good topwords
        intersect = new TreeSet(tfidfSet);
        intersect.retainAll(goodSet);
        
        returnIndexes = new ArrayList<Integer>(intersect);
        //for (int i=0; i < tmp.length; i++) {
        //    Object tmpint = tmp[i];

        return returnIndexes;
    }
}
