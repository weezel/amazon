package wordretrieval;

import java.util.ArrayList;
import java.util.Collections;

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

	/*
	 * Count how many times searhcable word appears in a comment and divide by
	 * the total count of the words in the comment.
	 * @return term frequency in comment
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

		return (double)matchCount / (double)comment_arr.size();
	}


	/*
	 * Count how many times searhcable word appears in all comments and
	 * calculate the inverse frequency.
	 * @return inverse term frequency in document
	 */
	public static double inverseDocumentFrequency(String[] doc, String s)
	{
		int matchCount, spos, epos;
		int[] searchArea;

		matchCount = 0;
		searchArea = filterByInitials(doc, s); // Reduces search space dramatically

		if (searchArea == null)
			return (0.0);

		spos = searchArea[0];
		epos = searchArea[1];

		/* Count matches in all comments */
		for (; spos <= epos; spos++) {
			String tmp[] = doc[spos].split("\\|");
			if (tmp.length > 0 && tmp[0].equals(s))
				matchCount++;
		}

		if (matchCount <= 0)
			return 0.0;

		return Math.log10((double)doc.length / (double)matchCount);
	}


	/*
	 * Reduce search area to the same initial and afterwards, only to words
	 * that equals "s".
	 * @return start and ending position for the same word
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
		r[0] = spos;
		r[1] = epos;

		return r;
	}


	public static double tfidf_score(double a, double b)
	{
		return a * b;
	}


	public static void runTests(String[] args)
	{
		double termfreq, invfreq;
		String boo = "boo";
		String[] documents1 = {
			"14sia|1:50",
			"auto|1:50",
			"amiraali|3:40",
			"anjovis|3:10",
			"boredom|4:55",
			"boo|5:10",
			"boo|5:60",
			"boo|6:20",
			"burzum|12:80",
			"klonkku|15:12",
			"jee|18:30"
		};
		String singlecomment = "14sia:boo:boo:boo:banjovis:auto:urzum:klonkku:bamiraali:oredom:jee:";
		String[] documents2 = {
			"bono:boo:hanjovis:kissa:koira:",
			"14sia:boo:boo:boo:banjovis:auto:urzum:klonkku:bamiraali:oredom:jee:",
			"jerkku:hekkku:takki:pakki:",
			"joona:erkki:merkki:",
			"cat:sack:hack:track:back:"
		};

		termfreq = termFrequencyInDocument(singlecomment, "boo");
		assert (Math.abs(termfreq - 0.2727272727272727) < 1.0E-8) : "termfreq should be: 0.2727272727272727, current value: " + termfreq;
		invfreq = inverseDocumentFrequency(documents1, "boo");
		assert (Math.abs(invfreq - 0.5642714304385625) < 1.0E-8) : "invfreq should be: 0.5642714304385625, current value: " + invfreq;
		System.out.println("'" + boo + "'" + " comment frequenzy: " + termfreq);
		System.out.println("'" + boo + "'" + " inverse frequenzy: " + invfreq);
		System.out.println("'" + boo + "'" + " tf-idf score     : " + tfidf_score(termfreq, invfreq));
	}


	public static void main (String[] args)
	{
		runTests(args);
	}

}
