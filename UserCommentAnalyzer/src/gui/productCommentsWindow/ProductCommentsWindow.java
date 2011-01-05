/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.productCommentsWindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileReader;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

/**
 * A window which searches for a keyword in a document which contents the user
 * comments of the product which that keyword is related with, and highlights 
 * occurrences of it.
 * 
 * @author javiersalcedogomez
 */
public class ProductCommentsWindow extends JFrame {

    /**
     * Text pane to display the comment file content.
     */
    final private JTextPane _textPane;
    /**
     * Keyword to be highlighted.
     */
    public static String _keyword;
    /**
     * Highlighter to highlight the keyword.
     */
    public static Highlighter _highlighter = new DefaultHighlighter();

    /**
     * Creates a new product comments window.
     *
     * @param keyword keyword to be highlighted.
     * @param index comment index.
     */
    public ProductCommentsWindow(String keyword, int index) {

        super("Product" + index + " user comments");

        // Gets the keyword to highlight
        _keyword = keyword;

        _textPane = new JTextPane();
        _textPane.setHighlighter(_highlighter);
        getContentPane().add(new JScrollPane(_textPane), "Center");

        // Loads the comments file content
        try {
            _textPane.read(new FileReader("Comments" + index + ".txt"), null);
        } catch (Exception e) {
            System.out.println("Failed to load file " + index);
            System.out.println(e);
        }

        // KEYWORD SEARCHER
        final KeywordSearcher searcher = new KeywordSearcher(_textPane);

        // Calls to the highlighter
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                int offset = searcher.search(_keyword);
                if (offset != -1) {
                    try {
                        _textPane.scrollRectToVisible(_textPane.modelToView(offset));
                    } catch (BadLocationException e) {
                    }
                }
            }
        });

        // Adds the document listener
        _textPane.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent evt) {
                searcher.search(_keyword);
            }

            @Override
            public void removeUpdate(DocumentEvent evt) {
                searcher.search(_keyword);
            }

            @Override
            public void changedUpdate(DocumentEvent evt) {
            }
        });

        // Sets the size
        setSize(400, 500);

        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = getSize().width;
        int h = getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;

        // Move the window
        setLocation(x, y);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}

/**
 * Searches for the keyword in the comments file and highlight every single
 * appearence of it.
 *
 * @author javiersalcedogomez
 */
class KeywordSearcher {

    /**
     * Text component.
     */
    protected JTextComponent _textComponent;
    /**
     * Highlighter _painter.
     */
    protected Highlighter.HighlightPainter _painter;

    /**
     * Creates a new keyword searcher.
     *
     * @param textComponent text component.
     */
    public KeywordSearcher(JTextComponent textComponent) {
        _textComponent = textComponent;
        _painter = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);
    }

    /**
     * Search for a _keyword and return the offset of the first occurrence.
     * Highlights are added for all
     *
     * @param keyword keyword to be highlighted.
     *
     * @return the first offset.
     */
    public int search(String keyword) {

        int firstOffset = -1;

        Highlighter highlighter = _textComponent.getHighlighter();

        // Remove any existing highlights for last _keyword
        Highlighter.Highlight[] highlights = highlighter.getHighlights();
        for (int i = 0; i < highlights.length; i++) {
            Highlighter.Highlight h = highlights[i];
            if (h.getPainter() instanceof DefaultHighlighter.DefaultHighlightPainter) {
                highlighter.removeHighlight(h);
            }
        }

        if (keyword == null || keyword.equals("")) {
            return -1;
        }

        // Look for the _keyword we are given - insensitive search
        String content = null;
        try {
            Document d = _textComponent.getDocument();
            content = d.getText(0, d.getLength()).toLowerCase();
        } catch (BadLocationException e) {
            // Cannot happen
            return -1;
        }

        keyword = keyword.toLowerCase();
        int lastIndex = 0;
        int wordSize = keyword.length();

        while ((lastIndex = content.indexOf(keyword, lastIndex)) != -1) {
            int endIndex = lastIndex + wordSize;

            char previousChar = content.charAt(lastIndex - 1);
            char nextChar = content.charAt(endIndex);


            // Only if they are separate words
            if (!Character.isLetterOrDigit(previousChar) && !Character.isLetterOrDigit(nextChar)) {
                try {
                    highlighter.addHighlight(lastIndex, endIndex, _painter);
                } catch (BadLocationException e) {
                    // Nothing to do
                }
            }
            if (firstOffset == -1) {
                firstOffset = lastIndex;
            }
            lastIndex = endIndex;
        }

        return firstOffset;
    }
}


