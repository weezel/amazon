/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ML.associationRules.logic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the association rules generation using the a priori algorithm.
 * 
 * @author javiersalcedogomez
 */
public class Mining {

    /**
     * String which defines the association rules report file path.
     */
    private static String _fileReport = "src/associationRules/report.txt";
    /**
     * Writes the report in the report file.
     */
    private BufferedWriter _bufferedWriter;
    /**
     * ItemSet transaction.
     */
    private Vector<Integer>[] _TID;
    /**
     * Minimum support.
     */
    private int _minimumSupport;
    /**
     * Number of transactions and items.
     */
    private int _numberOfTransactions, _numberOfItems;
    /**
     * Strong itemsets.
     */
    private Vector<Frequency> _strongItemsets;
    /**
     * Strong rules.
     */
    private Vector<Rule> _strongRules;
    /**
     * Boolean matrix.
     */
    private Integer[][] _booleanMatrix;

    /**
     * Creates a new mining object to executes and display the algorithm.
     *
     * @param booleanMatrix boolean matrix with 1's and 0's.
     */
    public Mining(Integer[][] booleanMatrix) {

        _booleanMatrix = booleanMatrix;
        _numberOfTransactions = booleanMatrix.length;
        _numberOfItems = booleanMatrix[0].length;
    }

    /**
     * A priori algorithm.
     *
     * @param minimumSupport minimum support for the algorithm.
     *
     * @return frequency vector.
     */
    public Vector<Frequency> APrioriAlgorithm(int minimumSupport) {

        // Updates the minimum support
        _minimumSupport = minimumSupport;
        Vector<Frequency> Ck = null;
        Vector<Frequency> Lk = null;
        Vector<Frequency> Lk2 = null;
        Ck = generateFirst(); //K=1
        Lk = debug(Ck);
        report(1, Ck, Lk);
        Ck = generateSecond(Lk); //K=2
        Lk2 = debug(Ck);
        report(2, Ck, Lk2);
        int k = 3;
        while (Lk2.size() > 1) {
            Lk = Lk2;
            Ck = generateJoin(k, Lk2);
            Lk2 = debug(Ck);
            report(k, Ck, Lk2);
            k++;
        }
        if (Lk2.size() == 1) {
            _strongItemsets = Lk2;
        } else {
            _strongItemsets = Lk;
        }
        display("\nResult: \n");
        reportVector(_strongItemsets);

        return _strongItemsets;
    }

    /**
     * Generate the TID for the transactions based in the boolean matrix.
     *
     * @param booleanMatrix boolean matrix to be analyzed.
     */
    private void generateTID(Integer[][] booleanMatrix) {
        
        this._TID = new Vector[_numberOfTransactions];
        display("TID:" + "\n");
        for (int i = 0; i < _numberOfTransactions; i++) {
            display("T[" + i + "]:");
            _TID[i] = new Vector();
            for (int j = 0; j < _numberOfItems; j++) {
                if (booleanMatrix[i][j] == 1) {
                    _TID[i].add(j);
                }
            }
            display("" + _TID[i] + "\n");
        }
    }

    /**
     * Generate the first iteration of the algorithm.
     *
     * @return frequency vector with the most frequent items.
     */
    private Vector<Frequency> generateFirst() {
        Vector<Frequency> Ck = new Vector();
        Frequency Fr = null;
        for (int i = 0; i < _numberOfItems; i++) {
            Fr = new Frequency();
            Fr._itemSet.add(i);
            Fr._totalFrecuency = getTotalFrecuency(Fr._itemSet);
            Ck.add(Fr);
        }
        return Ck;
    }

    /**
     * Generates the second iteration of the algorithm.
     *
     * @param Lk most frequent item set obtained in the first iteration.
     *
     * @return frequency vector with the most frequent items.
     */
    private Vector<Frequency> generateSecond(Vector<Frequency> Lk) {
        Vector<Frequency> Ck = new Vector();
        Vector<Integer> A = null, B = null;
        Frequency Fr = null;
        for (int i = 0; i < Lk.size(); i++) {
            for (int j = i + 1; j < Lk.size(); j++) {
                Fr = new Frequency();
                A = Lk.get(i)._itemSet;
                B = Lk.get(j)._itemSet;
                Fr._itemSet = union(A, B);
                Fr._totalFrecuency = getTotalFrecuency(Fr._itemSet);
                Ck.add(Fr);
            }
        }
        return Ck;
    }

    /**
     *
     * @param ConjItems
     * @return
     */
    private int getTotalFrecuency(Vector<Integer> itemSet) {
        boolean igual = false;
        int cont = 0;
        for (int i = 0; i < _numberOfTransactions; i++) {
            igual = true;
            for (int j = 0; j < itemSet.size() && igual; j++) {
                if (_TID[i].indexOf(itemSet.get(j)) < 0) {
                    igual = false;
                }
            }

            if (igual) {
                cont++;
            }
        }
        return cont;
    }

    /**
     *
     * @param Ck
     * @return
     */
    private Vector<Frequency> debug(Vector<Frequency> Ck) {
        Vector<Frequency> Lk = new Vector();
        for (int i = 0; i < Ck.size(); i++) {
            if (Ck.get(i)._totalFrecuency >= this._minimumSupport) {
                Lk.add(Ck.get(i));
            }
        }
        return Lk;
    }

    /**
     *
     * @param k
     * @param Lk
     * @return
     */
    private Vector<Frequency> generateJoin(int k, Vector<Frequency> Lk) {
        Vector<Frequency> Ck = new Vector();
        Vector<Integer> A = null, B = null;
        Frequency Fr = null;
        for (int i = 0; i < Lk.size(); i++) {
            for (int j = i + 1; j < Lk.size(); j++) {
                Fr = new Frequency();
                A = Lk.get(i)._itemSet;
                B = Lk.get(j)._itemSet;
                Fr._itemSet = union(A, B);
                if (Fr._itemSet.size() == k && !isInCk(Ck, Fr._itemSet)) {
                    Fr._totalFrecuency = getTotalFrecuency(Fr._itemSet);
                    Ck.add(Fr);
                }
            }
        }
        return Ck;
    }

    /**
     *
     * @param Ck
     * @param itemSet
     * @return
     */
    private boolean isInCk(Vector<Frequency> Ck, Vector<Integer> itemSet) {
        boolean result = false;
        Vector vItems = null;
        for (int i = 0; i < Ck.size() && !result; i++) {
            vItems = Ck.get(i)._itemSet;
            result = true;
            for (int j = 0; j < itemSet.size() && result; j++) {
                if (vItems.indexOf(itemSet.get(j)) < 0) {
                    result = false;
                }
            }
        }
        return result;
    }

    /**
     *
     * @param conf
     * @return
     */
    public Vector<Rule> generateAssociationRules(double conf) {

        Vector<Vector<Integer>> subSets = null;
        Vector<Integer> vItems = null;
        Rule reTem = null;
        double sopAB = 0.0, sopA = 0.0;
        _strongRules = new Vector();
        for (int i = 0; i < _strongItemsets.size(); i++) {
            vItems = _strongItemsets.get(i)._itemSet;
            sopAB = _strongItemsets.get(i)._totalFrecuency;
            subSets = generateSubSets(vItems);

            display("\nStong Items: " + vItems + "\n");
            display("SubSet:" + subSets + "\n");

            for (int j = 0; j < subSets.size(); j++) {
                reTem = new Rule();
                reTem._leftSide = subSets.get(j);
                reTem._rightSide = difference(vItems, reTem._leftSide);
                sopA = getTotalFrecuency(reTem._leftSide) * 1.0;
                reTem._confidence = (sopAB / sopA) * 100.0;
                if (reTem._confidence >= conf) {
                    _strongRules.add(reTem);
                }
                display("" + reTem + "\n");
            }

        }
        display("\nStrong Association rules:\n");
        reportVector(_strongRules);
        
        return this._strongRules;
    }

    /**
     *
     * @param original
     * @return
     */
    public Vector<Vector<Integer>> generateSubSets(Vector<Integer> original) {
        Vector<Vector<Integer>> list = new Vector();
        Vector<Integer> A = new Vector(), B = new Vector();
        int sizeA = 1;
        int iA = 0, iB = 0;
        int size = original.size();

        for (int i = 0; i < size; i++) {
            list.add(union(new Vector(), original.get(i)));
        }

        while (sizeA <= size - 2) {
            iA = 0;
            while ((sizeA + iA) <= (size - 1)) {
                A = new Vector();
                B = new Vector();
                A.addAll(original.subList(iA, iA + sizeA));
                iB = sizeA + iA;
                B.addAll(original.subList(iB, size));
                for (int i = 0; i < B.size(); i++) {
                    list.add(union(A, B.get(i)));
                }
                iA++;
            }
            sizeA++;
        }
        return list;
    }

    /**
     *
     * @param A
     * @param B
     * @return
     */
    public Vector<Integer> union(Vector<Integer> A, Vector<Integer> B) {
        Vector<Integer> C = new Vector();
        C.addAll(A.subList(0, A.size()));
        for (int i = 0; i < B.size(); i++) {

            // The element is not in C
            if (C.indexOf(B.get(i)) < 0) {
                C.add(B.get(i));
            }
        }
        return C;
    }

    /**
     *
     * @param A
     * @param el
     * @return
     */
    public Vector<Integer> union(Vector<Integer> A, int el) {
        Vector<Integer> C = new Vector();
        C.addAll(A.subList(0, A.size()));
        C.add(el);
        return C;
    }

    /**
     *
     * @param A
     * @param B
     * @return
     */
    public Vector<Integer> difference(Vector<Integer> A, Vector<Integer> B) {
        Vector<Integer> D = new Vector();
        for (int i = 0; i < A.size(); i++) {
            if (B.indexOf(A.get(i)) < 0) {
                D.add(A.get(i));
            }
        }
        return D;
    }

    /**
     * Writes the string in the report file.
     *
     * @param string string to write.
     */
    private void display(String string) {

        if (_bufferedWriter != null) {
            try {

                _bufferedWriter.write("\t" + string);
            } catch (IOException ex) {
                Logger.getLogger(Mining.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.print(string);
        }
    }

    /**
     * Writes the obtained frequent item sets in the report file.
     *
     * @param k iteration.
     * @param Ck frequent item set.
     * @param Lk frequent item set.
     */
    private void report(int k, Vector<Frequency> Ck, Vector<Frequency> Lk) {
        display("C" + k + "\n");
        reportVector(Ck);
        display("L" + k + "\n");
        reportVector(Lk);
        display("-------------------------------------------------\n");
    }

    /**
     * Writes the message in the report file.
     *
     * @param vector vector to print.
     */
    private void reportVector(Vector vector) {
        for (int i = 0; i < vector.size(); i++) {
            display("" + vector.get(i) + "\n");
        }
        display("\n");
    }

    /**
     * Creates the buffered writer.
     */
    public void createBufferedWriter(){
        try {

            // Creates the buffered writer to write in the text file
            _bufferedWriter = new BufferedWriter(new FileWriter(_fileReport));

        } catch (IOException ex) {
            Logger.getLogger(Mining.class.getName()).log(Level.SEVERE, null, ex);
        }

        generateTID(_booleanMatrix);
    }

    /**
     * Closes the buffered writer.
     */
    public void closeBufferedWriter(){

        try {
            _bufferedWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(Mining.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
