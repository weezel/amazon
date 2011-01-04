package wordretrieval;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/* Add this to levenstein unittest 
 String1       String2  Dist1  Dist2   Prox1  Prox2
     hte           hte    0.0    0.0    -0.0   -0.0
     hte          htne    1.0    1.0    -1.0   -1.0
     hte          thhe    2.0    2.0    -2.0   -2.0
     hte           the    2.0    1.0    -2.0   -1.0
     hte          then    3.0    2.0    -3.0   -2.0
     hte           The    2.0    2.0    -2.0   -2.0
     hte           THE    3.0    3.0    -3.0   -3.0
    htne           hte    1.0    1.0    -1.0   -1.0
    htne          htne    0.0    0.0    -0.0   -0.0
    htne          thhe    3.0    2.0    -3.0   -2.0
    htne           the    2.0    2.0    -2.0   -2.0
    htne          then    3.0    2.0    -3.0   -2.0
    htne           The    3.0    3.0    -3.0   -3.0
    htne           THE    4.0    4.0    -4.0   -4.0
    thhe           hte    2.0    2.0    -2.0   -2.0
    thhe          htne    3.0    2.0    -3.0   -2.0
    thhe          thhe    0.0    0.0    -0.0   -0.0
    thhe           the    1.0    1.0    -1.0   -1.0
    thhe          then    2.0    2.0    -2.0   -2.0
    thhe           The    2.0    2.0    -2.0   -2.0
    thhe           THE    4.0    4.0    -4.0   -4.0
     the           hte    2.0    1.0    -2.0   -1.0
     the          htne    2.0    2.0    -2.0   -2.0
     the          thhe    1.0    1.0    -1.0   -1.0
     the           the    0.0    0.0    -0.0   -0.0
     the          then    1.0    1.0    -1.0   -1.0
     the           The    1.0    1.0    -1.0   -1.0
     the           THE    3.0    3.0    -3.0   -3.0
    then           hte    3.0    2.0    -3.0   -2.0
    then          htne    3.0    2.0    -3.0   -2.0
    then          thhe    2.0    2.0    -2.0   -2.0
    then           the    1.0    1.0    -1.0   -1.0
    then          then    0.0    0.0    -0.0   -0.0
    then           The    2.0    2.0    -2.0   -2.0
    then           THE    4.0    4.0    -4.0   -4.0
     The           hte    2.0    2.0    -2.0   -2.0
     The          htne    3.0    3.0    -3.0   -3.0
     The          thhe    2.0    2.0    -2.0   -2.0
     The           the    1.0    1.0    -1.0   -1.0
     The          then    2.0    2.0    -2.0   -2.0
     The           The    0.0    0.0    -0.0   -0.0
     The           THE    2.0    2.0    -2.0   -2.0
     THE           hte    3.0    3.0    -3.0   -3.0
     THE          htne    4.0    4.0    -4.0   -4.0
     THE          thhe    4.0    4.0    -4.0   -4.0
     THE           the    3.0    3.0    -3.0   -3.0
     THE          then    4.0    4.0    -4.0   -4.0
     THE           The    2.0    2.0    -2.0   -2.0
     THE           THE    0.0    0.0    -0.0   -0.0
*/

public class SpellCheckers
{
	/** 
	 * Converted to Java from Wikipedia's article:
	 * http://en.wikipedia.org/wiki/Levenshtein_distance
	 * includes Damerau addition to detect transpositions too
	 */
	public static int levensteinDistance(String s1, String s2)
	{
		if (s1 == null || s2 == null || s1.length() < 1 || s2.length() < 1)
			return -1;

		int cost;
		int[][] resultable = new int[s1.length() + 1][s2.length() + 1];

		for (int i=0; i <= s1.length(); i++)
			resultable[i][0] = i;
		for (int j=0; j <= s2.length(); j++)
			resultable[0][j] = j;
		for (int j=1; j <= s2.length(); j++) {
			for (int i=1; i <= s1.length(); i++) {
				if (s1.charAt(i-1) == s2.charAt(j-1)) {
					cost = 0;
					//resultable[i][j] = resultable[i-1][j-1];
				} else {
					cost = 1;
					resultable[i][j] = Math.min(
							Math.min(resultable[i-1][j] + 1, // deletion
								resultable[i][j-1] + 1),     // insertion
							resultable[i-1][j-1] + cost);    // substitution
				}
				if (i > 2 && j > 2 &&
					s1.charAt(i-1) == s2.charAt(j-2) &&
					s1.charAt(i-2) == s2.charAt(j-1)) {
					resultable[i][j] = Math.min(resultable[i][j],
							resultable[i-2][j-2] + cost);	  // transposition
				}

			} // inner for
		} // outer for
		return resultable[s1.length()][s2.length()];
	}


	/**
	 * Converted to Java from Wikipedia's article:
	 * http://en.wikipedia.org/wiki/Dice%27s_coefficient
	 */
	public static double diceCoefficient(String s1, String s2)
	{
		double totcombigrams = 0;
		Set nx = new HashSet();
		Set ny = new HashSet();
		Set intersection = null;

		if (s1 == null || s2 == null || s1.length() <= 1 || s2.length() <= 1)
			return -1;

		for (int i=0; i < s1.length()-1; i++) {
			char x1 = s1.charAt(i);
			char x2 = s1.charAt(i+1);
			String tmp = Character.toString(x1) + Character.toString(x2);
			nx.add(tmp);
		}
		for (int j=0; j < s2.length()-1; j++) {
			char y1 = s2.charAt(j);
			char y2 = s2.charAt(j+1);
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

		System.out.println("Levenstein distance between '" + s1 + "' and '" + s2 + "' is " + levenResult);
		System.out.println("Dice's coeffiency for '" + s1 + "' and '" + s2 + "' is " + diceResult);

	}


	public static void main(String[] args)
	{
		runTests(args);
	}

}


