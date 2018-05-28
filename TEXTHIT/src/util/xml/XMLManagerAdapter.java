package util.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xhtmlrenderer.resource.FSEntityResolver;

/**
 * Concrete XMLManager.
 */
public class XMLManagerAdapter extends XMLManager {

    private volatile Document doc = null;
    private Element root = null;
    private static final String DESCENDANT_STR = "descendant::";

    @Override
    public void create(String rootName) {
        doc = DocumentHelper.createDocument();
        root = doc.addElement(rootName);
    }

    @Override
    public synchronized void load(String xmlPath, String schemaLocation) throws Exception {
        //read the xml contents without validation
        SAXReader reader =
                new SAXReader(false);

        reader.setEncoding("ISO-8859-1");

        //uses the xhtmlrenderer API with console logging disabled
        System.setProperty(
                "xr.util-logging.java.util.logging.ConsoleHandler.level",
                "OFF");

        /*
         *To cover the use of this application in an offline environment we
         *need to fetch the xml DTD locally. Using a customized
         *EntityResolver speeds up the loading of the document as a side
         *effect.
         */
        //Get the Singleton xhtmlrenderer custom EntityResolver instance
        //it has local DTD support for XML DOCBOOK
        //However it can't be used simultaneously (Not thread safe)
        reader.setEntityResolver(FSEntityResolver.instance());

        File xml = new File(xmlPath);

        doc = reader.read(xml);
        root = doc.getRootElement();

        //validation with Schema Factory
        String schemaLanguage = XMLConstants.W3C_XML_SCHEMA_NS_URI;
        SchemaFactory schemaFactory = SchemaFactory.newInstance(schemaLanguage);
        Schema schemaInstance =
                schemaFactory.newSchema(
                new StreamSource(new StringReader(schemaLocation)));

        Validator validator = schemaInstance.newValidator();
        DocumentSource source = new DocumentSource(doc);
        validator.validate(source);

        //document is valid
        root = doc.getRootElement();
    }

    @Override
    public synchronized boolean loadFromString(String strInput) {
        try {
            //read the xhtml contents without validation
            SAXReader reader =
                    new SAXReader(false);

            reader.setEncoding("ISO-8859-1");

            //uses the xhtmlrenderer API with console logging disabled
            System.setProperty(
                    "xr.util-logging.java.util.logging.ConsoleHandler.level",
                    "OFF");
            /*
             *To cover the use of this application in an offline environment we
             *need to fetch the xml DTD locally. Using a customized
             *EntityResolver speeds up the loading of the document as a side
             *effect.
             */
            //Get the Singleton xhtmlrenderer custom EntityResolver instance
            //it has local DTD support for XML DOCBOOK
            //However it can't be used simultaneously (Not thread safe)
            reader.setEntityResolver(FSEntityResolver.instance());

            doc = reader.read(new StringReader(strInput));
            root = doc.getRootElement();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean persistChanges(String xmlPath) {

        FileOutputStream fileOutputer = null;
        try {
            fileOutputer = new FileOutputStream(xmlPath);
        } catch (Exception e) {
            return false;
        }

        OutputStreamWriter outputWriter = null;
        try {
            outputWriter = new OutputStreamWriter(
                    fileOutputer, "ISO-8859-1");
        } catch (Exception e) {
            try {
                fileOutputer.close();
            } catch (Exception ex) {
            }
            return false;
        }

        //Creates an OutputFormat:
        //*with "Tab" indent added;
        //*without newlines between the Elements(cause document corruption);
        //*and with the "ISO-8859-1" encoding format.
        OutputFormat format = new OutputFormat("\t", false, "ISO-8859-1");

        XMLWriter writer = new XMLWriter(outputWriter, format);

        try {
            writer.write(doc);
        } catch (Exception e) {
        }
        try {
            writer.flush();
        } catch (Exception e) {
        }
        try {
            writer.close();
        } catch (Exception e) {
        }
        try {
            fileOutputer.close();
        } catch (Exception e) {
        }
        return true;
    }

    @Override
    public void addElement(String strName,
            String strText,
            String strAttributeName,
            String strAttributeText,
            String strParentElement) {
        //create new XML element
        Element elmToAdd = DocumentHelper.createElement(strName);
        //add text if needed
        if (strText != null) {
            elmToAdd.setText(strText);
        }
        //create new attribute if needed
        if ((strAttributeName != null) && (strAttributeText != null)) {
            elmToAdd.addAttribute(strAttributeName, strAttributeText);
        }
        //add to root or to a non-root parent element?
        if (strParentElement != null) {
            Element parentElement = getLastElement(root, strParentElement);
            parentElement.add(elmToAdd);
        } else {
            //add element to root
            root.add(elmToAdd);
        }
    }

    @Override
    public boolean removeElement(String strName) {
        try {
            Element removeCandidate = getFirstElement(root, strName);

            removeCandidate.detach();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void addAttribute(String strElementName, String strAttributeName, String strAttributeText) {
        Element elmToAdd = getLastElement(root, strElementName);

        if ((strAttributeName != null) && (strAttributeText != null)) {
            elmToAdd.addAttribute(strAttributeName, strAttributeText);
        }
    }

    @Override
    public Object[] getAllElements() {
        try {
            //retrieve all element using XPath expression
            List nodeList = doc.selectNodes("//*");

            List<Element> tmpList = new ArrayList<Element>();

            //iterate trough all the retrieved elements
            for (Iterator iter = nodeList.iterator(); iter.hasNext();) {
                try {
                    Element element = (Element) iter.next();
                    tmpList.add(element);
                } catch (ClassCastException ce) {
                    ce.printStackTrace();
                }
            }

            Object[] result = new Object[tmpList.size()];
            int i = 0;
            //iterate through the relevant elements
            for (Iterator iter = tmpList.iterator(); iter.hasNext();) {
                result[i] = (Element) iter.next();
                i++;
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getElementName(Object input) {
        try {
            Element elm = (Element) input;
            return elm.getName();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getElementText(String elementName) {
        try {
            Element elm = (Element) doc.selectSingleNode(DESCENDANT_STR + elementName);
            return elm.getText();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getElementText(String root, int rootIndex, String elementName) {
        try {
            Element elm = (Element) doc.selectSingleNode(DESCENDANT_STR + root
                    + "[" + Integer.toString(rootIndex) + "]"
                    + "/" + elementName);

            return elm.getText();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAttributeText(String root, int rootIndex, String attName) {
        try {
            Attribute att =
                    (Attribute) doc.selectSingleNode(DESCENDANT_STR + root
                    + "[" + Integer.toString(rootIndex) + "]"
                    + "/@" + attName);

            return att.getValue();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getInnerElementText(String root, int rootIndex,
            String innerNode, int innerIndex,
            String elementName) {
        try {
            Element elm = (Element) doc.selectSingleNode(DESCENDANT_STR + root
                    + "[" + Integer.toString(rootIndex) + "]"
                    + "/" + innerNode
                    + "[" + Integer.toString(innerIndex) + "]"
                    + "/" + elementName);

            return elm.getText();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getInnerAttributeText(String root, int rootIndex,
            String innerNode, int innerIndex,
            String attName) {
        try {
            Attribute att =
                    (Attribute) doc.selectSingleNode(DESCENDANT_STR + root
                    + "[" + Integer.toString(rootIndex) + "]"
                    + "/" + innerNode
                    + "[" + Integer.toString(innerIndex) + "]"
                    + "/@" + attName);

            return att.getValue();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getXMLContents() {
        try {
            return doc.asXML();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Recursively descend the tree and returns the first instance of the
     * desired element.
     *
     * @param element the root element of the tree that will be recursively
     * searched
     * @param nodeName the desired element to be retrieved in this tree
     * @return the desired element <p><code>null</code> if an error occurred or
     * no element found
     */
    private Element getFirstElement(Element element, String nodeName) {
        Element inspected = null;

        inspected = inspect(element, nodeName);
        if (inspected != null) {
            return inspected;
        }

        List content = element.elements();
        Iterator iterator = content.iterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            Element child = (Element) o;
            inspected = getFirstElement(child, nodeName);
            if (inspected != null) {
                return inspected;
            }
        }
        return inspected;
    }

    @Override
    public boolean setElementText(String elementName, String strText) {
        try {
            Element elm = getFirstElement(root, elementName);

            if (elm != null) {
                //element exists, set its new value
                elm.setText(strText);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Recursively descend the tree and returns the last instance of the desired
     * element.
     *
     * @param element the root element of the tree that will be recursively
     * searched
     * @param nodeName the desired element to be retrieved in this tree
     * @return the desired element <p><code>null</code> if an error occurred or
     * no element found
     */
    private Element getLastElement(Element element, String nodeName) {
        Element inspected = null;
        Element candidate = null;

        inspected = inspect(element, nodeName);
        if (inspected != null) {
            candidate = inspected;
        }

        List content = element.elements();
        Iterator iterator = content.iterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            Element child = (Element) o;
            inspected = getLastElement(child, nodeName);
            if (inspected != null) {
                candidate = inspected;
            }
        }
        return candidate;
    }

    /**
     * Helper method for the tree descent methods. <p>Inspects the contents of
     * an element, if this element has the given name it is returned.
     *
     * @param element the element being inspected
     * @param nodeName the desired name for an element
     * @return the desired element if its name matches the given one
     * <p><code>null</code> if an error occurred or no element found
     * @see #getLastElement(Element element, String nodeName)
     * @see #getFirstElement(Element element, String nodeName)
     */
    private Element inspect(Element element, String nodeName) {
        String localName = element.getName();

        if (localName.equals(nodeName)) {
            return element;
        }

        return null;
    }
}
