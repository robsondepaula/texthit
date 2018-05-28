package view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import resources.ViewInternationalization;

/**
 * Displayable component that allow the user to select a target node from a list
 * of possible link choices.
 */
public class InsertLinkWindow extends JFrame {

    private ViewInternationalization viewBundle = null;
    private DefaultComboBoxModel comboModel = null;

    /**
     * Constructs the insert link window.
     *
     * @param strSourceNode the source node
     * @param choiceList an array of Strings containing the available
     * destination node choices
     */
    public InsertLinkWindow(String strSourceNode, String[] choiceList) {
        viewBundle = new ViewInternationalization("resources.MessagesBundle");

        initComponents();

        if (choiceList != null) {
            comboModel = new DefaultComboBoxModel(choiceList);
            jCombo_choices.setModel(comboModel);
        } else {
            //failed building choice list
            jCombo_choices.setEnabled(false);
        }

        jTextField_info.setText(strSourceNode);
        setChoiceListEnabled(false);
        setTypeFieldEnabled(false);

        //Attempts to centralize the main frame window
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);

        setVisible(true);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the window closing request from
     * the user interface.
     */
    public void setFormWindowListener(WindowListener w1) {
        addWindowListener(w1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the cancel operation request
     * from the user interface.
     */
    public void setCancelButtonListener(ActionListener a1) {
        jButton_Cancel.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the insert link operation
     * request from the user interface.
     */
    public void setOkButtonListener(ActionListener a1) {
        jButton_Ok.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the internal link option
     * selection.
     */
    public void setInternalLinkOptionListener(ActionListener a1) {
        jRadioButton_Internal.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the external link option
     * selection.
     */
    public void setExternalLinkOptionListener(ActionListener a1) {
        jRadioButton_External.addActionListener(a1);
    }

    /**
     * Gets the text of the selected choice among the given list of available
     * choices.
     *
     * @return a String containing the selected choice from the given list
     * <p><code>null</code> if an error occurred while retrieving the selected
     * option
     */
    public String getSelectedChoice() {
        try {
            if (jCombo_choices.isEnabled()) {
                return jCombo_choices.getSelectedItem().toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the text of the typed URL for the Link.
     *
     * @return a String containing the typed URL for the Link
     * <p><code>null</code> if an error occurred while retrieving typed text
     */
    public String getTypedURL() {
        try {
            return viewBundle.getString("str_InsertLinkWindow_PreURL")
                    + jTextField_URL.getText();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * The user can choose to make this Link open a new browser window.
     * <p><code>true</code> if this Link opens a new browser window
     * <p><code>false</code> if this Link opens in te same browser window
     */
    public boolean linkOpensNewWindow() {
        try {
            return jCheckBoxNewWindow.isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sets the state of the URL text field.
     *
     * @param opt <p><code>true</code> enabled <p><code>false</code> disabled
     */
    public void setTypeFieldEnabled(boolean opt) {
        jLabel_PreURL.setEnabled(opt);
        jLabel_Type.setEnabled(opt);
        jTextField_URL.setEnabled(opt);
        jTextField_URL.setEditable(opt);
    }

    /**
     * Sets the state of the choice list of available link destinations.
     *
     * @param opt <p><code>true</code> enabled <p><code>false</code> disabled
     */
    public void setChoiceListEnabled(boolean opt) {
        jLabel_choice.setEnabled(opt);
        jCombo_choices.setEnabled(opt);
    }

    /**
     * Sets the state of the OK button.
     *
     * @param opt <p><code>true</code> enabled <p><code>false</code> disabled
     */
    public void setOKButtonEnabled(boolean opt) {
        jButton_Ok.setEnabled(opt);
    }

    /**
     * Gracefuly disposes this window
     */
    public void finish() {
        dispose();
    }

    /**
     * Exposes functionality for insertion of an External Link. Hides insertion
     * of Internal Links.
     */
    public void setModeToExternalLink() {
        setChoiceListEnabled(false);
        setTypeFieldEnabled(true);
    }

    /**
     * Exposes functionality for insertion of an Internal Link. Hides insertion
     * of External Links.
     */
    public void setModeToInternalLink() {
        setChoiceListEnabled(true);
        setTypeFieldEnabled(false);
        jTextField_URL.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        buttonGroup_linkType = new javax.swing.ButtonGroup();
        jPanel_selectChoice = new javax.swing.JPanel();
        jLabel_info = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextField_info = new javax.swing.JTextField();
        jLabel_choice = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jCombo_choices = new javax.swing.JComboBox();
        jButton_Ok = new javax.swing.JButton();
        jButton_Cancel = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel_Type = new javax.swing.JLabel();
        jLabel_PreURL = new javax.swing.JLabel();
        jTextField_URL = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jCheckBoxNewWindow = new javax.swing.JCheckBox();
        jRadioButton_Internal = new javax.swing.JRadioButton();
        jRadioButton_External = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(viewBundle.getString("strInsertLink_Title"));
        setName("JFrame_InsertLink");
        setResizable(false);
        jLabel_info.setText(viewBundle.getString("strInsertLink_SourceLabel"));

        jTextField_info.setEditable(false);
        jScrollPane1.setViewportView(jTextField_info);

        jLabel_choice.setText(viewBundle.getString("strInsertLink_TargetLabel"));

        jButton_Ok.setText(viewBundle.getString("strInsertLink_OkButton"));

        jButton_Cancel.setText(viewBundle.getString("strInsertLink_CancelButton"));

        jLabel_Type.setText(viewBundle.getString("str_InsertLinkWindow_TypeLinkURL"));

        jLabel_PreURL.setText(viewBundle.getString("str_InsertLinkWindow_PreURL"));

        jCheckBoxNewWindow.setText(viewBundle.getString("str_InsertLinkWindow_NewWindow"));
        jCheckBoxNewWindow.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxNewWindow.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroup_linkType.add(jRadioButton_Internal);
        jRadioButton_Internal.setText(viewBundle.getString("str_InsertLinkWindow_Internal"));
        jRadioButton_Internal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton_Internal.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroup_linkType.add(jRadioButton_External);
        jRadioButton_External.setText(viewBundle.getString("str_InsertLinkWindow_External"));
        jRadioButton_External.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton_External.setMargin(new java.awt.Insets(0, 0, 0, 0));

        org.jdesktop.layout.GroupLayout jPanel_selectChoiceLayout = new org.jdesktop.layout.GroupLayout(jPanel_selectChoice);
        jPanel_selectChoice.setLayout(jPanel_selectChoiceLayout);
        jPanel_selectChoiceLayout.setHorizontalGroup(
            jPanel_selectChoiceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel_selectChoiceLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel_selectChoiceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel_selectChoiceLayout.createSequentialGroup()
                        .add(jButton_Ok, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 214, Short.MAX_VALUE)
                        .add(jButton_Cancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jLabel_info)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE))
                .addContainerGap())
            .add(jSeparator2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
            .add(jPanel_selectChoiceLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel_Type)
                .addContainerGap(205, Short.MAX_VALUE))
            .add(jPanel_selectChoiceLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel_PreURL)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTextField_URL, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                .addContainerGap())
            .add(jSeparator3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
            .add(jPanel_selectChoiceLayout.createSequentialGroup()
                .addContainerGap()
                .add(jCheckBoxNewWindow)
                .addContainerGap(191, Short.MAX_VALUE))
            .add(jPanel_selectChoiceLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel_selectChoiceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel_choice)
                    .add(jCombo_choices, 0, 374, Short.MAX_VALUE))
                .addContainerGap())
            .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
            .add(jPanel_selectChoiceLayout.createSequentialGroup()
                .addContainerGap()
                .add(jRadioButton_External)
                .addContainerGap(289, Short.MAX_VALUE))
            .add(jPanel_selectChoiceLayout.createSequentialGroup()
                .addContainerGap()
                .add(jRadioButton_Internal)
                .addContainerGap(291, Short.MAX_VALUE))
        );
        jPanel_selectChoiceLayout.setVerticalGroup(
            jPanel_selectChoiceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel_selectChoiceLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel_info)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(15, 15, 15)
                .add(jRadioButton_Internal)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jRadioButton_External)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel_choice)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCombo_choices, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(15, 15, 15)
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel_Type)
                .add(15, 15, 15)
                .add(jPanel_selectChoiceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel_PreURL)
                    .add(jTextField_URL, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(19, 19, 19)
                .add(jSeparator3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxNewWindow)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 28, Short.MAX_VALUE)
                .add(jPanel_selectChoiceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton_Ok)
                    .add(jButton_Cancel)))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel_selectChoice, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel_selectChoice, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
                    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup_linkType;
    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_Ok;
    private javax.swing.JCheckBox jCheckBoxNewWindow;
    private javax.swing.JComboBox jCombo_choices;
    private javax.swing.JLabel jLabel_PreURL;
    private javax.swing.JLabel jLabel_Type;
    private javax.swing.JLabel jLabel_choice;
    private javax.swing.JLabel jLabel_info;
    private javax.swing.JPanel jPanel_selectChoice;
    private javax.swing.JRadioButton jRadioButton_External;
    private javax.swing.JRadioButton jRadioButton_Internal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField jTextField_URL;
    private javax.swing.JTextField jTextField_info;
    // End of variables declaration//GEN-END:variables
}
