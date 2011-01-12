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
    private String _rating;

    private double _tfrating;

    private int _inProductList;

    /**
     * Creates a new word info with the parameters received.
     *
     * @param theWord the word itself.
     * @param count keyword count.
     * @param rating keyword rating.
     */
    public WordInfo(String theWord, int count, String rating, double tfrating, int inProductList) {
        _count = count;
        _rating = rating;
        _theWord = theWord;
        _tfrating = tfrating;
        _inProductList = inProductList;
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
        String pd = "";
        switch (_inProductList) {
            case 1:
                pd = "+";
                break;
            case 2:
                pd = "-";
                break;
        }

        return String.format("%d: %-15s %-4s [%3.3f] %s",
                    _count, _theWord, _rating, _tfrating, pd);
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
    public String getRating() {
        return _rating;
    }

    /**
     * Set a new value to the keyword rating.
     *
     * @param rating new value to set.
     */
    public void setRating(String rating) {
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

    /**
     *  hii
     */
    public double getTFRating() {
        return _tfrating;
    }

    public void setTFScore(double d) {
        _tfrating = d;
    }


    public int getinProductList() {
        return _inProductList;
    }

    public void setinProductList(int d) {
        _inProductList = d;
    }
}
