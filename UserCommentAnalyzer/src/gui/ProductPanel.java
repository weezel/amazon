/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ProductPanel.java
 *
 * Created on Dec 16, 2010, 7:39:35 PM
 */

package gui;

import javax.swing.JList;
import wordRetrieval.WordInfo;

/**
 * Panel which shows the list of keywords related to one product. Also has two
 * buttons to close the product, so it will not be available for further
 * comparissons, or the statistics button to print the results with plots.
 *
 * @author javiersalcedogomez
 */
public class ProductPanel extends javax.swing.JPanel {

    /** 
     * Creates new form ProductPanel.
     */
    public ProductPanel() {

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

        _productScrollPane = new javax.swing.JScrollPane();
        _productList = new javax.swing.JList();
        _separator = new javax.swing.JSeparator();
        _buttonPanel = new javax.swing.JPanel();
        _closeButton = new javax.swing.JButton();
        _statisticsButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Product"));
        setLayout(new java.awt.GridBagLayout());

        _productScrollPane.setName("_productScrollPane"); // NOI18N
        _productScrollPane.setPreferredSize(new java.awt.Dimension(300, 400));

        _productList.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        _productList.setName("_productList"); // NOI18N
        _productList.setVisibleRowCount(-1);
        _productScrollPane.setViewportView(_productList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 239;
        gridBagConstraints.ipady = 205;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(42, 26, 26, 26);
        add(_productScrollPane, gridBagConstraints);

        _separator.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        _separator.setName("_separator"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(_separator, gridBagConstraints);

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

        _statisticsButton.setText("Statistics");
        _statisticsButton.setName("_statisticsButton"); // NOI18N
        _buttonPanel.add(_statisticsButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(_buttonPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Close button action listener. Closes the panel erasing from the 
     * panel list of the main window and updating the main window without it.
     * 
     * @param evt action event.
     */
    private void _closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__closeButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__closeButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel _buttonPanel;
    private javax.swing.JButton _closeButton;
    private javax.swing.JList _productList;
    private javax.swing.JScrollPane _productScrollPane;
    private javax.swing.JSeparator _separator;
    private javax.swing.JButton _statisticsButton;
    // End of variables declaration//GEN-END:variables

    /**
     * Returns the product list which contains the keywords extracted from 
     * the user product comments.
     * 
     * @return the product list which contains the keywords extracted from 
     * the user product comments.
     */
    public JList getProductList() {
        return _productList;
    }

    /**
     * Sets a new value to the product list data.
     *
     * @param keywordList new value to set.
     */
    public void setProductList(WordInfo[] keywordList) {
        _productList.setListData(keywordList);
    }
}