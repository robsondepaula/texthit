package net.sf.jacinth;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import net.sf.jacinth.modules.JacinthDialog;
import net.sf.jacinth.swing.IntegerField;
import net.sf.jacinth.util.Local;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class ImageDialog extends JacinthDialog{

    final static int NUMBER_FIELD_WIDTH = 50;
    final static ImageIcon icon = new ImageIcon(
            net.sf.jacinth.ImageDialog.class.getResource(
                    "resources/icons/imgbig.png"));
    
    GridBagConstraints gbc;
    JLabel jLabel1 = new JLabel();
    public JTextField fileField = new JTextField();
    JButton browseB = new JButton();
    JLabel jLabel2 = new JLabel();
    public JTextField altField = new JTextField();
    JLabel jLabel3 = new JLabel();
    public JTextField widthField = new IntegerField(false);
    JLabel jLabel4 = new JLabel();
    public JTextField heightField = new IntegerField(false);
    JLabel jLabel5 = new JLabel();
    public JTextField hspaceField = new IntegerField();
    JLabel jLabel6 = new JLabel();
    public JTextField vspaceField = new IntegerField();
    JLabel jLabel7 = new JLabel();
    public JTextField borderField = new IntegerField(false);
    JLabel jLabel8 = new JLabel();
    String[] aligns = {"left", "right", "top", "middle", "bottom", "absmiddle",
        "texttop", "baseline"}; 
    // Note: align values are not localized because they are HTML keywords 
    public JComboBox alignCB = new JComboBox(aligns);
    JLabel jLabel9 = new JLabel();
    public JTextField urlField = new JTextField();

    /**
     * This is the reference for all relative image path operations
     */
    URL documentBase;
    
    /**
     * This function allows to get a new image dialog by giving the
     * component that wants to show it.
     * The function searches the parent Dialog or Frame from the invoking component.
     * This way no headless problems occur.
     * 
     * @param component
     * @param documentBase
     * @return
     */
    static public ImageDialog getNewImageDialog(Component component, URL documentBase) {
        while (component != null && !(component instanceof Frame) && !(component instanceof Dialog))
            component = component.getParent();

        ImageDialog dialog = null;
        
        if (component == null)
            dialog = new ImageDialog((Frame)null, documentBase);
        else if (component instanceof Dialog)
            dialog = new ImageDialog((Dialog)component, documentBase);
        else
            dialog = new ImageDialog((Frame)component, documentBase);

        Dimension dlgSize = dialog.getPreferredSize();
        if (component != null) {
            Dimension frmSize = component.getSize();
            Point loc = component.getLocationOnScreen();
            dialog.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
                    (frmSize.height - dlgSize.height) / 2 + loc.y);
        } else {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            dialog.setLocation((screenSize.width - dlgSize.width) / 2,
                    (screenSize.height - dlgSize.height) / 2);
        }
        
        return dialog;
    }
    
    private ImageDialog(Frame frame, URL documentBase) {
        super(frame, Local.getString("Image"), icon, true);
        try {
            this.documentBase = documentBase;
            jbInit();
            pack();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ImageDialog(Dialog dialog, URL documentBase) {
        super(dialog, Local.getString("Image"), icon, true);
        try {
            this.documentBase = documentBase;
            jbInit();
            pack();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        JPanel areaPanel = getAreaPanel();

        jLabel1.setText(Local.getString("Image file"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(jLabel1, gbc);
        fileField.setMinimumSize(new Dimension(200, 25));
        fileField.setPreferredSize(new Dimension(285, 25));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.insets = new Insets(10, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        areaPanel.add(fileField, gbc);
        browseB.setMinimumSize(new Dimension(25, 25));
        browseB.setPreferredSize(new Dimension(25, 25));
        browseB.setIcon(new ImageIcon(
                net.sf.jacinth.ImageDialog.class.getResource(
                        "resources/icons/fileopen16.png")));
        browseB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browseB_actionPerformed(e);
            }
        });
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 5, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(browseB, gbc);
        jLabel2.setText(Local.getString("ALT text"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 10, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(jLabel2, gbc);
        altField.setPreferredSize(new Dimension(315, 25));
        altField.setMinimumSize(new Dimension(200, 25));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(5, 5, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        areaPanel.add(altField, gbc);
        jLabel3.setText(Local.getString("Width"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 10, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(jLabel3, gbc);
        widthField.setPreferredSize(new Dimension(NUMBER_FIELD_WIDTH, 25));
        widthField.setMinimumSize(new Dimension(NUMBER_FIELD_WIDTH, 25));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(widthField, gbc);
        jLabel4.setText(Local.getString("Height"));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 50, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(jLabel4, gbc);
        heightField.setMinimumSize(new Dimension(NUMBER_FIELD_WIDTH, 25));
        heightField.setPreferredSize(new Dimension(NUMBER_FIELD_WIDTH, 25));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(heightField, gbc);
        jLabel5.setText(Local.getString("H. space"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 10, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(jLabel5, gbc);
        hspaceField.setMinimumSize(new Dimension(NUMBER_FIELD_WIDTH, 25));
        hspaceField.setPreferredSize(new Dimension(NUMBER_FIELD_WIDTH, 25));
        hspaceField.setText("0");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(hspaceField, gbc);
        jLabel6.setText(Local.getString("V. space"));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 50, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(jLabel6, gbc);
        vspaceField.setMinimumSize(new Dimension(NUMBER_FIELD_WIDTH, 25));
        vspaceField.setPreferredSize(new Dimension(NUMBER_FIELD_WIDTH, 25));
        vspaceField.setText("0");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(vspaceField, gbc);
        jLabel7.setText(Local.getString("Border"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 10, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(jLabel7, gbc);
        borderField.setMinimumSize(new Dimension(NUMBER_FIELD_WIDTH, 25));
        borderField.setPreferredSize(new Dimension(NUMBER_FIELD_WIDTH, 25));
        borderField.setText("0");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(borderField, gbc);
        jLabel8.setText(Local.getString("Align"));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 50, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(jLabel8, gbc);
        alignCB.setBackground(new Color(230, 230, 230));
        alignCB.setFont(new java.awt.Font("Dialog", 1, 10));
        alignCB.setPreferredSize(new Dimension(100, 25));
        alignCB.setSelectedIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(alignCB, gbc);
        jLabel9.setText(Local.getString("Hyperlink"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 10, 10, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(jLabel9, gbc);
        urlField.setPreferredSize(new Dimension(315, 25));
        urlField.setMinimumSize(new Dimension(200, 25));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(5, 5, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(urlField, gbc);
    }

    private ImageIcon getPreviewIcon(URL url) {
        ImageIcon tmpIcon = new ImageIcon(url);
        ImageIcon thmb = null;
        if (tmpIcon.getIconHeight() > 48) {
            thmb = new ImageIcon(tmpIcon.getImage()
                    .getScaledInstance( -1, 48, Image.SCALE_DEFAULT));
        }
        else {
            thmb = tmpIcon;
        }
        if (thmb.getIconWidth() > 350) {
            return new ImageIcon(thmb.getImage()
                    .getScaledInstance(350, -1, Image.SCALE_DEFAULT));
        }
        else {
            return thmb;
        }
    }

    //java.io.File selectedFile = null;
    public void updatePreview() {
        try {
            URL url = new URL(documentBase, fileField.getText());
            if (url.getFile().length() > 0)
                replaceIcon(getPreviewIcon(url));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void browseB_actionPerformed(ActionEvent e) {
        // Fix until Sun's JVM supports more locales...
        UIManager.put("FileChooser.lookInLabelText", Local
                .getString("Look in:"));
        UIManager.put("FileChooser.upFolderToolTipText", Local.getString(
                "Up One Level"));
        UIManager.put("FileChooser.newFolderToolTipText", Local.getString(
                "Create New Folder"));
        UIManager.put("FileChooser.listViewButtonToolTipText", Local
                .getString("List"));
        UIManager.put("FileChooser.detailsViewButtonToolTipText", Local
                .getString("Details"));
        UIManager.put("FileChooser.fileNameLabelText", Local.getString(
                "File Name:"));
        UIManager.put("FileChooser.filesOfTypeLabelText", Local.getString(
                "Files of Type:"));
        UIManager.put("FileChooser.openButtonText", Local.getString("Open"));
        UIManager.put("FileChooser.openButtonToolTipText", Local.getString(
                "Open selected file"));
        UIManager
                .put("FileChooser.cancelButtonText", Local.getString("Cancel"));
        UIManager.put("FileChooser.cancelButtonToolTipText", Local.getString(
                "Cancel"));

        JFileChooser chooser = new JFileChooser();
        chooser.setFileHidingEnabled(false);
        chooser.setDialogTitle(Local.getString("Choose an image file"));
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.addChoosableFileFilter(
                new net.sf.jacinth.filechooser.ImageFilter());
        chooser.setAccessory(
                new net.sf.jacinth.filechooser.ImagePreview(
                        chooser));
        chooser.setPreferredSize(new Dimension(550, 375));
        java.io.File lastSel = (java.io.File) Context.get(
                "LAST_SELECTED_IMG_FILE");
        if (lastSel != null)
            chooser.setCurrentDirectory(lastSel);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                fileField.setText(chooser.getSelectedFile().toURI().toURL().toString());
                replaceIcon(getPreviewIcon(new URL("file", null, chooser.getSelectedFile().toString())));
                Context.put("LAST_SELECTED_IMG_FILE", chooser.getSelectedFile());
            }
            catch (Exception ex) {
                fileField.setText(chooser.getSelectedFile().getPath());
            }
            try {
                ImageIcon img = new ImageIcon(chooser.getSelectedFile()
                        .getPath());
                widthField.setText(new Integer(img.getIconWidth()).toString());
                heightField
                        .setText(new Integer(img.getIconHeight()).toString());
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}