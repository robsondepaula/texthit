package util;

/**
 * Abstract class for HypertextManipulation
 */
public abstract class HypertextManipulator {

    /**
     * Parse the contents of the hypertext file and loads its structure in
     * memory.
     *
     * @param strPath the absolute path to hypertext file
     * @return <code>true</code> if the hypertext was loaded
     * <p><code>false</code> if the operation failed
     */
    public abstract boolean load(String strPath);

    /**
     * Search the hypertext source code for the
     * <code>title</code> element and retrieves its contents.
     *
     * @return a String containing the title <p>an empty String if the title is
     * empty <p>a null value if an error occurred
     */
    public abstract String getTitle();

    /**
     * Set the value for the
     * <code>title</code> element in the hypertext source code.
     *
     * @return <code>true</code> if the operation succeeded
     * <p><code>false</code> if an error occurred
     */
    public abstract boolean setTitle(String hypertextTitle);

    /**
     * Commit to the persistent storage the in memory manipulations done to the
     * hypertext source code.
     *
     * @return <code>true</code> if the operation succeeded
     * <p><code>false</code> if an error occurred
     */
    public abstract boolean persistChanges(String hypertextPath);

    /**
     * Remove Cascading Style Sheets (CSS) from the Hypertext code. This is not
     * well suited in the current level of the project.
     *
     * @param hypertextPath the absolute path to write previously loaded
     * Hypertext file without CSS
     * @return <code>true</code> if the operation succeeded
     * <p><code>false</code> if an error occurred
     */
    public abstract boolean removeCSS(String hypertextPath);

    /**
     * Remove the parent directories from the Hyperlinks of the given Hypertext
     * file. This is desired for the publication process, where all the
     * HypertextMap files will be deployed in the same location
     *
     * @param hypertextPath the absolute path to write previously loaded
     * Hypertext file. All the Hyperlinks of this hypertext will not have parent
     * directories.
     * @return <code>true</code> if the operation succeeded
     * <p><code>false</code> if an error occurred
     */
    public abstract boolean removeParentDirsFromHyperlinks(String hypertextPath);

    /**
     * Retrieve tokens between the given start and end delimiters from the
     * Hypertext file.
     *
     * @param startDelimiter the delimiter that marks the start of a token
     * @param endDelimiter the delimiter that marks the end of a token
     * @return an String array with tokens from the Hypertext
     * <p><code>null</code> if an error occurred
     */
    public abstract String[] getTokens(String startDelimiter, String endDelimiter);
}
