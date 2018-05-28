package net.sf.jacinth;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;

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

public class FindDialog extends JacinthDialog {
    final static String FIND_REPLACE = "Find & replace";
    final static ImageIcon icon = new ImageIcon(
                net.sf.jacinth.ImageDialog.class.getResource(
                    "resources/icons/findbig.png"));
    
	JLabel lblSearch = new JLabel();
	public JTextField txtSearch = new JTextField();
	public JCheckBox chkReplace = new JCheckBox();
	public JCheckBox chkCaseSens = new JCheckBox();
	public JCheckBox chkWholeWord = new JCheckBox();
	public JCheckBox chkRegExp = new JCheckBox();
	public JTextField txtReplace = new JTextField();
	GridBagConstraints gbc;

      /**
         * This function allows to get a new find dialog by giving the component
         * that wants to show it. The function searches the parent Dialog or
         * Frame from the invoking component. This way no headless problems
         * occur.
         * 
         * @param component
         * @return
         */
    static public FindDialog getNewFindDialog(Component component) {
        while (component != null && !(component instanceof Frame)
                && !(component instanceof Dialog))
            component = component.getParent();

        FindDialog dialog;
        
        if (component == null)
            dialog = new FindDialog((Frame) null);
        else if (component instanceof Dialog)
            dialog = new FindDialog((Dialog) component);
        else
            dialog = new FindDialog((Frame) component);

        dialog.positionDialog();
        
        return dialog;
    }

    public FindDialog(Frame frame) {
        super(frame, Local.getString(FIND_REPLACE), icon, true);
        try {
            jbInit();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public FindDialog(Dialog dialog) {
        super(dialog, Local.getString(FIND_REPLACE), icon, true);
        try {
            jbInit();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	void jbInit() throws Exception {
        JPanel areaPanel = getAreaPanel();

		// build areaPanel
		lblSearch.setText(Local.getString("Search for") + ":");
		gbc = new GridBagConstraints();
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.insets = new Insets(10, 10, 5, 0);
		gbc.anchor = GridBagConstraints.WEST;
		areaPanel.add(lblSearch, gbc);
		txtSearch.setPreferredSize(new Dimension(300, 25));
		gbc = new GridBagConstraints();
		gbc.gridx = 0; gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 10, 5, 10);
		gbc.anchor = GridBagConstraints.WEST;
		areaPanel.add(txtSearch, gbc);
		chkWholeWord.setText(Local.getString("Whole words only"));
		gbc = new GridBagConstraints();
		gbc.gridx = 0; gbc.gridy = 2;
		gbc.insets = new Insets(5, 10, 5, 25);
		gbc.anchor = GridBagConstraints.WEST;
		areaPanel.add(chkWholeWord, gbc);
		chkRegExp.setText(Local.getString("Regular expressions"));
		gbc = new GridBagConstraints();
		gbc.gridx = 1; gbc.gridy = 2;
		gbc.insets = new Insets(5, 25, 5, 10);
		gbc.anchor = GridBagConstraints.WEST;
		areaPanel.add(chkRegExp, gbc);
		chkCaseSens.setText(Local.getString("Case sensitive"));
		gbc = new GridBagConstraints();
		gbc.gridx = 0; gbc.gridy = 3;
		gbc.insets = new Insets(5, 10, 5, 10);
		gbc.anchor = GridBagConstraints.WEST;
		areaPanel.add(chkCaseSens, gbc);
		chkReplace.setText(Local.getString("Replace with") + ":");
		chkReplace.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				replaceChB_actionPerformed(e);
			}
		});
		gbc = new GridBagConstraints();
		gbc.gridx = 0; gbc.gridy = 4;
		gbc.insets = new Insets(5, 10, 5, 10);
		gbc.anchor = GridBagConstraints.WEST;
		areaPanel.add(chkReplace, gbc);
		txtReplace.setPreferredSize(new Dimension(300, 25));
		txtReplace.setEnabled(false);
		gbc = new GridBagConstraints();
		gbc.gridx = 0; gbc.gridy = 5;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;
		areaPanel.add(txtReplace, gbc);
	}

	void replaceChB_actionPerformed(ActionEvent e) {
		txtReplace.setEnabled(chkReplace.isSelected());
	}

}