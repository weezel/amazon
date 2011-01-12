/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ML.associationRules;

import gui.waitingWindow.WaitingWindow;
import javax.swing.SwingUtilities;

/**
 * Handles the association rules generation in a different thread.
 *
 * @author javiersalcedogomez
 */
public class AssociationRulesProcess extends Thread {

    /**
     * Creates a new association rules process.
     */
    public AssociationRulesProcess() {

    }

    @Override
    public void run() {

        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {

                // Generate the association rules
                AssociationRulesWindow.getInstance().generateAssociationRules();
            }
        });
    }
}
