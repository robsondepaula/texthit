package util.xhtml;

/**
 * Abstract XHTMLManager.
 */
public abstract class XHTMLManager {

    /**
     * Create a new XHTML element in the in memory source code.
     *
     * @param strName the name for a new element
     * @param strText the value for the new element
     * @param strAttributeName the name for a new attribute
     * @param strAttributeText the value for the new attribute
     * @param strParentElement the name for the parent element of this new
     * element (<code>null</code>if no parent element)
     */
    public abstract void addElement(String strName,
            String strText,
            String strAttributeName,
            String strAttributeText,
            String strParentElement);

    /**
     * Set the value for the
     * <code>title</code> element in the XHTML source code.
     *
     * @return <code>true</code> if the operation succeeded
     * <p><code>false</code> if an error occurred
     */
    public abstract boolean setTitle(String xhtmlTitle);

    /**
     * Search the XHTML source code for the
     * <code>title</code> element and retrieves its contents.
     *
     * @return a String containing the title <p>an empty String if the title is
     * empty <p>a null value if an error occurred
     */
    public abstract String getTitle();

    /**
     * Commit to the persistent storage the in memory manipulations done to the
     * XHTML source code.
     *
     * @return <code>true</code> if the operation succeeded
     * <p><code>false</code> if an error occurred
     */
    public abstract boolean persistChanges(String xhtmlPath);

    /**
     * Parse the contents of the XHTML file and loads its structure in memory.
     *
     * @param xhtmlPath the absolute path to XHTML file
     * @return <code>true</code> if the XHTML was loaded <p><code>false</code>
     * if the operation failed
     */
    public abstract boolean load(String xhtmlPath);

    /**
     * Parse the contents of a String containing an XHTML source code and
     * creates its structure in memory.
     *
     * @param strInput a String containing an XHTML source code
     * @return <code>true</code> if the XHTML was created <p><code>false</code>
     * if the operation failed
     */
    public abstract boolean loadFromString(String strInput);

    /**
     * Retrieve the loaded document contents.
     *
     * @return a String containing the document contents <p><code>null</code> if
     * the operation failed
     */
    public abstract String getXHTMLContents();

    /**
     * Convert XHTML to HTML code.
     *
     * @param strSrcPath the absolute path to the source XHTML file
     * @return a String containing the transformed HTML code
     * <p><code>null</code> if an error occurred
     */
    public abstract String transformXHTML_To_HTML(String strSrcPath);

    /**
     * Transcode HTML code to XHTML.
     *
     * @param strSrcCode a String containing the HTML sourcecode
     * @return a String containing the transcoded XHTML code
     * <p><code>null</code> if an error occurred
     */
    public abstract String transformHTML_To_XHTML(String strSrcCode);
}
