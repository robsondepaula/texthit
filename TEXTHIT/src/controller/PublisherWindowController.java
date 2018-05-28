package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultFormatterFactory;
import model.HypertextMap;
import model.HypertextNode;
import model.HypertextReadingPath;
import resources.AppConfiguration;
import resources.ResourceManager;
import resources.TemporaryConfigManager;
import util.Communicator;
import util.FTPCommunicator;
import util.HTMLManipulator;
import util.HypertextManipulator;
import util.HypertextTools;
import util.PersistencePackager;
import util.RegexFormatter;
import util.XHTMLManipulator;
import util.xhtml.XHTMLManager;
import util.xhtml.XHTMLManagerFactory;
import util.xml.XMLManager;
import util.xml.XMLManagerFactory;
import view.FilesManipulationDialog;
import view.PublisherWindow;

/**
 * This class plays the Controller role in the Map Publisher secondary
 * application. It drives a View ({@link PublisherWindow}) and a Model
 * ({@link HypertextMap}). <p>Interaction is made with the main application
 * Controller ({@link AuthoringToolWindowController}) to coordinate the queries
 * in the main Model ({@link model.HypertextMap}).
 */
public class PublisherWindowController {

    private static PublisherWindow view = null;
    private static HypertextMap model = null;
    private static Thread publisherThread = null;
    private static File tempDirectory = null;
    private final static String TEMP_DIRECTORY = "publisher_temp_dir";
    private final static String MAP_PROJ_FILE = "map_proj.map";
    private final static String INDEX_FILE = "index.xhtml";
    private final static String WEB_DESC_DIR = "WEB-INF";
    private final static String WEB_DESC_FILE = "web.xml";
    private static String warFile = null;
    private final static String BODY_STR = "body";
    private static boolean isLocal = false;
    //error bag
    private final static String ERR_WAR_FILE = "WAR file can't be empty";
    private final static String ERR_TRANSFER_FAILED = "File transfer failed";

    /**
     * Constructs the secondary application Controller class.
     *
     * @param mainControl Main application Controller
     * @param model Main application Model
     */
    public PublisherWindowController(HypertextMap model, boolean local) {
        PublisherWindowController.model = model;
        PublisherWindowController.isLocal = local;
    }

    /**
     * Runs the secondary application. <p>All interaction made by the user
     * through the View ({@link PublisherWindow}) is captured by action
     * listeners.
     */
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                view = new PublisherWindow();

                if (!isLocal) {
                    view.enableServerControls();

                    //instantiate the Server Address Field filter
                    initializeServerAddressInputField();

                    //instantiate the Server Access String Field filter
                    initializeServerAccessInputField();

                    //initialize the server address field
                    view.setServerAddress(AppConfiguration.getServerIpAddress());
                    view.setServerLogin(AppConfiguration.getServerLogin());
                    view.setAccessString(AppConfiguration.getServerPath());
                }

                //create and set the listeners to the view
                view.setOkButtonListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        updateAppConfiguration();
                        //dismiss this secondary application
                        finish();
                    }
                });

                view.setCancelButtonListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (publisherThread != null) {
                            //interrupt the publisher thread
                            publisherThread.interrupt();
                            //nullify
                            publisherThread = null;
                            //finished atomic operation for the publisher thread
                            view.enableButtonOK();
                        }
                    }
                });

                view.setPublishButtonListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        updateAppConfiguration();
                        //begin atomic operation for the publishing thread
                        view.disableButtonOK();
                        //publication processing
                        publishHypertextMap();
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

    private static void updateAppConfiguration() {
        if (!isLocal) {
            AppConfiguration.setServerIpAddress(
                    view.getServerAddress());
            AppConfiguration.setServerLogin(
                    view.getServerLogin());
            AppConfiguration.setServerPath(
                    view.getAccessString());
        }
    }

    /**
     * Perform setup operations in the server address input field. The input
     * will be filtered using a ({@link RegexFormatter}) allowing only "valid"
     * IP addresses input (eg.: 172.168.0.2).
     */
    private static void initializeServerAddressInputField() {
        //Uses a Regex formatter to filter only "valid" IP addresses
        String ipRangeRegex =
                "(?:[0-9]|[1-9][0-9]|1[0-9][0-9]|2(?:[0-4][0-9]|5[0-5]))";
        Pattern ipPattern = Pattern.compile("^(?:" + ipRangeRegex
                + "\\.){3}" + ipRangeRegex + "$");
        RegexFormatter ipFormatter = new RegexFormatter(ipPattern);

        view.setServerAddressRegexFormatter(
                new DefaultFormatterFactory(ipFormatter));
    }

    /**
     * Perform setup operations in the server access string input field. The
     * input will be filtered using a ({@link RegexFormatter}) allowing only
     * "valid" access locations.
     */
    private static void initializeServerAccessInputField() {
        //Uses a Regex formatter to filter only "valid" access strings
        Pattern addressPattern = Pattern.compile("[a-z_-]+");
        RegexFormatter addressFormatter = new RegexFormatter(addressPattern);

        view.setAccessStringRegexFormatter(
                new DefaultFormatterFactory(addressFormatter));
    }

    /**
     * Ignites the publication processing. This processing is comprised of the
     * following tasks: <ul> <li>Copying the HypertextNode files to a temporary
     * location;</li> <li>Creating the Reading Path files in the temporary
     * location;</li> <li>Processing all the files in the temporary location to
     * make all HypertextLinks point to a relative location;</li> <li>Create the
     * "web.xml" web application descriptor in the temporary location
     * (subdirectory "/WEB-INF") with the HypertextMap values;</li> <li>Pack all
     * the files in the temporary location in a "war" (Web Application Archive)
     * file;</li> <li>Instantiate a communicator and transfer the newly created
     * "war" file to the configured publishing server;</li> <li>Perform
     * temporary location cleanup.</li> </ul> Progress of this time consuming
     * task is reported to the user by the user interface.
     */
    private static synchronized void publishHypertextMap() {
        publisherThread = new Thread() {

            @Override
            public void run() {
                //publication is an atomic operation, doesn't allow user quiting
                //without finishing the processing or canceling it
                view.disableButtonOK();

                try {
                    //create temporary directory
                    tempDirectory = new File(TEMP_DIRECTORY);
                    if (tempDirectory.exists()) {
                        //remove old uncleaned directory
                        deleteDirectory(tempDirectory);
                    }
                    if (!tempDirectory.mkdir()) {
                        throw new Exception();
                    }

                    if (!isLocal) {
                        //retrieve info from the user interface
                        warFile = view.getAccessString();
                        if (warFile == null) {
                            throw new Exception(ERR_WAR_FILE);
                        }
                        warFile += ".war";
                    }

                    publishHypertextMap_CopyMapNodes();
                    publishHypertextMap_GenerateReadingPaths();
                    publishHypertextMap_CreateWebDescriptor();
                    publishHypertextMap_ProcessHyperlinks();


                    if (!isLocal) {
                        publishHypertextMap_GeneratePackageFile(warFile);
                        publishHypertextMap_SendWARFileToServer();
                    } else {
                        //use the last publish map as reference destination
                        String strPath = TemporaryConfigManager.getLocalPublish();
                        //asks the output location to the user
                        String strLocation = showSaveFileDialog(strPath);
                        //user canceled the save operation
                        if (strLocation == null) {
                            finish();
                        }
                        //updates the last used location
                        TemporaryConfigManager.setLocalPublish(strLocation);
                        // let user choose were to put the file
                        String strFileName = strLocation.substring(strLocation.lastIndexOf(PersistencePackager.fileSeparator), strLocation.length());
                        publishHypertextMap_GeneratePackageFile(strFileName);
                        //deliver the package to the user
                        copyFile(tempDirectory.getAbsolutePath()
                                + PersistencePackager.fileSeparator + strFileName,
                                strLocation);
                    }
                    publishHypertextMap_PerformCleanup();
                } catch (Exception e) {
                    //retrieve exception cause
                    String msg = e.getMessage();

                    if (msg.equals(ERR_WAR_FILE)) {
                        //minor error occured
                        view.showErrorBox(
                                view.getViewBundleString(
                                "str_PublisherWindow_Publish_Process_Error_Msg_1"),
                                view.getViewBundleString(
                                "str_PublisherWindow_Publish_Process_Error_Title"));
                        return;
                    }

                    if (msg.equals(ERR_TRANSFER_FAILED)) {
                        //minor error occured
                        view.showErrorBox(
                                view.getViewBundleString(
                                "str_PublisherWindow_Publish_Process_Error_Msg_2"),
                                view.getViewBundleString(
                                "str_PublisherWindow_Publish_Process_Error_Title"));
                        return;
                    }

                    //user may have canceled the operation
                    if (publisherThread != null) {
                        //severe error occured
                        view.showErrorBox(
                                view.getViewBundleString(
                                "str_PublisherWindow_Publish_Process_Error_Msg"),
                                view.getViewBundleString(
                                "str_PublisherWindow_Publish_Process_Error_Title"));

                        //exit the controller
                        finish();
                    }
                }

                //finished processing
                view.enableButtonOK();
            }
        };
        publisherThread.start();
    }

    private static String showSaveFileDialog(String strPath) {
        String[] strExtension = {"zip"};

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

        File fSaveFile = FilesManipulationDialog.showFileChooser(view,
                view.getViewBundleString("strHypertextMapSaveFileChooser_Title"),
                view.getViewBundleString("strHypertextMapSaveFileChooser_OkButton"),
                view.getViewBundleString("strHypertextMapSaveFileChooser_FilterText") + strExtensionLabel,
                strExtension,
                strPath,
                false);

        try {
            return fSaveFile.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    private static void copyLocalFile(String localPath) {
    }
    /*
     *Publishing step 1 - Copy map nodes.
     *Copies every hypertext fragment in the hypertextmap to the temporary
     *location.
     *@throws Exception if the operation failed
     */

    private static synchronized void publishHypertextMap_CopyMapNodes()
            throws Exception {
        view.setStatusText(
                view.getViewBundleString(
                "str_PublisherWindow_Publish_Process_Msg_1"));
        //get the hypertextmap nodes
        List nodes = model.getThisMapNodes();
        //setup progress display
        view.setupProgressBar(0, nodes.size());
        int step = 0;
        String strNodePath;
        String strNodeFileName;
        for (Iterator iterMap = nodes.iterator(); iterMap.hasNext();) {
            HypertextNode persistNode = (HypertextNode) (iterMap.next());
            //retrieve hypertextnode storage path
            strNodePath =
                    HypertextTools.urlToFilePath(persistNode.getNodeURL());
            //retrive filename from node storage path
            strNodeFileName =
                    strNodePath.substring(strNodePath.lastIndexOf(PersistencePackager.fileSeparator));

            //copy hypertextmap node to temporary location
            copyFile(strNodePath,
                    tempDirectory.getAbsolutePath() + strNodeFileName);

            //update progress
            view.updateProgressBar(step++);
        }
        //update progress
        view.updateProgressBar(nodes.size());
    }

    /*
     *Copy files from the given origin to the given destination.
     *@param strIn a String containing the absolute path for the copy origin
     *@param strOut a String containing the absolute path for the copy destination
     *@throws Exception if the copy operation failed
     */
    private static void copyFile(String strIn, String strOut) {
        File in = new File(strIn);
        File out = new File(strOut);

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(in);
        } catch (Exception e) {
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(out);
        } catch (Exception e) {
        }

        byte[] buf = new byte[1024];
        int i;
        try {
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (Exception e) {
        }

        try {
            fis.close();
        } catch (Exception e) {
        }
        try {
            fos.close();
        } catch (Exception e) {
        }
    }

    /*
     *Remove this directory recursively deleting any files or subdirectories
     *stored in it.
     *@param fileDir a File object representing the topmost directory to be
     *removed
     */
    private static boolean deleteDirectory(File fileDir) {
        if (fileDir.exists()) {
            File[] files = fileDir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return fileDir.delete();
    }

    /*
     *Publishing step 2 - Generate reading path files.
     *Inspect the hypertextmap and generate its reading path files as needed.
     *Only sane hypertext reading paths will have its files generated.
     *@see HypertextReadingPath#validatePath()
     *@throws Exception if the operation failed
     */
    private static synchronized void publishHypertextMap_GenerateReadingPaths()
            throws Exception {
        view.setStatusText(
                view.getViewBundleString(
                "str_PublisherWindow_Publish_Process_Msg_2"));

        //get the hypertextmap read paths
        List readPaths = model.getThisMapReadingPaths();
        //setup progress display
        view.setupProgressBar(0, readPaths.size());
        int step = 0;
        for (Iterator iterMap = readPaths.iterator(); iterMap.hasNext();) {
            HypertextReadingPath persistPath = (HypertextReadingPath) (iterMap.next());

            //generate reading path in the temporary location
            generateReadingPathFile(persistPath,
                    tempDirectory.getAbsolutePath()
                    + "/"
                    + persistPath.getPathName()
                    + ".xhtml");

            //update progress
            view.updateProgressBar(step++);
        }
        //update progress
        view.updateProgressBar(readPaths.size());
    }

    /**
     * Creates a XHTML file that displays the current Model
     * ({@link HypertextReadingPath}). Uses a XHTML template stored inside the
     * application jar package.
     *
     * @param readPath the HypertextReadingPath which will produce a XHTML file
     * displaying its contents
     * @param filePath the absolute path the output the generated file
     * @throws Exception if the operation failed
     */
    private static void generateReadingPathFile(
            final HypertextReadingPath readPath,
            final String filePath) {
        //loads the xhtml template
        InputStream xhtmlTemplateStream =
                ResourceManager.getResourceInputStream(
                view.getViewBundleString("strReadPath_TemplateFile"));
        //raw reads
        BufferedReader bufReader = new BufferedReader(
                new InputStreamReader(xhtmlTemplateStream));
        String strTmp;

        StringBuffer res = new StringBuffer();
        try {
            while ((strTmp = bufReader.readLine()) != null) {
                res.append(strTmp);
            }
        } catch (Exception e) {
        }
        String xhtmlTemplate = res.toString();

        try {
            bufReader.close();
        } catch (Exception e) {
        }

        //instantiate the XHTML Manager
        XHTMLManagerFactory xMngFactory = new XHTMLManagerFactory();
        XHTMLManager xMng = xMngFactory.create();

        //loads the xhtml template
        if (!xMng.loadFromString(xhtmlTemplate)) {
            //couldn't find the template file
            view.showErrorBox(view.getViewBundleString("str_PublisherWindow_Publish_Process_Error_Msg_3"),
                    view.getViewBundleString("templateFile_ErrorTitle"));
            finish();
        }

        HypertextNode[] nodes = readPath.getPath();
        if (nodes != null) {
            for (int i = 0; i < nodes.length; i++) {
                xMng.addElement("li",
                        "",
                        null,
                        null,
                        "ol");

                xMng.addElement("a",
                        nodes[i].getNodeTitle(),
                        "href",
                        nodes[i].getNodeURL(),
                        "li");
            }
        }
        xMng.persistChanges(filePath);
    }

    /*
     *Publishing step 4 - Process hyperlinks in files.
     *Process hyperlinks in all the files in the temporary storage to make its
     *hyperlinks point to the current directory.
     *@throws Exception if the operation failed
     */
    private static synchronized void publishHypertextMap_ProcessHyperlinks()
            throws Exception {
        view.setStatusText(
                view.getViewBundleString(
                "str_PublisherWindow_Publish_Process_Msg_3"));
        //prepare language abstraction
        HypertextManipulator htManip = null;
        String curFile;
        //get all the files in the temporary storage
        File[] files = tempDirectory.listFiles();

        //setup progress display
        view.setupProgressBar(0, files.length);
        int step = 0;
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isDirectory()) {
                //retrieve current file path
                curFile = files[i].getAbsolutePath();

                //instantiate the proper language adapter
                if (curFile.indexOf(".xhtml") > 0) {
                    htManip = new XHTMLManipulator();
                } else {
                    if (curFile.indexOf(".html") > 0) {
                        htManip = new HTMLManipulator();
                    } else {
                        //file should not be processed
                        htManip = null;
                    }
                }

                if (htManip != null) {
                    //load the file in the manipulator
                    htManip.load(curFile);
                    //make all the hyperlinks point to the current directory
                    htManip.removeParentDirsFromHyperlinks(curFile);
                }

                //update progress
                view.updateProgressBar(step++);
            }
        }

        //update progress
        view.updateProgressBar(files.length);
    }

    /*
     *Publishing step 3 - Create web descriptor.
     *Commit the current HypertextMap project file to the temporary location,
     *write the index.html HypertextMap entry point page and create a web
     *descriptor file (web.xml).
     *<p>Uses XML templates stored inside the application jar package
     * ({@link ResourceManager}) to generate the web descriptor file.
     *@throws Exception if the operation failed
     */
    private static synchronized void publishHypertextMap_CreateWebDescriptor()
            throws Exception {
        view.setStatusText(
                view.getViewBundleString(
                "str_PublisherWindow_Publish_Process_Msg_4"));
        //setup progress display
        view.setupProgressBar(0, 3);

        //commits the current HypertextMap project
        AuthoringToolWindowController.saveMap(tempDirectory.getAbsolutePath()
                + "/"
                + MAP_PROJ_FILE);
        view.updateProgressBar(1);

        //generate hypertext map index file
        generateHypertextMapIndexFile();
        view.updateProgressBar(2);

        //create web descriptor
        generateWebDescriptorFile();
        view.updateProgressBar(3);
    }

    /**
     * Creates a XHTML file to be used as entry point for the HypertextMap. Uses
     * a XHTML template stored inside the application jar package.
     *
     * @throws Exception if the operation failed
     */
    private static void generateHypertextMapIndexFile() {
        InputStream xhtmlTemplateStream =
                ResourceManager.getResourceInputStream(
                view.getViewBundleString("index_file"));

        //raw reads
        BufferedReader bufReader = new BufferedReader(
                new InputStreamReader(xhtmlTemplateStream));
        String strTmp;

        StringBuffer res = new StringBuffer();
        try {
            while ((strTmp = bufReader.readLine()) != null) {
                res.append(strTmp);
            }
        } catch (Exception e) {
        }
        String xhtmlTemplate = res.toString();

        try {
            bufReader.close();
        } catch (Exception e) {
        }

        //instantiate the XHTML Manager
        XHTMLManagerFactory xMngFactory = new XHTMLManagerFactory();
        XHTMLManager xMng = xMngFactory.create();

        //loads the xhtml template
        if (!xMng.loadFromString(xhtmlTemplate)) {
            //couldn't find the template file
            view.showErrorBox(view.getViewBundleString("str_PublisherWindow_Publish_Process_Error_Msg_3"),
                    view.getViewBundleString("templateFile_ErrorTitle"));
            finish();
        }

        //write HypertextMapDescription <body> if available
        String strCode = HypertextTools.transformHTML_To_XHTML(model.getMapDescription());
        //parse through the source code to find the description
        Pattern p = Pattern.compile("<body(.*?)</body>",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        Matcher m = p.matcher(strCode);
        boolean result = m.find();

        if (result) {
            //get the content
            String element = m.group();
            element = element.substring(element.indexOf(">") + 1,
                    element.indexOf("</body>"));

            element = element.substring(element.indexOf("<p style=\"margin-top: 0\">") + 25,
                    element.indexOf("</p>"));

            if (element != null) {
                //add description
                xMng.addElement("p",
                        element.trim(),
                        null,
                        null,
                        BODY_STR);
            }
        }

        List readPaths = model.getThisMapReadingPaths();
        //check if this hypertextmap has readingpaths
        if (readPaths.size() > 0) {
            HypertextReadingPath path;

            //begin ordered list
            xMng.addElement("ol",
                    "",
                    null,
                    null,
                    BODY_STR);

            for (Iterator iterPaths = readPaths.iterator(); iterPaths.hasNext();) {
                path = (HypertextReadingPath) iterPaths.next();
                xMng.addElement("li",
                        "",
                        null,
                        null,
                        "ol");

                xMng.addElement("a",
                        path.getPathName(),
                        "href",
                        tempDirectory.getAbsolutePath()
                        + "/"
                        + path.getPathName()
                        + ".xhtml",
                        "li");
            }
        }

        //write the root nodes also as a sugestion
        xMng.addElement("p",
                view.getViewBundleString(
                "str_PublisherWindow_Index_File_RootNodesAsSugestion"),
                null,
                null,
                BODY_STR);
        //retrieve all root nodes
        String[] rootNodes = model.getRootNodes();
        //add a new ordered list element
        xMng.addElement("ol",
                "",
                null,
                null,
                BODY_STR);
        //iterate through root nodes and add them to list
        String fileName;
        for (int i = 0; i < rootNodes.length; i++) {
            xMng.addElement("li",
                    "",
                    null,
                    null,
                    "ol");

            fileName = model.getNode(rootNodes[i]).getNodeURL();
            fileName = fileName.substring(fileName.lastIndexOf("/"));
            xMng.addElement("a",
                    rootNodes[i],
                    "href", tempDirectory.getAbsolutePath() + "/" + fileName,
                    "li");
        }

        xMng.persistChanges(tempDirectory.getAbsolutePath()
                + "/"
                + INDEX_FILE);
    }

    /**
     * Creates a web descriptor XML file for the HypertextMap. Uses a XML
     * template stored inside the application jar package.
     *
     * @throws Exception if the operation failed
     */
    private static void generateWebDescriptorFile() {

        InputStream xmlTemplateStream =
                ResourceManager.getResourceInputStream(
                view.getViewBundleString("web_desc_file"));

        //raw reads
        BufferedReader bufReader = new BufferedReader(
                new InputStreamReader(xmlTemplateStream));
        String strTmp;

        StringBuffer res = new StringBuffer();
        try {
            while ((strTmp = bufReader.readLine()) != null) {
                res.append(strTmp);
            }
        } catch (Exception e) {
        }
        String xmlTemplate = res.toString();

        try {
            bufReader.close();
        } catch (Exception e) {
        }

        //instantiate the XHTML Manager
        XMLManagerFactory xMngFactory = new XMLManagerFactory();
        XMLManager xMng = xMngFactory.create();

        //loads the xhtml template
        if (!xMng.loadFromString(xmlTemplate)) {
            //couldn't find the template file
            view.showErrorBox(view.getViewBundleString("str_PublisherWindow_Publish_Process_Error_Msg_3"),
                    view.getViewBundleString("templateFile_ErrorTitle"));
            finish();
        }

        //write HypertextMapDescription <body>
        String strCode = model.getMapDescription();
        //parse through the source code to find the description
        Pattern p = Pattern.compile("<body(.*?)</body>",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        Matcher m = p.matcher(strCode);
        boolean result = m.find();

        if (result) {
            //get the content
            String element = m.group();
            element = element.substring(element.indexOf(">") + 1,
                    element.indexOf("</body>"));

            if (element != null) {
                //add description
                xMng.setElementText("display-name", element.trim());
            }
        }

        xMng.setElementText("welcome-file", INDEX_FILE);

        //create web desc dir
        File webDescDir = new File(tempDirectory, WEB_DESC_DIR);
        if (!webDescDir.mkdir()) {
            return;
        }

        xMng.persistChanges(webDescDir.getAbsolutePath()
                + "/"
                + WEB_DESC_FILE);
    }

    /*
     *Publishing Step 5 - Generate package file
     *@throws Exception if the operation failed
     */
    private static synchronized void publishHypertextMap_GeneratePackageFile(String packageName)
            throws Exception {
        view.setStatusText(
                view.getViewBundleString(
                "str_PublisherWindow_Publish_Process_Msg_5"));

        File[] files = tempDirectory.listFiles();

        //create the output package
        PersistencePackager persistPack = new PersistencePackager();
        persistPack.setPackagerForCompress(tempDirectory.getAbsolutePath()
                + "/"
                + packageName);

        //setup progress display
        view.setupProgressBar(0, files.length);

        //compress all files
        int step = 0;
        addToPackageFile(packageName, persistPack, files, step);

        //update progress
        view.updateProgressBar(step++);

        //close packager
        persistPack.closePackage();
    }

    private static void addToPackageFile(String packageName, PersistencePackager persistPack,
            File[] files,
            int step) throws Exception {

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                addToPackageFile(packageName, persistPack, files[i].listFiles(), step);
            } else {
                //skip package file
                if (!(files[i].getName().equals(packageName))) {
                    //retrieve entry name relative to the source package
                    String relativePath = tempDirectory.toURI().relativize(files[i].toURI()).getPath();

                    //add to package
                    persistPack.addToPackage(files[i].getAbsolutePath(),
                            relativePath);
                    //update progress
                    view.updateProgressBar(step++);
                }
            }
        }
    }

    /*
     *Publishing Step 6 - Send war file to server
     *@throws Exception if the operation failed
     */
    private static synchronized void publishHypertextMap_SendWARFileToServer()
            throws Exception {
        view.setStatusText(
                view.getViewBundleString(
                "str_PublisherWindow_Publish_Process_Msg_6"));
        //setup progress display
        view.setupProgressBar(0, 2);

        //instantiate a communicator using the input parameters retrieved
        //from the view
        Communicator com = new FTPCommunicator(view.getServerAddress(),
                view.getServerLogin(),
                view.getServerPass());

        view.updateProgressBar(1);

        boolean res = com.sendFile(tempDirectory.getAbsolutePath()
                + "/" + warFile);

        if (!res) {
            throw new Exception(ERR_TRANSFER_FAILED);
        }

        //finished using the communicator
        view.updateProgressBar(2);
        com.finish();
    }

    /*
     *7-Perform cleanup
     *@throws Exception if the operation failed
     */
    private static synchronized void publishHypertextMap_PerformCleanup()
            throws Exception {
        view.setStatusText(
                view.getViewBundleString(
                "str_PublisherWindow_Publish_Process_Msg_7"));

        //setup progress display
        view.setupProgressBar(0, 1);

        //remove old uncleaned directory
        deleteDirectory(tempDirectory);

        //finished using the communicator
        view.updateProgressBar(1);
    }

    /**
     * Attempts to bring this Controllers View to front. Leaving it in front of
     * all displayable components been presented in the display area.
     */
    protected static void bringViewToFront() {
        view.toFront();
    }

    /**
     * Gracefully finishes the secondary application View
     */
    public static void finish() {
        view.setVisible(false);
        view.finish();
        AuthoringToolWindowController.publishControlFinished();
    }
}
