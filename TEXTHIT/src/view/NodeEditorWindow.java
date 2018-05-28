package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import resources.TemporaryConfigManager;
import resources.ViewInternationalization;
import util.PersistencePackager;

/**
 * This class plays the View role in the Node Editor secondary application. It
 * is driven by a Controller ({@link controller.NodeEditorController}) and
 * displays the current status of a Model ({@link model.HypertextNode}). <p>It
 * uses the ({@link ViewInternationalization}) class to implement a multi-
 * language user interface. <p>It uses a HTMLManager to allow the WYSIWYG
 * edition of the Model.
 */
public class NodeEditorWindow extends JFrame {
    
    private ViewInternationalization viewBundle = null;

    /**
     * Constructs the Node Editor secondary application View class.
     */
    public NodeEditorWindow() {
        viewBundle = new ViewInternationalization("resources.MessagesBundle");
        
        initComponents();

        //Attempts to centralize the main frame window
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);

        //default is not in error condition
        setFileNotFoundMsgVisibility(false);
        //display the editor window
        setVisible(true);
    }

    /**
     * Attaches the given displayable component and shows it in the
     * NodeEditorView area of the secondary application window.
     *
     * @param displayable component to be attached
     */
    public void attachHTMLManager(Component displayable) {
        //attaches the given displayable component and shows it in the main window
        jPanel_EditArea.add(displayable);
        //updates parent JFrame
        pack();
    }

    /**
     * Detaches the HTMLManager displayable component and removes it from the
     * the secondary application window.
     */
    public void detachHTMLManager() {
        //removes the displayable component
        jPanel_EditArea.removeAll();
    }

    /**
     * Retrieves the multi-language value for the given key.
     *
     * @param strKey the key for the desired multi-language user interface item
     * @return the desired value stored in the given key <p>an error String if
     * the key is invalid or has no value stored in it
     */
    public String getViewBundleString(String strKey) {
        return viewBundle.getString(strKey);
    }

    /**
     * Allow the Controller ({@link controller.NodeEditorController}) to install
     * an action listener to receive the window closing request from the user
     * interface.
     */
    public void setFormWindowListener(WindowListener w1) {
        addWindowListener(w1);
    }

    /**
     * Allow the Controller ({@link controller.NodeEditorController}) to install
     * an action listener to receive the request to save the file pointed by
     * ({@link model.HypertextNode}) URL property.
     */
    public void setSaveButtonListener(ActionListener a1) {
        jButton_Save.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.NodeEditorController}) to install
     * an action listener to receive the cancel operation request from the user
     * interface.
     */
    public void setCancelButtonListener(ActionListener a1) {
        jButton_Cancel.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.NodeEditorController}) to install
     * an action listener to receive the find file operation request from the
     * user interface.
     */
    public void setFindButtonListener(ActionListener a1) {
        jButton_Find.addActionListener(a1);
    }

    /**
     * Gracefuly disposes the Node Editor secondary application View
     */
    public void finish() {
        dispose();
    }

    /**
     * Displays the {@link model.HypertextNode} title property in the user
     * interface.
     *
     * @param nodeTitle the title to be displayed
     */
    public void setNodeTitle(String nodeTitle) {
        jTextField_Title.setText(nodeTitle);
    }

    /**
     * Displays the {@link model.HypertextNode} URL property in the user
     * interface.
     *
     * @param nodeURL the URL to be displayed
     */
    public void setNodeURL(String nodeURL) {
        jTextField_URL.setText(nodeURL);
    }

    /**
     * Retrieves the title text from the user interface.
     *
     * @return a String containing the title text displayed and probably
     * modified in the user interface
     */
    public String getNodeTitle() {
        return jTextField_Title.getText();
    }

    /**
     * Retrieves the URL text from the user interface.
     *
     * @return a String containing the URL text displayed and probably modified
     * in the user interface
     */
    public String getNodeURL() {
        return jTextField_URL.getText();
    }

    /**
     * Shows the select file dialog {@link FilesManipulationDialog} and allows
     * the user to choose the destination for the {@link model.HypertextNode}
     * being edited.
     *
     * @param strPath the initial path shown by the open file dialog
     * @return a String containing the absolute path for HypertextNode
     */
    public String openNodeFile(String strPath) {
        File f = null;
        String[] strExtension = {viewBundle.getString("strNodeExtension_0"),
            viewBundle.getString("strNodeExtension_1"),
            viewBundle.getString("strNodeExtension_2")};
        
        StringBuffer res = new StringBuffer();
        res.append(" (");
        for (int i = 0; i < strExtension.length; i++) {
            res.append(".");
            res.append(strExtension[i]);
            if (i < (strExtension.length - 1)) {
                res.append(",");
            }
        }
        res.append(")");
        String strExtensionLabel = res.toString();
        
        if (strPath == null) {
            strPath = TemporaryConfigManager.getNodeEditorPath();
        }

        //opens a filechooser and allow the user to select a file to be inserted
        f = view.FilesManipulationDialog.showFileChooser(this,
                viewBundle.getString("strHypertextEditFileChooser_Title"),
                viewBundle.getString("strHypertextEditFileChooser_OkButton"),
                viewBundle.getString("strHypertextEditFileChooser_FilterText") + strExtensionLabel,
                strExtension,
                strPath,
                true);
        
        try {
            String tmpPath = f.getAbsolutePath();
            TemporaryConfigManager.setNodeEditorPath(tmpPath.substring(0, tmpPath.lastIndexOf(PersistencePackager.fileSeparator)));
            return tmpPath;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Enables all available commands exposed by this View.
     */
    public void enableViewInteraction() {
        jButton_Find.setEnabled(true);
        jButton_Save.setEnabled(true);
        jPanel_EditArea.setEnabled(true);
    }

    /**
     * Disables all available commands exposed by this View.
     */
    public void disableViewInteraction() {
        jButton_Save.setEnabled(false);
        jPanel_EditArea.setEnabled(false);
    }

    /**
     * Enables all available fields exposed by this View.
     */
    public void enableViewFields() {
        jTextField_Title.setEnabled(true);
        jTextField_URL.setEnabled(true);
        jButton_Find.setEnabled(true);
    }

    /**
     * Disables all available fields exposed by this View.
     */
    public void disableViewFields() {
        jTextField_Title.setEnabled(false);
        jTextField_URL.setEnabled(false);
        jButton_Find.setEnabled(false);
    }

    /**
     * Display an error box to the user.
     *
     * @param strErrorMsg the error message
     * @param strErrorTitle the title for the error box
     */
    public void showErrorBox(String strErrorMsg, String strErrorTitle) {
        JOptionPane.showMessageDialog(null,
                strErrorMsg,
                strErrorTitle,
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Display an information box to the user.
     *
     * @param strInfoMsg the message
     * @param strInfoTitle the title for the info box
     */
    public void showInfoBox(String strInfoMsg, String strInfoTitle) {
        JOptionPane.showMessageDialog(null,
                strInfoMsg,
                strInfoTitle,
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Hides or exposes to the user a file not found error message in the
     * HTMLManager visible area.
     *
     * @param visibility <code>true</code> display the error message       *<p> <code>false</code> hide the error message
     */
    public void setFileNotFoundMsgVisibility(boolean visibility) {
        jLabel_Err.setVisible(visibility);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel_Title = new javax.swing.JLabel();
        jTextField_Title = new javax.swing.JTextField();
        jLabel_URL = new javax.swing.JLabel();
        jTextField_URL = new javax.swing.JTextField();
        jButton_Save = new javax.swing.JButton();
        jButton_Cancel = new javax.swing.JButton();
        jButton_Find = new javax.swing.JButton();
        jPanel_EditArea = new javax.swing.JPanel();
        jLabel_Err = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(viewBundle.getString("strNodeEdit_WindowTitle"));
        setName("JFrameNEW");
        setResizable(false);
        jLabel_Title.setText(viewBundle.getString("strNodeEdit_Title"));

        jLabel_URL.setText(viewBundle.getString("strNodeEdit_URL"));

        jButton_Save.setText(viewBundle.getString("strNodeEdit_SaveButton"));

        jButton_Cancel.setText(viewBundle.getString("strNodeEdit_CancelButton"));

        jButton_Find.setText(viewBundle.getString("strNodeEdit_FindButton"));

        jPanel_EditArea.setLayout(new java.awt.BorderLayout());

        jPanel_EditArea.setBackground(new java.awt.Color(204, 204, 204));
        jPanel_EditArea.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jLabel_Err.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Err.setText(viewBundle.getString("strNodeEdit_FileNotFound"));
        jPanel_EditArea.add(jLabel_Err, java.awt.BorderLayout.CENTER);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(jButton_Save)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jButton_Cancel))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel_Title)
                            .add(jLabel_URL))
                        .add(9, 9, 9)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jTextField_URL, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 243, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jTextField_Title))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton_Find))
            .add(jPanel_EditArea, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(jPanel_EditArea, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel_Title)
                    .add(jTextField_Title, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(14, 14, 14)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel_URL)
                    .add(jButton_Find)
                    .add(jTextField_URL, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(29, 29, 29)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton_Save)
                    .add(jButton_Cancel))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
                    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_Find;
    private javax.swing.JButton jButton_Save;
    private javax.swing.JLabel jLabel_Err;
    private javax.swing.JLabel jLabel_Title;
    private javax.swing.JLabel jLabel_URL;
    private javax.swing.JPanel jPanel_EditArea;
    private javax.swing.JTextField jTextField_Title;
    private javax.swing.JTextField jTextField_URL;
    // End of variables declaration//GEN-END:variables
}
