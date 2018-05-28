package util;

import util.xml.XMLManager;
import util.xml.XMLManagerFactory;

/**
 * This class manipulates XML files.
 */
public class XMLManipulator {

    private XMLManager xMng = null;

    /**
     * It uses the XMLManagerFactory ({@link XMLManagerFactory}) to create a
     * concrete XMLManager ({@link XMLManager}) that will be used to do the
     * underlying work in the XML file source code.
     */
    public XMLManipulator() {
        try {
            XMLManagerFactory xMngFactory = new XMLManagerFactory();
            xMng = xMngFactory.create();
        } catch (Exception e) {
        }
    }

    /**
     * Creates an empty XML file and hold its structure in memory.
     *
     * @param rootName the name for the XML root element
     */
    public void create(String rootName) {
        xMng.create(rootName);
    }

    /**
     * Parse the contents of the XML file and loads its structure in memory. The
     * parser will validate the XML contents against an XMLSchema.
     *
     * @param xmlPath the absolute path to XML file
     * @param schemaLocation the absolute path to XMLSchema file
     * @throws Exception If errors prevented the XML File for being loaded
     */
    public void load(String xmlPath, String schemaLocation) throws Exception {
        xMng.load(xmlPath, schemaLocation);
    }

    /**
     * Commit the changes made in memory to an XML file.
     *
     * @param xmlPath the absolute path to XML file
     * @return <code>true</code> if the XHTML was commited <p><code>false</code>
     * if the operation failed
     */
    public boolean persistChanges(String xmlPath) {
        return xMng.persistChanges(xmlPath);
    }

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
    public void addElement(String strName,
            String strText,
            String strAttributeName,
            String strAttributeText,
            String strParentElement) {
        xMng.addElement(strName, strText, strAttributeName, strAttributeText, strParentElement);
    }

    /**
     * Create a new attribute in an existing XML element in the in memory source
     * code.
     *
     * @param strElementName the name for the existing XML element
     * @param strAttributeName the name for a new attribute
     * @param strAttributeText the value for the new attribute
     */
    public void addAttribute(String strElementName, String strAttributeName, String strAttributeText) {
        xMng.addAttribute(strElementName, strAttributeName, strAttributeText);
    }

    /**
     * Search the XML in memory structure and get its elements.
     *
     * @return an array of Objects containing all the XML elements in memory
     * <p><code>null</code> if an error occurred
     */
    public Object[] getAllElements() {
        return xMng.getAllElements();
    }

    /**
     * Parse an Object. If this object is an XML element, return its name.
     *
     * @return a String containing an XML element name <p><code>null</code> if
     * an error occurred
     */
    public String getElementName(Object input) {
        return xMng.getElementName(input);
    }

    /**
     * Query the XML in memory structure. Retrieves the text of the given
     * element.
     *
     * @param elementName the name of the desired element
     * @return the text for the desired element <p><code>null</code> if an error
     * occurred or the query wasn't successful
     */
    public String getElementText(String elementName) {
        return xMng.getElementText(elementName);
    }

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
    public String getElementText(String root, int rootIndex, String elementName) {
        return xMng.getElementText(root, rootIndex, elementName);
    }

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
    public String getAttributeText(String root, int rootIndex, String attName) {
        return xMng.getAttributeText(root, rootIndex, attName);
    }

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
    public String getInnerElementText(String root, int rootIndex,
            String innerNode, int innerIndex, String elementName) {
        return xMng.getInnerElementText(root, rootIndex, innerNode, innerIndex, elementName);
    }

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
    public String getInnerAttributeText(String root, int rootIndex,
            String innerNode, int innerIndex, String attName) {
        return xMng.getInnerAttributeText(root, rootIndex, innerNode, innerIndex, attName);
    }

    public String getXMLContents() {
        return xMng.getXMLContents();
    }
}
