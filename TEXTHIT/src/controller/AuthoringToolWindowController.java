package controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import model.HypertextLink;
import model.HypertextMap;
import model.HypertextNode;
import model.HypertextReadingPath;
import model.Perspective;
import resources.AppConfiguration;
import resources.ResourceManager;
import resources.TemporaryConfigManager;
import util.HTMLManipulator;
import util.HypertextManipulator;
import util.HypertextTools;
import util.PersistencePackager;
import util.XHTMLManipulator;
import util.XMLManipulator;
import util.html.HTMLManager;
import util.html.HTMLManagerFactory;
import util.xhtml.XHTMLManager;
import util.xhtml.XHTMLManagerFactory;
import view.AuthoringToolWindow;
import view.BusyBox;
import view.InsertNodeTitleWindow;
import view.MergeNodeWindow;
import view.PerspectiveEditorWindow;
import view.graph.GraphView;
import view.graph.GraphViewFactory;
import view.xhtml.XHTMLView;
import view.xhtml.XHTMLViewFactory;

/**
 * This class plays the Controller role in the main application. It drives the
 * main View ({@link AuthoringToolWindow}) and the main Model
 * ({@link HypertextMap}). It also drives the {@link GraphView} that is attached
 * in the main View to display to the user a graphical representation of the
 * main Model. <p>This class also, plays the role of the Observer in the
 * Observer-Observable Design Pattern. The GraphView is the Observable object of
 * this class. <p>It uses the configuration manager to store the user work
 * folder ({@link TemporaryConfigManager}). <p>Interaction is made with other
 * Controllers to dispatch secondary tasks: <ul> <li>Node content edition
 * ({@link NodeEditorController}) <li>Generation of Reading Paths
 * ({@link ReadingPathEditorController}) <li>HypertextMap publishing in a remote
 * server ({@link PublisherWindowController}) </ul>
 *
 * @see #update(Observable obj, Object arg)
 */
public class AuthoringToolWindowController implements Observer {

    public static final String ORIGINAL_TEXT = "Original";
    private static volatile HypertextMap model = null;
    private static AuthoringToolWindow view = null;
    private static volatile ReadingPathEditorController pathControl = null;
    private static volatile PublisherWindowController publishControl = null;
    private static InsertNodeTitleWindow insertNodeTitleWindow = null;
    private static MergeNodeWindow mergeNodeWindow = null;
    private static PerspectiveEditorWindow perspectiveEditor = null;
    private static GraphView graphView = null;
    private static XHTMLView descriptionPreview = null;
    private static volatile String mapPreviousSave = null;
    private static AuthoringToolWindowController controller = null;
    private static volatile NodeEditorController nodeEditControl = null;
    private static boolean editingDescription = false;
    private static final String MODEL_PACKAGE = "model.xml";
    private static final String MODEL_NODE = "node";
    private static final String MODEL_LINK = "link";
    private static final String MODEL_READING_PATH = "readingPath";
    private static final String MODEL_PATH_NODE = "pathNode";
    private static final String MODEL_PERSPECTIVE = "perspective";
    private static final String MODEL_PERSPECTIVE_COLOR = "perspectiveColor";
    private static final String VIEW_PACKAGE = "view.xml";
    private static final String VIEW_VERTEX = "vertex";
    private static final String VIEW_COLOR = "color";
    private static final String VIEW_EDGE = "edge";
    private static final String CONFIG_PACKAGE = "config.xml";

    /**
     * Constructs the main application Controller class.
     */
    public AuthoringToolWindowController() {
    }

    /**
     * Runs the main application. <p>All interaction made by the user through
     * the View ({@link AuthoringToolWindow}) is captured by action listeners.
     * Only the Controller modifies the Model ({@link HypertextMap}) and updates
     * the View.
     */
    public static void main(String args[]) {
        //force constructor invoke
        controller = new AuthoringToolWindowController();

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                // instantiate the view
                view = new AuthoringToolWindow();

                // create and set the listeners to the view
                view.setCreateMapListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //dispatch task asynchronously
                        new Thread() {

                            @Override
                            public void run() {
                                createMap();
                            }
                        }.start();
                    }
                });

                view.setOpenMapListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //dispatch task asynchronously
                        new Thread() {

                            @Override
                            public void run() {
                                //informes that a map is being opened
                                view.showStatus(
                                        view.getViewBundleString("strOpeningMap"));
                                openMap();
                            }
                        }.start();
                    }
                });

                view.setRewriteListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //dispatch task asynchronously
                        new Thread() {

                            @Override
                            public void run() {
                                createMap();
                                //process the rewrite
                                processRewrite();
                            }
                        }.start();
                    }
                });

                view.setAddNodeListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //dispatch task asynchronously
                        new Thread() {

                            @Override
                            public void run() {
                                //add a node to the map
                                addNode();
                            }
                        }.start();
                    }
                });

                view.setPerspectivesListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        editPerspectives();
                    }
                });

                view.setRemoveNodeListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //dispatch task asynchronously
                        new Thread() {

                            @Override
                            public void run() {
                                removeNode();
                            }
                        }.start();
                    }
                });

                view.setCreatePathListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //dispatch task asynchronously
                        new Thread() {

                            @Override
                            public void run() {
                                dispatchReadingPathEditor(null);
                            }
                        }.start();
                    }
                });

                view.setAboutListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //dispatch task asynchronously
                        new Thread() {

                            @Override
                            public void run() {
                                showAboutBox();
                            }
                        }.start();
                    }
                });

                view.setLicenseListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //dispatch task asynchronously
                        new Thread() {

                            @Override
                            public void run() {
                                showLicenseBox();
                            }
                        }.start();
                    }
                });

                view.setFormWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent e) {
                        //dispatch task asynchronously
                        new Thread() {

                            @Override
                            public void run() {
                                finish();
                            }
                        }.start();
                    }
                });

                view.setExitListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //dispatch task asynchronously
                        new Thread() {

                            @Override
                            public void run() {
                                finish();
                            }
                        }.start();
                    }
                });

                view.setPopMenuRemoveNodeListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //dispatch task asynchronously
                        new Thread() {

                            @Override
                            public void run() {
                                removeNode();
                            }
                        }.start();
                    }
                });

                view.setPopMenuPreviewListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //dispatch task asynchronously
                        new Thread() {

                            @Override
                            public void run() {
                                showPreview();
                            }
                        }.start();
                    }
                });

                view.setPopMenuPerspectiveChooserListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Retrieve this model's perspectives
                        List mapPersp = model.getThisMapPerspectives();
                        int pivot = 0;
                        Object[][] data = null;

                        // Populate raw array using model's perspectives. It will be used
                        // in the editor
                        if (mapPersp != null) {
                            data = new Object[mapPersp.size()][2];

                            for (Iterator iterPersp = mapPersp.iterator(); iterPersp.hasNext();) {
                                Perspective persp = (Perspective) (iterPersp.next());
                                data[pivot][0] = persp.getLabel();
                                data[pivot][1] = persp.getColor();
                                pivot++;
                            }
                        }

                        // Instantiate editor
                        perspectiveEditor =
                                new PerspectiveEditorWindow(data,
                                view.getViewBundleString("strPerspectiveChooser_Message")
                                + graphView.getSelectedVertexString());

                        perspectiveEditor.setFormWindowListener(new WindowAdapter() {

                            @Override
                            public void windowClosing(WindowEvent e) {
                                perspectiveEditor.finish();
                                perspectiveEditor = null;
                            }
                        });

                        perspectiveEditor.setOkButtonListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                //get selected object
                                Object[] rowData = perspectiveEditor.getSelectedObject();
                                if (rowData != null) {
                                    //retrieve selected object color
                                    Color chosenColor = (Color) rowData[1];

                                    //update the selected vertex with the chosen color
                                    graphView.setSelectedVertexColor(chosenColor.getRed(),
                                            chosenColor.getGreen(),
                                            chosenColor.getBlue());
                                }

                                //finish using this window
                                perspectiveEditor.finish();
                                perspectiveEditor = null;
                            }
                        });

                        //display perspective editor
                        perspectiveEditor.setVisible(true);
                    }
                });

                view.setPopMenuMergeNodeListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        final String strTitle = graphView.getSelectedVertexString();

                        mergeNode(strTitle);
                    }
                });

                view.setSaveMapListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //dispatch task asynchronously
                        new Thread() {

                            @Override
                            public void run() {
                                if (mapPreviousSave != null) {
                                    saveMap(mapPreviousSave);
                                } else {
                                    saveMapAs();
                                }
                            }
                        }.start();
                    }
                });

                view.setSaveMapAsListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //dispatch task asynchronously
                        new Thread() {

                            @Override
                            public void run() {
                                saveMapAs();
                            }
                        }.start();
                    }
                });

                view.setCreateNodeListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //dispatch task asynchronously
                        new Thread() {

                            @Override
                            public void run() {
                                dispatchNodeEditorWindow(null);
                            }
                        }.start();
                    }
                });

                view.setPopMenuEditNodeListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //dispatch task asynchronously
                        new Thread() {

                            @Override
                            public void run() {
                                dispatchNodeEditorWindow(model.getNode(
                                        graphView.getSelectedVertexString()));
                            }
                        }.start();
                    }
                });

                view.setMapPublishListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //dispatch task asynchronously
                        new Thread() {

                            @Override
                            public void run() {
                                dispatchPublisherWindow(false);
                            }
                        }.start();
                    }
                });

                view.setMapPublishLocalListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //dispatch task asynchronously
                        new Thread() {

                            @Override
                            public void run() {
                                dispatchPublisherWindow(true);
                            }
                        }.start();
                    }
                });
            }
        });
    }//main()

    /**
     * Perform setup operations in the {@link GraphView} and display it to the
     * user.
     */
    public static void initializeGraphView() {
        //instantiate the map graphical representation
        GraphViewFactory graphFactory = new GraphViewFactory();
        graphView = graphFactory.create();

        //initizaling the graph engine
        graphView.initializeGraph();

        //ignites pattern Observer/Observable
        graphView.addObserver(controller);

        //gets the displayable component and shows it in the main window
        view.attachGraphView((Component) graphView.getDisplayable());
    }

    /**
     * Perform cleanup operations in the {@link GraphView} and stop displaying
     * it to the user.
     */
    public static void stopGraphView() {
        //removes the displayable component from the main window
        view.detachGraphView();

        //stop the graph displaying engine
        try {
            graphView.stopGraph();
        } catch (Exception e) {
        }

        //stop the pattern Observer/Observable
        try {
            graphView.deleteObserver(controller);

        } catch (Exception e) {
        }
        graphView = null;

        //updates the MMI
        view.uiModifyState(view.UI_STATE_NO_MAP);
        view.showStatus(view.getViewBundleString("strGraphEngineStoped"));
    }

    /**
     * Creates an empty map. If there is one already in edition, prompts the
     * user for the action to be taken. <p>Creates the Model
     * ({@link HypertextMap}) and updates the View
     * ({@link AuthoringToolWindow}).
     */
    private static synchronized void createMap() {
        if (model == null) {
            //instantiate the model
            model = new HypertextMap();

            //loads default application configuration
            loadDefaultConfig();

            //initializes the view
            initializeGraphView();

            //reset previously saved map
            mapPreviousSave = null;

            //instantiate the description preview
            initializeDescriptionPreview();

            view.mapSuccessfullyCreated();
        } else {
            //prompts the user for the action to be taken over the current opened map
            int option = view.showOptionBox(view.getViewBundleString("strOverwriteMap_Msg"),
                    view.getViewBundleString("strOverwriteMap_Title"),
                    view.getViewBundleString("strOverwriteMap_ButtonYES"),
                    view.getViewBundleString("strOverwriteMap_ButtonNO"),
                    view.getViewBundleString("strOverwriteMap_ButtonCANCEL"));

            if (option == view.OPTION_CANCEL) {
                //user canceled the open map operation
                view.showStatus(view.getViewBundleString("strCreatingMapCanceled"));
                return;
            }
            if (option == view.OPTION_YES) {
                //user choosed to save the previous map
                if (!saveMap(mapPreviousSave)) {
                    //save map canceled or failed
                    view.showStatus(
                            view.getViewBundleString("strCreatingMapCanceled"));
                    return;
                }
            }
            //stops the current graph display
            stopGraphView();

            //stops the description display
            descriptionPreview.unload();
            view.dettachMapDescription();

            //overwrites current model
            model.dispose();

            //instantiate a new model
            model = new HypertextMap();

            //loads default application configuration
            loadDefaultConfig();

            //reset previously saved map
            mapPreviousSave = null;

            //initializes the view
            initializeGraphView();

            //instantiate the description preview
            initializeDescriptionPreview();

            view.mapSuccessfullyCreated();
        }

        //loads the blank html page from the jar package
        InputStream blankStream =
                ResourceManager.getResourceInputStream("blank.html");
        InputStreamReader inputStreamReader =
                new InputStreamReader(blankStream);
        BufferedReader in = new BufferedReader(inputStreamReader);

        String line;
        StringBuffer res = new StringBuffer();
        try {
            do {
                line = in.readLine();
                if (line != null) {
                    res.append(line);
                }
            } while (line != null);
        } catch (Exception ex) {
        }
        String srcCode = res.toString();

        if (srcCode.length() > 0) {
            //update map description
            model.setMapDescription(srcCode);
        }

        try {
            in.close();
        } catch (Exception ex) {
        }
        try {
            inputStreamReader.close();
        } catch (Exception ex) {
        }
        try {
            blankStream.close();
        } catch (Exception ex) {
        }

        if (srcCode.length() > 0) {
            //process HTML to XHTML transcoding
            String transcoded = HypertextTools.transformHTML_To_XHTML(srcCode);
            if (transcoded != null) {
                descriptionPreview.loadDocument(transcoded);
                return;
            }
        }
        //Transcoding failed
        view.showErrorBox(
                view.getViewBundleString("strErrorMsg_TranscodingFailed_Msg"),
                view.getViewBundleString("strErrorMsg_TranscodingFailed_Title"));
    }

    /**
     * Attempts to begin the rewriting process. Prompts the user for the
     * original hypertext node location and then fragments it into smaller
     * nodes. <p>Updates the Model ({@link HypertextMap}) and updates the View
     * ({@link AuthoringToolWindow}) with the newly created fragments.
     */
    private static synchronized void processRewrite() {
        String nodePath = null;
        String strPath = null;
        HypertextManipulator htManip = null;
        String[] paragraphs = null;

        //informs that a node is being added
        view.showStatus(view.getViewBundleString("strProcessingNodeForRewrite"));

        //get last used add node location
        strPath = TemporaryConfigManager.getTextToConvert();
        //allows the user to choose a file
        nodePath = view.openNodeFile(strPath);
        if (nodePath == null) {
            //user canceled the node insertion
            view.showStatus(view.getViewBundleString("strStatus_UserCanceledAddNode"));
            return;
        }
        //updates last used location
        TemporaryConfigManager.setTextToConvert(nodePath);

        BusyBox box = new BusyBox(view.getViewBundleString("strBusy_Title"),
                view.getViewBundleString("strBusy_Msg_Loading_XHTML"));
        box.showBox();

        if (nodePath.indexOf(".xhtml") > 0) {
            htManip = new XHTMLManipulator();
        } else {
            htManip = new HTMLManipulator();
        }

        if (!htManip.load(nodePath)) {
            if (box != null) {
                box.dismissBox();
            }
            //failed manipulating the hypertext
            view.showErrorBox(view.getViewBundleString("strErrorMsg_InsertNodeLoadFailed_Msg"),
                    view.getViewBundleString("strErrorMsg_InsertNodeLoadFailed_Title"));
            return;
        }

        //Currently we don't support CSS. Strip out CSS from the Hypertext file
        htManip.removeCSS(nodePath);

        //get paragraphs
        paragraphs = htManip.getTokens(
                view.getViewBundleString("paragraph_start_delimiter"),
                view.getViewBundleString("paragraph_end_delimiter"));

        //instantiate a new model concept
        model = new HypertextMap();
        HypertextNode newNode = model.addNode(ORIGINAL_TEXT, HypertextTools.filePathToURL(nodePath),
                HypertextNode.NODE_TYPE_READY_ONLY);
        if (newNode != null) {
            //Add original hypertext to the view
            graphView.addVertex(ORIGINAL_TEXT);
        } else {
            //node exists
            view.showErrorBox(view.getViewBundleString("strErrorMsg_NodeExists_Msg"),
                    view.getViewBundleString("strErrorMsg_NodeExists_Title"));
            return;
        }

        HTMLManipulator htmlManip = new HTMLManipulator();
        String newNodePath = null;
        String srcCode = null;
        //retrive filename from node storage path
        String strFragmentDir =
                nodePath.substring(0, nodePath.lastIndexOf(PersistencePackager.fileSeparator));

        //for each paragraph
        for (int i = 0; i < paragraphs.length; i++) {
            //*create a file from blank HTML template with the paragraph;
            srcCode = "";

            //Create an HTML from the blank.html template
            InputStream blankStream =
                    ResourceManager.getResourceInputStream("blank.html");
            InputStreamReader inputStreamReader =
                    new InputStreamReader(blankStream);
            BufferedReader in = new BufferedReader(inputStreamReader);

            StringBuffer res = new StringBuffer();
            try {
                String line;
                do {
                    line = in.readLine();
                    if (line != null) {
                        res.append(line);
                    }
                } while (line != null);
            } catch (Exception e) {
            }
            srcCode = res.toString();

            try {
                in.close();
            } catch (Exception ex) {
            }
            try {
                inputStreamReader.close();
            } catch (Exception ex) {
            }
            try {
                blankStream.close();
            } catch (Exception ex) {
            }

            if (srcCode.length() == 0) {
                view.showErrorBox(view.getViewBundleString("str_Cant_Rewrite_Msg"),
                        view.getViewBundleString("str_Cant_Rewrite_Title"));
                return;
            }

            //Add paragraph to the html's body
            srcCode = srcCode.substring(0, srcCode.indexOf("</body>"))
                    + paragraphs[i]
                    + srcCode.substring(srcCode.indexOf("</body>"));
            htmlManip.setHTMLSourceCode(srcCode);
            htmlManip.setTitle("Fragment" + i);
            //Obtain the new node path
            newNodePath = strFragmentDir + PersistencePackager.fileSeparator + "fragment" + i + ".html";
            htmlManip.persistChanges(newNodePath);

            //add created file as a node in the map
            newNode = model.addNode("Fragment" + i,
                    HypertextTools.filePathToURL(newNodePath));
            if (newNode != null) {
                //Add new hypertext fragment to the view
                graphView.addVertex("Fragment" + i);
            } else {
                //node exists
                view.showErrorBox(view.getViewBundleString("strErrorMsg_NodeExists_Msg"),
                        view.getViewBundleString("strErrorMsg_NodeExists_Title"));
                return;
            }
        }

        //dismiss busy box
        if (box != null) {
            box.dismissBox();
        }

        // Updates the View
        view.mapSuccessfullyCreated();
    }

    /**
     * Opens a previously saved map. <p>Parse the persistent storage and updates
     * the Model ({@link HypertextMap}) and the View
     * ({@link AuthoringToolWindow}) with the previously saved contents. <p>The
     * persistent storage location is entered by the user trough the View. It is
     * a compressed package containing the previously saved contents for the
     * Model and the View.
     *
     * @see #loadModel(String fileName)
     * @see #loadView(String fileName)
     */
    private static synchronized void openMap() {
        String strPath = null;
        try {
            if (model == null) {
                //get last used open map
                strPath = TemporaryConfigManager.getOpenMapLocation();
                String fileName = view.openMapFile(strPath);

                if (fileName == null) {
                    //user canceled the open operation
                    view.showStatus(view.getViewBundleString("strOpeningMapCanceled"));
                    return;
                }
                //updates the last used map
                TemporaryConfigManager.setOpenMapLocation(fileName);

                //loads the model from the file
                loadModel(fileName);

                //loads the view from the file
                loadView(fileName);

                try {
                    //loads the configuration from the file
                    loadConfig(fileName);
                } catch (Exception e) {
                    //loads default application configuration
                    loadDefaultConfig();
                }

                //update previous save location
                mapPreviousSave = fileName;

                view.mapSuccessfullyOpened();
            } else {
                //prompts the user for the action to be taken over the current opened map
                int option = view.showOptionBox(view.getViewBundleString("strOverwriteMap_Msg"),
                        view.getViewBundleString("strOverwriteMap_Title"),
                        view.getViewBundleString("strOverwriteMap_ButtonYES"),
                        view.getViewBundleString("strOverwriteMap_ButtonNO"),
                        view.getViewBundleString("strOverwriteMap_ButtonCANCEL"));

                if (option == view.OPTION_CANCEL) {
                    //user canceled the open map operation
                    view.showStatus(
                            view.getViewBundleString("strOpeningMapCanceled"));
                    return;
                }
                if (option == view.OPTION_YES) {
                    //user chose to save the previously opened map
                    if (!saveMap(mapPreviousSave)) {
                        //save map canceled or failed
                        view.showStatus(
                                view.getViewBundleString("strOpeningMapCanceled"));
                        return;
                    }
                }
                //stops the current graph display
                stopGraphView();

                //stops the description display
                descriptionPreview.unload();
                view.dettachMapDescription();

                //overwrites current model
                model.dispose();

                //get last used open map
                strPath = TemporaryConfigManager.getOpenMapLocation();
                String fileName = view.openMapFile(strPath);
                if (fileName == null) {
                    //user canceled the open operation
                    view.showStatus(
                            view.getViewBundleString("strOpeningMapCanceled"));
                    return;
                }
                //updates the last used map
                TemporaryConfigManager.setOpenMapLocation(fileName);

                loadModel(fileName);

                //overwrites current view
                loadView(fileName);

                //update previous save location
                mapPreviousSave = fileName;

                view.mapSuccessfullyOpened();
            }
        } catch (Exception ex) {
            model = null;
            //stops the view
            stopGraphView();

            //stops the description display
            if (descriptionPreview != null) {
                descriptionPreview.unload();
            }
            if (view != null) {
                view.dettachMapDescription();

                //failed manipulating the map file
                view.showErrorBox(
                        view.getViewBundleString("strErrorMsg_OpenMapFailed_Msg"),
                        view.getViewBundleString("strErrorMsg_OpenMapFailed_Title"));
                view.showStatus(
                        view.getViewBundleString("strOpeningMapFinished_With_Error"));
            }
        }
    }

    /**
     * Parse the persistent storage and updates the Model ({@link HypertextMap})
     * with the previously saved contents.
     *
     * @param fileName The location for the persistent storage for the Model
     */
    private static void loadModel(String fileName) {
        boolean keepGoing = true;

        //instantiate the packager
        PersistencePackager persistPack = new PersistencePackager();
        XMLManipulator persistMng = new XMLManipulator();

        //decompress the model persistence
        try {
            persistPack.setPackagerForDecompress(fileName);
        } catch (Exception e) {
            keepGoing = false;
        }
        try {
            persistPack.extractFromPackage(MODEL_PACKAGE);
        } catch (Exception e) {
            keepGoing = false;
        }

        if (keepGoing) {
            //loads the xml schema from the jar package
            InputStream schemaStream =
                    ResourceManager.getResourceInputStream("HypertextAuthoringToolModel.xsd");
            //raw reads
            InputStreamReader schemaStreamReader = new InputStreamReader(schemaStream);
            BufferedReader bufReader = new BufferedReader(schemaStreamReader);
            String strTmp = null;

            StringBuffer res = new StringBuffer();
            try {
                while ((strTmp = bufReader.readLine()) != null) {
                    res.append(strTmp);
                }
            } catch (Exception e) {
            }
            String modelSchema = res.toString();

            try {
                bufReader.close();
            } catch (Exception e) {
            }

            try {
                //load the model from the persistence
                persistMng.load(MODEL_PACKAGE, modelSchema);
            } catch (Exception e) {
                keepGoing = false;
            }

            if (keepGoing) {
                //get all elements from the persistence
                Object[] elements = persistMng.getAllElements();

                //instantiate a new model concept
                model = new HypertextMap();

                //iterate over the elements and classify them
                int nodeIndex = 0, linkIndex = 0, pathIndex = 0, subNode = 0, perspIndex = 0;
                for (int i = 0; i < elements.length; i++) {
                    //for each class get proper information
                    if (persistMng.getElementName(elements[i]).equals(MODEL_NODE)) {
                        nodeIndex++;
                        model.addNode(persistMng.getElementText(MODEL_NODE, nodeIndex, "title"),
                                persistMng.getElementText(MODEL_NODE, nodeIndex, "url"));
                    }
                    if (persistMng.getElementName(elements[i]).equals(MODEL_LINK)) {
                        linkIndex++;
                        model.addLink(persistMng.getElementText(MODEL_LINK, linkIndex, "sourceNodeTitle"),
                                persistMng.getElementText(MODEL_LINK, linkIndex, "targetNodeTitle"));
                    }
                    if (persistMng.getElementName(elements[i]).equals(MODEL_READING_PATH)) {
                        pathIndex++;
                        HypertextReadingPath newPath = new HypertextReadingPath();
                        String pathName = persistMng.getAttributeText(MODEL_READING_PATH, pathIndex, "readingPathName");
                        newPath.setPathName(pathName);

                        //updates view entries
                        addReadingPathMenuItem(pathName);

                        subNode = 1;
                        HypertextNode node = null;
                        while (persistMng.getInnerAttributeText(MODEL_READING_PATH, pathIndex,
                                MODEL_PATH_NODE, subNode,
                                "pathNodeIndex") != null) {
                            node = new HypertextNode(persistMng.getInnerElementText(MODEL_READING_PATH, pathIndex,
                                    MODEL_PATH_NODE, subNode,
                                    "pathNodeTitle"),
                                    persistMng.getInnerElementText(MODEL_READING_PATH, pathIndex,
                                    MODEL_PATH_NODE, subNode,
                                    "pathNodeURL"));

                            newPath.addNode(node);
                            subNode++;
                        }
                        model.addPath(newPath);
                    }
                    if (persistMng.getElementName(elements[i]).equals("mapDescription")) {
                        //retrieve map description
                        String strSrcCode = persistMng.getElementText("mapDescription");
                        //remove leading "<![CDATA[" and remove trailing "]]>"
                        strSrcCode = strSrcCode.substring(
                                strSrcCode.indexOf("<![CDATA[") + 9,
                                strSrcCode.indexOf("]]>"));

                        if (strSrcCode == null) {
                            //loads the blank html page from the jar package
                            InputStream blankStream =
                                    ResourceManager.getResourceInputStream("blank.html");
                            InputStreamReader inputStreamReader =
                                    new InputStreamReader(blankStream);
                            BufferedReader in = new BufferedReader(inputStreamReader);
                            String line;
                            res.setLength(0);
                            try {
                                do {
                                    line = in.readLine();
                                    if (line != null) {
                                        res.append(line);
                                    }
                                } while (line != null);
                            } catch (Exception e) {
                            }
                            String srcCode = res.toString();

                            try {
                                in.close();
                            } catch (Exception ex) {
                            }
                            try {
                                inputStreamReader.close();
                            } catch (Exception ex) {
                            }
                            try {
                                blankStream.close();
                            } catch (Exception ex) {
                            }

                            //update map description
                            model.setMapDescription(srcCode);
                        } else {
                            //updates the model
                            model.setMapDescription(strSrcCode);
                        }
                    }

                    if (persistMng.getElementName(elements[i]).equals(MODEL_PERSPECTIVE)) {
                        // update element looked at
                        perspIndex++;
                        // retrieve color
                        Color rgb = new Color(
                                Integer.valueOf(
                                persistMng.getElementText(MODEL_PERSPECTIVE_COLOR, perspIndex, "r")).intValue(),
                                Integer.valueOf(
                                persistMng.getElementText(MODEL_PERSPECTIVE_COLOR, perspIndex, "g")).intValue(),
                                Integer.valueOf(
                                persistMng.getElementText(MODEL_PERSPECTIVE_COLOR, perspIndex, "b")).intValue());
                        // create perspective
                        Perspective newPersp =
                                new Perspective(rgb, persistMng.getElementText(MODEL_PERSPECTIVE, perspIndex, "perspectiveLabel"));
                        // resume model
                        model.addPerspective((perspIndex - 1), newPersp);
                    }
                }
            }

            //remove temporary decompressed file
            File tmpFile = new File(MODEL_PACKAGE);
            tmpFile.delete();
        }
    }

    /**
     * Parse the persistent storage and updates the View
     * ({@link AuthoringToolWindow}) with the previously saved contents.
     *
     * @param fileName The location for the persistent storage for the View
     */
    private static void loadView(String fileName) {
        boolean keepGoing = true;

        //instantiate the packager
        PersistencePackager persistPack = new PersistencePackager();
        XMLManipulator persistMng = new XMLManipulator();

        try {
            //decompres the model persistence
            persistPack.setPackagerForDecompress(fileName);
        } catch (Exception e) {
            keepGoing = false;
        }
        try {
            //decompress the view from the package
            persistPack.extractFromPackage(VIEW_PACKAGE);
        } catch (Exception e) {
            keepGoing = false;
        }

        if (keepGoing) {
            //loads the xml schema from the jar package
            InputStream schemaStream =
                    ResourceManager.getResourceInputStream("HypertextAuthoringToolView.xsd");
            //raw reads
            InputStreamReader schemaStreamReader = new InputStreamReader(schemaStream);
            BufferedReader bufReader = new BufferedReader(schemaStreamReader);
            String strTmp = null;

            StringBuffer res = new StringBuffer();
            try {
                while ((strTmp = bufReader.readLine()) != null) {
                    res.append(strTmp);
                }
            } catch (Exception e) {
            }
            String viewSchema = res.toString();

            try {
                bufReader.close();
            } catch (Exception e) {
            }

            try {
                //load the model from the persistence
                persistMng.load(VIEW_PACKAGE, viewSchema);
            } catch (Exception e) {
                keepGoing = false;
            }

            if (keepGoing) {
                //get all elements from the persistence
                Object[] elements = persistMng.getAllElements();

                //initializes the view
                initializeGraphView();

                //instantiate the description preview
                initializeDescriptionPreview();

                //inserting all vertexes
                int vertexIndex = 0, edgeIndex = 0;
                for (int i = 0; i < elements.length; i++) {
                    if (persistMng.getElementName(elements[i]).equals(VIEW_VERTEX)) {
                        vertexIndex++;
                        graphView.addVertex(persistMng.getElementText(VIEW_VERTEX, vertexIndex, "label"),
                                Double.valueOf(
                                persistMng.getAttributeText(VIEW_VERTEX, vertexIndex, "xPosition")).doubleValue(),
                                Double.valueOf(
                                persistMng.getAttributeText(VIEW_VERTEX, vertexIndex, "yPosition")).doubleValue(),
                                Double.valueOf(
                                persistMng.getAttributeText(VIEW_VERTEX, vertexIndex, "width")).doubleValue(),
                                Double.valueOf(
                                persistMng.getAttributeText(VIEW_VERTEX, vertexIndex, "height")).doubleValue(),
                                Integer.valueOf(
                                persistMng.getElementText(VIEW_COLOR, vertexIndex, "r")).intValue(),
                                Integer.valueOf(
                                persistMng.getElementText(VIEW_COLOR, vertexIndex, "g")).intValue(),
                                Integer.valueOf(
                                persistMng.getElementText(VIEW_COLOR, vertexIndex, "b")).intValue());
                    } else {
                        if (persistMng.getElementName(elements[i]).equals(VIEW_EDGE)) {
                            edgeIndex++;
                            graphView.addEdge(persistMng.getElementText(VIEW_EDGE, edgeIndex, "sourceVertexLabel"),
                                    persistMng.getElementText(VIEW_EDGE, edgeIndex, "targetVertexLabel"));
                        }
                    }
                }
                //updates the view with the map description
                String transcoded =
                        HypertextTools.transformHTML_To_XHTML(model.getMapDescription());
                if (transcoded != null) {
                    descriptionPreview.loadDocument(transcoded);
                } else {
                    //Transcoding failed
                    view.showErrorBox(
                            view.getViewBundleString("strErrorMsg_TranscodingFailed_Msg"),
                            view.getViewBundleString("strErrorMsg_TranscodingFailed_Title"));
                }
            }

            //remove temporary decompressed file
            File tmpFile = new File(VIEW_PACKAGE);
            tmpFile.delete();
        }
    }

    /**
     * Parse the persistent storage loads the HypertextAuthoringTool
     * configuration with the previously saved contents.
     *
     * @param fileName The location for the persistent storage for the
     * Configuration
     */
    private static void loadConfig(String fileName) {
        boolean keepGoing = true;

        //instantiate the packager
        PersistencePackager persistPack = new PersistencePackager();
        XMLManipulator persistMng = new XMLManipulator();

        try {
            persistPack.setPackagerForDecompress(fileName);
        } catch (Exception e) {
            keepGoing = false;
        }
        try {
            //decompress the view from the package
            persistPack.extractFromPackage(CONFIG_PACKAGE);
        } catch (Exception e) {
            keepGoing = false;
        }

        if (keepGoing) {
            //loads the xml schema from the jar package
            InputStream schemaStream =
                    ResourceManager.getResourceInputStream("HypertextAuthoringToolConfig.xsd");
            //raw reads
            InputStreamReader schemaStreamReader = new InputStreamReader(schemaStream);
            BufferedReader bufReader = new BufferedReader(schemaStreamReader);
            String strTmp = null;
            StringBuffer res = new StringBuffer();
            try {
                while ((strTmp = bufReader.readLine()) != null) {
                    res.append(strTmp);
                }
            } catch (Exception e) {
            }
            String configSchema = res.toString();

            try {
                bufReader.close();
            } catch (Exception e) {
            }

            try {
                //load the configuration from the persistence
                persistMng.load(CONFIG_PACKAGE, configSchema);
            } catch (Exception e) {
                keepGoing = false;
            }

            if (keepGoing) {
                //get all elements from the persistence
                Object[] elements = persistMng.getAllElements();

                for (int i = 0; i < elements.length; i++) {
                    if (persistMng.getElementName(elements[i]).equals("publisherServerIPAddress")) {
                        //retrieve the publisher server ip address
                        AppConfiguration.setServerIpAddress(
                                persistMng.getElementText("publisherServerIPAddress"));
                    }
                    if (persistMng.getElementName(elements[i]).equals("publisherServerLogin")) {
                        //retrieve the publisher server login
                        AppConfiguration.setServerLogin(
                                persistMng.getElementText("publisherServerLogin"));
                    }
                    if (persistMng.getElementName(elements[i]).equals("publisherServerPath")) {
                        //retrieve the publisher server path
                        AppConfiguration.setServerPath(
                                persistMng.getElementText("publisherServerPath"));
                    }
                }
            }

            //remove temporary decompressed file
            File tmpFile = new File(CONFIG_PACKAGE);
            tmpFile.delete();
        }
    }

    /**
     * Load defaults for the HypertextAuthoringTool configuration. Used for
     * scenarios where no configuration file was found.
     */
    private static void loadDefaultConfig() {
        //retrieve the default publisher server ip address
        AppConfiguration.setServerIpAddress(
                view.getViewBundleString("str_DefaultPublisherServerIpAddress"));
    }

    /**
     * Allow the user to choose where to save the map in edition and then write
     * it to the persistent storage.
     *
     * @return <code>true</code> if save performed <p><code>false</code> if an
     * error occurred or the user canceled the operation
     */
    private static synchronized boolean saveMapAs() {
        String strPath = null;
        //informes that a map is being saved
        view.showStatus(view.getViewBundleString("strSavingMap"));

        //use the last saved map as reference destination
        strPath = TemporaryConfigManager.getSaveMapLocation();
        //asks the output location to the user
        String strLocation = view.showSaveFileDialog(strPath);
        //user canceled the save operation
        if (strLocation == null) {
            view.showStatus(view.getViewBundleString("strSavingMapCanceled"));
            return false;
        }
        //updates the last used location
        TemporaryConfigManager.setSaveMapLocation(strLocation);

        if (saveMap(strLocation)) {
            //keep track of the previous save location
            mapPreviousSave = strLocation;
            return true;
        } else {
            //map save operation failed
            return false;
        }
    }

    /**
     * Saves the current map in edition to the persistent storage. This method
     * comits the Model, the View and also the Application Configuration all at
     * the same time. This is done to ensure the compressed file consistency.
     *
     * @param strPath The fully qualified path to store the map
     * @return <code>true</code> if save performed <p><code>false</code> if an
     * error occurred
     */
    protected static synchronized boolean saveMap(String strPath) {
        try {
            //create the output package
            PersistencePackager persistPack = new PersistencePackager();
            persistPack.setPackagerForCompress(strPath);

            //instantiate the persistence manipulator
            XMLManipulator persistMap = new XMLManipulator();
            persistMap.create("hypertextMap");

            //persist Model
            for (Iterator iterMap = model.getThisMapNodes().iterator(); iterMap.hasNext();) {
                HypertextNode persistNode = (HypertextNode) (iterMap.next());
                //add a node to the persistence
                persistMap.addElement(MODEL_NODE,
                        null, null, null, null);
                persistMap.addElement("title",
                        persistNode.getNodeTitle(), null, null, MODEL_NODE);
                persistMap.addElement("url",
                        persistNode.getNodeURL(), null, null, MODEL_NODE);
            }

            for (Iterator iterMap = model.getThisMapLinks().iterator(); iterMap.hasNext();) {
                HypertextLink persistLink = (HypertextLink) (iterMap.next());
                //add a link to the persistence
                persistMap.addElement(MODEL_LINK,
                        null, null, null, null);
                persistMap.addElement("sourceNodeTitle",
                        persistLink.getSource().getNodeTitle(), null, null, MODEL_LINK);
                persistMap.addElement("targetNodeTitle",
                        persistLink.getTarget().getNodeTitle(), null, null, MODEL_LINK);
            }

            for (Iterator iterMap = model.getThisMapReadingPaths().iterator(); iterMap.hasNext();) {
                HypertextReadingPath persistPath = (HypertextReadingPath) (iterMap.next());
                HypertextNode[] path = persistPath.getPath();

                if (path == null) {
                    continue;
                }

                //add this reading path to the persistence
                persistMap.addElement(MODEL_READING_PATH,
                        null, "readingPathName", persistPath.getPathName(), null);

                for (int i = 0; i < path.length; i++) {
                    HypertextNode persistNode = path[i];

                    persistMap.addElement(MODEL_PATH_NODE,
                            null, "pathNodeIndex", Integer.toString(i), MODEL_READING_PATH);
                    persistMap.addElement("pathNodeTitle",
                            persistNode.getNodeTitle(), null, null, MODEL_PATH_NODE);
                    persistMap.addElement("pathNodeURL",
                            persistNode.getNodeURL(), null, null, MODEL_PATH_NODE);
                }
            }

            //add the map description to the persistence
            String preparedOutput = "<![CDATA["
                    + model.getMapDescription()
                    + "]]>";
            persistMap.addElement("mapDescription",
                    preparedOutput, null, null, "hypertextMap");

            //persist Perspectives
            for (Iterator iterMap = model.getThisMapPerspectives().iterator(); iterMap.hasNext();) {
                Perspective persistPersp = (Perspective) (iterMap.next());
                Color rgb = (Color) persistPersp.getColor();

                //add a perspective to the persistence
                persistMap.addElement(MODEL_PERSPECTIVE,
                        null, null, null, null);
                persistMap.addElement("perspectiveLabel",
                        persistPersp.getLabel(), null, null, MODEL_PERSPECTIVE);
                persistMap.addElement(MODEL_PERSPECTIVE_COLOR, null, null, null, MODEL_PERSPECTIVE);
                persistMap.addElement("r", Integer.toString(rgb.getRed()), null, null, MODEL_PERSPECTIVE_COLOR);
                persistMap.addElement("g", Integer.toString(rgb.getGreen()), null, null, MODEL_PERSPECTIVE_COLOR);
                persistMap.addElement("b", Integer.toString(rgb.getBlue()), null, null, MODEL_PERSPECTIVE_COLOR);
            }

            //commit Model persistence
            persistMap.persistChanges(MODEL_PACKAGE);
            //add to packager
            persistPack.addToPackage(MODEL_PACKAGE, MODEL_PACKAGE);

            //persist View
            persistMap.create("hypertextGraph");

            Object[] viewVertexes = graphView.getThisGraphVertexes();
            for (int i = 0; i < viewVertexes.length; i++) {
                Color rgb = (Color) graphView.getThisVertexColor(viewVertexes[i]);

                persistMap.addElement(VIEW_VERTEX, null, "xPosition", Double.toString(graphView.getThisVertexPosition_X(viewVertexes[i])), null);
                persistMap.addAttribute(VIEW_VERTEX, "yPosition", Double.toString(graphView.getThisVertexPosition_Y(viewVertexes[i])));
                persistMap.addAttribute(VIEW_VERTEX, "width", Double.toString(graphView.getThisVertexWidth(viewVertexes[i])));
                persistMap.addAttribute(VIEW_VERTEX, "height", Double.toString(graphView.getThisVertexHeight(viewVertexes[i])));
                persistMap.addElement("label", graphView.getThisVertexLabel(viewVertexes[i]), null, null, VIEW_VERTEX);
                persistMap.addElement(VIEW_COLOR, null, null, null, VIEW_VERTEX);
                persistMap.addElement("r", Integer.toString(rgb.getRed()), null, null, VIEW_COLOR);
                persistMap.addElement("g", Integer.toString(rgb.getGreen()), null, null, VIEW_COLOR);
                persistMap.addElement("b", Integer.toString(rgb.getBlue()), null, null, VIEW_COLOR);
            }

            Object[] viewEdges = graphView.getThisGraphEdges();
            for (int i = 0; i < viewEdges.length; i++) {
                persistMap.addElement(VIEW_EDGE, null, null, null, null);
                persistMap.addElement("sourceVertexLabel", graphView.getThisEdgeSourceVertexLabel(viewEdges[i]), null, null, VIEW_EDGE);
                persistMap.addElement("targetVertexLabel", graphView.getThisEdgeTargetVertexLabel(viewEdges[i]), null, null, VIEW_EDGE);
            }
            //commit View persistence
            persistMap.persistChanges(VIEW_PACKAGE);
            //add to packager
            persistPack.addToPackage(VIEW_PACKAGE, VIEW_PACKAGE);

            //persist Application Configuration
            persistMap.create("toolConfig");
            //add the publisher server ip address to the persistence
            persistMap.addElement("publisherServerIPAddress",
                    AppConfiguration.getServerIpAddress(), null, null, "toolConfig");
            persistMap.addElement("publisherServerLogin",
                    AppConfiguration.getServerLogin(), null, null, "toolConfig");
            persistMap.addElement("publisherServerPath",
                    AppConfiguration.getServerPath(), null, null, "toolConfig");
            //commit application configuration persistence
            persistMap.persistChanges(CONFIG_PACKAGE);
            //add to packager
            persistPack.addToPackage(CONFIG_PACKAGE, CONFIG_PACKAGE);

            //close packager
            persistPack.closePackage();

            //remove temporary files
            File modelFile = new File(MODEL_PACKAGE);
            File viewFile = new File(VIEW_PACKAGE);
            File configFile = new File(CONFIG_PACKAGE);
            modelFile.delete();
            viewFile.delete();
            configFile.delete();

            view.showStatus(
                    view.getViewBundleString("strStatus_UserSavedTheMap")
                    + " "
                    + strPath);

            return true;
        } catch (Exception e) {
            //failed manipulating the map file
            view.showErrorBox(view.getViewBundleString("strErrorMsg_SaveMapFailed_Msg"),
                    view.getViewBundleString("strErrorMsg_SaveMapFailed_Title"));
            view.showStatus(view.getViewBundleString("strSavingMapFinished_With_Error"));
            return false;
        }
    }

    /**
     * Add a Node ({@link HypertextNode}) to the Model ({@link HypertextMap})
     * and updates the View ({@link AuthoringToolWindow}). Currently the tool
     * doesn't support CSS. When adding a Node to the map the tool will strip
     * out its CSS styles in case they are found. <p>If the Node doesn't have a
     * title element or its value is blank ask the user to insert one.
     *
     * @see #addNodeTitle(String strNodeURL, HypertextManipulator htManip)
     */
    private static synchronized void addNode() {
        String nodePath = null;
        String strPath = null;
        String nodeTitle = null;
        HypertextManipulator titleSetter = null;

        //informes that a node is being added
        view.showStatus(view.getViewBundleString("strInsertingNode"));

        //get last used add node location
        strPath = TemporaryConfigManager.getAddNodeLocation();
        //allows the user to choose a file
        nodePath = view.openNodeFile(strPath);
        if (nodePath == null) {
            //user canceled the node insertion
            view.showStatus(view.getViewBundleString("strStatus_UserCanceledAddNode"));
            return;
        }
        //updates last used location
        TemporaryConfigManager.setAddNodeLocation(nodePath);

        BusyBox box = new BusyBox(view.getViewBundleString("strBusy_Title"),
                view.getViewBundleString("strBusy_Msg_Loading_XHTML"));
        box.showBox();

        if (nodePath.indexOf(".xhtml") > 0) {
            titleSetter = new XHTMLManipulator();
        } else {
            titleSetter = new HTMLManipulator();
        }

        if (!titleSetter.load(nodePath)) {
            if (box != null) {
                box.dismissBox();
            }
            //failed manipulating the hypertext
            view.showErrorBox(view.getViewBundleString("strErrorMsg_InsertNodeLoadFailed_Msg"),
                    view.getViewBundleString("strErrorMsg_InsertNodeLoadFailed_Title"));
            return;
        }

        //Currently we don't support CSS. Strip out CSS from the Hypertext file
        titleSetter.removeCSS(nodePath);

        //get title element
        nodeTitle = titleSetter.getTitle();

        //dismiss busy box
        if (box != null) {
            box.dismissBox();
        }

        if (nodeTitle == null) {
            //informes that the node insertion finished with an error
            view.showStatus(view.getViewBundleString("strStatus_AddingNodeFinished_With_Error"));

            //not an hypertext
            view.showErrorBox(view.getViewBundleString("strErrorMsg_NotXML_Msg"),
                    view.getViewBundleString("strErrorMsg_NotXML_Title"));
        } else {
            //it is an hypertext
            if (nodeTitle.length() != 0) {
                //this hypertext file has its <title>
                String nodeURL = HypertextTools.filePathToURL(nodePath);
                addNode(nodeTitle, nodeURL);
            } else {
                //node doesn't contain a title
                addNodeTitle(nodePath, titleSetter);
            }
        }
    }

    /**
     * Add a Node ({@link HypertextNode}) to the Model ({@link HypertextMap})
     * and updates the View ({@link AuthoringToolWindow}).
     *
     * @param nodeTitle a title to describe this hypertextnode.
     * @param nodeURL the URL that stores this hypertextnode
     * @return the newly created HypertextNode added to the model and displayed
     * in the view <p><code>null</code> in case this HypertextNode already
     * exists in the map or an error occurred
     */
    protected static HypertextNode addNode(String nodeTitle, String nodeURL) {
        HypertextNode newNode = model.addNode(nodeTitle, nodeURL);

        if (newNode != null) {
            //informes that the node insertion finished
            view.showStatus(view.getViewBundleString("strStatus_AddingNodeFinished"));

            //updates the view
            graphView.addVertex(nodeTitle);
            return newNode;
        } else {
            //node exists
            view.showErrorBox(view.getViewBundleString("strErrorMsg_NodeExists_Msg"),
                    view.getViewBundleString("strErrorMsg_NodeExists_Title"));
            return null;
        }
    }

    /**
     * Add a title for the {@link HypertextNode} to the Model
     * ({@link HypertextMap}) and updates the View
     * ({@link AuthoringToolWindow}). #param nodePath a String containing the
     * path to the node that needs a title to be inserted in the HypertextMap
     */
    private static void addNodeTitle(final String nodePath,
            final HypertextManipulator titleSetter) {
        if (insertNodeTitleWindow == null) {
            insertNodeTitleWindow = new InsertNodeTitleWindow(nodePath);

            insertNodeTitleWindow.setFormWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    insertNodeTitleWindow.finish();
                    insertNodeTitleWindow = null;

                    //user canceled the node insertion
                    view.showStatus(view.getViewBundleString("strStatus_UserCanceledAddNode"));
                }
            });

            insertNodeTitleWindow.setCancelButtonListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    insertNodeTitleWindow.finish();
                    insertNodeTitleWindow = null;

                    //user canceled the node insertion
                    view.showStatus(view.getViewBundleString("strStatus_UserCanceledAddNode"));
                }
            });

            insertNodeTitleWindow.setOkButtonListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    //asks for a node title
                    String nodeName = insertNodeTitleWindow.getNodeTitle();

                    if (nodeName != null) {
                        //updates Hypertext file
                        if (titleSetter.setTitle(nodeName)) {
                            String nodeURL = HypertextTools.filePathToURL(nodePath);
                            if (model.addNode(nodeName, nodeURL) != null) {
                                //persist the updated file
                                titleSetter.persistChanges(nodePath);

                                //informes that the node insertion finished
                                view.showStatus(view.getViewBundleString("strStatus_AddingNodeFinished"));

                                //updates the view
                                graphView.addVertex(nodeName);
                            } else {
                                //node exists
                                view.showErrorBox(view.getViewBundleString("strErrorMsg_NodeExists_Msg"),
                                        view.getViewBundleString("strErrorMsg_NodeExists_Title"));
                            }
                        } else {
                            //failed manipulating the hypertext
                            view.showErrorBox(view.getViewBundleString("strErrorMsg_InsertNodeFailed_Msg"),
                                    view.getViewBundleString("strErrorMsg_InsertNodeFailed_Title"));
                        }

                        //finish using this window
                        insertNodeTitleWindow.finish();
                        insertNodeTitleWindow = null;
                    } else {
                        //title empty again
                        view.showErrorBox(view.getViewBundleString("strInsertTitle_ErrorMsg"),
                                view.getViewBundleString("strInsertTitle_ErrorTitle"));
                    }
                }
            });
        } else {
            insertNodeTitleWindow.toFront();
        }
    }

    /**
     * Remove a Node ({@link HypertextNode}) from the Model
     * ({@link HypertextMap}) and updates the View
     * ({@link AuthoringToolWindow}).
     */
    private static synchronized void removeNode() {
        //remove a node from the map
        model.removeNode(graphView.getSelectedVertexString());

        //updates the view
        graphView.removeSelectedVertex();
    }

    /**
     * Invokes the View ({@link AuthoringToolWindow}) to show a preview of the
     * contents for the currently selected Node ({@link HypertextNode}).
     */
    private static synchronized void showPreview() {
        final String strTitle = graphView.getSelectedVertexString();
        final String strURL = model.getNode(strTitle).getNodeURL();
        final String strPath = HypertextTools.urlToFilePath(strURL);

        if (strURL.indexOf(".htm") > 0) {
            //need transcoding
            BusyBox box = new BusyBox(view.getViewBundleString("strBusy_Title"),
                    view.getViewBundleString("strBusy_Msg_HTML_XHTML"));
            box.showBox();

            //loads the HTML file in the HTMLManager to retrieve
            //its source code
            HTMLManagerFactory htmlFactory = new HTMLManagerFactory();
            HTMLManager htmlManager = htmlFactory.create();
            htmlManager.loadHTMLFile(strPath);

            //process HTML to XHTML transcoding
            String transcoded =
                    HypertextTools.transformHTML_To_XHTML(
                    htmlManager.getHTMLCode());
            //finished lengthy operation
            box.dismissBox();
            if (transcoded == null) {
                //Transcoding failed
                view.showErrorBox(
                        view.getViewBundleString("strErrorMsg_TranscodingFailed_Msg"),
                        view.getViewBundleString("strErrorMsg_TranscodingFailed_Title"));
            }

            //render the transcoded XHTML
            if (!view.showPreview(strTitle,
                    view.getViewBundleString("strAbout_ButtonClose"),
                    transcoded)) {
                //failed rendering XHTML
                view.showErrorBox(view.getViewBundleString("strErrorMsg_PreviewFailed_Msg")
                        + "\n\n" + strPath,
                        view.getViewBundleString("strErrorMsg_PreviewFailed_Title"));
            }
        } else {
            //"native" XHTML.
            try {
                XHTMLManagerFactory xMngFactory = new XHTMLManagerFactory();
                XHTMLManager xMng = xMngFactory.create();

                //loads the xhtml page
                xMng.load(strPath);

                String xhtmlContents = xMng.getXHTMLContents();

                if (!view.showPreview(strTitle,
                        view.getViewBundleString("strAbout_ButtonClose"),
                        xhtmlContents)) {
                    view.showErrorBox(view.getViewBundleString("strErrorMsg_PreviewFailed_Msg")
                            + "\n\n" + strPath,
                            view.getViewBundleString("strErrorMsg_PreviewFailed_Title"));
                }
            } catch (Exception e) {
                view.showErrorBox(view.getViewBundleString("strErrorMsg_PreviewFailed_Msg")
                        + "\n\n" + strURL,
                        view.getViewBundleString("strErrorMsg_PreviewFailed_Title"));
            }
        }
    }

    /**
     * Updates the View ({@link AuthoringToolWindow}) to reflect the addition in
     * the Model ({@link HypertextMap}) of a ReadingPath
     * ({@link HypertextReadingPath}). <p>Install an action listener to dispatch
     * the secondary Controller ({@link ReadingPathEditorController}), so the
     * user can edit the added ReadingPath when needed.
     *
     * @param pathName The name property for the ReadingPath being added to the
     * Model.
     */
    protected static void addReadingPathMenuItem(String pathName) {
        view.addReadingPathMenuItem(pathName, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                editReadingPath(e);
            }
        });
    }

    /**
     * Updates the View ({@link AuthoringToolWindow}) to reflect modifications
     * made in a ReadingPath ({@link HypertextReadingPath}) stored in the Model
     * ({@link HypertextMap}).
     */
    protected static void updateReadingPathMenuItems() {
        view.clearReadingPathMenuItems();
        for (Iterator iterMap = model.getThisMapReadingPaths().iterator(); iterMap.hasNext();) {
            HypertextReadingPath readingPath = (HypertextReadingPath) (iterMap.next());
            addReadingPathMenuItem(readingPath.getPathName());
        }
    }

    /**
     * Invokes a secondary Controller ({@link ReadingPathEditorController}), so
     * the user can edit an existing ReadingPath ({@link HypertextReadingPath})
     * or create a new one.
     *
     * @param pathToBeEdited Existing ReadingPath to be edited.
     */
    private static synchronized void dispatchReadingPathEditor(HypertextReadingPath pathToBeEdited) {
        if (pathControl == null) {
            pathControl = new ReadingPathEditorController(model,
                    pathToBeEdited);

            //starts the controller
            ReadingPathEditorController.main(null);
        } else {
            ReadingPathEditorController.bringViewToFront();
        }
    }

    /**
     * Retrieves from the Model ({@link HypertextMap}) a ReadingPath
     * ({@link HypertextReadingPath}) that the user selected in the View
     * ({@link AuthoringToolWindow}).
     *
     * @see #dispatchReadingPathEditor(HypertextReadingPath pathToBeEdited)
     */
    private static void editReadingPath(ActionEvent e) {
        HypertextReadingPath currentPath = null;

        currentPath = model.getHypertextReadingPath(e.getActionCommand());
        dispatchReadingPathEditor(currentPath);
    }

    /**
     * Invokes the View ({@link AuthoringToolWindow}) to show the contents of
     * the AboutBox from the application's jar package.
     */
    private static synchronized void showAboutBox() {
        view.showStatus(view.getViewBundleString("strShowingAboutBox"));

        //loads the xhtml about page from the jar package
        InputStream aboutStream =
                ResourceManager.getResourceInputStream(
                view.getViewBundleString("about_file"));
        //raw reads
        InputStreamReader aboutStreamReader = new InputStreamReader(aboutStream);
        BufferedReader bufReader = new BufferedReader(aboutStreamReader);
        String strTmp = null;

        StringBuffer res = new StringBuffer();
        try {
            while ((strTmp = bufReader.readLine()) != null) {
                res.append(strTmp);
            }
        } catch (Exception ex) {
        }
        String xhtmlContents = res.toString();

        try {
            aboutStream.close();
        } catch (Exception ex) {
        }
        try {
            aboutStreamReader.close();
        } catch (Exception ex) {
        }
        try {
            bufReader.close();
        } catch (Exception ex) {
        }

        if (xhtmlContents.length() > 0) {
            view.showPreview(
                    view.getViewBundleString("strAbout_Title"),
                    view.getViewBundleString("strAbout_ButtonClose"),
                    xhtmlContents);

            view.showStatus(view.getViewBundleString("strShowingAboutBoxFinished"));
        } else {
            //failed rendering XHTML
            view.showErrorBox(
                    view.getViewBundleString("strErrorMsg_PreviewFailed_Msg")
                    + "\n\nabout.xhtml",
                    view.getViewBundleString("strErrorMsg_PreviewFailed_Title"));
        }
    }

    /**
     * Invokes the View ({@link AuthoringToolWindow}) to show the contents of
     * the LicenseBox from the application's jar package.
     */
    private static synchronized void showLicenseBox() {
        view.showStatus(view.getViewBundleString("strShowingAboutBox"));

        //loads the xhtml about page from the jar package
        InputStream aboutStream =
                ResourceManager.getResourceInputStream(
                view.getViewBundleString("license_file"));
        //raw reads
        InputStreamReader aboutStreamReader = new InputStreamReader(aboutStream);
        BufferedReader bufReader = new BufferedReader(aboutStreamReader);
        String strTmp = null;

        StringBuffer res = new StringBuffer();
        try {
            while ((strTmp = bufReader.readLine()) != null) {
                res.append(strTmp);
            }
        } catch (Exception ex) {
        }
        String xhtmlContents = res.toString();

        try {
            aboutStream.close();
        } catch (Exception ex) {
        }
        try {
            aboutStreamReader.close();
        } catch (Exception ex) {
        }
        try {
            bufReader.close();
        } catch (Exception ex) {
        }

        if (xhtmlContents.length() > 0) {
            view.showPreview(
                    view.getViewBundleString("strAbout_Title"),
                    view.getViewBundleString("strAbout_ButtonClose"),
                    xhtmlContents);

            view.showStatus(view.getViewBundleString("strShowingAboutBoxFinished"));
        } else {
            //failed rendering XHTML
            view.showErrorBox(
                    view.getViewBundleString("strErrorMsg_PreviewFailed_Msg")
                    + "\n\nabout.xhtml",
                    view.getViewBundleString("strErrorMsg_PreviewFailed_Title"));
        }
    }

    /**
     * Gracefully finishes the main application
     */
    private static synchronized void finish() {
        //Do finishing tasks and gracefully exits the application
        try {
            model.dispose();
        } catch (Exception e) {
        }

        try {
            stopGraphView();
        } catch (Exception e) {
        }

        try {
            descriptionPreview.unload();
        } catch (Exception e) {
        }

        try {
            view.dettachMapDescription();
        } catch (Exception e) {
        }

        try {
            view.dispose();
        } catch (Exception e) {
        }

        model = null;
        view = null;
        pathControl = null;
        controller = null;

        System.exit(0);
    }

    /**
     * Invokes a secondary Controller ({@link NodeEditorController}), so the
     * user can edit an existing Node ({@link HypertextNode}) or create a new
     * one.
     *
     * @param nodeToBeEdited Existent Node to be edited
     */
    private static synchronized void dispatchNodeEditorWindow(HypertextNode nodeToBeEdited) {
        // Verify if the edition is attempted in the "Original" node
        if (nodeToBeEdited != null) {
            if (nodeToBeEdited.getNodeType() == HypertextNode.NODE_TYPE_READY_ONLY) {
                //can't allow this node edition
                view.showErrorBox(
                        view.getViewBundleString("strErrorMsg_Original_Is_Read_Only_Msg"),
                        view.getViewBundleString("strErrorMsg_Original_Is_Read_Only_Title"));

                showPreview();

                return;
            }
        }

        if (nodeEditControl == null) {
            nodeEditControl =
                    new NodeEditorController(nodeToBeEdited,
                    controller,
                    model,
                    graphView,
                    (view.getUIState() == view.UI_STATE_MAP_HAS_NODES) ? false : true);
            //starts the controller
            NodeEditorController.main(null);
        } else {
            NodeEditorController.bringViewToFront();
        }
    }

    /**
     * Invokes a secondary Controller ({@link NodeEditorController}), so the
     * user can edit a piece of HTML source code.
     *
     * @param nodeHTMLSrcCode A fragment of HTML source code to be edited
     */
    private static synchronized void dispatchNodeEditorWindowForHTMLSrcCode(
            String nodeHTMLSrcCode) {
        if (nodeEditControl == null) {
            nodeEditControl =
                    new NodeEditorController(nodeHTMLSrcCode,
                    controller,
                    model,
                    graphView);
            //starts the controller
            NodeEditorController.main(null);
        } else {
            NodeEditorController.bringViewToFront();
        }

    }

    /**
     * Allows the secondary {@link NodeEditorController} to notify the main
     * application controller that it finished executing.
     */
    protected static void nodeEditControlFinished() {
        nodeEditControl = null;
    }

    /**
     * Allows the secondary {@link ReadingPathEditorController} to notify the
     * main application controller that it finished executing.
     */
    protected static void readingPathEditControlFinished() {
        pathControl = null;
    }

    /**
     * Allows the secondary {@link PublisherWindowController} to notify the main
     * application controller that it finished executing.
     */
    protected static void publishControlFinished() {
        publishControl = null;
    }

    /**
     * Allows the secondary {@link NodeEditorController} to notify the main
     * application controller that it updated the {@link HypertextNode}
     * attributes.
     *
     * @param oldNode a String containing the old Vertex Label
     * @param newNode a String containing the new Vertex Label
     */
    protected void updateNodeView(String oldNode, String newNode) {
        graphView.updateVertexLabel(oldNode, newNode);
    }

    /**
     * Receive update messages from the {@link Observable} object and updates
     * the state of the {@link Observer} as necessary.
     *
     * @param obj the Observable object being observed
     * @param arg an Object containing data received from the Observable object
     */
    @Override
    public void update(Observable obj, Object arg) {
        Integer evtNum = (Integer) arg;

        if (evtNum.equals(GraphView.EVT_VERTEX_ADDED)) {
            view.showStatus(view.getViewBundleString("strStatus_NodeAdded"));
            view.uiModifyState(view.UI_STATE_MAP_HAS_NODE);
            return;
        }

        if (evtNum.equals(GraphView.EVT_ALL_VERTEXES_REMOVED)) {
            //no more Vertexes to be removed
            view.uiModifyState(view.UI_STATE_MAP_WITHOUT_NODES);
            return;
        }

        if (evtNum.equals(GraphView.EVT_ALL_EDGES_REMOVED)) {
            //no more Egdes to be removed
            view.uiModifyState(view.UI_STATE_READ_PATH_UNNAVAILABLE);
            return;
        }

        if (evtNum.equals(GraphView.EVT_SELECTION_CLEARED)) {
            //disallow node deletion
            view.uiModifyState(view.UI_STATE_MAP_CANT_REMOVE_NODES);
            return;
        }

        if (evtNum.equals(GraphView.EVT_VERTEX_MAY_LINK)) {
            //allow link creation
            view.uiModifyState(view.UI_STATE_MAP_HAS_NODES);
            return;
        }

        if (evtNum.equals(GraphView.EVT_VERTEX_CANT_LINK)) {
            //allow node deletion
            view.uiModifyState(view.UI_STATE_MAP_HAS_NODE);
            return;
        }

        if (evtNum.equals(GraphView.EVT_EDGE_SELECTED)) {
            view.uiModifyState(view.UI_STATE_MAP_CANT_REMOVE_NODES);
            return;
        }

        if (evtNum.equals(GraphView.EVT_RIGHT_MOUSE_BUTTON_IN_VERTEX)) {
            Point p = (Point) graphView.getLastClickedVertexPoint();
            //show pop-up menu with edge options
            view.showNodePopMenu((Component) graphView.getDisplayable(),
                    p.x,
                    p.y);
            return;
        }

        if (evtNum.equals(GraphView.EVT_DOUBLE_CLICK_VERTEX)) {
            //edit current double clicked node
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    String strTitle = graphView.getSelectedVertexString();
                    HypertextNode node = model.getNode(strTitle);
                    dispatchNodeEditorWindow(node);
                }
            });
            return;
        }
    }

    /**
     * Perform setup operations and display the description preview in the user
     * interface using an (@link XHTMLView) component.
     */
    private static void initializeDescriptionPreview() {
        XHTMLViewFactory xFactory = new XHTMLViewFactory();
        descriptionPreview =
                xFactory.create();
        descriptionPreview.setDimensions(100, 100);
        view.attachMapDescription((Component) descriptionPreview.getDisplayable());

        //listen for mouse events in the description XHTMLView
        descriptionPreview.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                // if left mouse button double clicks
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (e.getClickCount() == 2) {
                        //update flag for receiving data from other thread
                        editingDescription = true;

                        //dispatch editor window
                        dispatchNodeEditorWindowForHTMLSrcCode(
                                model.getMapDescription());
                    }
                }
            }
        });
    }

    /**
     * Receives the event from the secondary Controller
     * ({@link NodeEditorController}) that the user wants to save a piece of
     * HTML source code.
     *
     * @param strHTMLSrcCode An edited fragment of HTML source code
     */
    public static void nodeEditorSavedEditingSourceCode(String strHTMLSrcCode) {
        if (editingDescription) {
            //finished editing description
            editingDescription = false;

            //updates model
            model.setMapDescription(strHTMLSrcCode);
            String transcoded =
                    HypertextTools.transformHTML_To_XHTML(model.getMapDescription());
            if (transcoded != null) {
                //updates view
                descriptionPreview.loadDocument(transcoded);
            } else {
                //Transcoding failed
                view.showErrorBox(
                        view.getViewBundleString("strErrorMsg_TranscodingFailed_Msg"),
                        view.getViewBundleString("strErrorMsg_TranscodingFailed_Title"));
            }
        }
    }

    /**
     * Invokes a secondary Controller ({@link PublisherWindowController}), so
     * the user publish this HypertextMap.
     */
    private static synchronized void dispatchPublisherWindow(boolean local) {
        if (publishControl != null) {
            publishControl.finish();
        }
        publishControl = new PublisherWindowController(model, local);
        //starts the controller
        PublisherWindowController.main(null);
    }

    /**
     * Allow the user to choose a destination for the merge based on the given
     * source node. Merge two Nodes({@link model.HypertextNode}) and updates the
     * Main Model({@link model.HypertextMap}) and the Main
     * View({@link view.AuthoringToolWindow}).
     *
     * @param srcNode the source Node for the merge.
     * @see view.MergeNodeWindow
     */
    private static void mergeNode(final String srcNode) {
        if (mergeNodeWindow == null) {
            //is this node already in the map?
            if (model.getNode(srcNode) != null) {
                mergeNodeWindow = new MergeNodeWindow(srcNode,
                        model.getAvailableLinkDestinations(srcNode));

                mergeNodeWindow.setOKButtonEnabled(true);
            } else {
                //this node is not in the map yet
                view.showErrorBox(view.getViewBundleString("strErrMsg_NodeNotInTheMap"),
                        view.getViewBundleString("strErrTit_NodeNotInTheMap"));
                return;
            }

            mergeNodeWindow.setFormWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    mergeNodeWindow.finish();
                    mergeNodeWindow = null;
                }
            });

            mergeNodeWindow.setCancelButtonListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    mergeNodeWindow.finish();
                    mergeNodeWindow = null;
                }
            });

            mergeNodeWindow.setOkButtonListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    //get the merge destination
                    String selectedTargetLabel =
                            mergeNodeWindow.getSelectedChoice();

                    if (selectedTargetLabel != null) {
                        // Instantiate the HTMLManager
                        HTMLManagerFactory htmlFactory = new HTMLManagerFactory();
                        HTMLManager htmlManager = htmlFactory.create();

                        // Load source node in HTMLManager
                        String sourcePath = model.getNode(srcNode).getNodeURL();
                        sourcePath = HypertextTools.urlToFilePath(sourcePath);
                        htmlManager.loadHTMLFile(sourcePath);

                        // Retrieve source node content
                        String sourceContent = htmlManager.getHTMLEditorText();

                        // Load target node in HTMLManager
                        String targetPath =
                                model.getNode(selectedTargetLabel).getNodeURL();
                        targetPath = HypertextTools.urlToFilePath(targetPath);
                        htmlManager.loadHTMLFile(targetPath);

                        // Retrieve target node content
                        String targetContent = htmlManager.getHTMLEditorText();

                        // add source node content to the target node end
                        htmlManager.setHTMLEditorText(targetContent + sourceContent);

                        // commit changes to target node
                        htmlManager.saveHTMLFile(targetPath);

                        // remove old node
                        model.removeNode(srcNode);

                        //updates the view
                        graphView.removeSelectedVertex();
                    }

                    //finish using this window
                    mergeNodeWindow.finish();
                    mergeNodeWindow = null;
                }
            });
        } else {
            mergeNodeWindow.setOKButtonEnabled(false);
            mergeNodeWindow.toFront();
        }
    }

    /**
     * Allow the user to edit this Map({@link model.HypertextMap}) perspectives.
     *
     * @see view.PerspectiveEditorWindow
     */
    private static void editPerspectives() {
        if (perspectiveEditor == null) {
            // Retrieve this model's perspectives
            List mapPersp = model.getThisMapPerspectives();
            int pivot = 0;
            Object[][] data = null;

            // Populate raw array using model's perspectives. It will be used
            // in the editor
            if (mapPersp != null) {
                data = new Object[mapPersp.size()][2];

                for (Iterator iterPersp = mapPersp.iterator(); iterPersp.hasNext();) {
                    Perspective persp = (Perspective) (iterPersp.next());
                    data[pivot][0] = persp.getLabel();
                    data[pivot][1] = persp.getColor();
                    pivot++;
                }
            }

            // Instantiate editor
            perspectiveEditor = new PerspectiveEditorWindow(data);
            perspectiveEditor.setTableModelListener(new TableModelListener() {

                @Override
                public void tableChanged(TableModelEvent e) {
                    // Prepare to update model
                    int row = e.getFirstRow();
                    TableModel tabModel = (TableModel) e.getSource();

                    // Assume column = 0 for label and column = 1 for color
                    String perspLabel = (String) tabModel.getValueAt(row, 0);
                    Object perspColor = tabModel.getValueAt(row, 1);
                    Perspective newPersp = new Perspective(perspColor, perspLabel);

                    if (e.getType() == TableModelEvent.INSERT) {
                        // Add new perspective
                        model.addPerspective(row, newPersp);
                    } else if (e.getType() == TableModelEvent.UPDATE) {
                        // Update existing perspective
                        model.updatePerspective(row, newPersp);
                    }
                }
            });

            perspectiveEditor.setFormWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    perspectiveEditor.finish();
                    perspectiveEditor = null;
                }
            });

            perspectiveEditor.setOkButtonListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    //finish using this window
                    perspectiveEditor.finish();
                    perspectiveEditor = null;
                }
            });

            perspectiveEditor.setAppendButtonListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    //request to append empty new row
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            perspectiveEditor.appendNewRow();
                        }
                    });
                }
            });

            perspectiveEditor.setTableMouselListener(new MouseAdapter() {

                private void maybeShowPopup(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        perspectiveEditor.showTablePopMenu(e.getX(), e.getY());
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    maybeShowPopup(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    maybeShowPopup(e);
                }
            });

            perspectiveEditor.setPopMenuRemoveListener(
                    new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            //request to append empty new row
                            SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    int row = perspectiveEditor.removeSelectedObject();
                                    if (row != -1) {
                                        //remove perspective from model
                                        model.removePerspective(row);
                                    }
                                }
                            });
                        }
                    });

            //display perspective editor
            perspectiveEditor.setVisible(true);
        } else {
            perspectiveEditor.toFront();
        }
    }
}
