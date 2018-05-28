package net.sf.jacinth;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

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

public class FontDialog extends JacinthDialog {
    final static String TEXT_PROPERTIES = "Text properties";
    final static ImageIcon icon = new ImageIcon(
            net.sf.jacinth.ImageDialog.class.getResource(
            "resources/icons/fontbig.png"));
    
    GridBagConstraints gbc;

    public JComboBox fontSizeCB = new JComboBox(new Object[] 
    	{"", "1","2","3","4","5","6","7"});
    public JComboBox fontFamilyCB;
    public JLabel sample = new JLabel();
    JPanel samplePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    public JTextField colorField = new JTextField();
    JLabel lblTextColor = new JLabel();
    JButton colorB = new JButton();

    /**
     * This function allows to get a new font dialog by giving the
     * component that wants to show it.
     * The function searches the parent Dialog or Frame from the invoking component.
     * This way no headless problems occur.
     * 
     * @param component
     * @return
     */
    static public FontDialog getNewFontDialog(Component component) {
        while (component != null && !(component instanceof Frame) && !(component instanceof Dialog))
            component = component.getParent();

        FontDialog dialog;
        
        if (component == null)
            dialog = new FontDialog((Frame)null);
        else if (component instanceof Dialog)
            dialog = new FontDialog((Dialog)component);
        else
            dialog = new FontDialog((Frame)component);
        
        dialog.positionDialog();
        
        return dialog;
    }

    private FontDialog(Frame frame) {
        super(frame, Local.getString(TEXT_PROPERTIES), icon, true);
        try {
            jbInit();
            pack();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private FontDialog(Dialog dialog) {
        super(dialog, Local.getString(TEXT_PROPERTIES), icon, true);
        try {
            jbInit();
            pack();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        JPanel areaPanel = getAreaPanel();

        GraphicsEnvironment gEnv = 
        	GraphicsEnvironment.getLocalGraphicsEnvironment();
        String envfonts[] = gEnv.getAvailableFontFamilyNames();
        Vector fonts = new Vector();
        fonts.add("");
        fonts.add("serif");
        fonts.add("sans-serif");
        fonts.add("monospaced");
        for (int i = 0; i < envfonts.length; i++)
            fonts.add(envfonts[i]);
        fontFamilyCB = new JComboBox(fonts);
        
        fontFamilyCB.setMaximumRowCount(9);
		fontFamilyCB.setBorder(new TitledBorder(
			BorderFactory.createEmptyBorder(), 
			Local.getString("Font family")));
		fontFamilyCB.setPreferredSize(new Dimension(200, 50));
		fontFamilyCB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fontChanged(e);
			}
		});
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 5, 5);
        areaPanel.add(fontFamilyCB, gbc);
		fontSizeCB.setEditable(true);
		fontSizeCB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fontChanged(e);
			}
		});
		fontSizeCB.setBorder(new TitledBorder(
			BorderFactory.createEmptyBorder(), Local.getString("Font size")));
		fontSizeCB.setPreferredSize(new Dimension(60, 50));
		gbc = new GridBagConstraints();
		gbc.gridx = 2; gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 5, 5, 10);
		areaPanel.add(fontSizeCB, gbc);
		lblTextColor.setText(Local.getString("Font color"));		
		gbc = new GridBagConstraints();
		gbc.gridx = 0; gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(5, 20, 5, 5);
		areaPanel.add(lblTextColor, gbc);
		colorField.setPreferredSize(new Dimension(60, 25));
		gbc = new GridBagConstraints();
		gbc.gridx = 1; gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(5, 5, 5, 5);
		areaPanel.add(colorField, gbc);
		colorB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorB_actionPerformed(e);
			}
		});
		colorB.setIcon(new ImageIcon(
			net.sf.jacinth.FontDialog.class.getResource(
			"resources/icons/color.png")));
		colorB.setPreferredSize(new Dimension(25, 25));
		gbc = new GridBagConstraints();
		gbc.gridx = 2; gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(5, 5, 5, 5);
		areaPanel.add(colorB, gbc);
		samplePanel.setBackground(Color.white);
		samplePanel.setBorder(BorderFactory.createTitledBorder(
			Local.getString("Sample")));
		sample.setText(Local.getString("AaBbCcDd"));
		sample.setHorizontalAlignment(SwingConstants.CENTER);
		sample.setVerticalAlignment(SwingConstants.CENTER);
		sample.setPreferredSize(new Dimension(250, 50));
		samplePanel.add(sample);		
		gbc = new GridBagConstraints();
		gbc.gridx = 0; gbc.gridy = 2;
		gbc.gridwidth = 3;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 10, 10, 10);
		areaPanel.add(samplePanel, gbc);
    }

    void fontChanged(ActionEvent e) {
        int[] sizes = {8,10,13,16,18,24,32};
        int size = 16;
        String face;
        Font font = sample.getFont();
        if (fontSizeCB.getSelectedIndex() > 0)
            size = sizes[fontSizeCB.getSelectedIndex()-1];
        if (fontFamilyCB.getSelectedIndex() >0)
             face = (String)fontFamilyCB.getSelectedItem();
        else face = font.getName();        
        sample.setFont(new Font(face,Font.PLAIN, size));
    }
   
    void colorB_actionPerformed(ActionEvent e) {
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
        Color c = JColorChooser.showDialog(this, Local.getString("Font color"), 
        	Util.decodeColor(colorField.getText()));
        if (c == null) return;
        colorField.setText(Util.encodeColor(c));
        Util.setColorField(colorField);
        sample.setForeground(c);
    }

}