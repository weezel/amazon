/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package associationRules.logic;

import java.util.Vector;

/**
 * Handles the rule frequency.
 *
 * @author javiersalcedogomez
 */
public class Frequency {

    /**
     * Item set.
     */
    Vector<Integer> _itemSet;

    /**
     * Total frequency.
     */
    int _totalFrecuency;

    /**
     * Creates a new frequency.
     */
    public Frequency() {
        _itemSet = new Vector();
        _totalFrecuency = 0;
    }

    @Override
    public String toString() {
        String cad = "";
        cad += "" + _itemSet + ", " + _totalFrecuency;
        return cad;
    }
}
