package controller;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import model.HypertextMap;
import model.HypertextNode;
import resources.ResourceManager;
import util.HypertextTools;
import util.html.HTMLManager;
import util.html.HTMLManagerFactory;
import util.xhtml.XHTMLManager;
import util.xhtml.XHTMLManagerFactory;
import view.BusyBox;
import view.InsertLinkWindow;
import view.NodeEditorWindow;
import view.graph.GraphView;

/**
 * This class plays the Controller role in the Node Editor secondary
 * application. It drives a View ({@link NodeEditorWindow}) and a Model
 * ({@link HypertextNode}). <p>Interaction is made with the main application
 * Controller ({@link AuthoringToolWindowController}) so it can update the main
 * Model ({@link model.HypertextMap}).
 */
public class NodeEditorController {

    private static HypertextNode editingNode = null;
    private static AuthoringToolWindowController controller = null;
    private static NodeEditorWindow view = null;
    private static HTMLManager htmlManager = null;
    private static boolean transcoded = false;
    private static boolean fileNotFound = false;
    private static InsertLinkWindow insertLinkWindow = null;
    private static GraphView graphView = null;
    private static HypertextMap model = null;
    private static JMenuItem addLinkItem = null;
    private static JMenuItem splitTextItem = null;
    private static List<String> nodeLinks = null;
    private static String nodeHTMLSrcCode = null;
    private static boolean disallowLinks = false;
    private static final String HREF_STR = "href=\"";
    /**
     * Used to distinguish if the controller is editing an HypertextNode or an
     * HTML source code.
     */
    private static boolean editingSrcCode = false;
    /**
     * Used to distinguish an external link from internal links
     */
    private static final String externalLinkMark = "*";

    /**
     * Constructs the secondary application Controller class.
     *
     * @param editingNode Existing Node to be edited
     * @param controller Main application Controller
     * @param model Main application Model
     * @param graphView Main application View
     * @param disallowLinks Flag to indicate if the editor should disallow links
     */
    public NodeEditorController(HypertextNode editingNode,
            AuthoringToolWindowController controller,
            HypertextMap model,
            GraphView graphView,
            boolean disallowLinks) {
        NodeEditorController.editingNode = editingNode;
        NodeEditorController.nodeHTMLSrcCode = null;
        NodeEditorController.controller = controller;
        NodeEditorController.model = model;
        NodeEditorController.graphView = graphView;
        NodeEditorController.nodeLinks = new ArrayList<String>();
        editingSrcCode = false;
        NodeEditorController.disallowLinks = disallowLinks;
    }

    /**
     * Constructs the secondary application Controller class.
     *
     * @param nodeHTMLSrcCode Existing Node HTML source code to be edited
     * @param controller Main application Controller
     * @param model Main application Model
     * @param graphView Main application View
     */
    public NodeEditorController(String nodeHTMLSrcCode,
            AuthoringToolWindowController controller,
            HypertextMap model,
            GraphView graphView) {
        NodeEditorController.editingNode = null;
        NodeEditorController.nodeHTMLSrcCode = nodeHTMLSrcCode;
        NodeEditorController.controller = controller;
        NodeEditorController.model = model;
        NodeEditorController.graphView = graphView;
        NodeEditorController.nodeLinks = new ArrayList<String>();
        editingSrcCode = true;
        disallowLinks = true;
    }

    /**
     * Runs the secondary application. <p>All interaction made by the user
     * through the View ({@link NodeEditorWindow}) is captured by action
     * listeners. Only the Controller modifies the Model
     * ({@link HypertextNode}), updates the View and the main Controller
     * ({@link AuthoringToolWindowController}).
     */
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //instantiate the view
                view = new NodeEditorWindow();

                //instantiate the HTML Manager class
                HTMLManagerFactory htmlFactory = new HTMLManagerFactory();
                htmlManager = htmlFactory.create();

                if (!disallowLinks) {
                    //attach new a link manager for the HTML Manager class
                    addHTMLLinkManager();
                }

                //attach new split text manager
                addSplitTextManager();

                //attach mouse event listener for the HTML Manager class
                if (!editingSrcCode) {
                    htmlManager.addMouseListener(new PopupListener());
                }

                //verify editingNode
                if (editingNode != null) {
                    //fill in node attributes
                    view.setNodeTitle(editingNode.getNodeTitle());
                    final String strURL = editingNode.getNodeURL();
                    final String nodePath = HypertextTools.urlToFilePath(strURL);
                    //view displays file system path
                    view.setNodeURL(nodePath);

                    //load file
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if (!loadFileInEditor(nodePath)) {
                                //if the loading fails begin error handling
                                fileNotFound = true;
                                view.setFileNotFoundMsgVisibility(true);
                            } else {
                                //file loaded
                                fileNotFound = false;
                                view.setFileNotFoundMsgVisibility(false);
                            }
                        }
                    });
                } else {
                    //native html
                    transcoded = false;

                    if (editingSrcCode) {
                        //disalow properties edition
                        view.disableViewFields();

                        //will edit a piece of html code
                        view.setFileNotFoundMsgVisibility(false);

                        if (nodeHTMLSrcCode != null) {
                            //load source code
                            htmlManager.loadHTMLFromString(nodeHTMLSrcCode);
                        } else {
                            //new document
                            htmlManager.createEmptyHTML();
                        }

                        //attaches the HTMLManager
                        view.attachHTMLManager((Component) htmlManager.getDisplayable());

                        //attaches the HTMLManagerDocumentLisener
                        htmlManagerDocumentListener();

                        //rebuild link control list
                        rebuildLinkList();
                    } else {
                        //new document
                        htmlManager.createEmptyHTML();

                        //attaches the HTMLManager
                        view.attachHTMLManager((Component) htmlManager.getDisplayable());

                        //attaches documentListener
                        htmlManagerDocumentListener();
                    }
                }

                //create and set the listeners to the view
                view.setSaveButtonListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        performSave(false);
                    }
                });

                view.setCancelButtonListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        finish();
                    }
                });

                view.setFindButtonListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        view.setNodeURL(
                                view.openNodeFile(view.getNodeURL()));
                    }
                });


                view.setFormWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        finish();
                    }
                });
            }
        });
    }

    /**
     * Helper to load an HTML file in the underlying HTMLManager API. If the
     * current file in edition is an XHTML apply transcoding from XHTML to HTML.
     * This task may be time consuming.
     *
     * @param strPath the absolute file path to the file being loaded
     */
    protected static boolean loadFileInEditor(String strPath) {
        //check for file existence
        File f = new File(strPath);
        if (!f.exists()) {
            //file not found
            return false;
        } else {
            //load file
            if (strPath.indexOf(".htm") > 0) {
                htmlManager.loadHTMLFile(strPath);

                //attaches the HTMLManager
                view.attachHTMLManager((Component) htmlManager.getDisplayable());

                //attaches the HTMLManagerDocumentLisener
                htmlManagerDocumentListener();
                //rebuild link control list
                rebuildLinkList();
                //file loaded
                return true;
            } else {
                //needs transcoding, wait untill it completes
                view.disableViewInteraction();
                view.disableViewFields();

                transcoded = true;
                BusyBox box =
                        new BusyBox(
                        view.getViewBundleString("strBusy_Title"),
                        view.getViewBundleString("strBusy_Msg_XHTML_HTML"));
                box.showBox();

                //process XHTML to HTML transcoding
                String transcode =
                        HypertextTools.transformXHTML_To_HTML(strPath);
                //finished
                box.dismissBox();

                if (transcode != null) {
                    //display transcoded document
                    htmlManager.loadHTMLFromString(transcode);
                    //attaches the HTMLManager
                    view.attachHTMLManager(
                            (Component) htmlManager.getDisplayable());

                    view.enableViewInteraction();
                    view.enableViewFields();

                    //attaches the HTMLManagerDocumentLisener
                    htmlManagerDocumentListener();
                    //rebuild link control list
                    rebuildLinkList();
                    //file loaded
                    return true;
                } else {
                    //Transcoding failed
                    view.showErrorBox(
                            view.getViewBundleString("strErrorMsg_TranscodingFailed_Msg"),
                            view.getViewBundleString("strErrorMsg_TranscodingFailed_Title"));
                    //failed
                    return false;
                }
            }
        }
    }

    /**
     * Attempts to bring this Controllers View to front. Leaving it in front of
     * all displayable components been presented in the display area.
     */
    protected static void bringViewToFront() {
        view.toFront();
    }

    /**
     * Allow the user to choose a destination for the link based on the given
     * link source. Add a Link({@link model.HypertextLink}) with the given
     * source and selected destination to the Main
     * Model({@link model.HypertextMap}) and updates the Main
     * View({@link view.AuthoringToolWindow}).
     *
     * @param srcNode the source Node of the new link.
     * @see view.InsertLinkWindow
     */
    private static void addLink(final String srcNode) {
        if (insertLinkWindow == null) {
            //is this node already in the map?
            if (model.getNode(srcNode) != null) {
                insertLinkWindow = new InsertLinkWindow(srcNode,
                        model.getAvailableLinkDestinations(srcNode));

                insertLinkWindow.setOKButtonEnabled(false);
            } else {
                //this node is not in the map yet
                view.showErrorBox(view.getViewBundleString("strErrMsg_NodeNotInTheMap"),
                        view.getViewBundleString("strErrTit_NodeNotInTheMap"));
                return;
            }

            insertLinkWindow.setFormWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    insertLinkWindow.finish();
                    insertLinkWindow = null;
                }
            });

            insertLinkWindow.setCancelButtonListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    insertLinkWindow.finish();
                    insertLinkWindow = null;
                }
            });

            insertLinkWindow.setExternalLinkOptionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //user choosed to insert an External Link
                    insertLinkWindow.setModeToExternalLink();
                    //user can add the link
                    insertLinkWindow.setOKButtonEnabled(true);
                }
            });

            insertLinkWindow.setInternalLinkOptionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //user choosed to insert an Internal Link
                    insertLinkWindow.setModeToInternalLink();
                    //user can add the link
                    insertLinkWindow.setOKButtonEnabled(true);
                }
            });

            insertLinkWindow.setOkButtonListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //get the link destination
                    String selectedTargetLabel =
                            insertLinkWindow.getSelectedChoice();
                    final boolean opensNewWindow =
                            insertLinkWindow.linkOpensNewWindow();
                    final String selText =
                            htmlManager.getHTMLEditorSelectedText(false);

                    if (selectedTargetLabel != null) {
                        HypertextNode targetNode =
                                model.getNode(selectedTargetLabel);

                        //update HTMLManager API with the new link and
                        //also add this link to the internal control
                        final String selURL = targetNode.getNodeURL();
                        final String tag =
                                htmlManager.makeTextAnHyperlink(selText,
                                selURL,
                                opensNewWindow);
                        addLinkToList(tag);

                        //add a link between map nodes
                        if (model.addLink(srcNode,
                                selectedTargetLabel)) {
                            //updates the view
                            graphView.addEdge(srcNode, selectedTargetLabel);
                        }
                    } else {
                        //update HTMLManager API with the new external link
                        //uses the externalLinkMark to distinguish this link
                        //from internal links
                        final String typedURL =
                                insertLinkWindow.getTypedURL();
                        final String tag =
                                htmlManager.makeTextAnHyperlink(
                                selText + externalLinkMark,
                                typedURL,
                                opensNewWindow);
                        addLinkToList(tag);
                    }
                    //commit changes to file in edition by the HTMLManager
                    performSave(false);

                    //finish using this window
                    insertLinkWindow.finish();
                    insertLinkWindow = null;
                }
            });
        } else {
            insertLinkWindow.setOKButtonEnabled(false);

            insertLinkWindow.toFront();
        }
    }

    /**
     * Private class to implement a pop up menu for the underlying HTMLManager
     * API.
     */
    static class PopupListener extends MouseAdapter {
        //windows

        @Override
        public void mouseReleased(MouseEvent e) {
            verifyMouseEvent(e);
        }

        //linux
        @Override
        public void mousePressed(MouseEvent e) {
            verifyMouseEvent(e);
        }

        /**
         * Private method to first filter the valid events and triggers the the
         * proper pop-up menu.
         */
        private void verifyMouseEvent(MouseEvent e) {
            //Display proper popUpMenu for the underlying HTMLManager API.
            if (e.isPopupTrigger()) {
                JPopupMenu popupMenu = htmlManager.getHTMLEditorPopUpMenu();

                try {
                    if (htmlManager.getHTMLEditorSelectedText(false).length() > 0) {
                        if (!disallowLinks) {
                            popupMenu.addSeparator();
                            popupMenu.add(addLinkItem);
                        }

                        popupMenu.addSeparator();
                        popupMenu.add(splitTextItem);
                    }
                } catch (Exception ex) {
                    // no text selected
                }

                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    /**
     * Add a link to the internal control list. This internal control is
     * responsible to identify the moment when a link was removed in the
     * HTMLManager. This was not provided by the underlying API.
     *
     * @param strLink the HTML code for the Hyperlink.
     */
    private static void addLinkToList(String strLink) {
        nodeLinks.add(strLink);
    }

    /**
     * Remove a link from the internal control list. This internal control is
     * responsible to identify the moment when a link was removed in the
     * HTMLManager. This was not provided by the underlying API.
     *
     * @param strLink the HTML code for the Hyperlink.
     */
    private static void removeLinkFromList(String strLink) {
        //remove link from the internal control list
        nodeLinks.remove(strLink);
        String strURL = strLink.substring(
                strLink.indexOf(HREF_STR) + 6,
                strLink.indexOf("\"", strLink.indexOf(HREF_STR) + 6));

        //find occurence of the link in the list
        boolean contains = false;
        for (Iterator iterLinks = nodeLinks.iterator(); iterLinks.hasNext();) {
            String current = (String) iterLinks.next();
            String currentURL = current.substring(
                    current.indexOf(HREF_STR) + 6,
                    current.indexOf("\"", current.indexOf(HREF_STR) + 6));

            if (currentURL.equals(strURL)) {
                //more occurrences of the link in the internal control list
                contains = true;
                break;
            }
        }

        if (!contains) {
            //no more occurrences of the link, remove it from the main model
            String tgtNodeURL = strLink.substring(
                    strLink.indexOf(HREF_STR) + 6,
                    strLink.indexOf("\"", strLink.indexOf(HREF_STR) + 6));

            HypertextNode tgtNode = model.getNodeByURL(tgtNodeURL);
            if (tgtNode != null) {
                //remove link from the main model
                model.removeLink(view.getNodeTitle(),
                        tgtNode.getNodeTitle());
                //update the main view
                graphView.removeEdge(view.getNodeTitle(), tgtNode.getNodeTitle());
            }

            //commit changes to file in edition by the HTMLManager
            performSave(false);
        }
    }

    /**
     * Rebuild link internal control list. This internal control is responsible
     * to identify the moment when a link was removed in the HTMLManager. This
     * was not provided by the underlying API.
     */
    private static void rebuildLinkList() {
        String strCode = htmlManager.getHTMLCode();
        //flushes previous control
        nodeLinks.clear();
        //parse through the HTML code to find all the links
        Pattern p = Pattern.compile("<a(.*?)</a>",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        Matcher m = p.matcher(strCode);
        boolean result = m.find();

        //loop through all occurrences
        while (result) {
            addLinkToList(m.group());
            result = m.find();
        }
    }

    /**
     * Update link internal control list after a removal occurred. This internal
     * control is responsible to identify the moment when a link was removed in
     * the HTMLManager. This was not provided by the underlying API.
     */
    private static void updateLinkListAfterRemoval() {
        String strCode = htmlManager.getHTMLCode();
        //parse through the HTML code to find all the links
        Pattern p = Pattern.compile("<a(.*?)</a>",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        Matcher m = p.matcher(strCode);
        boolean result = m.find();

        List<String> codeLinks = new ArrayList<String>();
        //loop through all occurrences
        while (result) {
            codeLinks.add(m.group());
            result = m.find();
        }

        //List of links to be removed
        List<String> removedLinks = new ArrayList<String>();
        //Traverse the list of controlled links and find which where removed
        //from the source code by the user
        boolean addToRemove = true;
        for (Iterator iterLinks = nodeLinks.iterator(); iterLinks.hasNext();) {
            String current = (String) iterLinks.next();
            String currentURL = current.substring(
                    current.indexOf(HREF_STR) + 6,
                    current.indexOf("\"", current.indexOf(HREF_STR) + 6));

            for (Iterator iterCode = codeLinks.iterator(); iterCode.hasNext();) {
                String inCode = (String) iterCode.next();

                String codeURL = inCode.substring(
                        inCode.indexOf(HREF_STR) + 6,
                        inCode.indexOf("\"", inCode.indexOf(HREF_STR) + 6));

                if (currentURL.equals(codeURL)) {
                    addToRemove = false;
                    break;
                } else {
                    addToRemove = true;
                }
            }
            if (addToRemove) {
                removedLinks.add(current);
            }
        }

        //Notify link removal
        for (Iterator iterLinks = removedLinks.iterator(); iterLinks.hasNext();) {
            String current = (String) iterLinks.next();
            removeLinkFromList(current);
        }
    }

    /**
     * Attaches a Document Listener for the underlying HTMLManager API.
     */
    private static void htmlManagerDocumentListener() {
        //attach document event listener for the HTML Manager class
        htmlManager.addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                //verify if a link was removed
                updateLinkListAfterRemoval();
            }
        });
    }

    /**
     * Create addLink manager for the underlying HTMLManager API. Build menu
     * handler and attaches actionListener.
     */
    private static void addHTMLLinkManager() {
        InputStream input =
                ResourceManager.getResourceInputStream("anchorIco.png");
        Image img = null;
        try {
            img = ImageIO.read(input);
        } catch (Exception e) {
            try {
                input.close();
            } catch (Exception ex) {
            }
            //error while creating link handler
            view.showErrorBox(view.getViewBundleString("strErrMsg_LinkHandler"),
                    view.getViewBundleString("strErrTit_Corrupt"));
            return;
        }
        try {
            input.close();
        } catch (Exception ex) {
        }
        ImageIcon imgIcon = new ImageIcon(img);

        addLinkItem = new JMenuItem(
                view.getViewBundleString("strJMenuItem_AddLink"),
                imgIcon);

        addLinkItem.addActionListener(new ActionListener() {
            //insert new link listener
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        //call add link handler method
                        addLink(view.getNodeTitle());
                    }
                });
            }
        });
        try {
        } catch (Exception e) {
            //error while creating link handler
            view.showErrorBox(view.getViewBundleString("strErrMsg_LinkHandler"),
                    view.getViewBundleString("strErrTit_Corrupt"));
        }
    }

    /**
     * Create splitText manager. Build menu handler and attaches actionListener.
     */
    private static void addSplitTextManager() {
        InputStream input =
                ResourceManager.getResourceInputStream("splitIco.png");
        Image img = null;
        try {
            img = ImageIO.read(input);
        } catch (Exception e) {
            try {
                input.close();
            } catch (Exception ex) {
            }
            //error while creating split text handler
            view.showErrorBox(view.getViewBundleString("strErrMsg_SplitTextHandler"),
                    view.getViewBundleString("strErrTit_Corrupt"));
            return;
        }
        try {
            input.close();
        } catch (Exception ex) {
        }
        ImageIcon imgIcon = new ImageIcon(img);

        splitTextItem = new JMenuItem(
                view.getViewBundleString("strJMenuItem_SplitText"), imgIcon);

        splitTextItem.addActionListener(new ActionListener() {
            //insert split listener
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        //call split text handler method
                        splitSelectedTextInNewFragment();
                    }
                });
            }
        });
    }

    /**
     * Create a new node containing the currently select text.
     */
    private static void splitSelectedTextInNewFragment() {
        try {
            // store current selection while removing it from the original text
            String currentSelection =
                    htmlManager.getHTMLEditorSelectedText(true);

            // create new fragment with current selected text
            if (currentSelection.length() > 0) {
                // commit changes (synchronously)
                performSave(true);

                // notify user
                view.showInfoBox(
                        view.getViewBundleString("strSplit_InfoMsg"),
                        view.getViewBundleString("strSplit_InfoTitle"));

                // freshen the editor
                view.setVisible(false);
                editingNode = null;
                view.setNodeTitle("");
                view.setNodeURL("");

                // load new text in editor
                htmlManager.setHTMLEditorText(currentSelection);

                // display editor with new fragment
                view.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper thread to perform save operation in the HTML being edited.
     */
    private static class HTMLSaver implements Runnable {

        @Override
        public void run() {
            if (editingSrcCode) {
                //notify controller to update as necessary
                AuthoringToolWindowController.nodeEditorSavedEditingSourceCode(
                        htmlManager.getHTMLCode());
            } else {
                //sanity check
                if (view.getNodeTitle().trim().length() <= 0) {
                    view.showErrorBox(view.getViewBundleString("strInsertTitle_ErrorMsg"),
                            view.getViewBundleString("strInsertTitle_ErrorTitle"));
                    return;
                }

                if (view.getNodeURL().trim().length() <= 0) {
                    view.showErrorBox(view.getViewBundleString("strInsertURL_ErrorMsg"),
                            view.getViewBundleString("strInsertURL_ErrorTitle"));
                    return;
                }

                //file system path
                final String strPath = view.getNodeURL();
                //URI
                final String nodeURL =
                        HypertextTools.filePathToURL(strPath);
                if (fileNotFound) {
                    //update model
                    editingNode.setNodeTitle(view.getNodeTitle());
                    editingNode.setNodeURL(nodeURL);

                    //attempts to load the editor with a valid file
                    if (loadFileInEditor(strPath)) {
                        fileNotFound = false;
                        view.setFileNotFoundMsgVisibility(false);
                    } else {
                        fileNotFound = true;
                        view.setFileNotFoundMsgVisibility(true);
                    }
                    return;
                }

                htmlManager.setHTMLTitle(view.getNodeTitle());
                //check if transcoding is needed
                if (transcoded) {
                    //convert to XHTML again
                    String transcode =
                            HypertextTools.transformHTML_To_XHTML(
                            htmlManager.getHTMLCode());

                    if (transcode != null) {
                        //persist changes
                        XHTMLManagerFactory xMngFactory = new XHTMLManagerFactory();
                        XHTMLManager xMng = xMngFactory.create();

                        xMng.loadFromString(transcode);
                        xMng.persistChanges(strPath);
                    } else {
                        //Transcoding failed
                        view.showErrorBox(
                                view.getViewBundleString("strErrorMsg_TranscodingFailed_Msg"),
                                view.getViewBundleString("strErrorMsg_TranscodingFailed_Title"));
                        finish();
                    }
                } else {
                    htmlManager.saveHTMLFile(strPath);
                }
                //update main application
                if (editingNode != null) {
                    //first notify controller to update view
                    controller.updateNodeView(editingNode.getNodeTitle(),
                            view.getNodeTitle());

                    //then update model
                    editingNode.setNodeTitle(view.getNodeTitle());

                    editingNode.setNodeURL(nodeURL);
                } else {
                    //add newly created node
                    editingNode =
                            AuthoringToolWindowController.addNode(view.getNodeTitle(),
                            nodeURL);
                }
            }
        }
    }

    /**
     * Perform save operation in the editing HTML.
     *
     * @param synchronous flag to indicate if the method will run synchronously
     * (wait for the save operation to end) or asynchronously (request and
     * return).
     */
    public static synchronized void performSave(boolean synchronous) {
        HTMLSaver saver = new HTMLSaver();
        Thread saveThread = new Thread(saver);

        // dispatch save operation
        saveThread.start();

        if (synchronous) {
            try {
                // wait for the save operation to finish
                saveThread.join();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Gracefully finishes the secondary application
     */
    private static void finish() {
        view.detachHTMLManager();
        view.setVisible(false);
        view.finish();
        AuthoringToolWindowController.nodeEditControlFinished();
    }
}
