package net.sf.jacinth;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Vector;

import javax.swing.JEditorPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import net.sf.jacinth.listeners.DocumentChangeEvent;
import net.sf.jacinth.listeners.DocumentChangeListener;

public class HTMLEditorPane extends JEditorPane {

	boolean antiAlias = true;

	public HTMLEditorPane(String text) {
		super("text/html", text);
	}

	public boolean isAntialiasOn() {
		return antiAlias;
	}

	public void setAntiAlias(boolean on) {
		antiAlias = on;
	}

	public void paint(Graphics g) {
		if (antiAlias) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			/*g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
					RenderingHints.VALUE_FRACTIONALMETRICS_ON);*/
			super.paint(g2);
		} else {
			super.paint(g);
		}
	}

    /***
     * 
     * NEXT comes a system that allows to monitor the changements made on the html
     * 
     */

    public void setDocument(Document doc) {
        Document old = getDocument();
        super.setDocument(doc);
        fireDocumentChanged(old, doc);
    }

    /**
     * This vector contains a list of listeners waiting to get
     * notified when the document of the editor pane is replaced.
     */
    Vector documentChangeListeners = new Vector();
    Vector documentListeners = new Vector();
    
    
    DocumentListener documentListener = new DocumentListener() {

        public void changedUpdate(DocumentEvent e) {
            for (int i = 0; i < documentListeners.size(); i++)
                ((DocumentListener) documentListeners.get(i)).changedUpdate(e);
        }

        public void insertUpdate(DocumentEvent e) {
            for (int i = 0; i < documentListeners.size(); i++)
                ((DocumentListener) documentListeners.get(i)).insertUpdate(e);
        }

        public void removeUpdate(DocumentEvent e) {
            for (int i = 0; i < documentListeners.size(); i++)
                ((DocumentListener) documentListeners.get(i)).removeUpdate(e);
        }
        
    };
    
    public void addDocumentChangeListener(DocumentChangeListener documentChangeListener) {
        documentChangeListeners.add(documentChangeListener);
    }
    public void removeDocumentChangeListener(DocumentChangeListener documentChangeListener) {
        documentChangeListeners.add(documentChangeListener);
    }

    public void addDocumentListener(DocumentListener documentListener) {
        documentListeners.add(documentListener);
    }
    public void removeDocumentListener(DocumentListener documentListener) {
        documentListeners.add(documentListener);
    }

    protected void fireDocumentChanged(Document oldDocument, Document newDocument) {
        if (oldDocument != null)
            oldDocument.removeDocumentListener(documentListener);
        if (newDocument != null)
            newDocument.addDocumentListener(documentListener);
        
        if (documentChangeListeners != null)
            for (int i = 0; i < documentChangeListeners.size(); i++)
                ((DocumentChangeListener) documentChangeListeners.get(i)).documentChanged(new DocumentChangeEvent(this, oldDocument, newDocument));
    }
    
}
