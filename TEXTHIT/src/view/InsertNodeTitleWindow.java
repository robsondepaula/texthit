package view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import resources.ViewInternationalization;

/**
 * Displayable component that allow the user to insert a title for a node.
 */
public class InsertNodeTitleWindow extends javax.swing.JFrame {

    private ViewInternationalization viewBundle = null;

    /**
     * Constructs the insert node title window.
     *
     * @param nodeURL used to display to the user the URL for the node that
     * needs a title
     */
    public InsertNodeTitleWindow(String nodeURL) {
        viewBundle = new ViewInternationalization("resources.MessagesBundle");

        initComponents();

        //Attempts to centralize the main frame window
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);

        jTextField_nodeURL.setText(nodeURL);

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
     * to install an action listener to receive the insert title operation
     * request from the user interface.
     */
    public void setOkButtonListener(ActionListener a1) {
        jButton_OK.addActionListener(a1);
    }

    /**
     * Gets the text of the inserted title.
     *
     * @return a String containing the inserted title <p><code>null</code> if an
     * error occurred
     */
    public String getNodeTitle() {
        try {
            if (jTextField_title.getText().trim().length() == 0) {
                return null;
            }
            return jTextField_title.getText().trim();
        } catch (Exception e) {
            return null;
        }
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
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel_title = new javax.swing.JPanel();
        jLabel_Msg = new javax.swing.JLabel();
        jScrollPane_nodeURL = new javax.swing.JScrollPane();
        jTextField_nodeURL = new javax.swing.JTextField();
        jTextField_title = new javax.swing.JTextField();
        jButton_OK = new javax.swing.JButton();
        jButton_Cancel = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel_label = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(viewBundle.getString("strInsertTitle_WindowTitle"));
        setName("jFrame_InsertNode");
        setResizable(false);
        jLabel_Msg.setText(viewBundle.getString("strInsertTitle_Msg"));

        jTextField_nodeURL.setEditable(false);
        jScrollPane_nodeURL.setViewportView(jTextField_nodeURL);

        jButton_OK.setText(viewBundle.getString("strInsertTitle_OkButton"));

        jButton_Cancel.setText(viewBundle.getString("strInsertTitle_CancelButton"));

        jLabel_label.setText(viewBundle.getString("strInsertTitle_Label"));

        org.jdesktop.layout.GroupLayout jPanel_titleLayout = new org.jdesktop.layout.GroupLayout(jPanel_title);
        jPanel_title.setLayout(jPanel_titleLayout);
        jPanel_titleLayout.setHorizontalGroup(
            jPanel_titleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .add(jPanel_titleLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel_titleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel_titleLayout.createSequentialGroup()
                        .add(jScrollPane_nodeURL, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                        .addContainerGap())
                    .add(jPanel_titleLayout.createSequentialGroup()
                        .add(jLabel_Msg)
                        .addContainerGap(187, Short.MAX_VALUE))
                    .add(jPanel_titleLayout.createSequentialGroup()
                        .add(jButton_OK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(15, 15, 15)
                        .add(jButton_Cancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(215, Short.MAX_VALUE))
                    .add(jPanel_titleLayout.createSequentialGroup()
                        .add(jTextField_title, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                        .addContainerGap())
                    .add(jPanel_titleLayout.createSequentialGroup()
                        .add(jLabel_label)
                        .addContainerGap(203, Short.MAX_VALUE))))
        );
        jPanel_titleLayout.setVerticalGroup(
            jPanel_titleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel_titleLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel_Msg)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane_nodeURL, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel_label)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTextField_title, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 25, Short.MAX_VALUE)
                .add(jPanel_titleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton_OK)
                    .add(jButton_Cancel)))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel_title, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel_title, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_OK;
    private javax.swing.JLabel jLabel_Msg;
    private javax.swing.JLabel jLabel_label;
    private javax.swing.JPanel jPanel_title;
    private javax.swing.JScrollPane jScrollPane_nodeURL;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField_nodeURL;
    private javax.swing.JTextField jTextField_title;
    // End of variables declaration//GEN-END:variables
}
