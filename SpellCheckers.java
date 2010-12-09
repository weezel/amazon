//package wordretrieval;

import java.util.Scanner;

public class SpellCheckers
{
	/** 
	 * Converted to Java from Wikipedia's article:
	 * http://en.wikipedia.org/wiki/Levenshtein_distance
	 */
	public static int levensteinDistance(String s1, String s2)
	{
		if (s1 == null || s2 == null || s1.length() < 1 || s2.length() < 1)
			return -1;

		int[][] resultable = new int[s1.length() + 1][s2.length() + 1];

		for (int i=0; i <= s1.length(); i++)
			resultable[i][0] = i;
		for (int j=0; j <= s2.length(); j++)
			resultable[0][j] = j;
		for (int j=1; j <= s2.length(); j++) {
			for (int i=1; i <= s1.length(); i++) {
				if (s1.charAt(i - 1) == s2.charAt(j - 1))
					resultable[i][j] = resultable[i-1][j-1];
				else {
					resultable[i][j] = Math.min(
							Math.min(resultable[i-1][j] + 1, // deletion
								resultable[i][j-1] + 1), // insertion
							resultable[i-1][j-1] + 1); // substitution
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
		int count;
		double total;
		char nx1, nx2, ny1, ny2;

		count = 0;

		// Here we do intersection and count overlaps
		for (int j=0; j < s1.length()-1; j++) {
			nx1 = s1.charAt(j);
			nx2 = s1.charAt(j+1);
			for (int i=0; i < s2.length()-1; i++) {
				ny1 = s2.charAt(i);
				ny2 = s2.charAt(i+1);
				if (nx1 == ny1 && nx2 == ny2)
					count++;
			} // inner for
		} // outer for

		// Now that we know overlapping bigrams
		// let's count the rest of the formula:
		// (2 * overlap count) / total bigrams
		total = s1.length() + s2.length() - 2;
		System.out.println("Total " + total);

		return 2 * count / total;
	}


	public static void main(String[] args)
	{
		String s1 = null;
		String s2 = null;
		Scanner sc = new Scanner(System.in);

		if (args.length < 2) {
			//s1 = sc.next();
			//s2 = sc.next();
			s1 = "button";
			s2 = "buttons";
		} else {
			s1 = args[0];
			s2 = args[1];
		}

		int levenResult = levensteinDistance(s1, s2);
		double diceResult = diceCoefficient(s1, s2);

		System.out.println("Levenstein distance between ''" + s1 + "'' and ''" + s2 + "'' is " + levenResult);
		System.out.println("Dice's coeffiency for ''" + s1 + "'' and ''" + s2 + "'' is " + diceResult);
		sc.close();
	}

}


