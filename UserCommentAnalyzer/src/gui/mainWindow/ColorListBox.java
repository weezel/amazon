package gui.mainWindow;

import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import java.awt.Component;
import java.awt.Color;


public class ColorListBox extends JLabel implements ListCellRenderer {

    public ColorListBox() {
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        // Assumes the stuff in the list has a pretty toString
        setText(value.toString());

        Color lightRed = new Color(255,192,192);
        Color lightGreen = new Color(192,255,192);

        String ratingLoc = value.toString().substring(value.toString().indexOf(":")+1);
        double rating = 0;
        //if(!ratingLoc.equals(" "))
        ratingLoc = ratingLoc.substring(ratingLoc.indexOf(":")+2);
        ratingLoc = ratingLoc.substring(0, ratingLoc.indexOf("[") - 1);

        if(!ratingLoc.equals(""))
        {
            rating = Double.valueOf(ratingLoc);
        }



        if(rating > 50)
            setBackground(lightGreen);

        if(rating <= 50)
            setBackground(lightRed);

        if(rating == 0)
            setBackground(Color.WHITE);

        // based on the index you set the color.  This produces the every other effect.


        if(isSelected)
            setBackground(Color.LIGHT_GRAY);

        return this;
    }
}