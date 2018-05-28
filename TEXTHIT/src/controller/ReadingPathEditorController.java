package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import model.HypertextMap;
import model.HypertextNode;
import model.HypertextReadingPath;
import resources.ResourceManager;
import view.ReadingPathEditorWindow;

/**
 * This class plays the Controller role in the Reading Path Editor secondary
 * application. It drives a View ({@link ReadingPathEditorWindow}) and a Model
 * ({@link HypertextReadingPath}). <p>Interaction is made with the main
 * application Controller ({@link AuthoringToolWindowController}) so it can
 * update the main Model ({@link model.HypertextMap}).
 */
public class ReadingPathEditorController {

    private static HypertextMap model = null;
    private static ReadingPathEditorWindow view = null;
    private static boolean saveNeeded = false;
    private static HypertextReadingPath pathToBeEdited = null;
    private static String STR_EMPTY = "        ";
    private static String STR_SEPARATOR = " (";
    private static String STR_CLOSURE = ")";
    private static HypertextReadingPath readPath = null;

    /**
     * Constructs the secondary application Controller class. <p>Query the main
     * Model ({@link HypertextMap}) to build this Controller's Model
     * ({@link HypertextReadingPath}). <p>Creates an empty ReadingPath or edit
     * an existent one. <p>Uses a XHTML template stored inside the application
     * jar package ({@link ResourceManager}) to generate Reading Path XHTML
     * pages.
     *
     * @param model Main application Model
     * @param pathToBeEdited Existent ReadingPath to be edited
     */
    public ReadingPathEditorController(HypertextMap model,
            HypertextReadingPath pathToBeEdited) {
        //sets the current model
        ReadingPathEditorController.model = model;
        //usage mode
        ReadingPathEditorController.pathToBeEdited = pathToBeEdited;
    }

    /**
     * Runs the secondary application. <p>All interaction made by the user
     * through the View ({@link ReadingPathEditorWindow}) is captured by action
     * listeners. Only the Controller modifies the Model
     * ({@link HypertextReadingPath}), updates the View and the main Controller
     * ({@link AuthoringToolWindowController}).
     */
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //instantiate the model
                readPath = new HypertextReadingPath();

                //instantiate the view
                view = new ReadingPathEditorWindow();

                if (pathToBeEdited == null) {
                    //new path
                    view.listModelAdd(STR_EMPTY);

                    //path need elements
                    view.changePathStatus_BAD();
                } else {
                    //load this path attributes
                    view.setPathName(pathToBeEdited.getPathName());

                    HypertextNode[] nodes = pathToBeEdited.getPath();
                    if (nodes != null) {
                        //updates the model
                        for (int i = 0; i < nodes.length; i++) {
                            readPath.addNode(nodes[i]);
                            view.listModelAdd(nodes[i].getNodeTitle()
                                    + STR_SEPARATOR + nodes[i].getNodeURL() + STR_CLOSURE);
                        }

                        //updates the view with possible choices
                        String[] tmp = model.getAvailableLinkDestinations(
                                nodes[nodes.length - 1].getNodeTitle());

                        if (tmp.length == 0) {
                            //reached a leaf
                            view.comboModelClear();
                        } else {
                            //updates the seletion U.I.
                            view.comboModelClear();

                            //populates the combo box
                            for (int i = 0; i < tmp.length; i++) {
                                view.comboModelAdd(tmp[i]);
                            }
                        }
                    }
                    //allow this path to be removed from the map
                    view.enableRemoval();

                    //allow new insertions
                    view.listModelAdd(STR_EMPTY);

                    //previously saved map is supposed healthy
                    //path need elements
                    view.changePathStatus_OK();
                }

                // create and set the listeners to the view
                view.setInsertButtonListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        insertChoice();
                    }
                });

                view.setFormWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        finish();
                    }
                });

                view.setCancelButtonListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        finish();
                    }
                });

                view.setOkButtonListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //commit changes to the model
                        if (savePath()) {
                            finish();
                        } else {
                            //failed saving the readPath to the model
                            view.showErrorBox(view.getViewBundleString("strReadPath_ErrorMsg_SavePath"),
                                    view.getViewBundleString("strReadPath_ErrorTitle_SavePath"));
                        }
                    }
                });

                view.setRemoveButtonListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //is the user sure about the removal?
                        int option = view.showWarningOptionBox(view.getViewBundleString("strRemovePath_Msg"),
                                view.getViewBundleString("strRemovePath_Title"),
                                view.getViewBundleString("strRemovePath_ButtonYES"),
                                view.getViewBundleString("strRemovePath_ButtonNO"));

                        if (option == view.OPTION_YES) {
                            if (model.removePath(pathToBeEdited.getPathName())) {
                                finish();
                            } else {
                                //failed removing the editable path
                                view.showErrorBox(view.getViewBundleString("strReadPath_ErrorMsg_Remove"),
                                        view.getViewBundleString("strReadPath_ErrorTitle_Remove"));
                            }
                        }

                        if (option == view.OPTION_NO) {
                            return;
                        }
                    }
                });

                view.setListModelMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // only leafs can be removed
                        if (SwingUtilities.isRightMouseButton(e)) {
                            if ((readPath.size() - 1)
                                    == view.getSelectedListItemIndex()) {
                                view.showRemoveMenu(e.getX(), e.getY());
                            }
                        }
                        updateAvailableChoices(view.getSelectedListItemIndex());
                    }
                });

                view.setPopMenuRemoveListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //remove the selected node
                        removeLeaf();
                    }
                });

                //listens for name changes
                view.setNameChangeListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        saveNeeded = true;
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        saveNeeded = true;
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        //JTextField component don't fire this event
                    }
                });

            }//run()
        });
    }

    /**
     * Removes from the Model ({@link HypertextReadingPath}) the last added Node
     * ({@link HypertextNode}).
     */
    private static void removeLeaf() {
        readPath.removeNode(readPath.size() - 1);
        view.listModelRemove(view.getListModelSize() - 1);
        view.listModelReplace(view.getListModelSize() - 1, STR_EMPTY);

        if (readPath.size() == 0) {
            //path need elements
            view.changePathStatus_BAD();
        }
    }

    /**
     * Queries the main Model ({@link HypertextReadingPath}) to build the new
     * possible locations ({@link HypertextNode}) list for the ReadingPath Model
     * ({@link HypertextReadingPath}), according to the currently selected
     * location in the View ({@link ReadingPathEditorWindow}).
     *
     * @param index Currently selected location in the ReadingPath retrieved
     * from the View
     */
    private static void updateAvailableChoices(int index) {
        boolean hasChoices = false;
        String[] tmp = null;

        if (index == 0) {
            //insert every choice
            tmp = model.getAvailableLinkDestinations(null);

            view.comboModelClear();

            for (int i = 0; i < tmp.length; i++) {
                view.comboModelAdd(tmp[i]);
                hasChoices = true;
            }
        } else {
            String strTitle = view.getListItem(index - 1);
            strTitle = strTitle.substring(0, strTitle.indexOf(STR_SEPARATOR));

            HypertextNode previousNode = model.getNode(strTitle);
            if (previousNode != null) {
                //updates the view with possible choices
                tmp = model.getAvailableLinkDestinations(previousNode.getNodeTitle());
                if (tmp.length == 0) {
                    //reached a leaf
                    view.comboModelClear();
                } else {
                    //updates the seletion U.I.
                    view.comboModelClear();

                    //populates the combo box
                    for (int i = 0; i < tmp.length; i++) {
                        view.comboModelAdd(tmp[i]);
                        hasChoices = true;
                    }
                }
            }
        }

        if (view.getListItem(index).equals(STR_EMPTY)) {
            view.setInsertButtonLabel(
                    view.getViewBundleString("strReadPath_InsertButton"));
        } else {
            view.setInsertButtonLabel(
                    view.getViewBundleString("strReadPath_ReplaceButton"));
        }

        if (hasChoices) {
            view.enableInsertion();
        } else {
            view.disableInsertion();
        }
    }

    /**
     * Inserts a location ({@link HypertextNode}) in the ReadingPath Model
     * ({@link HypertextReadingPath}), chosen from the list of possible
     * locations shown to the user in the View
     * ({@link ReadingPathEditorWindow}).
     *
     * @see #updateAvailableChoices(int index)
     */
    private static void insertChoice() {
        int index = view.getSelectedListItemIndex();
        HypertextNode selectedNode = model.getNode(view.getSelectedComboItem());
        String[] tmp = null;

        //updates the model
        try {
            readPath.replaceNode(selectedNode, index);
        } catch (Exception e) {
            readPath.addNode(selectedNode);
        }

        view.listModelReplace(index,
                selectedNode.getNodeTitle() + STR_SEPARATOR
                + selectedNode.getNodeURL() + STR_CLOSURE);

        if ((view.getListModelSize() - 1) == index) {
            view.listModelAdd(STR_EMPTY);
        }

        saveNeeded = true;
        //path has elements
        view.changePathStatus_OK();
    }

    /**
     * Updates the main Model ({@link HypertextMap}) with the currently edited
     * ReadingPath ({@link HypertextReadingPath}). Retrieves the name for the
     * Model from the View ({@link ReadingPathEditorWindow}) and updates this
     * propertie in the Model.
     */
    private static boolean savePath() {
        if (saveNeeded) {
            saveNeeded = false;

            //doesn't allow empty path names
            String pathName = view.getPathName();
            if (pathName.trim().length() <= 0) {
                saveNeeded = true;
                return false;
            }

            //updates the model
            readPath.setPathName(view.getPathName());

            //check health and add
            if (pathToBeEdited != null) {
                //remove outdated entry
                model.removePath(pathToBeEdited.getPathName());
            }
            model.addPath(readPath);

            //updates the view entry
            AuthoringToolWindowController.updateReadingPathMenuItems();
            return true;
        }
        return true;
    }

    /**
     * Gracefully finishes the secondary application
     */
    private static void finish() {
        view.setVisible(false);
        view.finish();
        AuthoringToolWindowController.readingPathEditControlFinished();
    }

    /**
     * Attempts to bring this Controllers View to front. Leaving it in front of
     * all displayable components been presented in the display area.
     */
    protected static void bringViewToFront() {
        view.toFront();
    }
}
