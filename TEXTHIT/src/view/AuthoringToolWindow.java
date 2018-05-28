package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.JOptionPane;
import resources.ViewInternationalization;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import view.graph.GraphView;

/**
 * This class plays the View role in the main application. It is driven by a
 * Controller ({@link controller.AuthoringToolWindowController}) and displays
 * the current status of a Model ({@link model.HypertextMap}) through an
 * attached secondary View ({@link GraphView}). <p>It uses the
 * ({@link ViewInternationalization}) class to implement a multi- language user
 * interface.
 */
public class AuthoringToolWindow extends JFrame {

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel_MainPanel = new javax.swing.JPanel();
        jPanel_MapArea = new javax.swing.JPanel();
        jSeparatorMapArea = new javax.swing.JSeparator();
        jLabel_StatusLabel = new javax.swing.JLabel();
        jPanel_MapDesc = new javax.swing.JPanel();
        jLabel_MapDesc = new javax.swing.JLabel();
        jMenuBar_MainMenu = new javax.swing.JMenuBar();
        jMenu_Maps = new javax.swing.JMenu();
        jMenuItem_MapCreate = new javax.swing.JMenuItem();
        jMenuItem_MapOpen = new javax.swing.JMenuItem();
        jMenuItem_AutoFrag = new javax.swing.JMenuItem();
        jMenuItem_MapSave = new javax.swing.JMenuItem();
        jMenuItem_MapSaveAs = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItem_PublishMap = new javax.swing.JMenuItem();
        jMenuItem_PubliLocal = new javax.swing.JMenuItem();
        jSeparator_Maps = new javax.swing.JSeparator();
        jMenuItem_AppExit = new javax.swing.JMenuItem();
        jMenu_EditMap = new javax.swing.JMenu();
        jMenuItem_CreateNode = new javax.swing.JMenuItem();
        jMenuItem_AddNode = new javax.swing.JMenuItem();
        jMenuItem_RemoveNode = new javax.swing.JMenuItem();
        jMenuItem_Perspectives = new javax.swing.JMenuItem();
        jMenu_ReadPath = new javax.swing.JMenu();
        jMenuItem_CreatePath = new javax.swing.JMenuItem();
        jMenu_Help = new javax.swing.JMenu();
        jSeparatorHelp = new javax.swing.JSeparator();
        jMenuItem_About = new javax.swing.JMenuItem();
        jMenuItem_License = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(viewBundle.getString("strApp_Title")); // NOI18N
        setName("JFrame_ATW"); // NOI18N

        jPanel_MapArea.setBackground(new java.awt.Color(204, 204, 204));
        jPanel_MapArea.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel_MapArea.setEnabled(false);
        jPanel_MapArea.setLayout(new java.awt.BorderLayout());

        jLabel_StatusLabel.setBackground(new java.awt.Color(204, 204, 204));
        jLabel_StatusLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel_StatusLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jLabel_StatusLabel.setOpaque(true);

        jPanel_MapDesc.setBackground(new java.awt.Color(204, 204, 204));
        jPanel_MapDesc.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel_MapDesc.setEnabled(false);
        jPanel_MapDesc.setLayout(new java.awt.BorderLayout());

        jLabel_MapDesc.setText(viewBundle.getString("str_AuthorToolWindow_MapDesc")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel_MainPanelLayout = new org.jdesktop.layout.GroupLayout(jPanel_MainPanel);
        jPanel_MainPanel.setLayout(jPanel_MainPanelLayout);
        jPanel_MainPanelLayout.setHorizontalGroup(
            jPanel_MainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel_MainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel_MainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel_MapDesc, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)
                    .add(jPanel_MapArea, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparatorMapArea, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)
                    .add(jLabel_StatusLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)
                    .add(jLabel_MapDesc))
                .addContainerGap())
        );
        jPanel_MainPanelLayout.setVerticalGroup(
            jPanel_MainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel_MainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel_MapArea, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 351, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel_MapDesc)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel_MapDesc, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparatorMapArea, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(9, 9, 9)
                .add(jLabel_StatusLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jMenu_Maps.setText(viewBundle.getString("strJMenu_Maps")); // NOI18N
        jMenu_Maps.setToolTipText("");

        jMenuItem_MapCreate.setText(viewBundle.getString("strJMenuItem_MapCreate")); // NOI18N
        jMenu_Maps.add(jMenuItem_MapCreate);

        jMenuItem_MapOpen.setText(viewBundle.getString("strJMenuItem_MapOpen")); // NOI18N
        jMenu_Maps.add(jMenuItem_MapOpen);

        jMenuItem_AutoFrag.setText(viewBundle.getString("strJMenuItem_AutoFrag"));
        jMenu_Maps.add(jMenuItem_AutoFrag);

        jMenuItem_MapSave.setText(viewBundle.getString("strJMenuItem_MapSave")); // NOI18N
        jMenuItem_MapSave.setEnabled(false);
        jMenu_Maps.add(jMenuItem_MapSave);

        jMenuItem_MapSaveAs.setText(viewBundle.getString("strJMenuItem_MapSaveAs")); // NOI18N
        jMenuItem_MapSaveAs.setEnabled(false);
        jMenu_Maps.add(jMenuItem_MapSaveAs);
        jMenu_Maps.add(jSeparator1);

        jMenuItem_PublishMap.setText(viewBundle.getString("strJMenuItem_PublishMap")); // NOI18N
        jMenuItem_PublishMap.setEnabled(false);
        jMenu_Maps.add(jMenuItem_PublishMap);

        jMenuItem_PubliLocal.setText(viewBundle.getString("strJMenuItem_PublishMapLocal"));
        jMenuItem_PubliLocal.setEnabled(false);
        jMenu_Maps.add(jMenuItem_PubliLocal);
        jMenu_Maps.add(jSeparator_Maps);

        jMenuItem_AppExit.setText(viewBundle.getString("strJMenuItem_AppExit")); // NOI18N
        jMenu_Maps.add(jMenuItem_AppExit);

        jMenuBar_MainMenu.add(jMenu_Maps);

        jMenu_EditMap.setText(viewBundle.getString("strJMenu_EditMap")); // NOI18N
        jMenu_EditMap.setEnabled(false);

        jMenuItem_CreateNode.setText(viewBundle.getString("strJMenuItem_CreatedNode")); // NOI18N
        jMenu_EditMap.add(jMenuItem_CreateNode);

        jMenuItem_AddNode.setText(viewBundle.getString("strJMenuItem_AddNode")); // NOI18N
        jMenu_EditMap.add(jMenuItem_AddNode);

        jMenuItem_RemoveNode.setText(viewBundle.getString("strJMenuItem_RemoveNode")); // NOI18N
        jMenuItem_RemoveNode.setEnabled(false);
        jMenu_EditMap.add(jMenuItem_RemoveNode);

        jMenuItem_Perspectives.setText(viewBundle.getString("strJMenuItem_Perspectives"));
        jMenu_EditMap.add(jMenuItem_Perspectives);

        jMenuBar_MainMenu.add(jMenu_EditMap);

        jMenu_ReadPath.setText(viewBundle.getString("strJMenu_ReadPath")); // NOI18N
        jMenu_ReadPath.setEnabled(false);

        jMenuItem_CreatePath.setText(viewBundle.getString("strJMenuItem_CreatePath")); // NOI18N
        jMenuItem_CreatePath.setEnabled(false);
        jMenu_ReadPath.add(jMenuItem_CreatePath);

        jMenuBar_MainMenu.add(jMenu_ReadPath);

        jMenu_Help.setText(viewBundle.getString("strJMenu_Help")); // NOI18N
        jMenu_Help.add(jSeparatorHelp);

        jMenuItem_About.setText(viewBundle.getString("strJMenuItem_About")); // NOI18N
        jMenu_Help.add(jMenuItem_About);

        jMenuItem_License.setText(viewBundle.getString("license_menu_item"));
        jMenu_Help.add(jMenuItem_License);

        jMenuBar_MainMenu.add(jMenu_Help);

        setJMenuBar(jMenuBar_MainMenu);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel_MainPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel_MainPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel_MapDesc;
    private javax.swing.JLabel jLabel_StatusLabel;
    private javax.swing.JMenuBar jMenuBar_MainMenu;
    private javax.swing.JMenuItem jMenuItem_About;
    private javax.swing.JMenuItem jMenuItem_AddNode;
    private javax.swing.JMenuItem jMenuItem_AppExit;
    private javax.swing.JMenuItem jMenuItem_AutoFrag;
    private javax.swing.JMenuItem jMenuItem_CreateNode;
    private javax.swing.JMenuItem jMenuItem_CreatePath;
    private javax.swing.JMenuItem jMenuItem_License;
    private javax.swing.JMenuItem jMenuItem_MapCreate;
    private javax.swing.JMenuItem jMenuItem_MapOpen;
    private javax.swing.JMenuItem jMenuItem_MapSave;
    private javax.swing.JMenuItem jMenuItem_MapSaveAs;
    private javax.swing.JMenuItem jMenuItem_Perspectives;
    private javax.swing.JMenuItem jMenuItem_PubliLocal;
    private javax.swing.JMenuItem jMenuItem_PublishMap;
    private javax.swing.JMenuItem jMenuItem_RemoveNode;
    private javax.swing.JMenu jMenu_EditMap;
    private javax.swing.JMenu jMenu_Help;
    private javax.swing.JMenu jMenu_Maps;
    private javax.swing.JMenu jMenu_ReadPath;
    private javax.swing.JPanel jPanel_MainPanel;
    private javax.swing.JPanel jPanel_MapArea;
    private javax.swing.JPanel jPanel_MapDesc;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparatorHelp;
    private javax.swing.JSeparator jSeparatorMapArea;
    private javax.swing.JSeparator jSeparator_Maps;
    // End of variables declaration//GEN-END:variables
    //Declaration
    private ViewInternationalization viewBundle = null;
    private JPopupMenu nodeMenu = null;
    private JMenuItem previewItem = null;
    private JMenuItem removeNodeItem = null;
    private JMenuItem editNodeItem = null;
    private JMenuItem choosePerspectiveItem = null;
    private JMenuItem mergeNodeItem = null;
    private int uiState = 0;
    /**
     * User chose "Yes" option.
     *
     * @see #showOptionBox(String strMsg, String strTitle, String strYes, String
     * strNo, String strCancel)
     */
    public final int OPTION_YES = 1;
    /**
     * User chose "No" option.
     *
     * @see #showOptionBox(String strMsg, String strTitle, String strYes, String
     * strNo, String strCancel)
     */
    public final int OPTION_NO = 2;
    /**
     * User chose "Cancel" option.
     *
     * @see #showOptionBox(String strMsg, String strTitle, String strYes, String
     * strNo, String strCancel)
     */
    public final int OPTION_CANCEL = 3;
    /**
     * The UI will display functionality to open or create a Map.
     */
    public final int UI_STATE_NO_MAP = 0;
    /**
     * The UI will display functionality for an empty Map.
     */
    public final int UI_STATE_MAP_CREATED = 1;
    /**
     * The UI will display functionality for a Map that has a node on it.
     */
    public final int UI_STATE_MAP_HAS_NODE = 2;
    /**
     * The UI will display functionality for a Map that has nodes on it.
     */
    public final int UI_STATE_MAP_HAS_NODES = 3;
    /**
     * The UI will display functionality for a Map that doesnt have node but may
     * insert new ones.
     */
    public final int UI_STATE_MAP_WITHOUT_NODES = 4;
    /**
     * The UI will display functionality to create/edit Read Paths.
     */
    public final int UI_STATE_READ_PATH_POSSIBLE = 5;
    /**
     * The UI will hide functionality to create/edit Read Paths.
     */
    public final int UI_STATE_READ_PATH_UNNAVAILABLE = 6;
    /**
     * The UI will hide functionality of removing Nodes.
     */
    public final int UI_STATE_MAP_CANT_REMOVE_NODES = 7;

    /**
     * Constructs the main application View class. <p>Query the native VM to
     * display the user interface in the O.S. look and feel.
     */
    public AuthoringToolWindow() {
        //Attempts to change the UI look and feel to a more eye-candy one
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            //can't change, runs with default look and feel
        }

        viewBundle = new ViewInternationalization("resources.MessagesBundle");

        initComponents();

        //pop-up menu items
        previewItem = new JMenuItem(viewBundle.getString("strMap_PreviewString"));
        removeNodeItem = new JMenuItem(viewBundle.getString("strJMenuItem_RemoveNode"));
        editNodeItem = new JMenuItem(viewBundle.getString("strJMenuItem_EditNode"));
        choosePerspectiveItem = new JMenuItem(viewBundle.getString("strPerspectiveChooser_MenuItem"));
        mergeNodeItem = new JMenuItem(viewBundle.getString("strMergeNode_MenuItem"));

        //instantiate the pop-up menus
        nodeMenu = new JPopupMenu();

        //populate the Node pop-up menu
        nodeMenu.add(editNodeItem);
        nodeMenu.add(previewItem); //will launch preview
        nodeMenu.add(choosePerspectiveItem);
        nodeMenu.addSeparator();
        nodeMenu.add(removeNodeItem);//will launch the node removal process
        nodeMenu.add(mergeNodeItem);//launch merge node process

        //Attempts to centralize the main frame window
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);

        //display the main window
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
     * to install an action listener to receive the save map
     * ({@link model.HypertextMap}) request from the user interface.
     */
    public void setSaveMapListener(ActionListener a1) {
        jMenuItem_MapSave.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the event to save the map into a
     * different location from the user interface.
     */
    public void setSaveMapAsListener(ActionListener a1) {
        jMenuItem_MapSaveAs.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the event to publish the map.
     */
    public void setMapPublishListener(ActionListener a1) {
        jMenuItem_PublishMap.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the event to publish the map locally.
     */
    public void setMapPublishLocalListener(ActionListener a1) {
        jMenuItem_PubliLocal.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the display about box
     * ({@link XHTMLViewerDialog}) request from the user interface.
     */
    public void setAboutListener(ActionListener a1) {
        jMenuItem_About.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the display license box
     * ({@link XHTMLViewerDialog}) request from the user interface.
     */
    public void setLicenseListener(ActionListener a1) {
        jMenuItem_License.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the remove node
     * ({@link model.HypertextNode}) from the map ({@link model.HypertextMap})
     * request from the user interface.
     */
    public void setRemoveNodeListener(ActionListener a1) {
        jMenuItem_RemoveNode.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the add node
     * ({@link model.HypertextNode}) to the map ({@link model.HypertextMap})
     * request from the user interface.
     */
    public void setAddNodeListener(ActionListener a1) {
        jMenuItem_AddNode.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the Perspectives menu click from
     * the user interface.
     */
    public void setPerspectivesListener(ActionListener a1) {
        jMenuItem_Perspectives.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the open map
     * ({@link model.HypertextMap}) request from the user interface.
     */
    public void setOpenMapListener(ActionListener a1) {
        jMenuItem_MapOpen.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the create map
     * ({@link model.HypertextMap}) request from the user interface.
     */
    public void setCreateMapListener(ActionListener a1) {
        jMenuItem_MapCreate.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the begin rewrite processing
     * request from the user interface.
     */
    public void setRewriteListener(ActionListener a1) {
        jMenuItem_AutoFrag.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the exit application request
     * from the user interface.
     */
    public void setExitListener(ActionListener a1) {
        jMenuItem_AppExit.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the create reading path request
     * from the user interface.
     */
    public void setCreatePathListener(ActionListener a1) {
        jMenuItem_CreatePath.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the preview node
     * ({@link model.HypertextNode}) contents request from the user interface.
     */
    public void setPopMenuPreviewListener(ActionListener a1) {
        previewItem.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the edit node
     * ({@link model.HypertextNode}) contents request from the user interface.
     */
    public void setPopMenuEditNodeListener(ActionListener a1) {
        editNodeItem.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the choose node's color request
     * from the user interface.
     */
    public void setPopMenuPerspectiveChooserListener(ActionListener a1) {
        choosePerspectiveItem.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the remove node
     * ({@link model.HypertextNode}) from the map ({@link model.HypertextMap})
     * request from the user interface.
     */
    public void setPopMenuRemoveNodeListener(ActionListener a1) {
        removeNodeItem.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the merge node
     * ({@link model.HypertextNode}) from the map ({@link model.HypertextMap})
     * request from the user interface.
     */
    public void setPopMenuMergeNodeListener(ActionListener a1) {
        mergeNodeItem.addActionListener(a1);
    }

    /**
     * Allow the Controller ({@link controller.AuthoringToolWindowController})
     * to install an action listener to receive the create node
     * ({@link model.HypertextNode}) in the map ({@link model.HypertextMap})
     * request from the user interface.
     */
    public void setCreateNodeListener(ActionListener a1) {
        jMenuItem_CreateNode.addActionListener(a1);
    }

    /**
     * Retrieves a value for the given key. Using the
     * {@link ViewInternationalization} class.
     *
     * @param strKey the key for the desired value
     * @return the desired value stored in the given key <p> an error String if
     * the key is invalid or has no value stored in it
     */
    public String getViewBundleString(String strKey) {
        return viewBundle.getString(strKey);
    }

    /**
     * Display pop-up menu with node options to the user
     *
     * @param displayable displayable component that will have the popup menu
     * shown
     * @param x horizontal axis position over the displayable component
     * @param y vertical axis position over the displayable component
     */
    public void showNodePopMenu(Component displayable, int x, int y) {
        nodeMenu.show(displayable,
                x,
                y);
    }

    /**
     * Write a status String in the user interface.
     *
     * @param msg the status String to be displayed to the user
     */
    public void showStatus(String msg) {
        jLabel_StatusLabel.setText(msg);
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
     * Display an option box that will allow the user to choose the action to be
     * taken.
     *
     * @param strMsg the message containing a question to the user
     * @param strTitle the title for the option box
     * @param strYes label for the "yes" option
     * @param strNo label for the "no" option
     * @param strCancel label for the cancel option (no action to be taken)
     * @return <code>OPTION_YES</code> if the "yes" option was selected by the
     * user <p><code>OPTION_NO</code> if the "no" option was selected by the
     * user <p><code>OPTION_CANCEL</code> if the user choosed to cancel the
     * operation
     * @see #OPTION_YES
     * @see #OPTION_NO
     * @see #OPTION_CANCEL
     */
    public int showOptionBox(String strMsg, String strTitle, String strYes, String strNo, String strCancel) {
        Object[] buttonsLabel = {strYes, strNo, strCancel};

        int result = JOptionPane.showOptionDialog(null,
                strMsg,
                strTitle,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
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
                return OPTION_CANCEL;
        }
    }

    /**
     * Shows the open file dialog {@link FilesManipulationDialog} and allows the
     * user to choose the source file for the {@link model.HypertextNode} being
     * added in the {@link model.HypertextMap}.
     *
     * @param strPath the initial path shown by the open file dialog
     * @return a String containing the absolute path for the selected
     * HypertextNode source file
     */
    public String openNodeFile(String strPath) {
        File f = null;
        String[] strExtension = {viewBundle.getString("strNodeExtension_0"),
            viewBundle.getString("strNodeExtension_1"),
            viewBundle.getString("strNodeExtension_2")
        };

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

        //opens a filechooser and allow the user to select a file to be inserted
        f = view.FilesManipulationDialog.showFileChooser(this,
                viewBundle.getString("strHypertextFileChooser_Title"),
                viewBundle.getString("strHypertextFileChooser_OkButton"),
                viewBundle.getString("strHypertextFileChooser_FilterText") + strExtensionLabel,
                strExtension,
                strPath,
                false);

        try {
            return f.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Shows the open file dialog {@link FilesManipulationDialog} and allows the
     * user to choose the source file for the {@link model.HypertextMap} being
     * opened.
     *
     * @param strPath the initial path shown by the open file dialog
     * @return a String containing the absolute path for the selected
     * HypertextMap source file
     */
    public String openMapFile(String strPath) {
        File f = null;
        String[] strExtension = {viewBundle.getString("strMapExtension_0")};

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

        //opens a filechooser and allow the user to select a file to be inserted
        f = view.FilesManipulationDialog.showFileChooser(this,
                viewBundle.getString("strMapFileChooser_Title"),
                viewBundle.getString("strMapFileChooser_OkButton"),
                viewBundle.getString("strMapFileChooser_FilterText") + strExtensionLabel,
                strExtension,
                strPath,
                false);

        //opens an existing map
        try {
            return f.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Write a succes status String in the user interface. <p>Update the user
     * interface elements exposing new functionality.
     *
     * @see #uiModifyState(int state)
     */
    public void mapSuccessfullyOpened() {
        //updates the MMI
        uiModifyState(1);
        showStatus(viewBundle.getString("strStatus_MapOpened"));
    }

    /**
     * Write a succes status String in the user interface. <p>Update the user
     * interface elements exposing new functionality.
     *
     * @see #uiModifyState(int state)
     */
    public void mapSuccessfullyCreated() {
        //updates the MMI
        uiModifyState(1);
        showStatus(viewBundle.getString("strStatus_MapCreated"));
    }

    /**
     * Detaches the GraphView displayable component and removes it from the main
     * window.
     */
    public void detachGraphView() {
        //removes the displayable component
        jPanel_MapArea.removeAll();

        //clean reading path JMenu Items
        jMenu_ReadPath.removeAll();
        //add default item
        jMenu_ReadPath.add(jMenuItem_CreatePath);
    }

    /**
     * Attaches the given displayable component and shows it in the main window.
     *
     * @param displayable component to be attached in the GraphView area of the
     * main window
     */
    public void attachGraphView(Component displayable) {
        //attaches the given displayable component and shows it in the main window
        jPanel_MapArea.add(new JScrollPane((Component) displayable));
        //updates parent JFrame
        pack();
    }

    /**
     * Display an {@link XHTMLViewerDialog} that will render the give XHTML file
     * and show its contents to the user.
     *
     * @param previewTitle a String for the preview window title
     * @param dismissPreview a String for the dismiss window button
     * @param previewURL a String containing the URL to the XHTML
     * @return <code>true</code> if the XHTML was correctly rendered and
     * displayed <p><code>false</code> if the XHTML couldn't be displayed
     */
    public boolean showPreviewFromFile(String previewTitle,
            String dismissPreview,
            String previewURL) {
        XHTMLViewerDialog previewBox = null;
        try {
            //shows the Preview Box
            previewBox = new XHTMLViewerDialog(previewTitle,
                    dismissPreview,
                    previewURL,
                    800,
                    600);

            if (previewBox.getVisibility() == false) {
                previewBox.setResizable(false);
                previewBox.setVisible(true);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Display an {@link XHTMLViewerDialog} that will render the given XHTML
     * source code and show its contents to the user.
     *
     * @param previewTitle a String for the preview window title
     * @param dismissPreview a String for the dismiss window button
     * @param xhtmlContents a String containing the XHTML source code
     * @return <code>true</code> if the XHTML was correctly rendered and
     * displayed <p><code>false</code> if the XHTML couldn't be displayed
     */
    public boolean showPreview(String previewTitle,
            String dismissPreview,
            String xhtmlContents) {
        XHTMLViewerDialog previewBox = null;
        try {
            //shows the Preview Box
            previewBox = new XHTMLViewerDialog(800,
                    600,
                    previewTitle,
                    dismissPreview,
                    xhtmlContents);
            if (previewBox.getVisibility() == false) {
                previewBox.setResizable(false);
                previewBox.setVisible(true);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Shows the save file dialog {@link FilesManipulationDialog} and allows the
     * user to choose the destination file for the {@link model.HypertextMap}
     * being persisted.
     *
     * @param strPath the initial path shown by the save file dialog
     * @return a String containing the absolute path of the chosen destination
     */
    public String showSaveFileDialog(String strPath) {
        String[] strExtension = {viewBundle.getString("strMapExtension_0")};

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
                viewBundle.getString("strHypertextMapSaveFileChooser_Title"),
                viewBundle.getString("strHypertextMapSaveFileChooser_OkButton"),
                viewBundle.getString("strHypertextMapSaveFileChooser_FilterText") + strExtensionLabel,
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
     * Insert a new entry in the user interface allowing the user to view the
     * {@link model.HypertextReadingPath} available in the Model.
     *
     * @param strNewItem the name for the new entry
     * @param newListener a new listener to notify the
     * {@link controller.AuthoringToolWindowController} when the user selects
     * this newly created entry
     */
    public void addReadingPathMenuItem(String strNewItem, ActionListener newListener) {
        if (jMenu_ReadPath.getItemCount() == 1) {
            jMenu_ReadPath.addSeparator();
        }
        JMenuItem newItem = new JMenuItem(strNewItem);
        newItem.addActionListener(newListener);
        jMenu_ReadPath.add(newItem);
    }

    /**
     * Remove an {@link model.HypertextReadingPath} entry from the user
     * interface.
     *
     * @param strItemName the name for the entry been removed
     */
    public void removeReadingPathMenuItem(String strItemName) {
        int count = jMenu_ReadPath.getItemCount();

        for (int i = 0; i < count; i++) {
            JMenuItem tmp = jMenu_ReadPath.getItem(i);

            if (tmp.getText().equals(strItemName)) {
                jMenu_ReadPath.remove(tmp);
                break;
            }
        }
    }

    /**
     * Remove all {@link model.HypertextReadingPath} entries from the user
     * interface.
     */
    public void clearReadingPathMenuItems() {
        jMenu_ReadPath.removeAll();
        jMenu_ReadPath.add(jMenuItem_CreatePath);
    }

    /**
     * Updates the user interface hiding or exposing functionality.
     *
     * @param state internal state representing the number of functionality
     * available to the user
     */
    public void uiModifyState(int state) {
        //Reflect state in the UI capabilities
        switch (state) {
            case UI_STATE_NO_MAP:
                //there is no map
                jPanel_MapArea.setEnabled(false);
                jPanel_MapDesc.setEnabled(false);
                jMenu_EditMap.setEnabled(false);
                jMenu_ReadPath.setEnabled(false);
                jMenuItem_MapSave.setEnabled(false);
                jMenuItem_MapSaveAs.setEnabled(false);
                jMenuItem_PublishMap.setEnabled(false);
                jMenuItem_PubliLocal.setEnabled(false);
                //Updates the known UI state
                uiState = state;
                break;

            case UI_STATE_MAP_CREATED:
                //Map created or opened
                jPanel_MapArea.setEnabled(true);
                jPanel_MapDesc.setEnabled(true);
                jMenu_EditMap.setEnabled(true);
                jMenuItem_MapSave.setEnabled(true);
                jMenuItem_MapSaveAs.setEnabled(true);
                //Updates the known UI state
                uiState = state;
                break;

            case UI_STATE_MAP_HAS_NODE:
                //At least one node inserted
                jMenuItem_RemoveNode.setEnabled(true);
                jMenuItem_PublishMap.setEnabled(true);
                jMenuItem_PubliLocal.setEnabled(true);
                jMenu_ReadPath.setEnabled(false);
                jMenuItem_CreatePath.setEnabled(false);
                //Updates the known UI state
                uiState = state;
                break;

            case UI_STATE_MAP_HAS_NODES:
                //More than one node inserted
                jMenuItem_RemoveNode.setEnabled(true);
                jMenu_ReadPath.setEnabled(true);
                jMenuItem_CreatePath.setEnabled(true);
                //Updates the known UI state
                uiState = state;
                break;

            case UI_STATE_MAP_WITHOUT_NODES:
                //No more nodes to be removed
                jMenuItem_RemoveNode.setEnabled(false);
                jMenuItem_PublishMap.setEnabled(false);
                jMenuItem_PubliLocal.setEnabled(false);
                //Updates the known UI state
                uiState = state;
                break;

            case UI_STATE_MAP_CANT_REMOVE_NODES:
                //No more nodes to be removed
                jMenuItem_RemoveNode.setEnabled(false);
                //Updates the known UI state
                uiState = state;
                break;

            default:
                jLabel_StatusLabel.setText(viewBundle.getString("templateFile_ErrorTitle"));
                break;
        }
    }

    /**
     * Retrieve current UI state.
     *
     * @return an int containing the UI state
     */
    public int getUIState() {
        return uiState;
    }

    /**
     * Updates the user interface exposing a displayable component containing
     * the map description.
     *
     * @param displayable a displayable component to graphically expose the Map
     * description.
     */
    public void attachMapDescription(Component displayable) {
        try {
            JScrollPane descScrollPane =
                    new JScrollPane(displayable);
            descScrollPane.setPreferredSize(new Dimension(displayable.getWidth(),
                    displayable.getHeight()));
            jPanel_MapDesc.add(descScrollPane);
        } catch (Exception e) {
            jPanel_MapDesc.removeAll();
        }
    }

    /**
     * Updates the user interface hiding a displayable component that contained
     * the map description.
     */
    public void dettachMapDescription() {
        jPanel_MapDesc.removeAll();
    }
}