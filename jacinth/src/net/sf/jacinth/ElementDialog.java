package net.sf.jacinth;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.ImageIcon;
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

public class ElementDialog extends JacinthDialog {
  final static String OBJECT_PROPERTIES = "Object properties";
  final static ImageIcon icon = new ImageIcon(
        net.sf.jacinth.ElementDialog.class.getResource(
        "resources/icons/textbig.png"));

  JLabel lblClass = new JLabel();
  public JTextField classField = new JTextField();

  JLabel lblID = new JLabel();
  public JTextField idField = new JTextField();
  JLabel lblStyle = new JLabel();
  public JTextField styleField = new JTextField();
  GridBagConstraints gbc;

  /**
   * This function allows to get a new element dialog by giving the
   * component that wants to show it.
   * The function searches the parent Dialog or Frame from the invoking component.
   * This way no headless problems occur.
   * 
   * @param component
   * @return
   */
  static public ElementDialog getNewElementDialog(Component component) {
      while (component != null && !(component instanceof Frame) && !(component instanceof Dialog))
          component = component.getParent();

      ElementDialog dialog;
      
      if (component == null)
          dialog = new ElementDialog((Frame)null);
      else if (component instanceof Dialog)
          dialog = new ElementDialog((Dialog)component);
      else
          dialog = new ElementDialog((Frame)component);

      dialog.positionDialog();
      
      return dialog;
  }
  
  private ElementDialog(Frame frame) {
    super(frame, Local.getString(OBJECT_PROPERTIES), icon, true);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  private ElementDialog(Dialog dialog) {
      super(dialog, Local.getString(OBJECT_PROPERTIES), icon, true);
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
    
	lblID.setText(Local.getString("ID"));
	gbc = new GridBagConstraints();
	gbc.gridx = 0; gbc.gridy = 0;
	gbc.anchor = GridBagConstraints.WEST;
	gbc.insets = new Insets(10, 10, 5, 5);
    areaPanel.add(lblID, gbc);
	idField.setPreferredSize(new Dimension(300, 25));
	gbc = new GridBagConstraints();
	gbc.gridx = 1; gbc.gridy = 0;
	gbc.anchor = GridBagConstraints.WEST;
	gbc.insets = new Insets(10, 5, 5, 10);
	areaPanel.add(idField, gbc);
	lblClass.setText(Local.getString("Class"));
	gbc = new GridBagConstraints();
	gbc.gridx = 0; gbc.gridy = 1;
	gbc.anchor = GridBagConstraints.WEST;
	gbc.insets = new Insets(5, 10, 5, 5);
	areaPanel.add(lblClass, gbc);		
	classField.setPreferredSize(new Dimension(300, 25));
	gbc = new GridBagConstraints();
	gbc.gridx = 1; gbc.gridy = 1;
	gbc.anchor = GridBagConstraints.WEST;
	gbc.insets = new Insets(5, 5, 5, 10);
	areaPanel.add(classField, gbc);
	lblStyle.setText(Local.getString("Style"));
	gbc = new GridBagConstraints();
	gbc.gridx = 0; gbc.gridy = 2;
	gbc.anchor = GridBagConstraints.WEST;
	gbc.insets = new Insets(5, 10, 10, 5);
	areaPanel.add(lblStyle, gbc);
	styleField.setPreferredSize(new Dimension(300, 25));
	gbc = new GridBagConstraints();
	gbc.gridx = 1; gbc.gridy = 2;
	gbc.anchor = GridBagConstraints.WEST;
	gbc.insets = new Insets(5, 5, 10, 10);
	areaPanel.add(styleField, gbc);
  }

}