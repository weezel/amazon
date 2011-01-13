/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AssociationRulesWindow.java
 *
 * Created on Jan 6, 2011, 4:01:45 PM
 */
package ML.associationRules;

import ML.associationRules.logic.Mining;
import ML.associationRules.logic.Rule;
import gui.mainWindow.MainWindow;
import gui.waitingWindow.WaitingWindow;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private ArrayList<String> _labels;

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
        _labels = new ArrayList<String>();

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
                    cad += _labels.get(leftSide.get(i)) + " AND ";
                }
                cad += _labels.get(leftSide.get(i)) + " ]";
                cad += "\n\tAlso appears: [ ";
                for (i = 0; i < rightSide.size() - 1; i++) {
                    cad += _labels.get(rightSide.get(i)) + " AND ";
                }
                cad += _labels.get(rightSide.get(i)) + " ]";
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

        // Shows the help file of the application
        try{
        Desktop.getDesktop().open(new File("src/ML/associationRules/report.txt"));
        }
        catch(Exception ex){
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event__showReportButtonActionPerformed

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
     * Generates the boolean _matrix from the product keyword list given as a parameter.
     * 
     * Example:
     * With this productKeywordLists given as a parameter: <<beyonce, dvd,concert,cd,tour>,<celine,concert,dvd,cd,love>>
     *
     * First, we build the labels array list.
     * This array contains all the different elements in all the product keyword lists
     * in the matrix given as a parameter.
     * The _labels array is: <beyonce, dvd, concertm cd, tour, celine, love>
     *
     * The next step is to build the boolean matrix itself.
     * It is like _matrix[productKeywordLists.size()][_labels.size()];
     *
     * And after all the process the result _matrix is:
     * [1,1,1,1,1,0,0
     *  0,1,1,1,0,1,1]
     *
     * Each row represents the product, and each column the keyword. In the
     * example above, for instance, "beyonce" (the first word in the _labels) appears in the first product and
     * it does not appear in the second product.
     * 
     * @param productKeywordLists
     */
    private void generateBooleanMatrix(ArrayList<ArrayList<WordInfo>> productKeywordLists) {

        // Creates the labels
        for (int i = 0; i < productKeywordLists.size(); i++) {
            for (int j = 0; j < productKeywordLists.get(i).size(); j++) {

                // Without repetitions
                if(!_labels.contains(productKeywordLists.get(i).get(j).getTheWord()))
                    _labels.add(productKeywordLists.get(i).get(j).getTheWord());
            }
        }

        // DEBUG
        System.out.println("-------------------");
        System.out.print("\nList of labels: ");
        for (int i = 0; i < _labels.size(); i++)
            System.out.print(_labels.get(i) + ", ");
        System.out.println();

        // Create the matrix with the correct size
        _matrix = new Integer[productKeywordLists.size()][_labels.size()];
        boolean found = false;

        // Builds the boolean matrix
        for (int labelIndex = 0; labelIndex < _labels.size(); labelIndex++) {
            
            for (int keywordsIndex = 0; keywordsIndex < productKeywordLists.size(); keywordsIndex++) {

                found = false;

                // Looks for the word in the product list
                for(int index = 0; index < productKeywordLists.get(keywordsIndex).size(); index++){
                    if(productKeywordLists.get(keywordsIndex).get(index).getTheWord().matches(_labels.get(labelIndex)))
                        found = true;
                }

                // If exists
                if(found)
                    // Put 1
                    _matrix[keywordsIndex][labelIndex] = 1;
                else
                    // Put 0
                    _matrix[keywordsIndex][labelIndex] = 0;
            }
        }

        // DEBUG
        System.out.println("-------------------");
        System.out.println("\nBoolean matrix: ");
        System.out.print("[ ");
        for (int i = 0; i < _matrix.length; i++) {

            for (int j = 0; j < _matrix[i].length; j++) {

                if((i == _matrix.length - 1) && (j == _matrix[i].length - 1))
                    System.out.print(_matrix[i][j]);
                else
                    System.out.print(_matrix[i][j] + ", ");
            }
            System.out.println();
        }
        System.out.print(" ]");
    }
}
