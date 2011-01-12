package spellChecker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class SpellCheckers
{
    /**
     * @param s1 comparable word1
     * @param s2 comparable word2
     * @return distance between words
     *
     * Converted to Java from Wikipedia's article:
     * http://en.wikipedia.org/wiki/Levenshtein_distance
     * and added Damerau algorithm to detect transpositions too
     * http://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance
     */
    public static int levensteinDistance(String s1, String s2)
    {
        int cost;
        int[][] resultable = new int[s1.length() + 1][s2.length() + 1];


        if (s1 == null || s2 == null || s1.length() < 1 || s2.length() < 1)
            return -1;

        for (int i = 0; i <= s1.length(); i++)
            resultable[i][0] = i;
        for (int j = 0; j <= s2.length(); j++)
            resultable[0][j] = j;

        for (int i = 1; i < s1.length(); i++) {
            for (int j = 1; j < s2.length(); j++) {
                if (s1.charAt(i) == s2.charAt(j))
                    cost = 0;
                else
                    cost = 1;
                resultable[i][j] = Math.min(
                        Math.min(resultable[i - 1][j] + 1, // deletion
                        resultable[i][j - 1] + 1), // insertion
                        resultable[i - 1][j - 1] + cost);	// substitution
                if (i > 1 && j > 1
                        && s1.charAt(i) == s2.charAt(j - 1)
                        && s1.charAt(i - 1) == s2.charAt(j)) {
                    resultable[i][j] = Math.min(resultable[i][j],
                            resultable[i - 2][j - 2] + cost);	  // transposition
                }

            } // inner for
        } // outer for
        return resultable[s1.length() - 1][s2.length() - 1];
    }

    /**
     * @param s1 comparable word1
     * @param s2 comparable word2
     * @return dice coeffiency for words
     *
     * Converted to Java from Wikipedia's article:
     * http://en.wikipedia.org/wiki/Dice%27s_coefficient
     * also sent this implementation to WikiBooks
     * http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Dice%27s_coefficient
     */
    public static double diceCoefficient(String s1, String s2)
    {
        double totcombigrams = 0;
        Set nx = new HashSet();
        Set ny = new HashSet();
        Set intersection = null;

        if (s1 == null || s2 == null || s1.length() <= 1 || s2.length() <= 1)
            return -1;

        for (int i = 0; i < s1.length() - 1; i++) {
            char x1 = s1.charAt(i);
            char x2 = s1.charAt(i + 1);
            String tmp = Character.toString(x1) + Character.toString(x2);
            nx.add(tmp);
        }
        for (int j = 0; j < s2.length() - 1; j++) {
            char y1 = s2.charAt(j);
            char y2 = s2.charAt(j + 1);
            String tmp = Character.toString(y1) + Character.toString(y2);
            ny.add(tmp);
        }

        intersection = new TreeSet(nx);
        intersection.retainAll(ny);
        totcombigrams = intersection.size();

        return (2 * totcombigrams) / (nx.size() + ny.size());
    }


    public static void runTests(String[] args)
    {
        String s1 = null;
        String s2 = null;

        if (args.length < 2) {
            s1 = "button";
            s2 = "buttons";
        } else {
            s1 = args[0];
            s2 = args[1];
        }

        int levenResult = levensteinDistance(s1, s2);
        double diceResult = diceCoefficient(s1, s2);

        assert (diceCoefficient("kokko", "kokkot") <= 1) : "Should be < 1";
        assert (levensteinDistance("the", "teh") == 1) : "Should be 1";

        System.out.println("Levenstein distance between '" + s1 + "' and '" + s2 + "' is " + levenResult);
        System.out.println("Dice's coeffiency for '" + s1 + "' and '" + s2 + "' is " + diceResult);
    }


    /**
     *
     * @param s1 comparable word
     * @param s2 comparable word
     * @param l user given boundary that levenstein distance should exceed
     * @param e user given boundary that dice coeffiency should exceed
     * @return if words exceed values, return dice coeffiency, 0.0 on false
     */
    public static double combinedSpellcheck(String s1, String s2, int l, double d)
    {
        int lev;
        double dice;

        lev = 0;
        dice = 0.0;

        lev = levensteinDistance(s1, s2);
        dice = diceCoefficient(s1, s2);

        if (lev != -1 && dice != -1) {
            if (lev <= l && (dice * 100) >= d)
                return dice;
        }

        return (0.0);
    }


    /**
     * @param doc document containing all words
     * @param s searchable word
     * @param levdiff boundary value for levenstein distance. Values under this
     * won't be counted.
     * @param codiff boundary value for dice coefficient. Values under this
     * wont' be counted.
     * @return HashMap of words that are probably miss spelled
     */
    public static ArrayList<String> nearWords(String[] doc, String s, int levdiff, int codiff)
    {
        double dice;
        ArrayList<String> tophits = new ArrayList<String>();

        dice = 0.0;

        if (doc == null || doc.length < 1 || s == null
                || s.length() < 1 || codiff < 1 || levdiff < 1) {
            return null;
        }

        System.out.println("#######################");
        for (int i=0; i < doc.length; i++) {
            dice = SpellCheckers.diceCoefficient(doc[i], s);
            if (SpellCheckers.combinedSpellcheck(doc[i], s, levdiff, codiff) != 0.0) {
                System.out.println(String.format("\t%s", doc[i]));
                tophits.add(doc[i]);
            }
        }
        // Remove duplicates
        for (int i=0; i < tophits.size(); i++) {
            while (tophits.get(i+1) != null &&
                    tophits.get(i).equals(tophits.get(i+1)))
                tophits.remove(i+1);
        }

        return tophits;
    }


    /* @Override Treemap comparator */
    class ValueComparator implements Comparator
    {
        Map base;

        public ValueComparator(Map b) { base = b; }

        public int compare(Object a, Object b) {
            if ((Double) base.get(a) < (Double) base.get(b))
                return 1;
            else if (Math.abs((Double)base.get(a) - (Double)base.get(b)) <= 1.0E-8)
                return 0;
            else
                return -1;
        }
    }


    /*
    public static void main(String[] args)
    {
    runTests(args);
    }
     */
}


