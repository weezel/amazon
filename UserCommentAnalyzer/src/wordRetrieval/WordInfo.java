/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wordRetrieval;

/**
 * Fetches the user comments to extract the info required to be displayed to
 * the user.
 *
 * @author Arjen
 */
public class WordInfo implements Comparable {

    /**
     * The keyword itself.
     */
    private String _theWord;
    /**
     * Keyword count.
     */
    private int _count;
    /**
     * Keyword rating.
     */
    private float _rating;

    /**
     * Creates a new word info with the parameters received.
     *
     * @param theWord the word itself.
     * @param count keyword count.
     * @param rating keyword rating.
     */
    public WordInfo(String theWord, int count, float rating) {

        _count = count;
        _rating = rating;
        _theWord = theWord;
    }

    @Override
    public int compareTo(Object obj) {

        WordInfo wordInfo = (WordInfo) obj;

        if (this._count > wordInfo._count) {
            /* instance lt received */
            return -1;
        } else if (_count < wordInfo._count) {
            /* instance gt received */
            return 1;
        }
        /* instance == received */
        return 0;
    }

    @Override
    public String toString() {

        return "count: " + _count + ", " + _theWord + ", Rating: " + _rating/_count;
    }



    /**
     * Returns the keyword count.
     *
     * @return the keyword count.
     */
    public int getCount() {
        return _count;
    }

    /**
     * Set a new value to the keyword count.
     *
     * @param count new value to set.
     */
    public void setCount(int count) {
        _count = count;
    }

    /**
     * Returns the keyword count.
     *
     * @return the keyword count.
     */
    public float getRating() {
        return _rating;
    }

    /**
     * Set a new value to the keyword rating.
     *
     * @param rating new value to set.
     */
    public void setRating(float rating) {
        _rating = rating;
    }

    /**
     * Return the word itself.
     *
     * @return the word itself.
     */
    public String getTheWord() {
        return _theWord;
    }

    /**
     * Sets a new value to the word itself.
     *
     * @param theWord new value to set.
     */
    public void setTheWord(String theWord) {
        _theWord = theWord;
    }
}