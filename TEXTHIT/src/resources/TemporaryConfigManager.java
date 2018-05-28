package resources;

import java.util.prefs.Preferences;

/**
 * This class represents the application temporary configuration.
 */
public class TemporaryConfigManager {

    private static Preferences prefs = Preferences.userRoot().node(TemporaryConfigManager.class.getName());
    private static final String OPEN_MAP = "OPEN_MAP";
    private static final String SAVE_MAP = "SAVE_MAP";
    private static final String ADD_EXISTING_NODE = "ADD_EXISTING_NODE";
    private static final String LOCAL_PUBLISH = "LOCAL_PUBLISH";
    private static final String TEXT_TO_CONVERT = "TEXT_TO_CONVERT";
    private static final String NODE_EDITOR_PATH = "NODE_EDITOR_PATH";
    public static final String EMPTY_STRING = new String();

    public static String getNodeEditorPath() {
        return prefs.get(NODE_EDITOR_PATH, EMPTY_STRING);
    }

    public static void setNodeEditorPath(String editorPath) {
        prefs.put(NODE_EDITOR_PATH, editorPath);
    }

    public static String getTextToConvert() {
        return prefs.get(TEXT_TO_CONVERT, EMPTY_STRING);
    }

    public static void setTextToConvert(String textToConvert) {
        prefs.put(TEXT_TO_CONVERT, textToConvert);
    }

    public static String getLocalPublish() {
        return prefs.get(LOCAL_PUBLISH, EMPTY_STRING);
    }

    public static void setLocalPublish(String lastLocalPublish) {
        prefs.put(LOCAL_PUBLISH, lastLocalPublish);
    }

    /**
     * Configures the last Open Map location.
     *
     * @param newLastOpenMap a String containing a file system location
     */
    public static void setOpenMapLocation(String newLastOpenMap) {
        prefs.put(OPEN_MAP, newLastOpenMap);
    }

    /**
     * Retrieves the last Open Map location.
     *
     * @return a String containing a file system location
     */
    public static String getOpenMapLocation() {
        return prefs.get(OPEN_MAP, EMPTY_STRING);
    }

    /**
     * Configures the last Save Map location.
     *
     * @param newLastSaveMap a String containing a file system location
     */
    public static void setSaveMapLocation(String newLastSaveMap) {
        prefs.put(SAVE_MAP, newLastSaveMap);
    }

    /**
     * Retrieves the last Save Map location.
     *
     * @return a String containing a file system location
     */
    public static String getSaveMapLocation() {
        return prefs.get(SAVE_MAP, EMPTY_STRING);
    }

    /**
     * Configures the last Add Node location.
     *
     * @param newLastAddNode a String containing a file system location
     */
    public static void setAddNodeLocation(String newLastAddNode) {
        prefs.put(ADD_EXISTING_NODE, newLastAddNode);
    }

    /**
     * Retrieves the last Add Node location.
     *
     * @return a String containing a file system location
     */
    public static String getAddNodeLocation() {
        return prefs.get(ADD_EXISTING_NODE, EMPTY_STRING);
    }
}
