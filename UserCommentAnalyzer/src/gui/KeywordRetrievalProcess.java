/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

/**
 * Executes the keyword retrieval process to extract the keyword list from 
 * the product to be displayed in the main window.
 *
 * @author javiersalcedogomez
 */
class KeywordRetrievalProcess extends Thread {

    /**
     * Creates a new keyword retrieval process.
     */
    public KeywordRetrievalProcess() {
    }

    @Override
    public void run() {

        // Shows the waiting window
        WaitingWindow.getInstance().showWaitingWindow();

        // Updates the keyword list with the new product
        MainWindow.getInstance().updatesKeywordList();

        // Builds the results panel
        MainWindow.getInstance().buildResultsPanel();

        // Closes the waiting window
        WaitingWindow.getInstance().closeWaitingWindow();
    }
}
