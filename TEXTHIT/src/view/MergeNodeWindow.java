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
 * of possible merge choices.
 */
public class MergeNodeWindow extends JFrame {

    private ViewInternationalization viewBundle = null;
    private DefaultComboBoxModel comboModel = null;

    /**
     * Constructs the insert link window.
     *
     * @param strSourceNode the source node
     * @param choiceList an array of Strings containing the available
     * destination node choices
     */
    public MergeNodeWindow(String strSourceNode, String[] choiceList) {
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

        //Attempts to centralize the main frame window
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);

        setVisible(true);

        // enable interaction
        setChoiceListEnabled(true);
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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel_info = new javax.swing.JLabel();
        jTextField_info = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel_choice = new javax.swing.JLabel();
        jCombo_choices = new javax.swing.JComboBox();
        jButton_Ok = new javax.swing.JButton();
        jButton_Cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(viewBundle.getString("strMergeNode_Title"));
        setResizable(false);

        jLabel_info.setText(viewBundle.getString("strInsertLink_SourceLabel")); // NOI18N

        jTextField_info.setEditable(false);

        jLabel_choice.setText(viewBundle.getString("strInsertLink_TargetLabel")); // NOI18N

        jButton_Ok.setText(viewBundle.getString("strInsertLink_OkButton")); // NOI18N

        jButton_Cancel.setText(viewBundle.getString("strInsertLink_CancelButton")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jLabel_choice)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 256, Short.MAX_VALUE))
                    .add(jCombo_choices, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 426, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .add(66, 66, 66)
                .add(jButton_Ok, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 127, Short.MAX_VALUE)
                .add(jButton_Cancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(91, 91, 91))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 424, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jTextField_info, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 424, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel_info)
                .addContainerGap(263, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jLabel_info)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTextField_info, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel_choice)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jCombo_choices, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(26, 26, 26)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton_Ok)
                    .add(jButton_Cancel))
                .add(94, 94, 94))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_Ok;
    private javax.swing.JComboBox jCombo_choices;
    private javax.swing.JLabel jLabel_choice;
    private javax.swing.JLabel jLabel_info;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField_info;
    // End of variables declaration//GEN-END:variables
}
