/*
 * Created on 17.01.2007
 * @author Stephan Richard Palm
 * 
 * This editor panel is intendet for direct use in other software.
 * There is a set and a get methode for html as well as 
 * the posibility to add an action listener as change listener for the html.
 */
package net.sf.jacinth.demo;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.sf.jacinth.HTMLEditor;
import net.sf.jacinth.ObjectListener;

public class SimpleEditorPanel extends JPanel {

    private HTMLEditor editor = createHTMLEditor();

    private Vector changeListeners = new Vector();

    public HTMLEditor getEditor(){
        return editor;
    }
    
    /**
     * This function is design to allow to be overriden
     * by classes that extend this class.
     * 
     * This way one can add modules or modify the HTML
     * editor befor the constructor of this class is 
     * executed.
     * 
     * @return
     */
    protected HTMLEditor createHTMLEditor() {
        return new HTMLEditor();
    }
    
    public void addChangeListener(ActionListener listener) {
        changeListeners.add(listener);
    }
    
    public void removeChangeListener(ActionListener listener) {
        changeListeners.remove(listener);
    }

    public void addObjectListener(ObjectListener listener) {
        editor.addObjectListener(listener);
    }
    
    public void removeObjectListener(ObjectListener listener) {
        editor.removeObjectListener(listener);
    }
    
    public void fireChange() {
        for (int i = 0; i < changeListeners.size(); i++)
            ((ActionListener) changeListeners.get(i)).actionPerformed(null);
    }
    
    public SimpleEditorPanel() {
        setLayout(new BorderLayout());
        JToolBar editToolbar = editor.getEditToolbar(this);
        JToolBar formatToolbar = editor.getFormatToolbar(this);

        // Make a Panel to include the second toolbar
        // this way the toolbars are free to move, at least are able to
        // move a lot.
        JPanel secondToolbarPanel = new JPanel();
        secondToolbarPanel.setLayout(new BorderLayout());
        secondToolbarPanel.add(formatToolbar, BorderLayout.PAGE_START);
        secondToolbarPanel.add(editor);

        add(editToolbar, BorderLayout.PAGE_START);
        add(secondToolbarPanel);
        
        editor.editor.addDocumentListener(new DocumentListener(){

            public void changedUpdate(DocumentEvent e) {
                fireChange();
            }

            public void insertUpdate(DocumentEvent e) {
                fireChange();
            }

            public void removeUpdate(DocumentEvent e) {
                fireChange();
            }
            
        });
    }
    
    public String getHTML() {
        return editor.editor.getText();
    }
    
    public void setHTML(String html) {
        if (html == null)
            html = "";
        editor.setDocumentHtml(html);
    }
    
}
