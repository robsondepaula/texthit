package util.xhtml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xhtmlrenderer.resource.FSEntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import resources.ResourceManager;
import resources.ViewInternationalization;

/**
 * Concrete XHTMLManager.
 */
public class XHTMLManagerAdapter extends XHTMLManager {

    private volatile Document doc = null;
    private Element root = null;
    private static final String STR_TITLE = "title";

    @Override
    public void addElement(String strName, String strText,
            String strAttributeName, String strAttributeText,
            String strParentElement) {
        // create new XHTML element
        QName elmName = DocumentFactory.getInstance().createQName(strName, "",
                "http://www.w3.org/1999/xhtml");
        Element elmToAdd = DocumentFactory.getInstance().createElement(elmName);

        // add text if needed
        if (strText != null) {
            elmToAdd.setText(strText);
        }
        // create new attribute if needed
        if ((strAttributeName != null) && (strAttributeText != null)) {
            elmToAdd.addAttribute(strAttributeName, strAttributeText);
        }
        // add to root or to a non-root parent element?
        if (strParentElement != null) {
            Element parentElement = getLastElement(root, strParentElement);
            parentElement.add(elmToAdd);
        } else {
            // add element to root
            root.add(elmToAdd);
        }
    }

    @Override
    public synchronized boolean load(String xhtmlPath) {
        try {
            // read the xhtml contents without validation
            SAXReader reader = new SAXReader(false);

            reader.setEncoding("ISO-8859-1");

            // uses the xhtmlrenderer API with console logging disabled
            System.setProperty(
                    "xr.util-logging.java.util.logging.ConsoleHandler.level",
                    "OFF");

            // Get the Singleton xhtmlrenderer custom EntityResolver instance
            // it has local DTD support for XHTML 1.0 and 1.1
            // However it can't be used simultaneously (Not thread safe)
            reader.setEntityResolver(FSEntityResolver.instance());

            File xhtml = new File(xhtmlPath);

            doc = reader.read(xhtml);
            root = doc.getRootElement();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public synchronized boolean loadFromString(String strInput) {
        try {
            // read the xhtml contents without validation
            SAXReader reader = new SAXReader(false);

            reader.setEncoding("ISO-8859-1");
            // uses the xhtmlrenderer API with console logging disabled
            System.setProperty(
                    "xr.util-logging.java.util.logging.ConsoleHandler.level",
                    "OFF");
            /*
             * To cover the use of this application in an offline environment we
             * need to fetch the xhtml DTD locally. Using a customized
             * EntityResolver speeds up the loading of the document as a side
             * effect.
             */
            // Get the Singleton xhtmlrenderer custom EntityResolver instance
            // it has local DTD support for XHTML 1.0 and 1.1
            // However it can't be used simultaneously (Not thread safe)
            reader.setEntityResolver(FSEntityResolver.instance());

            doc = reader.read(new StringReader(strInput));
            root = doc.getRootElement();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public synchronized boolean persistChanges(String xhtmlPath) {
        FileOutputStream fileOutputer = null;
        try {
            fileOutputer = new FileOutputStream(xhtmlPath);
        } catch (Exception e) {
            return false;
        }

        OutputStreamWriter outputWriter = null;
        try {
            outputWriter = new OutputStreamWriter(fileOutputer, "ISO-8859-1");
        } catch (Exception e) {
            try {
                fileOutputer.close();
            } catch (Exception ex) {
            }
            return false;
        }

        // Creates an OutputFormat for "my" XHTML pretty print
        OutputFormat format = new OutputFormat();
        format.setXHTML(true);
        format.setExpandEmptyElements(true);
        format.setNewlines(true);
        format.setIndent(true);
        format.setEncoding("ISO-8859-1");
        XMLWriter xhtmlWriter = new XMLWriter(outputWriter, format);

        try {
            xhtmlWriter.write(doc);
        } catch (Exception e) {
        }
        try {
            xhtmlWriter.flush();
        } catch (Exception e) {
        }
        try {
            xhtmlWriter.close();
        } catch (Exception e) {
        }
        try {
            fileOutputer.close();
        } catch (Exception e) {
        }
        return true;
    }

    @Override
    public String getTitle() {
        try {
            Element title = getFirstElement(root, STR_TITLE);

            return title.getTextTrim();
        } catch (NullPointerException nE) {
            // "java.lang.NullPointerException" = <title> element empty or not
            // found
            nE.printStackTrace();
            return "";
        } catch (Exception e) {
            // strPath doesn't point to a xhtml
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean setTitle(String xhtmlTitle) {
        try {
            Element title = getFirstElement(root, STR_TITLE);

            if (title == null) {
                // <title> not found. Finds <head> and adds the <title> as its
                // child
                Element head = getFirstElement(root, "head");
                if (head == null) {
                    // <head> not found. Creat the <head> element
                    QName headName = DocumentFactory.getInstance().createQName(
                            "head", "", "http://www.w3.org/1999/xhtml");
                    head = DocumentFactory.getInstance().createElement(headName);

                    // create <title> element
                    QName titleName = DocumentFactory.getInstance().createQName(STR_TITLE, "",
                            "http://www.w3.org/1999/xhtml");
                    title = DocumentFactory.getInstance().createElement(
                            titleName);
                    // sets the title value
                    title.setText(xhtmlTitle);

                    // adds the <title> as head's child
                    head.add(title);
                    // insert the newly created pair in the right position in
                    // the
                    // main document
                    List<Node> list = root.content();
                    list.add(1, head);
                } else {
                    // <head> exists. Creates <title>
                    head.addElement(STR_TITLE).setText(xhtmlTitle);
                }
            } else {
                // Element <title> exists, set its new value
                title.setText(xhtmlTitle);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getXHTMLContents() {
        try {
            // Creates an OutputFormat for "my" XHTML pretty print
            OutputFormat format = new OutputFormat();
            format.setXHTML(true);
            format.setExpandEmptyElements(true);
            format.setNewlines(true);
            format.setIndent(true);
            format.setEncoding("ISO-8859-1");
            Writer stringWriter = new StringWriter();

            XMLWriter writer = new XMLWriter(stringWriter, format);
            writer.write(doc);
            writer.flush();
            writer.close();

            return stringWriter.toString();
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
     * @return the desired element <p> <code>null</code> if an error occurred or
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

    /**
     * Recursively descend the tree and returns the last instance of the desired
     * element.
     *
     * @param element the root element of the tree that will be recursively
     * searched
     * @param nodeName the desired element to be retrieved in this tree
     * @return the desired element <p> <code>null</code> if an error occurred or
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
     * Helper method for the tree descent methods. <p> Inspects the contents of
     * an element, if this element has the given name it is returned.
     *
     * @param element the element being inspected
     * @param nodeName the desired name for an element
     * @return the desired element if its name matches the given one <p>
     * <code>null</code> if an error occurred or no element found
     * @see #getLastElement(Element element, String nodeName)
     * @see #getFirstElement(Element element, String nodeName)
     */
    private Element inspect(Element element, String nodeName) {
        try {
            String localName = element.getName();

            if (localName.equals(nodeName)) {
                return element;
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Performs an XSLT transformation to convert XHTML to HTML code.
     *
     * @param strSrcPath the absolute path to the XHTML file
     * @return a String containing the transformed HTML code <p>
     * <code>null</code> if an error occurred
     */
    @Override
    public synchronized String transformXHTML_To_HTML(String strSrcPath) {
        // get a SAXParserFactory instance
        SAXParserFactory saxPF = SAXParserFactory.newInstance();
        // enabling the namespaces processing
        saxPF.setNamespaceAware(true);
        // disable validation
        saxPF.setValidating(false);
        // get a SAXParser object
        SAXParser parser = null;
        try {
            parser = saxPF.newSAXParser();
        } catch (Exception e) {
            // transformation failed
            return null;
        }
        // get the XMLReader
        XMLReader reader = null;
        try {
            reader = parser.getXMLReader();
        } catch (Exception e) {
            parser = null;
            // transformation failed
            return null;
        }
        // uses the xhtmlrenderer API with console logging disabled
        System.setProperty(
                "xr.util-logging.java.util.logging.ConsoleHandler.level", "OFF");
        /*
         * To cover the use of this application in an offline environment we
         * need to fetch the xhtml DTD locally. Using a customizedEntityResolver
         * speeds up the loading of the document as a side effect.
         */
        // Get the Singleton xhtmlrenderer custom EntityResolver instance
        // it has local DTD support for XHTML 1.0 and 1.1
        // However it can't be used simultaneously (Not thread safe)
        reader.setEntityResolver(FSEntityResolver.instance());

        Source xhtmlSource = new SAXSource(reader, new InputSource(strSrcPath));

        ViewInternationalization viewBundle = new ViewInternationalization(
                "resources.MessagesBundle");

        InputStream inputStream = ResourceManager.getResourceInputStream(viewBundle.getString("strTemplateFile_Transform_XHTML2HTML"));

        InputSource inputStreamSource = new InputSource(inputStream);
        Source xsltSource = new SAXSource(reader, inputStreamSource);

        // the factory pattern supports different XSLT processors
        TransformerFactory transFact = TransformerFactory.newInstance();
        Transformer trans = null;
        try {
            trans = transFact.newTransformer(xsltSource);
        } catch (Exception e) {
        }

        ByteArrayOutputStream transOut = new ByteArrayOutputStream();
        try {
            trans.transform(xhtmlSource, new StreamResult(transOut));
        } catch (Exception e) {
        }

        String strRes = null;
        try {
            strRes = transOut.toString("ISO-8859-1");
        } catch (Exception e) {
        }
        try {
            transOut.close();
        } catch (Exception e) {
        }

        try {
            inputStream.close();
        } catch (Exception e) {
        }

        return strRes;
    }

    @Override
    public synchronized String transformHTML_To_XHTML(String strSrcCode) {
        try {
            // setup I/O "ISO-8859-1"
            ByteArrayInputStream inputStr = new ByteArrayInputStream(
                    Charset.forName("ISO-8859-1").encode(strSrcCode).array());
            ByteArrayOutputStream transOut = new ByteArrayOutputStream();

            org.w3c.tidy.Tidy tidy = new org.w3c.tidy.Tidy();
            // disable API logging mechanism
            tidy.setQuiet(true);
            // remove recursive and annoying appends of meta description
            tidy.setTidyMark(false);
            // avoid entitie mapping - this could cause trouble with multiple
            // charset encodings
            tidy.setCharEncoding(org.w3c.tidy.Configuration.RAW);
            // will output XHTML
            tidy.setXHTML(true);
            // transcode
            tidy.parse(inputStr, transOut);

            return transOut.toString("ISO-8859-1");
        } catch (Exception e) {
            // transformation failed
            return null;
        }
    }
}
