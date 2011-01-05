/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import gui.mainWindow.MainWindow;
import java.io.File;
import javax.swing.SwingUtilities;

/**
 * Main class of the application. Shows the Main Window of the application.
 * 
 * @author javiersalcedogomez
 */
public class Main {

    /**
     * Main method of the application that shows the Main Window of
     * the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {

        // Gets the current directory
        File directory = new File(".");

        // Gets the directory file list
        String[] files = directory.list();

        // If any
        if(files != null){

            for(int i = 0 ; i < files.length; i++){

                // Gets the file
                File file = new File(files[i]);

                // If the file name contains comments
                if(file.getName().contains("comments"))

                    // Deletes it
                    file.delete();
            }
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Shows the input data window
                MainWindow.getInstance().setVisible(true);
            }
        });
    }
}