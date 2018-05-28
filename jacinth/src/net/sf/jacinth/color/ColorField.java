/*
 * Created on 16.01.2007
 * @author Stephan Richard Palm
 * Copyright by Stephan Richard Palm
 * 
 */
package net.sf.jacinth.color;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.sf.jacinth.Util;

public class ColorField extends JFormattedTextField {

    public ColorField() {
        this.getDocument().addDocumentListener(new DocumentListener(){

            public void changedUpdate(DocumentEvent arg0) {
                update();
            }

            public void insertUpdate(DocumentEvent arg0) {
                update();
            }

            public void removeUpdate(DocumentEvent arg0) {
                update();
            }
            
        });
    }

    protected void update() {
        Color c = Util.decodeColor(getText(), Color.white);
        setBackground(c);
        setForeground(new Color(~c.getRGB()));
    }

    public Dimension getMinimumSize() {
        Dimension dim = getMinimumSize();
        return new Dimension(Math.max(dim.width, 200), dim.height);
    }
    
    
}
