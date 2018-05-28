/*
 * Created on 18.01.2007
 * @author Stephan Richard Palm
 * �Copyright by Stephan Richard Palm
 * 
 */
package net.sf.jacinth.modules;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;

import net.sf.jacinth.HTMLEditor;

public interface Module {

    /**
     * Does the module recognize the object at 
     * the given position as editable by this module.
     * 
     * @param element
     * @return
     */
    public boolean applicableFor(HTMLDocument document, int pos);

    /**
     * Returns the html element/object that goes with the given position.
     * @param document
     * @param pos
     * @return
     */
    public Object getObject(HTMLDocument document, int pos);

    /**
     * Return a short description of the object.
     * @param newObject An instance recieved from getObject of the same module.
     * @return
     */
    public String getDescription(Object newObject);
    
    /**
     * Edit the element and return a String that can replace the element.
     * 
     * @param component 
     * @param document Convenient way for editor.document
     * @param pEl Convenient way for document.getParagraphElement(editor.getCaretPosition()
     * @param pos Convenient way for editor.getCaretPosition()
     * @return
     */
    public void editProperties(Component component, 
            HTMLEditor editor, HTMLDocument document, int pos,
            AbstractDocument.BranchElement pEl);

    
    public ImageIcon getImageIcon();
    public JacinthTag getNewInstance(Component component, HTMLDocument document, String selection, int pos);

    public String getLocalizedName();
    public String getLocalizedMenuName();
    public String getLocalizedTooltip();

}
