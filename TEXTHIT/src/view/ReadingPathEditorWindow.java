package view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;
import java.io.File;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.DocumentListener;
import resources.ViewInternationalization;

/**
 * This class plays the View role in the Reading Path Editor secondary
 * application. It is driven by a Controller
 * ({@link controller.ReadingPathEditorController}) and displays the current
 * status of a Model ({@link model.HypertextReadingPath}). <p>It uses the
 * ({@link ViewInternationalization}) class to implement a multi- language user
 * interface.
 */
public class ReadingPathEditorWindow extends JFrame {
    //Declaration

    private ViewInternationalization viewBundle = null;
    private DefaultListModel listModel = null;
    private DefaultComboBoxModel comboModel = null;
    private JPopupMenu removeMenu = null;
    private JMenuItem removeItem = null;
    /**
     * User chose "Yes" option.
     *
     * @see #showWarningOptionBox(String strMsg, String strTitle, String strYes,
     * String strNo)
     */
    public final int OPTION_YES = 1;
    /**
     * User chose "No" option.
     *
     * @see #showWarningOptionBox(String strMsg, String strTitle, String strYes,
     * String strNo)
     */
    public final int OPTION_NO = 2;

    /**
     * Constructs the Reading Path Editor secondary application View class.
     */
    public ReadingPathEditorWindow() {
        viewBundle = new ViewInternationalization("resources.MessagesBundle");

        initComponents();

        comboModel = new DefaultComboBoxModel();
        listModel = new DefaultListModel();
        jComboBox_Select.setModel(comboModel);
        jList_Path.setModel(listModel);

        //Attempts to centralize the main frame window
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);

        //only shows these actions when they are alowed
        disableRemoval();
        disableInsertion();

        //pop-up menu item
        removeItem = new JMenuItem(viewBundle.getString("strJMenuItem_RemoveNode"));
        //instantiate the pop-up menu
        removeMenu = new JPopupMenu();
        removeMenu.add(removeItem);

        //display the editor window
        setVisible(true);
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
     * Allow the Controller ({@link controller.ReadingPathEditorController}) to
     * install an action listener to receive the window closing request from the
     * user interface.
     */
    public void setFormWindowListener(WindowListener w1) {
        addWindowListener(w1);
    }

    /**
     * Allow the Controller ({@link controller.ReadingPathEditorController}) to
     * install an action listener to receive the selection change request from
     * the "Combo Box Of Possible Choices" for the Model
     * ({@link model.HypertextReadingPath}).
     *
     * @see #comboModelAdd(String str)
     */
    public void setComboSelectListener(ActionListener a1) {
        jComboBox_Select.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.ReadingPathEditorController}) to
     * install an action listener to receive the insert choice in the Model
     * ({@link model.HypertextReadingPath}) request.
     */
    public void setInsertButtonListener(ActionListener a1) {
        jButton_Insert.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.ReadingPathEditorController}) to
     * install an action listener to receive the cancel operation request from
     * the user interface.
     */
    public void setCancelButtonListener(ActionListener a1) {
        jButton_Cancel.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.NodeEditorController}) to install
     * an action listener to receive the request to save the changes in the
     * Model ({@link model.HypertextReadingPath}).
     */
    public void setOkButtonListener(ActionListener a1) {
        jButton_Ok.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.NodeEditorController}) to install
     * an action listener to receive the request to remove this
     * ({@link model.HypertextReadingPath}) from the main application Model
     * ({@link model.HypertextMap}).
     */
    public void setRemoveButtonListener(ActionListener a1) {
        jButton_Remove.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.NodeEditorController}) to install
     * an action listener to receive the notification that a inserted choice was
     * selected in the user interface.
     */
    public void setListModelMouseListener(MouseListener m1) {
        jList_Path.addMouseListener(m1);
    }

    /**
     * Allow the Controller ({@link controller.NodeEditorController}) to install
     * an action listener to receive the notification that the remove possible
     * choice operation was requested.
     */
    public void setPopMenuRemoveListener(ActionListener a1) {
        removeItem.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.NodeEditorController}) to request
     * this View to display the remove possible choice user interface element.
     *
     * @param x horizontal axis position to display the element
     **@param y vertical axis position to display the element
     */
    public void showRemoveMenu(int x, int y) {
        //show pop-up menu with vertex options
        removeMenu.show(jList_Path,
                x,
                y);
    }

    /**
     * Allow the Controller ({@link controller.NodeEditorController}) to request
     * this View to enable the remove possible choice operation.
     */
    public void enableRemoval() {
        jButton_Remove.setEnabled(true);
    }

    /**
     * Allow the Controller ({@link controller.NodeEditorController}) to request
     * this View to disable the remove possible choice operation.
     */
    public void disableRemoval() {
        jButton_Remove.setEnabled(false);
    }

    /**
     * Allow the Controller ({@link controller.NodeEditorController}) to request
     * this View to enable the insertion of a possible choice in the Model
     * ({@link model.HypertextReadingPath}).
     *
     * @see #comboModelAdd(String str)
     */
    public void enableInsertion() {
        jButton_Insert.setEnabled(true);
    }

    /**
     * Allow the Controller ({@link controller.NodeEditorController}) to request
     * this View to disable the insertion of a possible choice in the Model
     * ({@link model.HypertextReadingPath}).
     *
     * @see #comboModelAdd(String str)
     */
    public void disableInsertion() {
        jButton_Insert.setEnabled(false);
    }

    /**
     * Insert the text to be displayed in the button that triggers the insertion
     * of a possible choice in the Model ({@link model.HypertextReadingPath}).
     *
     * @param str the text to be displayed
     */
    public void setInsertButtonLabel(String str) {
        jButton_Insert.setText(str);
    }

    /**
     * Insert a possible choice in the "Combo Box Of Possible Choices" that can
     * be inserted in the model ({@link model.HypertextReadingPath}).
     *
     * @param str the possible choice to be displayed
     */
    public void comboModelAdd(String str) {
        comboModel.addElement(str);
    }

    /**
     * Removes all inserted choices in the "Combo Box Of Possible Choices".
     */
    public void comboModelClear() {
        comboModel.removeAllElements();
    }

    /**
     * Insert a possible choice in the graphical representation of the Model
     * ({@link model.HypertextReadingPath}).
     *
     * @param str the possible choice to be displayed
     */
    public void listModelAdd(String str) {
        listModel.addElement(str);
    }

    /**
     * Replace a possible choice in the graphical representation of the Model
     * ({@link model.HypertextReadingPath}).
     *
     * @param index the index to be replaced
     * @param str the new possible choice to be displayed at the given index
     */
    public void listModelReplace(int index, String str) {
        listModel.set(index, str);
    }

    /**
     * Remove a possible choice from the graphical representation of the Model
     * ({@link model.HypertextReadingPath}).
     *
     * @param index the index of the possible choice to be removed
     */
    public void listModelRemove(int index) {
        listModel.remove(index);
    }

    /**
     * Retrieves the number of elements inserted in the graphical representation
     * of the Model ({@link model.HypertextReadingPath}).
     *
     * @return an int containing the number of elements inserted in the
     * graphical representation of the model
     */
    public int getListModelSize() {
        return listModel.size();
    }

    /**
     * Retrieves the currently selected possible choice of the graphical
     * representation of the Model ({@link model.HypertextReadingPath}).
     *
     * @return a String containing the currently selected possible choice
     */
    public String getSelectedListItem() {
        return (String) listModel.getElementAt(jList_Path.getSelectedIndex());
    }

    /**
     * Retrieves the index of the currently selected possible choice in the
     * graphical representation of the Model
     * ({@link model.HypertextReadingPath}).
     *
     * @return an int containing the index of currently selected possible choice
     */
    public int getSelectedListItemIndex() {
        return jList_Path.getSelectedIndex();
    }

    /**
     * Retrieves the possible choice of the graphical representation of the
     * Model ({@link model.HypertextReadingPath}) at the given index.
     *
     * @param index the index of the desired possible choice
     * @return a String containing the possible choice in the given index
     */
    public String getListItem(int index) {
        return (String) listModel.getElementAt(index);
    }

    /**
     * Retrieves the currently selected possible choice from the "Combo Box Of
     * Possible Choices".
     *
     * @return a String containing the currently selected possible choice
     */
    public String getSelectedComboItem() {
        return (String) comboModel.getElementAt(jComboBox_Select.getSelectedIndex());
    }

    /**
     * Displays in the user interface that this
     * {@link model.HypertextReadingPath} is incorrect according to the main
     * Model {@link model.HypertextMap}. Possible the main Model was changed and
     * this Reading Path became invalid.
     */
    public void changePathStatus_BAD() {
        jLabel_PathStatus.setForeground(new java.awt.Color(255, 0, 0));
        jLabel_PathStatus.setText(viewBundle.getString("strReadPath_StatusBad"));
    }

    /**
     * Displays in the user interface that this
     * {@link model.HypertextReadingPath} is correct according to the main Model
     * {@link model.HypertextMap}.
     */
    public void changePathStatus_OK() {
        jLabel_PathStatus.setForeground(new java.awt.Color(0, 153, 153));
        jLabel_PathStatus.setText(viewBundle.getString("strReadPath_StatusGood"));
    }

    /**
     * Retrieves the path name text from the user interface.
     *
     * @return a String containing the path name text displayed in the user
     * interface
     */
    public String getPathName() {
        try {
            return jTextField_PathName.getText();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Allow the Controller ({@link controller.NodeEditorController}) to be
     * notified if the path name text was modified in the user interface.
     */
    public void setNameChangeListener(DocumentListener dl) {
        jTextField_PathName.getDocument().addDocumentListener(dl);
    }

    /**
     * Displays the {@link model.HypertextReadingPath} name property in the user
     * interface.
     *
     * @param strName the name to be displayed
     */
    public void setPathName(String strName) {
        jTextField_PathName.setText(strName);
    }

    /**
     * Gracefully disposes the Reading Path Editor secondary application View
     */
    public void finish() {
        dispose();
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
     * Display a message box to the user.
     *
     * @param strMsg the message to be displayed
     * @param strTitle the title for the message box
     */
    public void showMsgBox(String strMsg, String strTitle) {
        JOptionPane.showMessageDialog(null,
                strMsg,
                strTitle,
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows the save file dialog {@link FilesManipulationDialog} and allows the
     * user to choose the destination file for the Hypertext file representing
     * the Model {@link model.HypertextReadingPath}.
     *
     * @param strPath the initial path shown by the save file dialog
     * @return a String containing the absolute path of the chosen destination
     */
    public String showSaveFileDialog(String strPath) {
        String[] strExtension = {"xhtml"};

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

        File fSaveFile = view.FilesManipulationDialog.showFileChooser(this,
                viewBundle.getString("strHypertextSaveFileChooser_Title"),
                viewBundle.getString("strHypertextSaveFileChooser_OkButton"),
                viewBundle.getString("strHypertextSaveFileChooser_FilterText") + strExtensionLabel,
                strExtension,
                strPath,
                false);

        try {
            return fSaveFile.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Display an warning option box that will allow the user to choose the
     * action to be taken.
     *
     * @param strMsg the message containing a question to the user
     * @param strTitle the title for the option box
     * @param strYes label for the "yes" option
     * @param strNo label for the "no" option
     * @return <code>OPTION_YES</code> if the "yes" option was selected by the
     * user <p><code>OPTION_NO</code> if the "no" option was selected by the
     * user
     * @see #OPTION_YES
     * @see #OPTION_NO
     */
    public int showWarningOptionBox(String strMsg, String strTitle, String strYes, String strNo) {
        Object[] buttonsLabel = {strYes, strNo};

        int result = JOptionPane.showOptionDialog(null,
                strMsg,
                strTitle,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                buttonsLabel,
                buttonsLabel[0]);

        //return the user choice
        switch (result) {
            case JOptionPane.YES_OPTION:
                return OPTION_YES;
            case JOptionPane.NO_OPTION:
                return OPTION_NO;
            default:
                return OPTION_NO;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel_SelectPath = new javax.swing.JPanel();
        jLabel_Edit = new javax.swing.JLabel();
        jComboBox_Select = new javax.swing.JComboBox();
        jButton_Insert = new javax.swing.JButton();
        jScrollPane_Path = new javax.swing.JScrollPane();
        jList_Path = new javax.swing.JList();
        jButton_Cancel = new javax.swing.JButton();
        jLabel_PathName = new javax.swing.JLabel();
        jTextField_PathName = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jButton_Ok = new javax.swing.JButton();
        jButton_Remove = new javax.swing.JButton();
        jLabel_PossibleChoices = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel_prePathStatus = new javax.swing.JLabel();
        jLabel_PathStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(viewBundle.getString("strReadPath_EditorWindowTitle"));
        setName("JFrameRPEW");
        setResizable(false);
        jLabel_Edit.setText(viewBundle.getString("strReadPath_EditPath"));

        jButton_Insert.setText(viewBundle.getString("strReadPath_InsertButton"));

        jList_Path.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane_Path.setViewportView(jList_Path);

        jButton_Cancel.setText(viewBundle.getString("strReadPath_CancelButton"));

        jLabel_PathName.setText(viewBundle.getString("strReadPath_PathName"));

        jButton_Ok.setText(viewBundle.getString("strReadPath_OkButton"));

        jButton_Remove.setText(viewBundle.getString("strReadPath_RemoveButton"));

        jLabel_PossibleChoices.setText(viewBundle.getString("strReadPath_PossibleChoices"));

        jLabel_prePathStatus.setText(viewBundle.getString("strReadPath_PathStatus"));

        jLabel_PathStatus.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel_PathStatus.setForeground(new java.awt.Color(255, 0, 0));
        jLabel_PathStatus.setText(viewBundle.getString("strReadPath_StatusBad"));

        org.jdesktop.layout.GroupLayout jPanel_SelectPathLayout = new org.jdesktop.layout.GroupLayout(jPanel_SelectPath);
        jPanel_SelectPath.setLayout(jPanel_SelectPathLayout);
        jPanel_SelectPathLayout.setHorizontalGroup(
            jPanel_SelectPathLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 699, Short.MAX_VALUE)
            .add(jPanel_SelectPathLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane_Path, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE)
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel_SelectPathLayout.createSequentialGroup()
                .addContainerGap()
                .add(jComboBox_Select, 0, 556, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton_Insert)
                .addContainerGap())
            .add(jPanel_SelectPathLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel_PathName)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTextField_PathName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 432, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 32, Short.MAX_VALUE)
                .add(jButton_Remove)
                .add(5, 5, 5))
            .add(jPanel_SelectPathLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel_Edit)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 199, Short.MAX_VALUE)
                .add(jLabel_prePathStatus)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel_PathStatus)
                .addContainerGap())
            .add(jPanel_SelectPathLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel_PossibleChoices)
                .addContainerGap(598, Short.MAX_VALUE))
            .add(jSeparator2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 699, Short.MAX_VALUE)
            .add(jPanel_SelectPathLayout.createSequentialGroup()
                .addContainerGap()
                .add(jButton_Ok, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 519, Short.MAX_VALUE)
                .add(jButton_Cancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel_SelectPathLayout.setVerticalGroup(
            jPanel_SelectPathLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel_SelectPathLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel_SelectPathLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel_PathName)
                    .add(jTextField_PathName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton_Remove))
                .add(13, 13, 13)
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel_SelectPathLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel_Edit)
                    .add(jLabel_PathStatus)
                    .add(jLabel_prePathStatus))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane_Path, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 235, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel_PossibleChoices)
                .add(6, 6, 6)
                .add(jPanel_SelectPathLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton_Insert)
                    .add(jComboBox_Select, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(28, 28, 28)
                .add(jPanel_SelectPathLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton_Ok)
                    .add(jButton_Cancel))
                .add(23, 23, 23))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel_SelectPath, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel_SelectPath, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_Insert;
    private javax.swing.JButton jButton_Ok;
    private javax.swing.JButton jButton_Remove;
    private javax.swing.JComboBox jComboBox_Select;
    private javax.swing.JLabel jLabel_Edit;
    private javax.swing.JLabel jLabel_PathName;
    private javax.swing.JLabel jLabel_PathStatus;
    private javax.swing.JLabel jLabel_PossibleChoices;
    private javax.swing.JLabel jLabel_prePathStatus;
    private javax.swing.JList jList_Path;
    private javax.swing.JPanel jPanel_SelectPath;
    private javax.swing.JScrollPane jScrollPane_Path;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField_PathName;
    // End of variables declaration//GEN-END:variables
}
