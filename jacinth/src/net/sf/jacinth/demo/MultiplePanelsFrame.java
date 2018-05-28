/*
 * Created on 15.02.2007
 * @author Stephan Richard Palm
 * Copyright by Stephan Richard Palm
 * 
 * One interest in writing this class was 
 * to fix a bug occuring when multiple editors
 * are open at once.
 * And it gives the posibility to show the use
 * of the SimpleEditorPanel
 * 
 */
package net.sf.jacinth.demo;

import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class MultiplePanelsFrame extends JFrame{

    SimpleEditorPanel panels[][] = new SimpleEditorPanel[2][];
    
    public MultiplePanelsFrame(){
        setLayout(new GridLayout(2,2));
        for (int i = 0; i < 2; i++) {
            panels[i] = new SimpleEditorPanel[2];
            for (int j = 0; j < 2; j++){
                final String name = "SimpleEditorPanel[" + i + "][" + j + "]";
                panels[i][j] = new SimpleEditorPanel();
                panels[i][j].getEditor().editor.addFocusListener(new FocusListener(){

                    public void focusGained(FocusEvent e) {
                        System.out.println("Focus gained on " + name);
                    }

                    public void focusLost(FocusEvent e) {
                        System.out.println("Focus lost on " + name);
                    }
                    
                });
                add(panels[i][j]);
            }
        }
        setSize(800, 600);
    }
    
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowStateEvent(e);
        // Exit if this window gets closed
        if (e.getID() == WindowEvent.WINDOW_CLOSING) 
            System.exit(0);
    }

    public static void main(String args[]) {
        MultiplePanelsFrame frame = new MultiplePanelsFrame();
        frame.setVisible(true);
    }
    
    
}
