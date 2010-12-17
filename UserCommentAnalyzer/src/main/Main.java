/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import gui.MainWindow;
import java.awt.EventQueue;

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
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Shows the input data window
                new MainWindow().setVisible(true);
            }
        });
    }
}