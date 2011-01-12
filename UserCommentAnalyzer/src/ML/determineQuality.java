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
    public static ArrayList quality()
    {
        final double TFBOUNDARY = 0.2;
        final int GOODNESS = 50;
        ArrayList<Integer> returnIndexes;
        Set goodSet = new HashSet();
        Set tfidfSet   = new HashSet();
        Set intersect  = null;

        for (int i=0; i< 10; i++) {
            double tfidf_score = KeywordRetrieval.result[i].getTFRating();

            double goodnesspercentage = 0.0;
            if (KeywordRetrieval.posScoresArray[i] > 0 || KeywordRetrieval.negScoresArray[i] > 0) {
                goodnesspercentage = ((double) KeywordRetrieval.posScoresArray[i] / ((double) KeywordRetrieval.posScoresArray[i] + (double) KeywordRetrieval.negScoresArray[i])) * 100;
            } 

            if (Double.compare(tfidf_score, TFBOUNDARY) >= 0)
                tfidfSet.add(i);
            if (goodnesspercentage >= GOODNESS)
                goodSet.add(i);
        }

        // Perform intersection so we can get good topwords
        intersect = new TreeSet(tfidfSet);
        intersect.retainAll(goodSet);
        
        returnIndexes = new ArrayList<Integer>(intersect);

        ArrayList output = new ArrayList();

        for (int i=0; i < returnIndexes.size(); i++) {
            output.add(String.format("%s %d", KeywordRetrieval.result[returnIndexes.get(i)].getTheWord(), i));
        }
        //for (int i=0; i < tmp.length; i++) {
        //    Object tmpint = tmp[i];

        return output;
    }
}
