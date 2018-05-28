package net.sf.jacinth;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

import net.sf.jacinth.color.ColorField;
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

public class TableDialog extends JacinthDialog {
    
    final static String TABLE = "Table";
    final static ImageIcon icon = new ImageIcon(
            TableDialog.class.getResource(
            "resources/icons/tablebig.png"));
    
	GridBagConstraints gbc;
	JLabel lblWidth = new JLabel();
	public JTextField heightField = new JTextField();
	JLabel lblHeight = new JLabel();
	public JTextField widthField = new JTextField();
	String[] aligns = {"", Local.getString("left"), Local.getString("center"),
		Local.getString("right")};
	String[] valigns = {"", Local.getString("top"), Local.getString("center"),
		Local.getString("bottom")};
	JLabel lblPadding = new JLabel();
	JLabel lblSpacing = new JLabel();

	JLabel lblColumns = new JLabel();
	JLabel lblRows = new JLabel();
	public JComboBox vAlignCB = new JComboBox(valigns);
	JLabel lblOutline = new JLabel();
	public JComboBox alignCB = new JComboBox(aligns);
	JLabel lblVertOutline = new JLabel();
	public JTextField bgcolorField = new ColorField();
	JLabel lblFillColor = new JLabel();
	JButton bgColorB = new JButton();
	JLabel lblBorder = new JLabel();
	public JSpinner columns = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
	public JSpinner rows = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
	public JSpinner cellpadding = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
	public JSpinner cellspacing = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
	public JSpinner border = new JSpinner(new SpinnerNumberModel(1, 0, 999, 1));

      /**
         * This function allows to get a new table dialog by giving the
         * component that wants to show it. The function searches the parent
         * Dialog or Frame from the invoking component. This way no headless
         * problems occur.
         * 
         * @param component
         * @return
         */
    static public TableDialog getNewTDDialog(Component component) {
        while (component != null && !(component instanceof Frame)
                && !(component instanceof Dialog))
            component = component.getParent();

        TableDialog dialog = null;
        
        if (component == null)
            dialog = new TableDialog((Frame)null);
        else if (component instanceof Dialog)
            dialog = new TableDialog((Dialog)component);
        else
            dialog = new TableDialog((Frame)component);

        dialog.positionDialog();
        
        return dialog;
    }

    private TableDialog(Frame frame) {
        super(frame, Local.getString(TABLE), icon, true);
        try {
            jbInit();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private TableDialog(Dialog dialog) {
        super(dialog, Local.getString(TABLE), icon, true);
        try {
            jbInit();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	void jbInit() throws Exception {
        JPanel areaPanel = getAreaPanel();
		
		lblColumns.setText(Local.getString("Columns"));
		gbc = new GridBagConstraints();
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 10, 5, 5);
		areaPanel.add(lblColumns, gbc);
		columns.setPreferredSize(new Dimension(50, 24));
		gbc = new GridBagConstraints();
		gbc.gridx = 1; gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 5, 5, 0);
		areaPanel.add(columns, gbc);
		lblRows.setText(Local.getString("Rows"));
		gbc = new GridBagConstraints();
		gbc.gridx = 3; gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 5, 5, 5);
		areaPanel.add(lblRows, gbc);
		rows.setPreferredSize(new Dimension(50, 24));
		gbc = new GridBagConstraints();
		gbc.gridx = 4; gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 5, 5, 0);
		areaPanel.add(rows, gbc);
		lblWidth.setText(Local.getString("Width"));
		gbc = new GridBagConstraints();
		gbc.gridx = 0; gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 10, 5, 5);
		areaPanel.add(lblWidth, gbc);
		widthField.setPreferredSize(new Dimension(50, 25));
		widthField.setText("100%");
		gbc = new GridBagConstraints();
		gbc.gridx = 1; gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 0);
		areaPanel.add(widthField, gbc);
		lblHeight.setText(Local.getString("Height"));
		gbc.gridx = 3; gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 5);
		areaPanel.add(lblHeight, gbc);
		heightField.setPreferredSize(new Dimension(50, 25));
		gbc.gridx = 4; gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 0);
		areaPanel.add(heightField, gbc);
		lblPadding.setText(Local.getString("Cell padding"));
		gbc.gridx = 0; gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 10, 5, 5);
		areaPanel.add(lblPadding, gbc);
		cellpadding.setPreferredSize(new Dimension(50, 24));
		gbc.gridx = 1; gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 0);
		areaPanel.add(cellpadding, gbc);
		lblSpacing.setText(Local.getString("Cell spacing"));
		gbc.gridx = 3; gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 5);
		areaPanel.add(lblSpacing, gbc);
		cellspacing.setPreferredSize(new Dimension(50, 24));
		gbc.gridx = 4; gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 0);
		areaPanel.add(cellspacing, gbc);
		lblBorder.setText(Local.getString("Border"));
		gbc.gridx = 0; gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 10, 5, 5);
		areaPanel.add(lblBorder, gbc);
		border.setPreferredSize(new Dimension(50, 24));
		gbc.gridx = 1; gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 0);
		areaPanel.add(border, gbc);
		lblFillColor.setText(Local.getString("Fill color"));
		gbc.gridx = 3; gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 5);
		areaPanel.add(lblFillColor, gbc);
		bgcolorField.setPreferredSize(new Dimension(50, 24));
		gbc.gridx = 4; gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 5);
		areaPanel.add(bgcolorField, gbc);
		bgColorB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bgColorB_actionPerformed(e);
			}
		});
		bgColorB.setIcon(new ImageIcon(
			net.sf.jacinth.ImageDialog.class.getResource(
			"resources/icons/color.png")));
		bgColorB.setPreferredSize(new Dimension(25, 25));
		gbc.gridx = 5; gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 10);
		areaPanel.add(bgColorB, gbc);
		lblOutline.setText(Local.getString("Align"));
		gbc.gridx = 0; gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 10, 10, 5);
		areaPanel.add(lblOutline, gbc);
		alignCB.setBackground(new Color(230, 230, 230));
		alignCB.setFont(new java.awt.Font("Dialog", 1, 10));
		alignCB.setPreferredSize(new Dimension(70, 25));
		gbc.gridx = 1; gbc.gridy = 4;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 10, 5);
		areaPanel.add(alignCB, gbc);																																																																				
		lblVertOutline.setText(Local.getString("Vert. align"));
		gbc.gridx = 3; gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 10, 5);
		areaPanel.add(lblVertOutline, gbc);		
		vAlignCB.setPreferredSize(new Dimension(70, 25));
		vAlignCB.setFont(new java.awt.Font("Dialog", 1, 10));
		vAlignCB.setBackground(new Color(230, 230, 230));
		gbc.gridx = 4; gbc.gridy = 4;
		gbc.gridwidth = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 10, 0);
		areaPanel.add(vAlignCB, gbc);
	}

	void bgColorB_actionPerformed(ActionEvent e) {
		// Fix until Sun's JVM supports more locales...
		UIManager.put(
			"ColorChooser.swatchesNameText",
			Local.getString("Swatches"));
		UIManager.put("ColorChooser.hsbNameText", Local.getString("HSB"));
		UIManager.put("ColorChooser.rgbNameText", Local.getString("RGB"));
		UIManager.put(
			"ColorChooser.swatchesRecentText",
			Local.getString("Recent:"));
		UIManager.put("ColorChooser.previewText", Local.getString("Preview"));
		UIManager.put(
			"ColorChooser.sampleText",
			Local.getString("Sample Text")
				+ " "
				+ Local.getString("Sample Text"));
		UIManager.put("ColorChooser.okText", Local.getString("OK"));
		UIManager.put("ColorChooser.cancelText", Local.getString("Cancel"));
		UIManager.put("ColorChooser.resetText", Local.getString("Reset"));
		UIManager.put("ColorChooser.hsbHueText", Local.getString("H"));
		UIManager.put("ColorChooser.hsbSaturationText", Local.getString("S"));
		UIManager.put("ColorChooser.hsbBrightnessText", Local.getString("B"));
		UIManager.put("ColorChooser.hsbRedText", Local.getString("R"));
		UIManager.put("ColorChooser.hsbGreenText", Local.getString("G"));
		UIManager.put("ColorChooser.hsbBlueText", Local.getString("B2"));
		UIManager.put("ColorChooser.rgbRedText", Local.getString("Red"));
		UIManager.put("ColorChooser.rgbGreenText", Local.getString("Green"));
		UIManager.put("ColorChooser.rgbBlueText", Local.getString("Blue"));

		Color initColor = Util.decodeColor(bgcolorField.getText());

		Color c =
			JColorChooser.showDialog(
				this,
				Local.getString("Table background color"),
				initColor);
		if (c == null)
			return;

		bgcolorField.setText(
			"#" + Integer.toHexString(c.getRGB()).substring(2).toUpperCase());
	}

}