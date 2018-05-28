package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.TableModelListener;
import model.PerspectiveTableModel;
import resources.ViewInternationalization;

/**
 * Displayable component that allow the user to edit the perspectives used for
 * the classification of nodes
 */
public class PerspectiveEditorWindow extends javax.swing.JFrame {

    private ViewInternationalization viewBundle = null;
    private PerspectiveTableModel tableModel = null;
    private JPopupMenu tableMenu = null;
    private JMenuItem removePerspectiveItem = null;

    /**
     * Privately constructs the perspective editor window.
     *
     * @param rowData a two dimensional matrix with perspective data
     * @param perspectiveSelectionMode flag to indicate if this window will be
     * used for perspective selection instead of perspective edition
     */
    private PerspectiveEditorWindow(Object[][] rowData, boolean perspectiveSelectionMode) {
        viewBundle = new ViewInternationalization("resources.MessagesBundle");

        initComponents();

        String[] columnNames = {
            viewBundle.getString("strPerspectiveTable_perspective"),
            viewBundle.getString("strPerspectiveTable_color")};

        if (perspectiveSelectionMode) {
            tableModel = new PerspectiveTableModel(columnNames, rowData, true);
        } else {
            tableModel = new PerspectiveTableModel(columnNames, rowData);
        }
        jTable_Perspective.setModel(tableModel);

        //Set up renderer and editor for the 'Color' related columns.
        jTable_Perspective.setDefaultRenderer(Color.class,
                new ColorRenderer(true));
        jTable_Perspective.setDefaultEditor(Color.class,
                new ColorEditor());

        //Attempts to centralize the main frame window
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);

        tableMenu = new JPopupMenu();
        removePerspectiveItem = new JMenuItem(
                viewBundle.getString("strPerspectiveTable_removePerspective"));
        tableMenu.add(removePerspectiveItem);
    }

    /**
     * Constructs the perspective editor window.
     *
     * @param rowData a two dimensional matrix with perspective data
     */
    public PerspectiveEditorWindow(Object[][] rowData) {
        //construct full editor
        this(rowData, false);
    }

    /**
     * Constructs the perspective editor window without editing capabilites.
     *
     * @param rowData a two dimensional matrix with perspective data
     * @param message a message to be displayed in the perspective editor window
     */
    public PerspectiveEditorWindow(Object[][] rowData, String message) {
        //construct editor without editing capabalities
        this(rowData, true);

        //trim down edition features
        jButton_AppendRow.setEnabled(false);
        tableMenu.setEnabled(false);

        //display a message
        jLabel_Message.setText(message);
    }

    /**
     * Display pop-up menu with table options to the user
     *
     * @param x horizontal axis position over the displayable component
     * @param y vertical axis position over the displayable component
     */
    public void showTablePopMenu(int x, int y) {
        tableMenu.show(jTable_Perspective,
                x,
                y);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the remove perspective request
     * from the user interface.
     */
    public void setPopMenuRemoveListener(ActionListener a1) {
        removePerspectiveItem.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install a table model listener to receive table edits from the user
     * interface.
     */
    public void setTableModelListener(TableModelListener t1) {
        jTable_Perspective.getModel().addTableModelListener(t1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install a table mouse listener to receive show popup requests from the
     * user interface.
     */
    public void setTableMouselListener(MouseAdapter m1) {
        jTable_Perspective.addMouseListener(m1);
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
     * to install an action listener to receive the confirm operation request
     * from the user interface.
     */
    public void setOkButtonListener(ActionListener a1) {
        jButton_Ok.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the append row request from the
     * user interface.
     */
    public void setAppendButtonListener(ActionListener a1) {
        jButton_AppendRow.addActionListener(a1);
    }

    /**
     * Gracefuly disposes this window
     */
    public void finish() {
        dispose();
    }

    /**
     * Append an empty row
     */
    public void appendNewRow() {
        // Append a row
        tableModel.appendRow(new Object[]{" ", new Color(0, 0, 0)});
    }

    /**
     * Remove the selected object from the perspective table.
     *
     * @return the index of the removed object, '-1' if an error occurred
     */
    public int removeSelectedObject() {
        try {
            int row = jTable_Perspective.getSelectedRow();

            tableModel.removeRow(row);

            return row;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Retrieve the selected object from the perspective table.
     *
     * @return an Object array containing the selected object data
     */
    public Object[] getSelectedObject() {
        try {
            int row = jTable_Perspective.getSelectedRow();
            Object[] rowData = new Object[2];

            rowData[0] = tableModel.getValueAt(row, 0);
            rowData[1] = tableModel.getValueAt(row, 1);

            return rowData;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton_Ok = new javax.swing.JButton();
        jScrollPane_PerspecTable = new javax.swing.JScrollPane();
        jTable_Perspective = new javax.swing.JTable();
        jButton_AppendRow = new javax.swing.JButton();
        jLabel_Message = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(viewBundle.getString("strPerspectiveEditor_Title"));
        setResizable(false);

        jButton_Ok.setText(viewBundle.getString("strInsertLink_OkButton")); // NOI18N

        jTable_Perspective.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane_PerspecTable.setViewportView(jTable_Perspective);

        jButton_AppendRow.setText(viewBundle.getString("strPerspectiveTable_addPerspective"));

        jLabel_Message.setText(" ");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jScrollPane_PerspecTable, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                            .add(jButton_AppendRow)))
                    .add(layout.createSequentialGroup()
                        .add(157, 157, 157)
                        .add(jButton_Ok, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel_Message, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel_Message)
                .add(13, 13, 13)
                .add(jButton_AppendRow)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane_PerspecTable, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 169, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 30, Short.MAX_VALUE)
                .add(jButton_Ok)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_AppendRow;
    private javax.swing.JButton jButton_Ok;
    private javax.swing.JLabel jLabel_Message;
    private javax.swing.JScrollPane jScrollPane_PerspecTable;
    private javax.swing.JTable jTable_Perspective;
    // End of variables declaration//GEN-END:variables
}
