package util.xml;

/**
 * Abstract XMLManager.
 */
public abstract class XMLManager {

    /**
     * Creates an empty XML file and hold its structure in memory.
     *
     * @param rootName the name for the XML root element
     */
    public abstract void create(String rootName);

    /**
     * Commit the changes made in memory to an XML file.
     *
     * @param xmlPath the absolute path to XML file
     * @return <code>true</code> if the XHTML was commited <p><code>false</code>
     * if the operation failed
     */
    public abstract boolean persistChanges(String xmlPath);

    /**
     * Parse the contents of the XML file and loads its structure in memory. The
     * parser will validate the XML contents against an XMLSchema.
     *
     * @param xmlPath the absolute path to XML file
     * @param schemaLocation the absolute path to XMLSchema file
     * @throws Exception If errors prevented the XML File for being loaded
     */
    public abstract void load(String xmlPath, String schemaLocation) throws Exception;

    /**
     * Parse the contents of a String containing an XML source code and creates
     * its structure in memory.
     *
     * @param strInput a String containing an XML source code
     * @return <code>true</code> if the XML was created <p><code>false</code> if
     * the operation failed
     */
    public abstract boolean loadFromString(String strInput);

    /**
     * Create a new XML element in the in memory source code.
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
     * Remove a XML element in the in memory source code.
     *
     * @param strName the name of the element to be removed
     * @return <code>true</code> if element removed <p><code>false</code> if the
     * remove operation failed
     */
    public abstract boolean removeElement(String strName);

    /**
     * Create a new attribute in an existing XML element in the in memory source
     * code.
     *
     * @param strElementName the name for the existing XML element
     * @param strAttributeName the name for a new attribute
     * @param strAttributeText the value for the new attribute
     */
    public abstract void addAttribute(String strElementName,
            String strAttributeName,
            String strAttributeText);

    /**
     * Search the XML in memory structure and get its elements.
     *
     * @return an array of Objects containing all the XML elements in memory
     * <p><code>null</code> if an error occurred
     */
    public abstract Object[] getAllElements();

    /**
     * Parse an Object. If this object is an XML element, return its name.
     *
     * @return a String containing an XML element name <p><code>null</code> if
     * an error occurred
     */
    public abstract String getElementName(Object input);

    /**
     * Query the XML in memory structure. Retrieves the text of the given
     * element.
     *
     * @param elementName the name of the desired element
     * @return the text for the desired element <p><code>null</code> if an error
     * occurred or the query wasn't successful
     */
    public abstract String getElementText(String elementName);

    /**
     * Query the XML in memory structure. Retrieves the text of the given
     * element that has the given root element of the given index.
     *
     * @param root the root element of the desired element
     * @param rootIndex the desired occurrence of the root element
     * @param elementName the name of the desired element
     * @return the text for the desired element <p><code>null</code> if an error
     * occurred or the query wasn't successful
     */
    public abstract String getElementText(String root, int rootIndex, String elementName);

    /**
     * Set the given value for the given element in the XML source code.
     *
     * @param elementName a String containing the name for the element to be
     * edited
     * @param strText a String containing the new value for the element
     * @return <code>true</code> if the operation succeeded
     * <p><code>false</code> if an error occurred
     */
    public abstract boolean setElementText(String elementName, String strText);

    /**
     * Query the XML in memory structure. Retrieves the attribute text that has
     * the given root element of the given index.
     *
     * @param root the root element of the desired attribute
     * @param rootIndex the desired occurrence of the root element
     * @param attName the name of the desired attribute
     * @return the text for the desired attribute <p><code>null</code> if an
     * error occurred or the query wasn't successful
     */
    public abstract String getAttributeText(String root, int rootIndex, String attName);

    /**
     * Query the XML in memory structure. Retrieves the text of the desired
     * inner element that has the given root element of the given index.
     *
     * @param root the root element of the desired element
     * @param rootIndex the desired occurrence of the root element
     * @param elementName the name of the desired element
     * @param innerNode the name of the desired inner element
     * @param innerIndex the desired occurrence of the inner element
     * @return the text for the desired element <p><code>null</code> if an error
     * occurred or the query wasn't successful
     */
    public abstract String getInnerElementText(String root, int rootIndex, String innerNode, int innerIndex, String elementName);

    /**
     * Query the XML in memory structure. Retrieves the text of the desired
     * inner attribute that has the given root element of the given index.
     *
     * @param root the root element of the desired element
     * @param rootIndex the desired occurrence of the root element
     * @param attName the name of the desired attribute
     * @param innerNode the name of the desired inner element
     * @param innerIndex the desired occurrence of the inner element
     * @return the text for the desired attribute <p><code>null</code> if an
     * error occurred or the query wasn't successful
     */
    public abstract String getInnerAttributeText(String root, int rootIndex, String innerNode, int innerIndex, String attName);

    /**
     * Retrieve the loaded document contents.
     *
     * @return a String containing the document contents <p><code>null</code> if
     * the operation failed
     */
    public abstract String getXMLContents();
}
