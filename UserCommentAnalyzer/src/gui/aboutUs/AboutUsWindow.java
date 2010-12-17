/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AboutUsWindow.java
 *
 * Created on Dec 16, 2010, 1:16:42 AM
 */

package gui.aboutUs;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author javiersalcedogomez
 */
public class AboutUsWindow extends javax.swing.JFrame {

    /** 
     * Creates new form AboutUsWindow.
     */
    public AboutUsWindow() {

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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        _imageLabel = new javax.swing.JLabel();
        _infoPanel = new javax.swing.JPanel();
        _authorsLabel = new javax.swing.JLabel();
        _villeLabel = new javax.swing.JLabel();
        _arjenLabel = new javax.swing.JLabel();
        _javierLabel = new javax.swing.JLabel();
        _homeSiteLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        _buttonPanel = new javax.swing.JPanel();
        _closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("About Us");

        _imageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/resources/userCommentAnalyzer.png"))); // NOI18N
        _imageLabel.setName("_imageLabel"); // NOI18N
        getContentPane().add(_imageLabel, java.awt.BorderLayout.NORTH);

        _infoPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        _infoPanel.setName("_infoPanel"); // NOI18N
        _infoPanel.setLayout(new java.awt.GridBagLayout());

        _authorsLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        _authorsLabel.setText("Authors:");
        _authorsLabel.setName("_authorsLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        _infoPanel.add(_authorsLabel, gridBagConstraints);

        _villeLabel.setText("- Ville Valkonen");
        _villeLabel.setName("_villeLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 17, 0, 17);
        _infoPanel.add(_villeLabel, gridBagConstraints);

        _arjenLabel.setText("- Arjen Meurers");
        _arjenLabel.setName("_arjenLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 18, 0, 18);
        _infoPanel.add(_arjenLabel, gridBagConstraints);

        _javierLabel.setText("- Javier Salcedo");
        _javierLabel.setName("_javierLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 18, 0, 18);
        _infoPanel.add(_javierLabel, gridBagConstraints);

        _homeSiteLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        _homeSiteLabel.setText("Home Site:");
        _homeSiteLabel.setName("_homeSiteLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 0);
        _infoPanel.add(_homeSiteLabel, gridBagConstraints);

        jLabel1.setText("http://github.com/2ID25work/amazon");
        jLabel1.setName("jLabel1"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        _infoPanel.add(jLabel1, gridBagConstraints);

        getContentPane().add(_infoPanel, java.awt.BorderLayout.CENTER);

        _buttonPanel.setName("_buttonPanel"); // NOI18N
        _buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        _closeButton.setText("Close");
        _closeButton.setName("_closeButton"); // NOI18N
        _closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _closeButtonActionPerformed(evt);
            }
        });
        _buttonPanel.add(_closeButton);

        getContentPane().add(_buttonPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Close the about us window.
     * 
     * @param evt action event.
     */
    private void _closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__closeButtonActionPerformed

        // Closes the window
        dispose();
    }//GEN-LAST:event__closeButtonActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AboutUsWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel _arjenLabel;
    private javax.swing.JLabel _authorsLabel;
    private javax.swing.JPanel _buttonPanel;
    private javax.swing.JButton _closeButton;
    private javax.swing.JLabel _homeSiteLabel;
    private javax.swing.JLabel _imageLabel;
    private javax.swing.JPanel _infoPanel;
    private javax.swing.JLabel _javierLabel;
    private javax.swing.JLabel _villeLabel;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

}