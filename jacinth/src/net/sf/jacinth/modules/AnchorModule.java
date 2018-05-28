/*
 * Created on 18.01.2007
 * @author Stephan Richard Palm
 * Copyright by Stephan Richard Palm
 * 
 */
package net.sf.jacinth.modules;

import net.sf.jacinth.HTMLEditor;
import net.sf.jacinth.LinkDialog;
import net.sf.jacinth.util.Local;

import javax.swing.ImageIcon;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.awt.Component;
import java.util.Enumeration;
import java.util.Hashtable;

public class AnchorModule implements Module {

    public boolean applicableFor(HTMLDocument document, int pos) {
        AbstractDocument.BranchElement pEl = (AbstractDocument.BranchElement) document.getParagraphElement(pos);
        Element element = pEl.positionToElement(pos);
        AttributeSet attrs = element.getAttributes();

        Object k = null;
        for (Enumeration en = attrs.getAttributeNames(); en.hasMoreElements();) {
            k = en.nextElement();
            if (k.toString().equals("a")) 
                return true;
        }
        return false;
    }

    public Object getObject(HTMLDocument document, int pos) {
        AbstractDocument.BranchElement pEl = (AbstractDocument.BranchElement) document.getParagraphElement(pos);
        Element element = pEl.positionToElement(pos);
        
        return element;
    }

    public String getDescription(Object newObject) {
        Element element = (Element) newObject;
        AttributeSet attrs = element.getAttributes();

        Object k = null;
        for (Enumeration en = attrs.getAttributeNames(); en.hasMoreElements();) {
            k = en.nextElement();
            if (k.toString().equals("a")) {
                String[] param = attrs.getAttribute(k).toString().split(" ");
                for (int i = 0; i < param.length; i++)
                    if (param[i].startsWith("href="))
                        return param[i].split("=")[1];
            }
        }

        return null;
    }



    public ImageIcon getImageIcon() {
        return new ImageIcon(HTMLEditor.class.getResource("resources/icons/link.png"));
    }

    public String getLocalizedMenuName() {
        return Local.getString("Hyperlink") + "...";
    }

    public String getLocalizedName() {
        return Local.getString("Insert hyperlink");
    }

    public String getLocalizedTooltip() {
        return Local.getString("Insert hyperlink");
    }

    public JacinthTag getNewInstance(Component component, HTMLDocument document, String selection, int pos) {
        LinkDialog dlg = LinkDialog.getNewLinkDialog(component);
        dlg.setModal(true);

        if (selection != null)
            dlg.txtDesc.setText(selection);
        dlg.setVisible(true);
        if (!dlg.isClosedWithOk())
            return null;
        Hashtable attrs = new Hashtable();
        if (dlg.txtName.getText().length() > 0)
            attrs.put("name", dlg.txtName.getText());
        if (dlg.txtTitle.getText().length() > 0)
            attrs.put("title", dlg.txtTitle.getText());
        if (dlg.chkNewWin.isSelected())
            attrs.put("target", "_blank");
        return insertLink(document, dlg.txtURL.getText(), attrs, dlg.txtDesc.getText());
    }

    public JacinthTag insertLink(HTMLDocument document, String href, Hashtable attrs, String text) {
        String aTag = "<a href=\"" + href + "\"";
        if (attrs != null) {
            for (Enumeration en = attrs.keys(); en.hasMoreElements();) {
                String aName = (String) en.nextElement();
                String aValue = attrs.get(aName).toString();
                aTag += " " + aName + "=\"" + aValue + "\"";
            }
        }
        aTag += ">" + text + "</a>";
//        if (editor.getCaretPosition() == document.getLength())
//            aTag += "&nbsp;";
//        editor.replaceSelection("");
    
        return new JacinthTag(aTag, HTML.Tag.A, 0, 0);
    }

    public void editProperties(Component component, 
            HTMLEditor editor, HTMLDocument document, int pos,
            AbstractDocument.BranchElement pEl) {
        Element element = pEl.positionToElement(pos);
        AttributeSet attrs = element.getAttributes();

        Object k = null;
        for (Enumeration en = attrs.getAttributeNames(); en.hasMoreElements();) {
            k = en.nextElement();
            if (k.toString().equals("a")) {
                String[] param = attrs.getAttribute(k).toString().split(" ");
                String href = "", target = "", title = "", name = "";
                for (int i = 0; i < param.length; i++)
                    if (param[i].startsWith("href="))
                        href = param[i].split("=")[1];
                    else if (param[i].startsWith("title="))
                        title = param[i].split("=")[1];
                    else if (param[i].startsWith("target="))
                        target = param[i].split("=")[1];
                    else if (param[i].startsWith("name="))
                        name = param[i].split("=")[1];
                setLinkProperties(component, document, element, href, target, title, name);
            }
            System.out.println(k + " = '" + attrs.getAttribute(k) + "'");
        }
    }

    void setLinkProperties(Component component, HTMLDocument document, Element el, String href, String target, String title, String name) {
        LinkDialog dlg = LinkDialog.getNewLinkDialog(component, Local.getString("Hyperlink properties"));
        //dlg.setLocation(linkActionB.getLocationOnScreen());
        dlg.setModal(true);
        // dlg.descPanel.setVisible(false);
        dlg.txtURL.setText(href);
        dlg.txtName.setText(name);
        dlg.txtTitle.setText(title);
        try {
            dlg.txtDesc.setText(document.getText(el.getStartOffset(), el.getEndOffset()
                    - el.getStartOffset()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        dlg.chkNewWin.setSelected(target.toUpperCase().equals("_BLANK"));
        dlg.setVisible(true);
        
        if (!dlg.isClosedWithOk())
            return;

        String aTag = "<a";
        if (dlg.txtURL.getText().length() > 0)
            aTag += " href=\"" + dlg.txtURL.getText() + "\"";
        if (dlg.txtName.getText().length() > 0)
            aTag += " name=\"" + dlg.txtName.getText() + "\"";
        if (dlg.txtTitle.getText().length() > 0)
            aTag += " title=\"" + dlg.txtTitle.getText() + "\"";
        if (dlg.chkNewWin.isSelected())
            aTag += " target=\"_blank\"";
        aTag += ">" + dlg.txtDesc.getText() + "</a>";
        
        try {
            document.setOuterHTML(el, aTag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
