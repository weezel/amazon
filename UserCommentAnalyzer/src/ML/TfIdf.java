package ML;

import java.util.ArrayList;

/*
Consider a document containing 100 words wherein the word cow appears 3
times.  Following the previously defined formulas, the term frequency (TF)
for cow is then (3 / 100) = 0.03.
Now, assume we have 10 million documents and cow appears in one thousand of
these. Then, the inverse document frequency is calculated as log(10 000 000 /
1 000) = 4. The TF-IDF score is the product of these quantities: 0.03 × 4 =
0.12.
 */
public class TfIdf
{
    /**
     * Count how many times searhcable word appears in a comment and divide by
     * the total count of the words in the comment.
     * @param doc comment
     * @param s searchable string
     * @return term frequency in comment
     */
    public static double termFrequencyInComment(String[] doc, String s)
    {
        int matchCount;

        if (doc == null || doc.length < 1 || s == null || s.length() < 1)
            return (0.0);

        matchCount = 0;

        /* The actual counting happens here */
        for (int i=0; i < doc.length; i++) {
            if (doc[i].equals(s))
                matchCount++;
        }

        return (double)matchCount / (double)doc.length;
    }


    /**
     * Count how many times searhcable word appears in all comments and
     * calculate frequency.
     * @param doc all comments. Must be sorted!
     * @return inverse term frequency in document
     */
    public static double documentFrequency(ArrayList<String> doc, String s)
    {
        /* Count how many comments this term occurs */
        int matchCount, spos, epos;
        int[] searchArea;

        if (doc == null || doc.size() < 1 || s == null || s.length() < 1)
            return (0.0);

        matchCount = 0;
        searchArea = filterByInitials(doc, s); // Reduces search space dramatically

        if (searchArea == null)
            return (0.0);

        spos = searchArea[0];
        epos = searchArea[1];

        /* Count matches in all comments */
        int cmntNmbr = 0, prevIdx = -1;
        for (; spos <= epos; spos++) {
            String tmp[] = doc.get(spos).split("\\|");
            if (tmp.length > 0) {
                cmntNmbr = Integer.parseInt(tmp[1].substring(0, tmp[1].indexOf(":")));
                assert (cmntNmbr != -1) : "cmntNmbr cannot be -1";
                if (tmp[0].equals(s) && cmntNmbr != prevIdx) {
                    prevIdx = cmntNmbr;
                    matchCount++;
                }
            }
        }

        if (matchCount <= 0)
            return 0.0;

        return (double)matchCount / (double)doc.size();
    }


    /*
     * Reduce search area to the same initial and afterwards, only to words
     * that equals "s". Cheaper than compare immediately against the word.
     * @return start and ending position for the same word
     */
    public static int[] filterByInitials(ArrayList<String> doc, String s)
    {
        int spos, epos;
        int[] r = new int[2];
        char cmpr = s.charAt(0);

        if (doc == null || doc.size() < 1 || s == null || s.length() < 1)
            return null;

        spos = epos = -1;

        for (int i = 0; i < doc.size(); i++) {
            /* Rewind to the correct inital position in the array */
            if (doc.get(i).charAt(0) == cmpr) {
                spos = i;
                while (i < doc.size() - 1 && doc.get(++i).charAt(0) == cmpr);
                epos = i - 1;
                if (epos < spos)
                    epos = spos;
                break;
            }
        }

        /* There is no such initial letter that equals s[0] */
        if (spos == -1 || epos == -1)
            return null;

        /* Only count in words that equals with the parameter 's' */
        for (int i = spos; i <= epos; i++) {
            if (doc.get(i).equals(s)) {
                spos = i;
                while (i < doc.size() - 1 && doc.get(++i).equals(s));
                epos = i - 1;
                if (epos < spos)
                    epos = spos;
                break;
            }
        }
        r[0] = spos;
        r[1] = epos;

        return r;
    }

    /**
     * Calculate Term Frequency-Inverse document frequency
     * @param termfreq term frequency
     * @param docfreq document frequency
     * @return tf-idf score
     */
    public static double tfidf_score(double termfreq, double docfreq)
    {
        return termfreq * (Math.log10(1.0 / docfreq));
    }


    public static void runTests() {
        double termfreq, invfreq, tfscore;
        String boo = "boo";
        String comment[] = {
            "amiraali",
            "anjovis",
            "boo",
            "boo",
            "jee",
            "korn"
        };
        ArrayList<String> documents1 = new ArrayList<String>();
            documents1.add("14sia|1:50");
            documents1.add("auto|1:50");
            documents1.add("amiraali|3:40");
            documents1.add("anjovis|3:10");
            documents1.add("boo|5:10");
            documents1.add("boo|5:60");
            documents1.add("boo|6:20");
            documents1.add("boredom|4:55");
            documents1.add("burzum|12:80");
            documents1.add("jee|18:30");
            documents1.add("klonkku|15:12");

        termfreq = termFrequencyInComment(comment, "boo");
        assert (Math.abs(termfreq - 0.3333333333333333) < 1.0E-8) : "termfreq should be: 0.3333333333333333, current value: " + termfreq;
        invfreq = documentFrequency(documents1, "boo");
        assert (Math.abs(invfreq - 0.18181818181818182) < 1.0E-8) : "invfreq should be: 0.18181818181818182, current value: " + invfreq;
        tfscore = tfidf_score(termfreq, invfreq);
        assert (Math.abs(invfreq - 0.24678756316474795) < 1.0E-8) : "tfscore should be: 0.24678756316474795, current value: " + tfscore;
        System.out.println("'" + boo + "'" + " comment frequenzy: " + termfreq);
        System.out.println("'" + boo + "'" + " inverse frequenzy: " + invfreq);
        System.out.println("'" + boo + "'" + " tf-idf score     : " + tfscore);
    }

/*
	public static void main (String[] args)
	{
		runTests();
	}
*/
}
