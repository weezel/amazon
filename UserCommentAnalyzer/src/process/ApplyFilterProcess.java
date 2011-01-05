/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package process;

import gui.mainWindow.MainWindow;

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
        MainWindow.getInstance().applyFilter();
    }
}
