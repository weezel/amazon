/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package process;

import gui.mainWindow.MainWindow;
import gui.waitingWindow.WaitingWindow;

/**
 * Executes the applying filter process to update the product list displayed in
 * the main window.
 * 
 * @author javiersalcedogomez
 */
public class ApplyFilterProcess extends Thread{

    /**
     * Creates a new apply filter process.
     */
    public ApplyFilterProcess(){
        
    }

    @Override
    public void run(){

        // Shows the waiting window
        WaitingWindow.getInstance().showWaitingWindow();

        // Applies the filter in the main window
        MainWindow.getInstance().applyFilter();

        // Closes the waiting window
        WaitingWindow.getInstance().closeWaitingWindow();
    }
}
