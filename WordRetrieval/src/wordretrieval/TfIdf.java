package wordretrieval;

//import java.util.StringTokenizer;
import java.util.Vector;

public class TfIdf
{
	/*
	Consider a document containing 100 words wherein the word cow appears 3
	times. Following the previously defined formulas, the term frequency
	(TF) for cow is then (3 / 100) = 0.03.
	Now, assume we have 10 million documents and cow appears in one thousand of
	these. Then, the inverse document frequency is calculated as log(10 000 000
	/ 1 000) = 4. The TF-IDF score is the product of these quantities: 0.03 × 4
	= 0.12.
    */

	/*
	 * Count how many times searhcable word appears in a comment and divide the
	 * total count of the words in comment by the sum of the found words.
	 */
	public static double termFrequencyInDocument(String[] doc, int[] sArea, String s)
	{
		int matchCount, spos, epos;

		matchCount = 0;
		spos = sArea[0];
		epos = sArea[1];

		for (; spos <= epos; spos++) {
			if (doc[spos].equals(s))
				matchCount++;
		}
		//System.out.println("matchCount = '" + matchCount + "'" + ", doc.length=" + doc.length);

		return (double) matchCount / doc.length;
	}


	/*
	 * Count how many times searhcable word appears in all comments and
	 * calculate the inverse frequency.
	 */
	public static double inverseDocumentFrequency(String[] doc, int[] sArea, int cmtcount, String s)
	{
		int matchCount, spos, epos;

		matchCount = 0;
		spos = sArea[0];
		epos = sArea[1];

		for (; spos <= epos; spos++) {
			if (doc[spos].equals(s))
				matchCount++;
		}

		assert (matchCount > 0) : "Cannot divide by zero! \n\tCaused by foundNtimes: " + matchCount;
		if (matchCount <= 0)
			return 0.0;

		return (double) Math.log10(doc.length / matchCount);
	}

	
	/* doc is the entire document
	 * cmcnt is count of the comments
	 * searchable is searchable word
	 */
	public static double tf_idf(String[] doc, int cmcnt, String searchable)
	{
		int[] searchArea;
		double invtermfreq, termfreq;

		searchArea = filterByInitials(doc, searchable);

		termfreq = termFrequencyInDocument(doc, searchArea, searchable); // this also declares nTimesInComment(s)
		//System.out.println("TERMFREQ = " + termfreq);
		invtermfreq = inverseDocumentFrequency(doc, searchArea, cmcnt, searchable);
		//System.out.println("INVTERMFREQ = " + invtermfreq);

		if (termfreq == -1)
			return (0.0);

		return termfreq * invtermfreq;
	}


	/*
	 * Reduce search area to the same initial and afterwards only words that
	 * equals "s".
	 * @return start and ending position to the document
	 */
	public static int[] filterByInitials(String[] doc, String s)
	{
		int spos, epos;
		int[] r = new int[2];
		char cmpr = s.charAt(0);

		spos = epos = -1;

		for (int i = 0; i < doc.length; i++) {
			/* Rewind to the correct inital position in the array */
			if (doc[i].charAt(0) == cmpr) {
				spos = i;
				while (i < doc.length - 1 && doc[++i].charAt(0) == cmpr);
				epos = i - 1;
				break;
			}
		}
		//System.out.println("Before, spos: " + spos + ", epos: " + epos);

		/* There is no such initial letter that equals s[0] */
		if (spos == -1 || epos == -1)
			return null;

		/* Only count in words that equals with the parameter 's' */
		for (int i = spos; i <= epos; i++) {
			if (doc[i].equals(s)) {
				spos = i;
				while (i < doc.length - 1 && doc[++i].equals(s));
				epos = i - 1;
				break;
			}
		}
		//System.out.println("After, spos: " + spos + ", epos: " + epos);

		r[0] = spos;
		r[1] = epos;

		return r;
	}
	

	public static void main (String[] args)
	{
		double jaa;
		String[] t = {
			"14sia",
			"auto", "amiraali", "anjovis",
			"boredom", "boo", "boo", "boo", "burzum",
			"klonkku",
			"jee"
		};

		for (int i=0; i < t.length; i++)
			System.out.println(String.format("[%d] = %s", i, t[i]));
		System.out.println();

		//jaa = termFrequencyInDocument(t, "dukka");
		jaa = tf_idf(t, t.length, "boo");
		System.out.println("jaa = " + jaa);
	}

}
