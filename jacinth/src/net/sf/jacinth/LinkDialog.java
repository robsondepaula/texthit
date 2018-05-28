package net.sf.jacinth;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sf.jacinth.modules.JacinthDialog;
import net.sf.jacinth.util.Local;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class LinkDialog extends JacinthDialog {
    
    final static String INSERT_HYPERLINK = "Insert hyperlink";
    final static ImageIcon icon = new ImageIcon(
            net.sf.jacinth.ImageDialog.class.getResource(
            "resources/icons/linkbig.png"));
    
    GridBagConstraints gbc;
    JLabel lblURL = new JLabel();
    public JTextField txtURL = new JTextField();
    JLabel lblName = new JLabel();
    public JTextField txtName = new JTextField();
    JLabel lblTitle = new JLabel();
    public JTextField txtTitle = new JTextField();
    JLabel lblDesc = new JLabel();
    public JTextField txtDesc = new JTextField();
    public JCheckBox chkNewWin = new JCheckBox();

    String[] aligns = {"", Local.getString("left"), 
            Local.getString("center"), Local.getString("right")};


    /**
     * This function allows to get a new link dialog by giving the
     * component that wants to show it.
     * The function searches the parent Dialog or Frame from the invoking component.
     * This way no headless problems occur.
     * 
     * @param component
     * @return
     */
    static public LinkDialog getNewLinkDialog(Component component) {
        return getNewLinkDialog(component, Local.getString(INSERT_HYPERLINK));
    }
    static public LinkDialog getNewLinkDialog(Component component, String title) {
        while (component != null && !(component instanceof Frame) && !(component instanceof Dialog))
            component = component.getParent();

        LinkDialog dialog = null;

        if (component == null)
            dialog = new LinkDialog((Frame)null, title);
        else if (component instanceof Dialog)
            dialog = new LinkDialog((Dialog)component, title);
        else
            dialog = new LinkDialog((Frame)component, title);

        dialog.positionDialog();

        return dialog;
    }

    private LinkDialog(Frame frame, String title) {
        super(frame, title, icon, true);
        try {
            jbInit();
            pack();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private LinkDialog(Dialog dialog, String title) {
        super(dialog, title, icon, true);
        try {
            jbInit();
            pack();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }


  void jbInit() throws Exception {
      JPanel areaPanel = getAreaPanel();
      
      lblURL.setText(Local.getString("URL"));
      gbc = new GridBagConstraints();
      gbc.gridx = 0; gbc.gridy = 0;
      gbc.insets = new Insets(10, 10, 5, 5);
      gbc.anchor = GridBagConstraints.WEST;
      areaPanel.add(lblURL, gbc);
      txtURL.setPreferredSize(new Dimension(300, 25));
      txtURL.setText("http://");
      gbc = new GridBagConstraints();
      gbc.gridx = 1; gbc.gridy = 0;
      gbc.insets = new Insets(10, 5, 5, 10);
      gbc.anchor = GridBagConstraints.WEST;
      areaPanel.add(txtURL, gbc);
      lblName.setText(Local.getString("Name"));
      gbc = new GridBagConstraints();
      gbc.gridx = 0; gbc.gridy = 1;
      gbc.insets = new Insets(5, 10, 5, 5);
      gbc.anchor = GridBagConstraints.WEST;
      areaPanel.add(lblName, gbc);
      txtName.setPreferredSize(new Dimension(300, 25));
      gbc = new GridBagConstraints();
      gbc.gridx = 1; gbc.gridy = 1;
      gbc.insets = new Insets(5, 5, 5, 10);
      gbc.anchor = GridBagConstraints.WEST;
      areaPanel.add(txtName, gbc);
      lblTitle.setText(Local.getString("Title"));
      gbc = new GridBagConstraints();
      gbc.gridx = 0; gbc.gridy = 2;
      gbc.insets = new Insets(5, 10, 5, 5);
      gbc.anchor = GridBagConstraints.WEST;
      areaPanel.add(lblTitle, gbc);
      txtTitle.setPreferredSize(new Dimension(300, 25));
      gbc = new GridBagConstraints();
      gbc.gridx = 1; gbc.gridy = 2;
      gbc.insets = new Insets(5, 5, 5, 10);
      gbc.anchor = GridBagConstraints.WEST;
      areaPanel.add(txtTitle, gbc);
      lblDesc.setText(Local.getString("Description"));
      gbc = new GridBagConstraints();
      gbc.gridx = 0; gbc.gridy = 3;
      gbc.insets = new Insets(5, 10, 5, 5);
      gbc.anchor = GridBagConstraints.WEST;
      areaPanel.add(lblDesc, gbc);
      txtDesc.setPreferredSize(new Dimension(300, 25));
      gbc = new GridBagConstraints();
      gbc.gridx = 1; gbc.gridy = 3;
      gbc.insets = new Insets(5, 5, 5, 10);
      gbc.anchor = GridBagConstraints.WEST;
      areaPanel.add(txtDesc, gbc);
      chkNewWin.setText(Local.getString("Open in a new window"));
      gbc = new GridBagConstraints();
      gbc.gridx = 1; gbc.gridy = 4;
      gbc.insets = new Insets(5, 5, 10, 10);
      gbc.anchor = GridBagConstraints.WEST;
      areaPanel.add(chkNewWin, gbc);
  }
}