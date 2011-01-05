package wordretrieval;

import java.util.ArrayList;
import java.util.Collections;

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
	public static double termFrequencyInDocument(String doc, String s)
	{
		int matchCount, spos, epos;
		String splitted_comment[];
		ArrayList<String> comment_arr;

		matchCount = spos = epos = 0;
		comment_arr = new ArrayList<String>();

		/* Sort comments */
		splitted_comment = doc.split(":");
		for (int i = 0; i < splitted_comment.length; i++)
			comment_arr.add(splitted_comment[i]);
		Collections.sort(comment_arr);

//		System.out.println(comment_arr.toString());

		/* Rewind to the correct inital position in the array */
		for (int i = 0; i < comment_arr.size(); i++) {
			if (comment_arr.get(i).charAt(0) == s.charAt(0)) {
				spos = i;
				while (i < comment_arr.size() - 1 && comment_arr.get(++i).charAt(0) == s.charAt(0));
				epos = i - 1;
				break;
			}
		}
		/* Only count in words that equals with the parameter 's' */
		for (int i = spos; i <= epos; i++) {
			if (comment_arr.get(i).equals(s)) {
				spos = i;
				while (i < comment_arr.size() - 1 && comment_arr.get(++i).equals(s));
				epos = i - 1;
				break;
			}
		}
		/* The actual counting happens here */
		for (; spos <= epos; spos++) {
			if (comment_arr.get(spos).equals(s))
				matchCount++;
		}
//		System.out.println("matchCount = " + matchCount + ", doc.length = " + comment_arr.size());

		return (double) matchCount / comment_arr.size();
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
	public static double tf_idf(String[] doc, String cmnt, int cmcnt, String searchable)
	{
		int[] searchArea;
		double invtermfreq, termfreq;

		searchArea = filterByInitials(doc, searchable);

		termfreq = termFrequencyInDocument(cmnt, searchable); 
		//termfreq = termFrequencyInDocument(cmnt, searchable);
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


	public static void runTests(String[] args)
	{
		double jaa;
		String[] t = {
			"14sia|1:50",
			"auto|1:50",
			"amiraali|3:40",
			"anjovis|3:10",
			"boredom|",
			"boo|5:10",
			"boo|5:60",
			"boo|6:20",
			"burzum|12:80",
			"klonkku|15:12",
			"jee|18:30"
		};
		String singlecomment = "14sia:boo:boo:boo:banjovis:auto:urzum:klonkku:bamiraali:oredom:jee:";

		/*
		for (int i=0; i < t.length; i++)
			System.out.println(String.format("[%d] = %s", i, t[i]));
		System.out.println();
		*/

		jaa = termFrequencyInDocument(singlecomment, "boo");
		//jaa = tf_idf(t, t.length, "boo");
		System.out.println("jaa = " + jaa);
	}


	public static void main (String[] args)
	{
		runTests(args);
	}

}
