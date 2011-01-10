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

        // Shows the waiting window
        WaitingWindow.getInstance().showWaitingWindow();

        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                AssociationRulesWindow.getInstance().generateAssociationRules();
            }
        });
        
        // Closes the waiting window
        WaitingWindow.getInstance().closeWaitingWindow();
    }
}
