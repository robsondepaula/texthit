package view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultFormatterFactory;
import resources.ViewInternationalization;

/**
 * This class plays the View role in the Map Publisher secondary application. It
 * is driven by a Controller ({@link controller.PublisherWindowController}) and
 * displays the current status of a Model ({@link model.HypertextMap}). <p>It
 * uses the ({@link ViewInternationalization}) class to implement a multi-
 * language user interface.
 */
public class PublisherWindow extends JFrame {

    private ViewInternationalization viewBundle = null;

    /**
     * Constructs the Map Publisher secondary application View class.
     */
    public PublisherWindow() {
        viewBundle = new ViewInternationalization("resources.MessagesBundle");

        initComponents();

        //Attempts to centralize the main frame window
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);

        //display the editor window
        setVisible(true);
    }

    public void enableServerControls() {
        jLabel_ServerAddress.setEnabled(true);
        jButton_OK.setEnabled(true);
        jFormattedTextField_ServerAddress.setEnabled(true);
        jLabel_ServerLogin.setEnabled(true);
        jLabel_ServerPass.setEnabled(true);
        jPasswordField_ServerPass.setEnabled(true);
        jTextField_ServerLogin.setEnabled(true);
        jLabel_ServerAccessString.setEnabled(true);
        jFormattedTextField_AccessString.setEnabled(true);
    }

    /**
     * Allow the Controller ({@link controller.PublisherWindowController}) to
     * install a Regex Formatter to filter the user inputs for the server
     * address field in the user interface.
     */
    public void setServerAddressRegexFormatter(
            DefaultFormatterFactory formatter) {
        jFormattedTextField_ServerAddress.setFormatterFactory(formatter);
    }

    /**
     * Retrieves the server address from the user interface.
     *
     * @return a String containing the server IP address text displayed and
     * probably modified in the user interface
     */
    public String getServerAddress() {
        try {
            return (String) jFormattedTextField_ServerAddress.getValue();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Sets the server address in the user interface.
     *
     * @param serverIPAddress a String containing the server IP address
     */
    public void setServerAddress(String serverIPAddress) {
        jFormattedTextField_ServerAddress.setValue(serverIPAddress);
    }

    /**
     * Allow the Controller ({@link controller.PublisherWindowController}) to
     * install a Regex Formatter to filter the user inputs for the remote path
     * field in the user interface.
     */
    public void setAccessStringRegexFormatter(
            DefaultFormatterFactory formatter) {
        jFormattedTextField_AccessString.setFormatterFactory(formatter);
    }

    /**
     * Retrieves the server access string from the user interface. The access
     * string is what the user will input in the browser address to access the
     * hypertexts in the remote server.
     *
     * @return a String containing the server access string text displayed and
     * probably modified in the user interface
     */
    public String getAccessString() {
        try {
            return (String) jFormattedTextField_AccessString.getValue();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Sets the server access string in the user interface.
     *
     * @param accessString a String containing the server remote folder
     */
    public void setAccessString(String accessString) {
        jFormattedTextField_AccessString.setValue(accessString);
    }

    /**
     * Retrieves the server login from the user interface.
     *
     * @return a String containing the server login text displayed and probably
     * modified in the user interface
     */
    public String getServerLogin() {
        try {
            return jTextField_ServerLogin.getText();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Sets the server login in the user interface.
     *
     * @param serverLogin a String containing the server login
     */
    public void setServerLogin(String serverLogin) {
        jTextField_ServerLogin.setText(serverLogin);
    }

    /**
     * Retrieves the server password from the user interface.
     *
     * @return a String containing the server password probably modified in the
     * user interface
     */
    public String getServerPass() {
        try {
            return new String(jPasswordField_ServerPass.getPassword());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Sets the server password in the user interface.
     *
     * @param serverPass a String containing the server password
     */
    public void setServerPass(String serverPass) {
        jPasswordField_ServerPass.setText(serverPass);
    }

    /**
     * Allow the Controller ({@link controller.PublisherWindowController}) to
     * install an action listener to receive the cancel operation request from
     * the user interface.
     */
    public void setCancelButtonListener(ActionListener a1) {
        jButton_Cancel.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.PublisherWindowController}) to
     * install an action listener to receive the Ok Button pressed event from
     * the user interface.
     */
    public void setOkButtonListener(ActionListener a1) {
        jButton_OK.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.PublisherWindowController}) to
     * install an action listener to receive the publish operation request from
     * the user interface.
     */
    public void setPublishButtonListener(ActionListener a1) {
        jButton_Publish.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.PublisherWindowController}) to
     * install an action listener to receive the window closing request from the
     * user interface.
     */
    public void setFormWindowListener(WindowListener w1) {
        addWindowListener(w1);
    }

    /**
     * Allow the Controller ({@link controller.PublisherWindowController}) to
     * enable the OK Button in the user interface.
     */
    public void enableButtonOK() {
        jButton_OK.setEnabled(true);
    }

    /**
     * Allow the Controller ({@link controller.PublisherWindowController}) to
     * disable the OK Button in the user interface.
     */
    public void disableButtonOK() {
        jButton_OK.setEnabled(false);
    }

    /**
     * Sets a status information text in the user interface.
     *
     * @param strStatus a String containing the desired status message to be
     * displayed in the user interface
     */
    public void setStatusText(String strStatus) {
        jLabel_Status.setText(strStatus);
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
     * Update progress bar condition in the user interface.
     *
     * @param currentStep the actual progress between the minimum and maximum
     * values of the progress bar
     */
    public void updateProgressBar(int currentStep) {
        jProgressBar_Publishing.setValue(currentStep);
    }

    /**
     * Setup the progress bar in the user interface.
     *
     * @param minimum the minimum value of the displayed progress bar
     * @param maximum the maximum value of the displayed progress bar
     */
    public void setupProgressBar(int minimum, int maximum) {
        jProgressBar_Publishing.setMinimum(minimum);
        jProgressBar_Publishing.setMaximum(maximum);
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
     * Gracefully disposes the Map Publisher secondary application View
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

        jPanel1 = new javax.swing.JPanel();
        jLabel_ServerAddress = new javax.swing.JLabel();
        jProgressBar_Publishing = new javax.swing.JProgressBar();
        jButton_Publish = new javax.swing.JButton();
        jButton_Cancel = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButton_OK = new javax.swing.JButton();
        jFormattedTextField_ServerAddress = new javax.swing.JFormattedTextField();
        jLabel_Status = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel_ServerLogin = new javax.swing.JLabel();
        jLabel_ServerPass = new javax.swing.JLabel();
        jPasswordField_ServerPass = new javax.swing.JPasswordField();
        jTextField_ServerLogin = new javax.swing.JTextField();
        jLabel_ServerAccessString = new javax.swing.JLabel();
        jFormattedTextField_AccessString = new javax.swing.JFormattedTextField();

        setTitle(viewBundle.getString("str_PublisherWindow_Title")); // NOI18N
        setResizable(false);

        jLabel_ServerAddress.setText(viewBundle.getString("str_PublisherWindow_ServerAddress")); // NOI18N
        jLabel_ServerAddress.setEnabled(false);

        jProgressBar_Publishing.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jProgressBar_Publishing.setEnabled(false);
        jProgressBar_Publishing.setStringPainted(true);

        jButton_Publish.setText(viewBundle.getString("str_PublisherWindow_ButtonPublish")); // NOI18N

        jButton_Cancel.setText(viewBundle.getString("str_PublisherWindow_ButtonCancel")); // NOI18N

        jButton_OK.setText(viewBundle.getString("str_PublisherWindow_ButtonOk")); // NOI18N
        jButton_OK.setEnabled(false);

        jFormattedTextField_ServerAddress.setEnabled(false);

        jLabel_Status.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jLabel_Status.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        jLabel_ServerLogin.setText(viewBundle.getString("str_PublisherWindow_ServerLogin")); // NOI18N
        jLabel_ServerLogin.setEnabled(false);

        jLabel_ServerPass.setText(viewBundle.getString("str_PublisherWindow_ServerPass")); // NOI18N
        jLabel_ServerPass.setEnabled(false);

        jPasswordField_ServerPass.setEnabled(false);
        jPasswordField_ServerPass.setMinimumSize(new java.awt.Dimension(11, 21));
        jPasswordField_ServerPass.setPreferredSize(new java.awt.Dimension(11, 21));

        jTextField_ServerLogin.setEnabled(false);
        jTextField_ServerLogin.setMinimumSize(new java.awt.Dimension(11, 21));
        jTextField_ServerLogin.setPreferredSize(new java.awt.Dimension(11, 21));

        jLabel_ServerAccessString.setText(viewBundle.getString("str_PublisherWindow_AccessString")); // NOI18N
        jLabel_ServerAccessString.setEnabled(false);

        jFormattedTextField_AccessString.setEnabled(false);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jButton_Publish)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 200, Short.MAX_VALUE)
                        .add(jButton_Cancel))
                    .add(jProgressBar_Publishing, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel_ServerAccessString)
                            .add(jLabel_ServerPass)
                            .add(jLabel_ServerAddress)
                            .add(jLabel_ServerLogin))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jFormattedTextField_ServerAddress, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jTextField_ServerLogin, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPasswordField_ServerPass, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jFormattedTextField_AccessString, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jLabel_Status, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(159, 159, 159)
                        .add(jButton_OK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 66, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 135, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel_ServerAddress)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jFormattedTextField_ServerAddress, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel_ServerLogin)
                    .add(jTextField_ServerLogin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel_ServerPass)
                    .add(jPasswordField_ServerPass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(17, 17, 17)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel_ServerAccessString)
                    .add(jFormattedTextField_AccessString, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(19, 19, 19)
                .add(jProgressBar_Publishing, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 10, Short.MAX_VALUE)
                .add(jLabel_Status, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton_Publish)
                    .add(jButton_Cancel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton_OK)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_OK;
    private javax.swing.JButton jButton_Publish;
    private javax.swing.JFormattedTextField jFormattedTextField_AccessString;
    private javax.swing.JFormattedTextField jFormattedTextField_ServerAddress;
    private javax.swing.JLabel jLabel_ServerAccessString;
    private javax.swing.JLabel jLabel_ServerAddress;
    private javax.swing.JLabel jLabel_ServerLogin;
    private javax.swing.JLabel jLabel_ServerPass;
    private javax.swing.JLabel jLabel_Status;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField_ServerPass;
    private javax.swing.JProgressBar jProgressBar_Publishing;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField_ServerLogin;
    // End of variables declaration//GEN-END:variables
}
