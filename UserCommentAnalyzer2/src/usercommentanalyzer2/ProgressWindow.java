/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ProgressWindow.java
 *
 * Created on Nov 19, 2010, 1:34:34 PM
 */

package usercommentanalyzer2;

import javax.swing.JFrame;

/**
 * Fetching comment analysis window. Displays a progress bar which shows the
 * process status and gives the chance to the user to stop the process pressing
 * the stop process button.
 *
 * @author javiersalcedogomez
 */
public class ProgressWindow extends JFrame {

    /**
     * Class instance.
     */
    private static ProgressWindow _instance;

    /**
     * Returns the unique class instance.
     *
     * @return the unique class instace.
     */
    public static ProgressWindow getInstance(){

        if(_instance != null)
            _instance = new ProgressWindow();
        return _instance;
    }

    /** 
     * Creates new form ProgressWindow.
     */
    public ProgressWindow() {
        initComponents();
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

        _progressPanel = new javax.swing.JPanel();
        _progressBar = new javax.swing.JProgressBar();
        _stopProcessButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(usercommentanalyzer2.UserCommentAnalyzer2App.class).getContext().getResourceMap(ProgressWindow.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        _progressPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("_progressPanel.border.title"))); // NOI18N
        _progressPanel.setName("_progressPanel"); // NOI18N
        _progressPanel.setLayout(new java.awt.GridBagLayout());

        _progressBar.setName("_progressBar"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 150;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        _progressPanel.add(_progressBar, gridBagConstraints);

        _stopProcessButton.setText(resourceMap.getString("_stopProcessButton.text")); // NOI18N
        _stopProcessButton.setName("_stopProcessButton"); // NOI18N
        _stopProcessButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _stopProcessButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        _progressPanel.add(_stopProcessButton, gridBagConstraints);

        getContentPane().add(_progressPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void _stopProcessButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__stopProcessButtonActionPerformed
        // TODO add your handling code here:
        // STOPS THE PROCESS

    }//GEN-LAST:event__stopProcessButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar _progressBar;
    private javax.swing.JPanel _progressPanel;
    private javax.swing.JButton _stopProcessButton;
    // End of variables declaration//GEN-END:variables

    /**
     * Shows the progress window.
     */
    public void shows() {
        setVisible(true);
    }

    /**
     * Closes the progress window.
     */
    public void close(){
        dispose();
    }

    /**
     * Updates the progress bar with the current value.
     *
     * @param value new value to set.
     */
    public void update(String value){
        _progressBar.setValue(Integer.parseInt(value));
    }
}