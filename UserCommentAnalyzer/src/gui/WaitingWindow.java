/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

/**
 * Waiting window to display when a process is being processed.
 *
 * @author javiersalcedogomez
 */
public class WaitingWindow extends JWindow{

    /**
     * Waiting window unique class instance.
     */
    private static WaitingWindow _instance;
    /**
     * Label which displays an animated hourglass.gif.
     */
    private JLabel _image;
    /**
     * Label which displays an informative message to the user.
     */
    private JLabel _message;

    /**
     * Returns the waiting window unique class instance.
     * 
     * @return the waiting window unique class instance.
     */
    public static WaitingWindow getInstance(){

        if(_instance == null)
            _instance = new WaitingWindow();
        return _instance;
    }

    /**
     * Creates a new waiting window.
     */
    public WaitingWindow(){

        initComponents();

        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = getSize().width;
        int h = getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;

        // Move the window
        setLocation(x, y);
    }

    /**
     * Inits the waiting window components.
     */
    private void initComponents() {

        JPanel content = (JPanel) getContentPane();

        // Sets the layout
        content.setLayout(new GridBagLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // IMAGE
        _image = new JLabel(new ImageIcon("src/gui/resources/images/hourglass.gif"));
        
        // MESSAGE
        _message = new JLabel("Wait a few seconds until the process is finished...");

        // Adds the components to the window with the layout
        GridBagConstraints constraints = new GridBagConstraints();
        
        // Adds the image to the panel
        constraints.insets = new Insets(5,5,5,5);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 0;
        content.add(_image, constraints);
        
         // Adds the message to the panel
        constraints.gridy = 1;
        content.add(_message, constraints);

        pack();
    }

    /**
     * Shows the waiting window.
     */
    public void showWaitingWindow(){      
        setVisible(true);
    }

    /**
     * Closes the waiting window.
     */
    public void closeWaitingWindow(){
        dispose();
    }

    /**
     * Main method of the application that shows the Main Window of
     * the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Shows the input data window
                WaitingWindow.getInstance().setVisible(true);
            }
        });
    }
}
