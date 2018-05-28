/*
 * Created on 18.01.2007
 * @author Stephan Richard Palm
 * Copyright by Stephan Richard Palm
 * 
 */
package net.sf.jacinth.modules;

import net.sf.jacinth.HTMLEditor;
import net.sf.jacinth.TableDialog;
import net.sf.jacinth.TdDialog;
import net.sf.jacinth.Util;
import net.sf.jacinth.util.Local;

import javax.swing.ImageIcon;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.awt.Component;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;

public class TableModule implements Module {

    public boolean applicableFor(HTMLDocument document, int pos) {
        AbstractDocument.BranchElement pEl = (AbstractDocument.BranchElement) document.getParagraphElement(pos);
        if (pEl.getParentElement().getName().toUpperCase().equals("TD")) 
            return true;
        return false;
    }
    
    

    public Object getObject(HTMLDocument document, int pos) {
        AbstractDocument.BranchElement pEl = (AbstractDocument.BranchElement) document.getParagraphElement(pos);

        return pEl.getParentElement();
    }

    public String getDescription(Object object) {
        
        Element td = (Element) object;
        Element tr = td.getParentElement();
        Element table = tr.getParentElement();
                
        int x = -1, y = -1;
        
        for (int i=0; i<tr.getElementCount(); i++)
            if (tr.getElement(i).equals(td)) {
                x = i;
                break;
            }
        for (int i=0; i<table.getElementCount(); i++)
            if (table.getElement(i).equals(tr)) {
                y = i;
                break;
            }

        return "Cell (" + (y+1) + ", " + (x+1) + ")";
    }


    public ImageIcon getImageIcon() {
        return new ImageIcon(HTMLEditor.class.getResource("resources/icons/table.png"));
    }

    public String getLocalizedMenuName() {
        return Local.getString("Table") + "...";
    }

    public String getLocalizedName() {
        return Local.getString("Insert table");
    }

    public String getLocalizedTooltip() {
        return Local.getString("Insert Table");
    }

    public JacinthTag getNewInstance(Component component, HTMLDocument document, String selection, int pos) {
        TableDialog dlg = TableDialog.getNewTDDialog(component);

        dlg.setModal(true);
        dlg.setVisible(true);
        
        if (!dlg.isClosedWithOk())
            return null;
        
        Hashtable attrs = new Hashtable();
        String w = dlg.widthField.getText().trim();
        if (w.length() > 0)
            attrs.put("width", w);
        String h = dlg.heightField.getText().trim();
        if (h.length() > 0)
            attrs.put("height", h);
        String cp = dlg.cellpadding.getValue().toString();
        try {
            Integer.parseInt(cp, 10);
            attrs.put("cellpadding", cp);
        } catch (Exception ex) {
        }
        String cs = dlg.cellspacing.getValue().toString();
        try {
            Integer.parseInt(cs, 10);
            attrs.put("cellspacing", cs);
        } catch (Exception ex) {
        }
        String b = dlg.border.getValue().toString();
        try {
            Integer.parseInt(b, 10);
            attrs.put("border", b);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (dlg.alignCB.getSelectedIndex() > 0)
            attrs.put("align", dlg.alignCB.getSelectedItem());
        if (dlg.vAlignCB.getSelectedIndex() > 0)
            attrs.put("valign", dlg.vAlignCB.getSelectedItem());
        if (dlg.bgcolorField.getText().length() > 0)
            attrs.put("bgcolor", dlg.bgcolorField.getText());
        int cols = 1;
        int rows = 1;
        try {
            cols = ((Integer) dlg.columns.getValue()).intValue();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            rows = ((Integer) dlg.rows.getValue()).intValue();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return insertTable(document, cols, rows, attrs);        
    }

    public JacinthTag insertTable(HTMLDocument document, int cols, int rows, Hashtable attrs) {
        String tableTag = "<table";
        if (attrs != null) {
            for (Enumeration en = attrs.keys(); en.hasMoreElements();) {
                String aName = (String) en.nextElement();
                String aValue = attrs.get(aName).toString();
                tableTag += " " + aName + "=\"" + aValue + "\"";
            }
        }
        tableTag += ">";
        for (int r = 0; r < rows; r++) {
            tableTag += "<tr>";
            for (int c = 0; c < cols; c++)
                tableTag += "<td><p></p></td>";
            tableTag += "</tr>";
        }
        tableTag += "</table>";
        
        return new JacinthTag(tableTag, HTML.Tag.TABLE, 1, 0);
    }

    public void editProperties(Component component, 
            HTMLEditor editor, HTMLDocument document, int pos,
            AbstractDocument.BranchElement pEl) {

        Element td = pEl.getParentElement();
        Element tr = td.getParentElement();
        Element table = tr.getParentElement();
        TdDialog dlg = TdDialog.getNewTDDialog(component);
        //dlg.setLocation(editor.getLocationOnScreen());
        dlg.setModal(true);
        dlg.setTitle(Local.getString("Table properties"));
        /** **********PARSE ELEMENTS*********** */
        // TD***
        AttributeSet tda = td.getAttributes();
        if (tda.isDefined(HTML.Attribute.BGCOLOR)) 
            dlg.tdBgcolorField.setText(tda.getAttribute(HTML.Attribute.BGCOLOR).toString());
        if (tda.isDefined(HTML.Attribute.WIDTH))
            dlg.tdWidthField.setText(tda.getAttribute(HTML.Attribute.WIDTH).toString());
        if (tda.isDefined(HTML.Attribute.HEIGHT))
            dlg.tdHeightField.setText(tda.getAttribute(HTML.Attribute.HEIGHT).toString());
        if (tda.isDefined(HTML.Attribute.COLSPAN))
            try {
                Integer i = new Integer(tda.getAttribute(HTML.Attribute.COLSPAN).toString());
                dlg.tdColspan.setValue(i);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        if (tda.isDefined(HTML.Attribute.ROWSPAN))
            try {
                Integer i = new Integer(tda.getAttribute(HTML.Attribute.ROWSPAN).toString());
                dlg.tdRowspan.setValue(i);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        if (tda.isDefined(HTML.Attribute.ALIGN))
            dlg.tdAlignCB.setSelectedItem(tda.getAttribute(HTML.Attribute.ALIGN).toString()
                    .toLowerCase());
        if (tda.isDefined(HTML.Attribute.VALIGN))
            dlg.tdValignCB.setSelectedItem(tda.getAttribute(HTML.Attribute.VALIGN).toString()
                    .toLowerCase());
        dlg.tdNowrapChB.setSelected((tda.isDefined(HTML.Attribute.NOWRAP)));
        // TR ****
        AttributeSet tra = tr.getAttributes();
        if (tra.isDefined(HTML.Attribute.BGCOLOR)) 
            dlg.trBgcolorField.setText(tra.getAttribute(HTML.Attribute.BGCOLOR).toString());
        if (tra.isDefined(HTML.Attribute.ALIGN))
            dlg.trAlignCB.setSelectedItem(tra.getAttribute(HTML.Attribute.ALIGN).toString()
                    .toLowerCase());
        if (tra.isDefined(HTML.Attribute.VALIGN))
            dlg.trValignCB.setSelectedItem(tra.getAttribute(HTML.Attribute.VALIGN).toString()
                    .toLowerCase());
        // TABLE ****
        AttributeSet ta = table.getAttributes();
        if (ta.isDefined(HTML.Attribute.BGCOLOR)) 
            dlg.bgcolorField.setText(ta.getAttribute(HTML.Attribute.BGCOLOR).toString());
        if (ta.isDefined(HTML.Attribute.WIDTH))
            dlg.widthField.setText(ta.getAttribute(HTML.Attribute.WIDTH).toString());
        if (ta.isDefined(HTML.Attribute.HEIGHT))
            dlg.heightField.setText(ta.getAttribute(HTML.Attribute.HEIGHT).toString());
        if (ta.isDefined(HTML.Attribute.ALIGN))
            dlg.alignCB.setSelectedItem(ta.getAttribute(HTML.Attribute.ALIGN).toString()
                    .toLowerCase());
        if (ta.isDefined(HTML.Attribute.VALIGN))
            dlg.vAlignCB.setSelectedItem(ta.getAttribute(HTML.Attribute.VALIGN).toString()
                    .toLowerCase());
        if (ta.isDefined(HTML.Attribute.CELLPADDING))
            try {
                Integer i = new Integer(ta.getAttribute(HTML.Attribute.CELLPADDING).toString());
                dlg.cellpadding.setValue(i);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        if (ta.isDefined(HTML.Attribute.CELLSPACING))
            try {
                Integer i = new Integer(ta.getAttribute(HTML.Attribute.CELLSPACING).toString());
                dlg.cellspacing.setValue(i);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        if (ta.isDefined(HTML.Attribute.BORDER))
            try {
                Integer i = new Integer(ta.getAttribute(HTML.Attribute.BORDER).toString());
                dlg.border.setValue(i);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        /** ****************************** */
        dlg.setVisible(true);
        if (!dlg.isClosedWithOk())
            return;

        /** ******** SET ATTRIBUTES ********* */
        // TD***
        String tdTag = "<td";
        if (dlg.tdBgcolorField.getText().length() > 0)
            tdTag += " bgcolor=\"" + dlg.tdBgcolorField.getText() + "\"";
        if (dlg.tdWidthField.getText().length() > 0)
            tdTag += " width=\"" + dlg.tdWidthField.getText() + "\"";
        if (dlg.tdHeightField.getText().length() > 0)
            tdTag += " height=\"" + dlg.tdHeightField.getText() + "\"";
        if (!dlg.tdColspan.getValue().toString().equals("0"))
            tdTag += " colspan=\"" + dlg.tdColspan.getValue().toString() + "\"";
        if (!dlg.tdRowspan.getValue().toString().equals("0"))
            tdTag += " rowspan=\"" + dlg.tdRowspan.getValue().toString() + "\"";
        if (dlg.tdAlignCB.getSelectedItem().toString().length() > 0)
            tdTag += " align=\"" + dlg.tdAlignCB.getSelectedItem().toString() + "\"";
        if (dlg.tdValignCB.getSelectedItem().toString().length() > 0)
            tdTag += " valign=\"" + dlg.tdValignCB.getSelectedItem().toString() + "\"";
        if (dlg.tdNowrapChB.isSelected())
            tdTag += " nowrap";
        tdTag += ">";
        // TR***
        String trTag = "<tr";
        if (dlg.trBgcolorField.getText().length() > 0)
            trTag += " bgcolor=\"" + dlg.trBgcolorField.getText() + "\"";
        if (dlg.trAlignCB.getSelectedItem().toString().length() > 0)
            trTag += " align=\"" + dlg.trAlignCB.getSelectedItem().toString() + "\"";
        if (dlg.trValignCB.getSelectedItem().toString().length() > 0)
            trTag += " valign=\"" + dlg.trValignCB.getSelectedItem().toString() + "\"";
        trTag += ">";
        // TABLE ***
        String tTag = "<table";
        if (dlg.bgcolorField.getText().length() > 0)
            tTag += " bgcolor=\"" + dlg.bgcolorField.getText() + "\"";
        if (dlg.widthField.getText().length() > 0)
            tTag += " width=\"" + dlg.widthField.getText() + "\"";
        if (dlg.heightField.getText().length() > 0)
            tTag += " height=\"" + dlg.heightField.getText() + "\"";
        tTag += " cellpadding=\"" + dlg.cellpadding.getValue().toString() + "\"";
        tTag += " cellspacing=\"" + dlg.cellspacing.getValue().toString() + "\"";
        tTag += " border=\"" + dlg.border.getValue().toString() + "\"";
        if (dlg.alignCB.getSelectedItem().toString().length() > 0)
            tTag += " align=\"" + dlg.alignCB.getSelectedItem().toString() + "\"";
        if (dlg.vAlignCB.getSelectedItem().toString().length() > 0)
            tTag += " valign=\"" + dlg.vAlignCB.getSelectedItem().toString() + "\"";
        tTag += ">";
        /** ****************************** */
        /** ** UPDATE TABLE ***** */
        try {
            // Rewrite cell
            StringWriter sw = new StringWriter();
            editor.editorKit.write(sw, document, td.getStartOffset(), td.getEndOffset()
                    - td.getStartOffset());
            String tdContent = Util.stripTagContentFromWriterOutput(sw.toString(), td);
            document.setOuterHTML(td, tdTag + tdContent + "</td>");

            // Rewrite row
            sw = new StringWriter();
            editor.editorKit.write(sw, document, tr.getStartOffset(), tr.getEndOffset()
                    - tr.getStartOffset());
            String trContent = Util.stripTagContentFromWriterOutput(sw.toString(), tr);
            document.setOuterHTML(tr, trTag + trContent + "</tr>");

            // Rewrite table
            sw = new StringWriter();
            editor.editorKit.write(sw, document, table.getStartOffset(), table.getEndOffset()
                    - table.getStartOffset());
            String tableContent = Util.stripTagContentFromWriterOutput(sw.toString(), table);
            document.setOuterHTML(table, tTag + tableContent + "</table>");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
