package view.xhtml;

import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.StringReader;
import java.net.URL;

//API imports
import org.w3c.dom.Document;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.resource.XMLResource;
import org.xml.sax.InputSource;

/**
 * Concrete XHTMLViewer.
 */
public class XHTMLViewAdapter extends XHTMLView {

    private XHTMLPanel panel = null;
    private Document dom = null;
    private URL refURL = null;

    /**
     * Constructs the concrete XHTMLView.
     */
    public XHTMLViewAdapter() {
        //instantiate the renderer with console logging disabled
        System.setProperty(
                "xr.util-logging.java.util.logging.ConsoleHandler.level",
                "OFF");
        panel = new XHTMLPanel();

        //remove unnecessary mouselisteners
        MouseListener[] mListeners = panel.getMouseListeners();
        for (int i = 0; i < mListeners.length; i++) {
            panel.removeMouseListener(mListeners[i]);
        }
    }

    @Override
    public boolean loadPage(String strURL) {
        try {
            if (strURL.startsWith("http")) {
                panel.setDocument(strURL);
            } else {
                refURL = new File(strURL).toURI().toURL();
                panel.setDocument(refURL.toExternalForm());
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean loadDocument(String srcCode) {
        try {
            StringReader strReader = new StringReader(srcCode);
            InputSource inputSource = new InputSource(strReader);
            dom = XMLResource.load(inputSource).getDocument();
            panel.setDocument(dom);

            inputSource = null;
            strReader = null;

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void setDimensions(int width, int height) {
        panel.setPreferredSize(new Dimension(width, height));
    }

    @Override
    public Object getDisplayable() {
        return panel;
    }

    @Override
    public void addMouseListener(MouseListener mListener) {
        panel.addMouseListener(mListener);
    }

    @Override
    public void unload() {
        try {
            panel.setDocument((Document) null);
        } catch (Exception e) {
        }
        try {
            panel.setDocument((File) null);
        } catch (Exception e) {
        }
        try {
            panel.setDocument((String) null);
        } catch (Exception e) {
        }
        
        refURL = null;
        dom = null;
        
        panel.removeAll();
        panel = null;
    }
}
