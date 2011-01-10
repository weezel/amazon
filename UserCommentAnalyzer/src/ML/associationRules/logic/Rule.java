/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ML.associationRules.logic;

import java.util.Vector;

/**
 * Handle the UCA rules.
 *
 * @author javiersalcedogomez
 */
public class Rule {

    /**
     * Rule left side.
     */
    Vector<Integer> _leftSide;

    /**
     * Rule right side.
     */
    Vector<Integer> _rightSide;

    /**
     * Rule Confidence.
     */
    double _confidence;

    /**
     * Creates a new rule.
     */
    public Rule() {
    }

    /**
     * Returns the rule left side.
     * 
     * @return the rule left side.
     */
    public Vector<Integer> getLeftSide() {
        return this._leftSide;
    }

    /**
     * Returns the rule right side.
     *
     * @return the rule right side.
     */
    public Vector<Integer> getRightSide() {
        return this._rightSide;
    }

    @Override
    public String toString() {
        String cad = "";
        int i = 0;
        for (i = 0; i < _leftSide.size() - 1; i++) {
            cad += "i" + _leftSide.get(i) + " AND ";
        }
        cad += "i" + _leftSide.get(i);
        cad += " -> ";
        for (i = 0; i < _rightSide.size() - 1; i++) {
            cad += "i" + _rightSide.get(i) + " AND ";
        }
        cad += "i" + _rightSide.get(i);
        cad += "  conf: " + _confidence;
        return cad;
    }
}
