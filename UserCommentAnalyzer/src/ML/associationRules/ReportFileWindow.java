/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ReportFileWindow.java
 *
 * Created on Jan 8, 2011, 3:24:07 PM
 */
package ML.associationRules;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileReader;
import javax.swing.SwingUtilities;

/**
 * Report file window.
 *
 * Displays the association rules report file content.
 *
 * @author javiersalcedogomez
 */
public class ReportFileWindow extends javax.swing.JFrame {

    /**
     * String which defines the association rules report file path.
     */
    private static String _fileReport = "src/ML/associationRules/report.txt";
    /**
     * Report file window unique class instance.
     */
    private static ReportFileWindow _instance;

    /**
     * Returns the report file window unique class instance.
     *
     * @return the report file window unique class instance.
     */
    public static ReportFileWindow getInstance() {

        if (_instance == null) {
            _instance = new ReportFileWindow();
        }
        return _instance;
    }

    /** 
     * Creates a new report file window.
     */
    public ReportFileWindow() {

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
     * Updates the report text area.
     */
    public void updateReportTextArea() {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Loads the comments file content
                try {
                    _reportTextPane.read(new FileReader(_fileReport), null);
                } catch (Exception e) {
                    System.out.println("Failed to load file " + _fileReport);
                    System.out.println(e);
                }
            }
        });

    }

    /**
     * Closes the window.
     */
    public void closeWindow() {

        // Disposes the window
        dispose();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        _mainPanel = new javax.swing.JPanel();
        _reportScrollPane = new javax.swing.JScrollPane();
        _reportTextPane = new javax.swing.JTextPane();
        _buttonPanel = new javax.swing.JPanel();
        _closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Association Rules Report Window");
        setBackground(new java.awt.Color(170, 185, 210));
        setMinimumSize(new java.awt.Dimension(550, 600));
        setPreferredSize(new java.awt.Dimension(550, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        _mainPanel.setBackground(new java.awt.Color(170, 185, 210));
        _mainPanel.setForeground(new java.awt.Color(80, 80, 100));
        _mainPanel.setName("_mainPanel"); // NOI18N
        _mainPanel.setLayout(new java.awt.GridBagLayout());

        _reportScrollPane.setName("_reportScrollPane"); // NOI18N

        _reportTextPane.setEditable(false);
        _reportTextPane.setForeground(new java.awt.Color(80, 80, 100));
        _reportTextPane.setToolTipText("Displays the association rules report file content");
        _reportTextPane.setName("_reportTextPane"); // NOI18N
        _reportScrollPane.setViewportView(_reportTextPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        _mainPanel.add(_reportScrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(_mainPanel, gridBagConstraints);

        _buttonPanel.setBackground(new java.awt.Color(170, 185, 210));
        _buttonPanel.setForeground(new java.awt.Color(80, 80, 100));
        _buttonPanel.setName("_buttonPanel"); // NOI18N
        _buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        _closeButton.setBackground(new java.awt.Color(170, 185, 210));
        _closeButton.setForeground(new java.awt.Color(80, 80, 100));
        _closeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/resources/images/icons/Close.png"))); // NOI18N
        _closeButton.setText("Close");
        _closeButton.setToolTipText("Closes the report file window");
        _closeButton.setName("_closeButton"); // NOI18N
        _closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _closeButtonActionPerformed(evt);
            }
        });
        _buttonPanel.add(_closeButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(_buttonPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Close button action performed.
     * 
     * @param evt action event.
     */
    private void _closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__closeButtonActionPerformed

        // Closes the window
        closeWindow();
    }//GEN-LAST:event__closeButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ReportFileWindow().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel _buttonPanel;
    private javax.swing.JButton _closeButton;
    private javax.swing.JPanel _mainPanel;
    private javax.swing.JScrollPane _reportScrollPane;
    private javax.swing.JTextPane _reportTextPane;
    // End of variables declaration//GEN-END:variables

    /**
     * Shows the report file window.
     */
    public void showWindow() {

        setVisible(true);
    }
}
