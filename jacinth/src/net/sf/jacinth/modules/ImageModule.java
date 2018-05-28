/*
 * Created on 18.01.2007
 * @author Stephan Richard Palm
 * Copyright by Stephan Richard Palm
 * 
 */
package net.sf.jacinth.modules;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

import net.sf.jacinth.HTMLEditor;
import net.sf.jacinth.ImageDialog;
import net.sf.jacinth.util.Local;

public class ImageModule implements Module {

    public boolean applicableFor(HTMLDocument document, int pos) {
        AbstractDocument.BranchElement pEl = (AbstractDocument.BranchElement) document.getParagraphElement(pos);
        Element element = pEl.positionToElement(pos);
        AttributeSet attrs = element.getAttributes();

        String elName = attrs.getAttribute(StyleConstants.NameAttribute).toString().toUpperCase();
        return (elName.equals("IMG"));
    }

    public Object getObject(HTMLDocument document, int pos) {
        AbstractDocument.BranchElement pEl = (AbstractDocument.BranchElement) document.getParagraphElement(pos);
        Element element = pEl.positionToElement(pos);

        return element;
    }
    
    

    public String getDescription(Object newObject) {
        Element element = (Element) newObject;
        AttributeSet attrs = element.getAttributes();
        if (attrs.isDefined(HTML.Attribute.SRC))
            return attrs.getAttribute(HTML.Attribute.SRC).toString();
        return null;
    }

    public void editProperties(Component component, 
            HTMLEditor editor, HTMLDocument document, int pos,
            AbstractDocument.BranchElement pEl) {
        Element element = pEl.positionToElement(pos);
        AttributeSet attrs = element.getAttributes();

        String src = "", alt = "", width = "", height = "", hspace = "", vspace = "", border = "", align = "";
        if (attrs.isDefined(HTML.Attribute.SRC))
            src = attrs.getAttribute(HTML.Attribute.SRC).toString();
        if (attrs.isDefined(HTML.Attribute.ALT))
            alt = attrs.getAttribute(HTML.Attribute.ALT).toString();
        if (attrs.isDefined(HTML.Attribute.WIDTH))
            width = attrs.getAttribute(HTML.Attribute.WIDTH).toString();
        if (attrs.isDefined(HTML.Attribute.HEIGHT))
            height = attrs.getAttribute(HTML.Attribute.HEIGHT).toString();
        if (attrs.isDefined(HTML.Attribute.HSPACE))
            hspace = attrs.getAttribute(HTML.Attribute.HSPACE).toString();
        if (attrs.isDefined(HTML.Attribute.VSPACE))
            vspace = attrs.getAttribute(HTML.Attribute.VSPACE).toString();
        if (attrs.isDefined(HTML.Attribute.BORDER))
            border = attrs.getAttribute(HTML.Attribute.BORDER).toString();
        if (attrs.isDefined(HTML.Attribute.ALIGN))
            align = attrs.getAttribute(HTML.Attribute.ALIGN).toString();
        
        setImageProperties(component, document, element, pos, src, alt, width, height, hspace, vspace, border, align);
    }


    private void setImageProperties(Component component, HTMLDocument document, Element element, int pos, String src, String alt, String width, String height, String hspace, String vspace, String border, String align) {
        ImageDialog dlg = ImageDialog.getNewImageDialog(component, document.getBase());
        //dlg.setLocation(imageActionB.getLocationOnScreen());
        dlg.setModal(true);
        dlg.setTitle(Local.getString("Image properties"));
        dlg.fileField.setText(src);
        dlg.altField.setText(alt);
        dlg.widthField.setText(width);
        dlg.heightField.setText(height);
        dlg.hspaceField.setText(hspace);
        dlg.vspaceField.setText(vspace);
        dlg.borderField.setText(border);
        dlg.alignCB.setSelectedItem(align);
        dlg.updatePreview();
        dlg.setVisible(true);
        
        if (!dlg.isClosedWithOk())
            return;

        String tag = ImageDialogToHTML(dlg);
        try {
            document.setOuterHTML(element, tag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JacinthTag getNewInstance(Component component, HTMLDocument document, String selection, int pos) {
        ImageDialog dlg = ImageDialog.getNewImageDialog(component, document.getBase());

        dlg.altField.setText(selection);
        dlg.setModal(true);
        dlg.setVisible(true);
        if (!dlg.isClosedWithOk())
            return null;

        String imgTag = ImageDialogToHTML(dlg);

        if (dlg.urlField.getText().length() > 0)  {
//////      What was this unreachable code about??                
            if (pos == document.getLength())
                imgTag += "&nbsp;";
            return new JacinthTag(imgTag, HTML.Tag.A, 0, 0);
        }
        else
            return new JacinthTag(imgTag, HTML.Tag.IMG, 0, 0);
    }

    public String ImageDialogToHTML(ImageDialog dlg) {
        String imgTag = "<img src=\"" + dlg.fileField.getText() + "\" alt=\""
        + dlg.altField.getText() + "\" ";

        String w = dlg.widthField.getText();
        if (w.length() > 0)
            imgTag += " width=\"" + w + "\" ";

        String h = dlg.heightField.getText();
        if (h.length() > 0)
            imgTag += " height=\"" + h + "\" ";

        String hs = dlg.hspaceField.getText();
        if (hs.length() > 0)
            imgTag += " hspace=\"" + hs + "\" ";

        String vs = dlg.vspaceField.getText();
        if (vs.length() > 0)
            imgTag += " vspace=\"" + vs + "\" ";

        String b = dlg.borderField.getText();
        if (b.length() > 0)
            imgTag += " border=\"" + b + "\" ";

        if (dlg.alignCB.getSelectedIndex() > 0)
            imgTag += " align=\"" + dlg.alignCB.getSelectedItem() + "\" ";
        imgTag += ">";

        if (dlg.urlField.getText().length() > 0) 
            imgTag = "<a href=\"" + dlg.urlField.getText() + "\">" + imgTag + "</a>";
        
        return imgTag;
    }
    
    public ImageIcon getImageIcon() {
        return new ImageIcon(HTMLEditor.class.getResource("resources/icons/image.png"));
    }


    public String getLocalizedName() {
        return Local.getString("Insert image");
    }
    
    public String getLocalizedMenuName() {
        return Local.getString("Image") + "...";
    }
    
    public String getLocalizedTooltip() {
        return Local.getString("Insert image");
    }

}
