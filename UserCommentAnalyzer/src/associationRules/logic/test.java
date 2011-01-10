/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package associationRules.logic;

import associationRules.AssociationRulesWindow;

/**
 *
 * @author Administrador
 */
public class test {

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        Integer[][] matrix = {{1, 1, 0, 0, 0, 1},
            {1, 0, 0, 1, 1, 0},
            {0, 1, 0, 1, 1, 0},
            {1, 0, 1, 1, 0, 0},
            {0, 1, 0, 0, 1, 1},
            {1, 1, 1, 1, 0, 0},
            {1, 1, 0, 1, 1, 0},
            {0, 1, 0, 0, 1, 0},
            {0, 0, 1, 1, 0, 1},
            {1, 0, 1, 1, 0, 0},};
       
        //Mining mina=new Mining(matriz, AssociationRulesWindow.getInstance().getOutput());
        Mining mining = new Mining(matrix);
        mining.APrioriAlgorithm(2);
        mining.generateAssociationRules(75.0);
    }
}
