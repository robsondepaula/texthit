/*
 * Created on 18.01.2007
 * @author Stephan Richard Palm
 * Copyright by Stephan Richard Palm
 * 
 */
package net.sf.jacinth.listeners;

import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;

import net.sf.jacinth.HTMLEditorPane;

public class DocumentChangeEvent {

    final HTMLEditorPane source;
    final Document oldDocument;
    final Document newDocument;
    final DocumentEvent documentEvent;
    
    public DocumentChangeEvent(HTMLEditorPane source, Document oldDocument, Document newDocument) {
        this.source = source;
        this.oldDocument = oldDocument;
        this.newDocument = newDocument;
        this.documentEvent = null;
    }

    public DocumentChangeEvent(HTMLEditorPane source, DocumentEvent e) {
        this.source = source;
        this.oldDocument = null;
        this.newDocument = null;
        this.documentEvent = e;
    }

    public Document getNewDocument() {
        return newDocument;
    }
    public Document getOldDocument() {
        return oldDocument;
    }
    public HTMLEditorPane getSource() {
        return source;
    }
    
    
    
}
