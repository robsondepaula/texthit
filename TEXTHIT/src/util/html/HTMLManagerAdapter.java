package util.html;

import java.awt.event.MouseAdapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import javax.swing.JPopupMenu;
import javax.swing.event.DocumentListener;
import javax.swing.text.html.HTMLDocument;
import net.sf.jacinth.AltHTMLWriter;
import net.sf.jacinth.HTMLEditor;

/**
 * Concrete HTMLManager.
 */
public class HTMLManagerAdapter extends HTMLManager {

    private HTMLEditor editor = null;
    private HTMLEditorPanel editorPanel = null;

    /**
     * Constructs the HTMLManagerAdapter.
     */
    public HTMLManagerAdapter() {
        //HTMLEditor(addImage, addAnchor, addTable,
        //addSourceMode, useDefaultMouseListener, addUndoManager, addObjectProperties
        editor = new HTMLEditor(false, false, true, false, false, false, false);

        editorPanel = new HTMLEditorPanel(editor);
    }

    @Override
    public void addDocumentListener(DocumentListener docListener) {
        editor.addDocumentChangeListener(docListener);
    }

    @Override
    public void addMouseListener(MouseAdapter mouseAdapter) {
        editor.addMouseListener(mouseAdapter);
    }

    @Override
    public Object getDisplayable() {
        return editorPanel;
    }

    @Override
    public void createEmptyHTML() {
        editor.setDocumentHtml("");
    }

    @Override
    public boolean saveHTMLFile(String strPath) {

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(strPath);
        } catch (Exception e) {
            return false;
        }

        OutputStreamWriter fw = null;
        try {
            fw = new OutputStreamWriter(fileOutputStream, "ISO-8859-1");
        } catch (Exception e) {
            try {
                fileOutputStream.close();
            } catch (Exception ex) {
            }
            return false;
        }

        AltHTMLWriter writer = new AltHTMLWriter(fw,
                (HTMLDocument) editor.document);
        try {
            writer.write();
        } catch (Exception e) {
        }

        try {
            fw.flush();
        } catch (Exception e) {
        }

        try {
            fw.close();
        } catch (Exception e) {
        }

        try {
            fileOutputStream.close();
        } catch (Exception e) {
        }

        return true;
    }

    @Override
    public boolean loadHTMLFile(String strPath) {

        File file = new File(strPath);

        if (!file.exists()) {
            return false;
        }

        StringBuffer str = new StringBuffer();
        String line;
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
        } catch (Exception e) {
            return false;
        }

        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(fileInput, "ISO-8859-1");
        } catch (Exception e) {
            try {
                fileInput.close();
            } catch (Exception ex) {
            }
            return false;
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(inputStreamReader);
        } catch (Exception e) {
        }

        try {
            while ((line = reader.readLine()) != null) {
                str.append(line + System.getProperty("line.separator"));
            }
        } catch (Exception e) {
        }

        try {
            editor.setDocumentBase(new URL("file", null, file.getAbsolutePath()));
        } catch (Exception e) {
        }

        try {
            fileInput.close();
        } catch (Exception ex) {
        }

        editor.setDocumentHtml(str.toString());
        return true;
    }

    @Override
    public boolean loadHTMLFromString(String strInput) {
        try {
            editor.setDocumentHtml(strInput);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean setHTMLTitle(String title) {
        try {
            String strTmp = editor.getDocumentHtml();
            String preStr = null;
            String posStr = null;

            //search for <head> tag
            if (strTmp.indexOf("<head>") < 0) {
                //there is no <head>, insert <head>+<title>
                preStr = strTmp.substring(0, strTmp.indexOf("<body>") + 6);
                posStr = strTmp.substring(strTmp.indexOf("<body>") + 6);

                editor.setDocumentHtml(preStr + "\n<head>\n<title>" + title
                        + "</title>\n</head>\n" + posStr);
            } else {
                //search for <title>
                if (strTmp.indexOf("<title>") < 0) {
                    //there is no <title>
                    preStr = strTmp.substring(0, strTmp.indexOf("<head>") + 6);
                    posStr = strTmp.substring(strTmp.indexOf("<head>") + 6);

                    editor.setDocumentHtml(preStr + "\n<title>" + title
                            + "</title>\n" + posStr);
                } else {
                    //change existing <title> tag
                    preStr = strTmp.substring(0, strTmp.indexOf("<title>") + 7);
                    posStr = strTmp.substring(strTmp.indexOf("</title>"));

                    editor.setDocumentHtml(preStr + title + posStr);
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getHTMLTitle() {
        try {
            String strTmp = editor.getDocumentHtml();
            String title = strTmp.substring(strTmp.indexOf("<title>") + 7,
                    strTmp.indexOf("</title>")).trim();

            return title;
        } catch (StringIndexOutOfBoundsException sE) {
            return "";
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getHTMLCode() {
        return editor.getDocumentHtml();
    }

    @Override
    public void setHTMLCode(String srcCode) {
        editor.setDocumentHtml(srcCode);
    }

    @Override
    public String getHTMLEditorSelectedText(boolean deleteSelection) {
        return editor.getHTMLEditorSelectedText(deleteSelection);
    }

    @Override
    public String getHTMLEditorText() {
        return editor.getHTMLEditorContent();
    }

    @Override
    public void setHTMLEditorText(String text) {
        editor.setHTMLEditorContent(text);
    }

    @Override
    public JPopupMenu getHTMLEditorPopUpMenu() {
        return editor.getHTMLEditorPopUpMenu();
    }

    @Override
    public String makeTextAnHyperlink(String strText,
            String strURL,
            boolean newWindow) {
        //Makes the given text an hyperlink
        return editor.makeTextAnHyperlink(strText, strURL, newWindow);
    }
}
