/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AssociationRulesWindow.java
 *
 * Created on Jan 6, 2011, 4:01:45 PM
 */
package associationRules;

import associationRules.logic.Mining;
import associationRules.logic.Rule;
import gui.waitingWindow.WaitingWindow;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import wordRetrieval.WordInfo;

/**
 * Association rules window.
 *
 * Executes the algoritm and displays the results in the panel.
 *
 * @author javiersalcedogomez
 */
public class AssociationRulesWindow extends javax.swing.JFrame {

    /**
     * Association rules window unique class instance.
     */
    private static AssociationRulesWindow _instance;
    /**
     * Object for the association rules generation.
     */
    private Mining _mining;
    /**
     * Boolean matrix which contains the association rules data to extract from.
     */
    private Integer[][] _matrix;
    /**
     * Represents the keyword labels to display in the rules panel which translates
     * the indexes generated in the algorithm.
     */
    private ArrayList<String> _keywordName;

    /**
     * Returns the association rules window unique class instance.
     *
     * @return the association rules window unique class instance.
     */
    public static AssociationRulesWindow getInstance() {

        if (_instance == null) {
            _instance = new AssociationRulesWindow();
        }
        return _instance;
    }

    /** 
     * Creates a new form AssociationRulesWindow
     */
    public AssociationRulesWindow() {

        // Creates the keyword names to display the rules
        _keywordName = new ArrayList<String>();

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
     * Shows the association rules window, initializing the output first.
     */
    public void showAssociationRulesWindow(ArrayList<ArrayList<WordInfo>> keywordList) {

        // Generates the boolean matrix
        generateBooleanMatrix(keywordList);

        // Clears the rules text area
        _rulesTextArea.setText("");

        // Creates the object for mining with the output as display component
        _mining = new Mining(_matrix);

        // Disables the show report button
        _showReportButton.setEnabled(false);

        // Shows the association rules window
        setVisible(true);
    }

    /**
     * Generates the association rules and updates the output panel.
     */
    public void generateAssociationRules() {

        // Gets the minimum support from the text field
        int minimumSupport = Integer.parseInt(_minSupportTextField.getText());

        // Gets the minimum confidence from the text field
        double minimumConfidence = Double.parseDouble(_minConfidenceTextField.getText());

        // Gets the maximum number of rules displayed from the text field
        int maximumRulesDisplayed = Integer.parseInt(_maxNumRulesDisplayedTextField.getText());

        // Creates the buffered writer
        _mining.createBufferedWriter();

        // Executes the algorithm
        _mining.APrioriAlgorithm(minimumSupport);

        // Generate the rules
        Vector<Rule> rules = _mining.generateAssociationRules(minimumConfidence);

        // Show the the keywords
        String cad = "";

        // Print the rules
        Vector<Integer> leftSide = null, rightSide = null;

        if (rules.size() > 0) {

            // In order to avoid exceptions we take the minimum value between 
            // rulesList.size and the user parameter
            int parameter = maximumRulesDisplayed;
            if(rules.size() < parameter)
                parameter = rules.size();

            int i = 0, l = 0;
            for (l = 0; l < parameter; l++) {
                leftSide = rules.get(l).getLeftSide();
                rightSide = rules.get(l).getRightSide();
                cad += "\n- If appears:[ ";

                for (i = 0; i < leftSide.size() - 1; i++) {
                    cad += _keywordName.get(leftSide.get(i)) + " AND ";
                }
                cad += _keywordName.get(leftSide.get(i)) + " ]";
                cad += "\n\tAlso appears: [ ";
                for (i = 0; i < rightSide.size() - 1; i++) {
                    cad += _keywordName.get(rightSide.get(i)) + " AND ";
                }
                cad += _keywordName.get(rightSide.get(i)) + " ]";
            }

            // Sets the text with the rules
            _rulesTextArea.setText(cad);

            // Updates the report file window with the new text
            ReportFileWindow.getInstance().updateReportTextArea();

            // Enables the show report button
            _showReportButton.setEnabled(true);

            // Closes the buffered writer
            _mining.closeBufferedWriter();

        }else

            // No results to display message
            JOptionPane.showMessageDialog(this, "There are no rules to display", "Warning", JOptionPane.WARNING_MESSAGE);
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

        _controlPanel = new javax.swing.JPanel();
        _minSupportTextField = new javax.swing.JTextField();
        _minSupportLabel = new javax.swing.JLabel();
        _minConfidenceTextField = new javax.swing.JTextField();
        _minimumConfidenceLabel = new javax.swing.JLabel();
        _executeButton = new javax.swing.JButton();
        _maxNumRulesDisplayedLabel = new javax.swing.JLabel();
        _maxNumRulesDisplayedTextField = new javax.swing.JTextField();
        _showReportButton = new javax.swing.JButton();
        _displayPanel = new javax.swing.JPanel();
        _rulesPanel = new javax.swing.JPanel();
        _rulesScrollPane = new javax.swing.JScrollPane();
        _rulesTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Association Rules Window");
        setBackground(new java.awt.Color(170, 185, 210));
        setPreferredSize(new java.awt.Dimension(529, 556));

        _controlPanel.setBackground(new java.awt.Color(170, 185, 210));
        _controlPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Control Panel", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Lucida Grande", 0, 13), new java.awt.Color(80, 80, 100))); // NOI18N
        _controlPanel.setName("_controlPanel"); // NOI18N
        _controlPanel.setLayout(new java.awt.GridBagLayout());

        _minSupportTextField.setForeground(new java.awt.Color(80, 80, 100));
        _minSupportTextField.setText("2");
        _minSupportTextField.setName("_minSupportTextField"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        _controlPanel.add(_minSupportTextField, gridBagConstraints);

        _minSupportLabel.setForeground(new java.awt.Color(80, 80, 100));
        _minSupportLabel.setText("Minimum Support:");
        _minSupportLabel.setName("_minSupportLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        _controlPanel.add(_minSupportLabel, gridBagConstraints);

        _minConfidenceTextField.setForeground(new java.awt.Color(80, 80, 100));
        _minConfidenceTextField.setText("75.0");
        _minConfidenceTextField.setName("_minConfidenceTextField"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        _controlPanel.add(_minConfidenceTextField, gridBagConstraints);

        _minimumConfidenceLabel.setForeground(new java.awt.Color(80, 80, 100));
        _minimumConfidenceLabel.setText("Minimum Confidence:");
        _minimumConfidenceLabel.setName("_minimumConfidenceLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        _controlPanel.add(_minimumConfidenceLabel, gridBagConstraints);

        _executeButton.setBackground(new java.awt.Color(170, 185, 210));
        _executeButton.setForeground(new java.awt.Color(80, 80, 100));
        _executeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/resources/images/icons/run.png"))); // NOI18N
        _executeButton.setText("Execute Algorithm");
        _executeButton.setName("_executeButton"); // NOI18N
        _executeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _executeButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        _controlPanel.add(_executeButton, gridBagConstraints);

        _maxNumRulesDisplayedLabel.setForeground(new java.awt.Color(80, 80, 100));
        _maxNumRulesDisplayedLabel.setText("Maximum number of Rules displayed:");
        _maxNumRulesDisplayedLabel.setName("_maxNumRulesDisplayedLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        _controlPanel.add(_maxNumRulesDisplayedLabel, gridBagConstraints);

        _maxNumRulesDisplayedTextField.setText("10");
        _maxNumRulesDisplayedTextField.setName("_maxNumRulesDisplayedTextField"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 30;
        _controlPanel.add(_maxNumRulesDisplayedTextField, gridBagConstraints);

        _showReportButton.setBackground(new java.awt.Color(170, 185, 210));
        _showReportButton.setForeground(new java.awt.Color(80, 80, 100));
        _showReportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/resources/images/icons/Show.png"))); // NOI18N
        _showReportButton.setText("Show Report File");
        _showReportButton.setName("_showReportButton"); // NOI18N
        _showReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _showReportButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        _controlPanel.add(_showReportButton, gridBagConstraints);

        getContentPane().add(_controlPanel, java.awt.BorderLayout.NORTH);

        _displayPanel.setName("_displayPanel"); // NOI18N
        _displayPanel.setLayout(new java.awt.GridBagLayout());

        _rulesPanel.setBackground(new java.awt.Color(170, 185, 210));
        _rulesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Rules Panel", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Lucida Grande", 0, 13), new java.awt.Color(80, 80, 100))); // NOI18N
        _rulesPanel.setName("_rulesPanel"); // NOI18N
        _rulesPanel.setLayout(new java.awt.BorderLayout());

        _rulesScrollPane.setName("_rulesScrollPane"); // NOI18N

        _rulesTextArea.setColumns(20);
        _rulesTextArea.setForeground(new java.awt.Color(80, 80, 100));
        _rulesTextArea.setRows(5);
        _rulesTextArea.setTabSize(3);
        _rulesTextArea.setToolTipText("Displays the obtained rules with format");
        _rulesTextArea.setName("_rulesTextArea"); // NOI18N
        _rulesScrollPane.setViewportView(_rulesTextArea);

        _rulesPanel.add(_rulesScrollPane, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        _displayPanel.add(_rulesPanel, gridBagConstraints);

        getContentPane().add(_displayPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Execute button action performed.
     *
     * @param evt action event.
     */
    private void _executeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__executeButtonActionPerformed

        new AssociationRulesProcess().start();
    }//GEN-LAST:event__executeButtonActionPerformed

    /**
     * Show report button action performed.
     * 
     * @param evt action event.
     */
    private void _showReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__showReportButtonActionPerformed

        // Shows the report window
        ReportFileWindow.getInstance().showWindow();
    }//GEN-LAST:event__showReportButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new AssociationRulesWindow().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel _controlPanel;
    private javax.swing.JPanel _displayPanel;
    private javax.swing.JButton _executeButton;
    private javax.swing.JLabel _maxNumRulesDisplayedLabel;
    private javax.swing.JTextField _maxNumRulesDisplayedTextField;
    private javax.swing.JTextField _minConfidenceTextField;
    private javax.swing.JLabel _minSupportLabel;
    private javax.swing.JTextField _minSupportTextField;
    private javax.swing.JLabel _minimumConfidenceLabel;
    private javax.swing.JPanel _rulesPanel;
    private javax.swing.JScrollPane _rulesScrollPane;
    private javax.swing.JTextArea _rulesTextArea;
    private javax.swing.JButton _showReportButton;
    // End of variables declaration//GEN-END:variables

    /**
     * Generates the boolean matrix from a keyword list given as a parameter.
     * 
     * The list contains productList.size() components and each one of the
     * components contains the 10 best words for that product.
     * 
     * In order to generate the matrix, we go throught all the keywords in each 
     * component generating at the end a matrix like:
     * 
     *                  -- productList.size() --
     *              [                           
     *                                              |
     *                                              |
     *                                              ∑productList.size()
     *                                              |
     *                                              |
     * 
     *                                          ]
     * Each value contains 1 (if the element appears in this product) or 
     * 
     * 
     * @param matrix
     */
    private void generateBooleanMatrix(ArrayList<ArrayList<WordInfo>> matrix) {

        int currentList = 0;
        boolean found = false;

        // Creates the columns
        ArrayList<ArrayList<Integer>> resultMatrix = new ArrayList<ArrayList<Integer>>();

        for (int i = 0; i < matrix.size(); i++) {

            // Creates the rows
            ArrayList<Integer> row = new ArrayList<Integer>();

            for (int j = 0; j < matrix.get(i).size(); j++) {

                // Gets the word to look for
                String word = matrix.get(i).get(j).getTheWord();

                currentList = 0;

                // Looks for it in the other products
                while (currentList < matrix.size()) {

                    // If the current list is
                    if (currentList == i) {

                        // Put 1 in the boolean matrix
                        row.add(1);

                        currentList++;
                    } else {

                        found = false;

                        // Look for the keyword in the other lists
                        for (int k = 0; k < matrix.get(currentList).size(); k++) {

                            // If the product contains the word
                            if (matrix.get(currentList).get(k).getTheWord().matches(word)) {
                                found = true;
                            }
                        }


                        if (found) {
                            // Put 1 in the boolean matrix
                            row.add(1);
                        } else {
                            // Put 0 in the boolean matrix
                            row.add(0);
                        }

                        currentList++;
                    }
                }
            }

            // Adds the rows
            resultMatrix.add(row);
        }

        System.out.print("-----\nThe boolean matrix");

        for (int i = 0; i < resultMatrix.size(); i++) {

            System.out.println("Product " + i);
            for (int j = 0; j < resultMatrix.get(i).size(); j++) {
                System.out.println(resultMatrix.get(i).get(j) + ", ");
            }
            System.out.println();
        }

        // Builds the boolean matrix
        _matrix = new Integer[resultMatrix.size()][resultMatrix.get(0).size()];
        for (int i = 0; i < _matrix.length; i++) {
            for (int j = 0; j < _matrix[i].length; j++) {
                _matrix[i][j] = resultMatrix.get(i).get(j);
            }
        }

        // Builds the keyword names to display in the rules
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                _keywordName.add(matrix.get(i).get(j).getTheWord());
            }
        }
    }
}
