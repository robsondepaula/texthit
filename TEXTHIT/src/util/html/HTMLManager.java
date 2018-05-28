package util.html;

/**
 * Abstract HTMLManager.
 */
public abstract class HTMLManager {

    /**
     * Enable controlling application to listen for the Mouse events of the
     * underlying MHTMLMAnager API.
     */
    public abstract void addMouseListener(java.awt.event.MouseAdapter mouseAdapter);

    /**
     * Enable controlling application to listen for the Document events of the
     * underlying MHTMLMAnager API.
     */
    public abstract void addDocumentListener(javax.swing.event.DocumentListener docListener);

    /**
     * Retrieves the HTMLManager displayable component used to edit HTML pages.
     *
     * @return the HTMLManager displayable component.
     */
    public abstract Object getDisplayable();

    /**
     * Creates an empty HTML page to be edited in the HTMLManager.
     */
    public abstract void createEmptyHTML();

    /**
     * Requests the HTMLManager to save the HTML file under edition.
     *
     * @param strPath absolute path to save the HTML file
     * @return <code>true</code> if the file was saved <p><code>false</code> if
     * an error occurred
     */
    public abstract boolean saveHTMLFile(String strPath);

    /**
     * Requests the HTMLManager to load an HTML file.
     *
     * @param strPath absolute path to the HTML file
     * @return <code>true</code> if the file was loaded <p><code>false</code> if
     * an error occurred
     */
    public abstract boolean loadHTMLFile(String strPath);

    /**
     * Requests the HTMLManager to load an HTML source code.
     *
     * @param strInput the HTML source code
     * @return <code>true</code> if the source code was loaded
     * <p><code>false</code> if an error occurred
     */
    public abstract boolean loadHTMLFromString(String strInput);

    /**
     * Requests the HTMLManager to set a new value for the HTML title tag.
     *
     * @param title the new value for the title tag
     * @return <code>true</code> if the title was set <p><code>false</code> if
     * an error occurred
     */
    public abstract boolean setHTMLTitle(String title);

    /**
     * Requests the HTMLManager to get the HTML title.
     */
    public abstract String getHTMLTitle();

    /**
     * Retrieves the HTML source code being edited in the HTMLManager.
     *
     * @return a String containing the HTML source code being edited
     * <p><code>null</code> if an error occurred
     */
    public abstract String getHTMLCode();

    /**
     * Overwrites the HTML source code being edited in the HTMLManager.
     *
     * @param srcCode a String containing the HTML source code that will
     * overwrite the currently code in edition
     */
    public abstract void setHTMLCode(String srcCode);

    /**
     * Retrieves the currently selected plain text from the underlying
     * HTMLManager API.
     *
     * @param deleteSelection a flag to indicate if the selection should be
     * deleted while the text is retrieve; like the "Cut" operation to clipboard
     * @return a String containing the currently selected plain text
     * <p><code>null</code> if an error occurred
     */
    public abstract String getHTMLEditorSelectedText(boolean deleteSelection);

    /**
     * Retrieves the plain text being edited in the HTMLManager.
     *
     * @return a String containing the plain text being edited
     * <p><code>null</code> if an error occurred
     */
    public abstract String getHTMLEditorText();

    /**
     * Overwrites the plain text being edited in the HTMLManager.
     *
     * @param strText a String containing the plain text that will overwrite the
     * currently text in edition
     */
    public abstract void setHTMLEditorText(String strText);

    /**
     * Retrieves the default popUp Menu from the underlying HTMLManager API, so
     * it can be customized.
     *
     * @return a javax.swing.JPopupMenu containing the default HTMLManger popUp
     * Menu. <p><code>null</code> if an error occurred
     */
    public abstract javax.swing.JPopupMenu getHTMLEditorPopUpMenu();

    /**
     * Replaces the currently selected plain text to an Hyperlink with the given
     * text in the underlying HTMLManager API.
     *
     * @param strText the given text for the Hyperlink
     * @param strURL the URL for the Hyperlink
     * @param newWindow <p><code>true</code> create HTML code that will open
     * this Hyperlink in a new browser Window <p><code>false</code> will open
     * this Hyperlink in the current Window
     * @return <p>the HTML tag representing the newly created Hyperlink
     * <p><code>null</code> in case an error occurred
     */
    public abstract String makeTextAnHyperlink(String strText,
            String strURL,
            boolean newWindow);
}