package net.sf.jacinth;

import net.sf.jacinth.listeners.DocumentChangeEvent;
import net.sf.jacinth.listeners.DocumentChangeListener;
import net.sf.jacinth.modules.*;
import net.sf.jacinth.util.Local;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.event.DocumentListener;

/**
 *
 * todo JLB ImageModule and TableModule access to HTMLEditor.isEditable()
 */
public class HTMLEditor extends JPanel {

    /**
     * This is the id of the tag that will be put at the end of a html source to
     * mark the end. This is used for a fix,
     */
    static final String JACINTH_END_TAG = "JacinthEndTag";
    /**
     * A list of all Object Listeners. All elements of the vector are of the
     * type ObjectListener
     */
    Vector objectListeners = new Vector();
    /**
     * A list of all used modules
     */
    Modules modules = null;

    public Modules getModules() {
        return modules;
    }
    public HTMLEditorPane editor = new HTMLEditorPane("");
    JScrollPane jScrollPane1 = new JScrollPane();
    public HTMLEditorKit editorKit = new HTMLEditorKit();
    public HTMLDocument document = null;
    private boolean editable = true;
    boolean bold = false;
    boolean italic = false;
    boolean under = false;
    boolean list = false;
    String currentTagName = "BODY";
    Element currentParaElement = null;
    Border border1, border2;
    Class cl = net.sf.jacinth.HTMLEditor.class;
    URL documentBase = null;
    CharTablePanel charTablePanel = new CharTablePanel(editor);
    boolean charTableShow = false;
    public JTabbedPane toolsPanel = new JTabbedPane();
    public boolean toolsPanelShow = false;
    /**
     * Listener for the edits on the current document.
     */
    protected UndoableEditListener undoHandler = new UndoHandler();
    /**
     * UndoManager that we add edits to.
     */
    protected UndoManager undo = new UndoManager();
    /* formatToolbar */
    JToolBar formatToolbar = null;
    JButton jAlignActionB;
    JButton lAlignActionB;
    JButton olActionB;
    JButton italicActionB;
    JButton propsActionB;
    JButton boldActionB;
    JButton ulActionB;
    JButton rAlignActionB;
    JButton cAlignActionB;
    JButton underActionB;
    JButton brActionB;
    JButton hrActionB;
    JButton insCharActionB;
    JButton sourceModeB;
    /* New constructor */
    boolean addSourceMode = true;
    boolean addUndoManager = true;
    boolean useDefaultMouseListener = true;
    boolean addObjectProperties = true;

    /* editToolbar */
    JToolBar editToolbar = null;
    JButton undoActionB;
    JButton redoActionB;
    JButton cutActionB;
    JButton copyActionB;
    JButton pasteActionB;
    JButton findActionB;
    public static final int T_P = 0;
    public static final int T_H1 = 1;
    public static final int T_H2 = 2;
    public static final int T_H3 = 3;
    public static final int T_H4 = 4;
    public static final int T_H5 = 5;
    public static final int T_H6 = 6;
    public static final int T_PRE = 7;
    public static final int T_BLOCKQ = 8;
    String[] elementTypes = {
        Local.getString("Paragraph"), Local.getString("Header") + " 1",
        Local.getString("Header") + " 2", Local.getString("Header") + " 3",
        Local.getString("Header") + " 4", Local.getString("Header") + " 5",
        Local.getString("Header") + " 6", Local.getString("Preformatted"),
        Local.getString("Blockquote")
    };
    public JComboBox blockCB = new JComboBox(elementTypes);
    boolean blockCBEventsLock = false;
    public static final int I_NORMAL = 0;
    public static final int I_EM = 1;
    public static final int I_STRONG = 2;
    public static final int I_CODE = 3;
    public static final int I_CITE = 4;
    public static final int I_SUPERSCRIPT = 5;
    public static final int I_SUBSCRIPT = 6;
    public static final int I_CUSTOM = 7;
    String[] inlineTypes = {
        Local.getString("Normal"), Local.getString("Emphasis"), Local.getString("Strong"),
        Local.getString("Code"), Local.getString("Cite"), Local.getString("Superscript"),
        Local.getString("Subscript"), Local.getString("Custom style") + "..."
    };
    public JComboBox inlineCB = new JComboBox(inlineTypes);
    boolean inlineCBEventsLock = false;
    BorderLayout borderLayout1 = new BorderLayout();
    int currentCaret = 0;
    int currentFontSize = 4;
    JTextPane sourcePane = new JTextPane();
    boolean sourceMode = false;

    public HTMLEditor(final boolean imageModule,
            final boolean anchorModule,
            final boolean tableModule) {
        this(new Vector() {
            {
                if (imageModule) {
                    add(new ImageModule());
                }
                if (anchorModule) {
                    add(new AnchorModule());
                }
                if (tableModule) {
                    add(new TableModule());
                }
            }
        });
    }

    public HTMLEditor() {
        this(true, true, true);
    }

    public HTMLEditor(Collection mods) {
        modules = new Modules();
        for (Iterator iter = mods.iterator(); iter.hasNext();) {
            Object o = iter.next();
            if (o instanceof Module) {
                modules.add((Module) o);
            }
        }

        try {
            init();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HTMLEditor(boolean addImageModule, boolean addAnchorModule, boolean addTableModule, boolean addSourceMode, boolean useDefaultMouseListener, boolean addUndoManager, boolean addObjectProperties) {
        try {
            modules = new Modules();
            if (addImageModule) {
                modules.add(new ImageModule());
            }
            if (addAnchorModule) {
                modules.add(new AnchorModule());
            }
            if (addTableModule) {
                modules.add(new TableModule());
            }

            this.addSourceMode = addSourceMode;
            this.useDefaultMouseListener = useDefaultMouseListener;
            this.addUndoManager = addUndoManager;
            this.addObjectProperties = addObjectProperties;
            init();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void init() throws Exception {
        final Action findAction = getFindAction(this);

        cutAction.putValue(Action.SMALL_ICON, new ImageIcon(cl
                .getResource("resources/icons/cut.png")));
        cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        cutAction.putValue(Action.NAME, Local.getString("Cut"));
        cutAction.putValue(Action.SHORT_DESCRIPTION, Local.getString("Cut"));
        copyAction.putValue(Action.SMALL_ICON, new ImageIcon(cl
                .getResource("resources/icons/copy.png")));
        copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        copyAction.putValue(Action.NAME, Local.getString("Copy"));
        copyAction.putValue(Action.SHORT_DESCRIPTION, Local.getString("Copy"));
        pasteAction.putValue(Action.SMALL_ICON, new ImageIcon(cl
                .getResource("resources/icons/paste.png")));
        pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        pasteAction.putValue(Action.NAME, Local.getString("Paste"));
        pasteAction.putValue(Action.SHORT_DESCRIPTION, Local.getString("Paste"));
        stylePasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + KeyEvent.SHIFT_MASK));
        stylePasteAction.putValue(Action.NAME, Local.getString("Paste special"));
        stylePasteAction.putValue(Action.SHORT_DESCRIPTION, Local.getString("Paste special"));
        selectAllAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        boldAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_B, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        italicAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_I, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        underAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        breakAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
                KeyEvent.SHIFT_MASK));
        breakAction.putValue(Action.NAME, Local.getString("Insert Break"));
        breakAction.putValue(Action.SHORT_DESCRIPTION, Local.getString("Insert Break"));
        findAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        border1 = BorderFactory.createEtchedBorder(Color.white, new Color(142, 142, 142));
        border2 = BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.white,
                new Color(142, 142, 142), new Color(99, 99, 99));
        this.setLayout(borderLayout1);

        editor.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                editor_caretUpdate(e);
            }
        });
        editor.setEditorKit(editorKit);
        editor.addDocumentChangeListener(new DocumentChangeListener() {
            public void documentChanged(DocumentChangeEvent event) {
                updateDocumentBase();
            }
        });

        editorKit.setDefaultCursor(new Cursor(Cursor.TEXT_CURSOR));
        document = (HTMLDocument) editorKit.createDefaultDocument();
        editor.setDocument(document);
        if (addUndoManager) {
            document.addUndoableEditListener(undoHandler);
        }
        this.setPreferredSize(new Dimension(500, 400));

        this.add(jScrollPane1, BorderLayout.CENTER);

        jScrollPane1.getViewport().add(editor, null);
        toolsPanel.setTabPlacement(JTabbedPane.BOTTOM);
        toolsPanel.setFont(new Font("Dialog", 1, 10));
        editor.getKeymap().removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        editor.getKeymap().removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        editor.getKeymap().removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        editor.getKeymap().removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        if (useDefaultMouseListener) {
            editor.addMouseListener(new PopupListener());
        }
        //document.getStyleSheet().setBaseFontSize(currentFontSize);
        sourcePane.setFont(Font.getFont("monospaced"));
        setStyleSheet(new BufferedReader(new InputStreamReader(
                HTMLEditor.class
                .getResourceAsStream("resources/css/default.css"))));
        this.requestFocusInWindow();

        /**
         * Listener on caret position, this invokes listener events for object
         * listener
         */
        editor.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {

                int pos = editor.getCaretPosition();

                for (int i = 0; i < modules.size(); i++) {
                    Module module = (Module) modules.get(i);

                    if (module.applicableFor(document, pos)) {
                        fireObjectChanged(module, module.getObject(document, pos));
                        return;
                    }
                }
                fireObjectChanged(null, null);
            }
        });
        editor.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                editor.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), new ParaBreakAction());
                editor.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), copyAction);
                editor.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), findAction);
                editor.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), pasteAction);
                editor.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + KeyEvent.SHIFT_MASK), stylePasteAction);
                editor.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), cutAction);
            }

            public void focusLost(FocusEvent e) {
                editor.getKeymap().removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
                editor.getKeymap().removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
                editor.getKeymap().removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
                editor.getKeymap().removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
                editor.getKeymap().removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + KeyEvent.SHIFT_MASK));
                editor.getKeymap().removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            }
        });
    }

    class PopupListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                JPopupMenu popupMenu = new JPopupMenu();
                popupMenu.setFocusable(false);
                if (addUndoManager) {
                    popupMenu.add(jMenuItemUndo);
                    popupMenu.add(jMenuItemRedo);
                    popupMenu.addSeparator();
                }
                popupMenu.add(jMenuItemCut);
                popupMenu.add(jMenuItemCopy);
                popupMenu.add(jMenuItemPaste);
                popupMenu.addSeparator();
                if (jMenuItemInsCell.getAction().isEnabled()) {
                    popupMenu.add(jMenuItemInsCell);
                    jMenuItemInsCell.setEnabled(true);
                    popupMenu.add(jMenuItemInsRow);
                    jMenuItemInsRow.setEnabled(true);
                    popupMenu.addSeparator();
                }
                if (addObjectProperties) {
                    popupMenu.add(jMenuItemProp);
                }
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    public JToolBar getFormatToolbar(final Component component) {
        if (formatToolbar != null) {
            return formatToolbar;
        }
        formatToolbar = new JToolBar();
        formatToolbar.setRequestFocusEnabled(false);
        formatToolbar.setToolTipText("");

        boldActionB = new ToolbarButton(boldAction);
        italicActionB = new ToolbarButton(italicAction);
        underActionB = new ToolbarButton(underAction);
        lAlignActionB = new ToolbarButton(lAlignAction);
        rAlignActionB = new ToolbarButton(rAlignAction);
        cAlignActionB = new ToolbarButton(cAlignAction);
        ulActionB = new ToolbarButton(ulAction);
        olActionB = new ToolbarButton(olAction);
//      Can't use any of the toolbar elements to get the head
//  because they are not associated to any head yet
        if (addObjectProperties) {
            propsActionB = new ToolbarButton(getPropsAction(component));
        }
        brActionB = new ToolbarButton(breakAction);
        hrActionB = new ToolbarButton(insertHRAction);
        insCharActionB = new ToolbarButton(insCharAction);
        if (addSourceMode) {
            sourceModeB = new ToolbarButton(toggleSourceModeAction);
        }
        blockCB.setBackground(new Color(230, 230, 230));
        blockCB.setMaximumRowCount(12);
        blockCB.setFont(new java.awt.Font("Dialog", 1, 10));
        blockCB.setMaximumSize(new Dimension(120, 22));
        blockCB.setMinimumSize(new Dimension(60, 22));
        blockCB.setPreferredSize(new Dimension(79, 22));
        blockCB.setFocusable(false);
        blockCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                blockCB_actionPerformed(e);
            }
        });
        inlineCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inlineCB_actionPerformed(formatToolbar, e);
            }
        });
        inlineCB.setFocusable(false);
        inlineCB.setPreferredSize(new Dimension(79, 22));
        inlineCB.setMinimumSize(new Dimension(60, 22));
        inlineCB.setMaximumSize(new Dimension(120, 22));
        inlineCB.setFont(new java.awt.Font("Dialog", 1, 10));
        inlineCB.setMaximumRowCount(12);
        inlineCB.setBackground(new Color(230, 230, 230));
        if (addObjectProperties) {
            formatToolbar.add(propsActionB, null);
            formatToolbar.addSeparator();
        }
        formatToolbar.add(blockCB, null);
        formatToolbar.addSeparator();
        formatToolbar.add(inlineCB, null);
        formatToolbar.addSeparator();
        formatToolbar.add(boldActionB, null);
        formatToolbar.add(italicActionB, null);
        formatToolbar.add(underActionB, null);
        formatToolbar.addSeparator();
        formatToolbar.add(ulActionB, null);
        formatToolbar.add(olActionB, null);
        formatToolbar.addSeparator();
        formatToolbar.add(lAlignActionB, null);
        formatToolbar.add(cAlignActionB, null);
        formatToolbar.add(rAlignActionB, null);
        formatToolbar.addSeparator();

        // For every module, create a button in the toolbar
        for (int i = 0; i < modules.size(); i++) {
            final Module module = (Module) modules.get(i);
            Action moduleAction = new HTMLEditorAction(module.getLocalizedName(), module.getImageIcon()) {
                public void actionPerformed(ActionEvent e) {
                    addNewInstance(component, module);
                }
            };
            formatToolbar.add(new ToolbarButton(moduleAction), null);
        }

        formatToolbar.addSeparator();
        formatToolbar.add(hrActionB, null);
        formatToolbar.add(brActionB, null);
        formatToolbar.add(insCharActionB, null);
        formatToolbar.addSeparator();
        if (addSourceMode) {
            formatToolbar.add(sourceModeB, null);
        }
        return formatToolbar;
    }

    public boolean firstPositionOfCell() {
        int pos = editor.getCaretPosition();

        AbstractDocument.BranchElement pEl = (AbstractDocument.BranchElement) document.getParagraphElement(pos);

        if (!pEl.getParentElement().getName().toUpperCase().equals("TD")) {
            return false;
        }

        // If we are at the first position of the document
        // then we are at the first position in the cell
        if (pos == 0) {
            return true;
        }

        AbstractDocument.BranchElement pElPrev = (AbstractDocument.BranchElement) document.getParagraphElement(pos - 1);

        // If the Prev position is also in the same cell 
        // we don't are in the first position
        if (pEl.getParentElement() == pElPrev.getParentElement()) {
            return false;
        }

        return true;
    }

    public void addNewInstance(Component component, Module module) {
        JacinthTag tag = module.getNewInstance(component, document, editor.getSelectedText(), editor.getCaretPosition());
        if (tag == null) {
            return;
        }
        try {
            editor.replaceSelection("");

            // The following if else structure
            // is a fix for JEditorPane misbehavior an tables
            int pos = editor.getCaretPosition();
            AbstractDocument.BranchElement pEl = (AbstractDocument.BranchElement) document.getParagraphElement(pos);

            // Are we at the first position in cell of a table
            if (firstPositionOfCell()) {
                Element td = pEl.getParentElement();
                AttributeSet tda = td.getAttributes();
                String tagStart = "<TD ";

                // Copy original attributes
                Enumeration enumeration = tda.getAttributeNames();
                while (enumeration.hasMoreElements()) {
                    Object name = enumeration.nextElement();
                    if (!name.toString().equalsIgnoreCase("name")) {
                        tagStart += name.toString() + "=\"" + tda.getAttribute(name).toString() + "\" ";
                    }
                }
                tagStart = tagStart.trim() + ">";


                // Get the original cell
                StringWriter sw = new StringWriter();
                editorKit.write(sw, document, td.getStartOffset(), td.getEndOffset() - td.getStartOffset());

                // Get the content of the original cell
                String tdContent = Util.stripTagContentFromWriterOutput(sw.toString(), td);

                // Include in existing paragraph if there exists one
                String newTd;
                if (tdContent.startsWith("<p>")) {
                    newTd = tagStart + "<p>" + tag.getContent() + tdContent.substring("<p>".length()) + "</TD>";
                } else {
                    newTd = tagStart + tag.getContent() + tdContent + "</TD>";
                }

                document.setOuterHTML(pEl.getParentElement(), newTd);
            } else {
                editorKit.insertHTML(document, pos, tag.getContent(), tag.getPopDepth(), tag.getPushDepth(), tag.getType());
            }
            editor.updateUI();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JToolBar getEditToolbar(Component component) {
        if (editToolbar != null) {
            return editToolbar;
        }
        editToolbar = new JToolBar();
        editToolbar.setRequestFocusEnabled(false);
        editToolbar.setToolTipText("");

        if (addUndoManager) {
            undoActionB = new ToolbarButton(undoAction);
            redoActionB = new ToolbarButton(redoAction);
        }
        cutActionB = new ToolbarButton(cutAction);
        copyActionB = new ToolbarButton(copyAction);
        pasteActionB = new ToolbarButton(pasteAction);
        findActionB = new ToolbarButton(getFindAction(component));

        if (addUndoManager) {
            editToolbar.add(undoActionB, null);
            editToolbar.add(redoActionB, null);
            editToolbar.addSeparator();
        }
        editToolbar.add(cutActionB, null);
        editToolbar.add(copyActionB, null);
        editToolbar.add(pasteActionB, null);
        editToolbar.addSeparator();
        editToolbar.add(findActionB, null);

        return editToolbar;
    }

    class ToolbarButton extends JButton {

        public ToolbarButton(Action a) {
            super(a);
            this.setBorder(border1);
            this.setMaximumSize(new Dimension(22, 22));
            this.setMinimumSize(new Dimension(22, 22));
            this.setPreferredSize(new Dimension(22, 22));
            this.setBorderPainted(false);
            this.setFocusable(false);
            this.setOpaque(false);
            this.setText("");
        }
    }

    public String getDocumentHtml() {
        StringWriter sw = new StringWriter();
        try {
            getDocumentHtml(sw);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sw.toString();
    }

    public void getDocumentHtml(Writer w) throws IOException, BadLocationException {
        net.sf.jacinth.AltHTMLWriter writer = new net.sf.jacinth.AltHTMLWriter(w, document);
        writer.write();
        w.flush(); // bugfix (flush before close)
        w.close();
    }

    // JLB added isEditable and getScrollPane properties
    /**
     * @return true if the document is editable; false if it is read-only
     */
    public boolean isEditable() {
        return editable && isEnabled();
    }

    /**
     * Set the document to be editable or read-only.
     *
     * @param editable true if the document is editable; false to make it
     * read-only
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
        editor.setEditable(editable);
        sourcePane.setEditable(editable);
        // hide the toolbars if the document is not editable
        editToolbar.setVisible(editable);
        formatToolbar.setVisible(editable);
    }

    public JScrollPane getScrollPane() {
        return jScrollPane1;
    }
    // JLB end

    public void setDocumentHtml(String str) {
        try {
            document = (HTMLDocument) editorKit.createDefaultDocument();
            document.putProperty("IgnoreCharsetDirective", Boolean.TRUE);

            // Add an extra tag at the end of the body 
            // to fix an issue with the HTMLDocument and HTMLReader
            // destroying tables and alike at the end of a document
            // the end tag will be removed after the document has been parsed
            editorKit.read(new StringReader(addHardEnd(str)),
                    document,
                    0);
            Element element = document.getElement(JACINTH_END_TAG);
            try {
                if (element != null) {
                    document.remove(element.getStartOffset(), element.getEndOffset() - element.getStartOffset());
                }
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            initEditor();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * This function adds a Jacinth end tag to the end of the body. to fix an
     * issue with the HTMLDocument and HTMLReader destroying tables and alike at
     * the end of a document
     *
     * @param html
     * @return
     */
    public String addHardEnd(String html) {
        // Get the position of the last <body> tag (Well there should only by one)
        int index = html.toLowerCase().lastIndexOf("</body>");
        if (index == -1) {
            return html;
        }

        // Add a hard end tag before the end
        return html.substring(0, index) + "<a id=\"" + JACINTH_END_TAG + "\">END</a>" + html.substring(index);
    }

    public boolean toggleSourceMode() {
        if (!this.sourceMode) {
            sourcePane.setText(getDocumentHtml());
            this.jScrollPane1.getViewport().remove(editor);
            this.jScrollPane1.getViewport().add(sourcePane);
            sourceModeB.setBorder(border2);
            this.sourceMode = true;
        } else {
            setDocumentHtml(sourcePane.getText());
            this.jScrollPane1.getViewport().remove(sourcePane);
            this.jScrollPane1.getViewport().add(editor);
            sourceModeB.setBorder(border1);
            this.sourceMode = false;
        }
        sourceModeB.setBorderPainted(sourceMode);
        return this.sourceMode;
    }

    abstract class HTMLEditorAction extends AbstractAction {

        HTMLEditorAction(String name, ImageIcon icon) {
            super(name, icon);
            super.putValue(Action.SHORT_DESCRIPTION, name);
        }

        HTMLEditorAction(String name) {
            super(name);
            super.putValue(Action.SHORT_DESCRIPTION, name);
        }
    }
    /**
     * Toggle the source mode
     */
    public Action toggleSourceModeAction = new HTMLEditorAction(Local.getString("Source mode"), new ImageIcon(cl
            .getResource("resources/icons/source.png"))) {
        public void actionPerformed(ActionEvent e) {
            toggleSourceMode();
        }
    };
    /**
     * Set bold font style
     */
    public Action boldAction = new HTMLEditorAction(Local.getString("Bold"), new ImageIcon(cl
            .getResource("resources/icons/bold.png"))) {
        public void actionPerformed(ActionEvent e) {
            if (!isEditable()) {
                return;
            }
            toggleBold();
        }
    };
    /**
     * Set italic font style
     */
    public Action italicAction = new HTMLEditorAction(Local.getString("Italic"), new ImageIcon(cl
            .getResource("resources/icons/italic.png"))) {
        public void actionPerformed(ActionEvent e) {
            if (!isEditable()) {
                return;
            }
            toggleItalic();
        }
    };
    /**
     * Set underlined font style
     */
    public Action underAction = new HTMLEditorAction(Local.getString("Underline"), new ImageIcon(cl
            .getResource("resources/icons/underline.png"))) {
        public void actionPerformed(ActionEvent e) {
            if (!isEditable()) {
                return;
            }
            toggleUnderline();
        }
    };
    /**
     * Insert unordered (bulleted) list
     */
    public Action ulAction = new HTMLEditorAction(Local.getString("Unordered list"), new ImageIcon(
            cl.getResource("resources/icons/listunordered.png"))) {
        public void actionPerformed(ActionEvent e) {
            if (!isEditable()) {
                return;
            }
            insertUList();
        }
    };
    /**
     * Insert ordered (numbered) list
     */
    public Action olAction = new HTMLEditorAction(Local.getString("Ordered list"), new ImageIcon(cl
            .getResource("resources/icons/listordered.png"))) {
        public void actionPerformed(ActionEvent e) {
            if (!isEditable()) {
                return;
            }
            insertOList();
        }
    };
    /**
     * Align paragraph left
     */
    public Action lAlignAction = new HTMLEditorAction(Local.getString("Align left"), new ImageIcon(
            cl.getResource("resources/icons/alignleft.png"))) {
        public void actionPerformed(ActionEvent e) {
            if (!isEditable()) {
                return;
            }
            alignLeft();
        }
    };
    /**
     * Center paragraph
     */
    public Action cAlignAction = new HTMLEditorAction(Local.getString("Align center"),
            new ImageIcon(cl.getResource("resources/icons/aligncenter.png"))) {
        public void actionPerformed(ActionEvent e) {
            if (!isEditable()) {
                return;
            }
            alignCenter();
        }
    };
    /**
     * Align paragraph right
     */
    public Action rAlignAction = new HTMLEditorAction(Local.getString("Align right"),
            new ImageIcon(cl.getResource("resources/icons/alignright.png"))) {
        public void actionPerformed(ActionEvent e) {
            if (!isEditable()) {
                return;
            }
            alignRight();
        }
    };
    /**
     * Insert new table cell
     */
    public InsertTableCellAction insertTableCellAction = new InsertTableCellAction();
    /**
     * Insert new table row
     */
    public InsertTableRowAction insertTableRowAction = new InsertTableRowAction();

    /**
     * Show element properties dialog and update the properties
     */
    public Action getPropsAction(final Component component) {
        return new HTMLEditorAction(Local.getString("Object properties"),
                new ImageIcon(cl.getResource("resources/icons/properties.png"))) {
            public void actionPerformed(ActionEvent e) {
                if (!isEditable()) {
                    return;
                }
                elementPropertiesDialog(component);
            }
        };
    }
    /**
     * "Select All" action
     */
    public Action selectAllAction = new HTMLEditorAction(Local.getString("Select all")) {
        public void actionPerformed(ActionEvent e) {
            editor.selectAll();
        }
    };
    /**
     * Insert horizontal rule
     */
    public Action insertHRAction = new HTMLEditorAction(Local.getString("Insert horizontal rule"),
            new ImageIcon(cl.getResource("resources/icons/hr.png"))) {
        public void actionPerformed(ActionEvent e) {
            if (!isEditable()) {
                return;
            }
            try {
                editorKit.insertHTML(document, editor.getCaretPosition(), "<hr>", 0, 0, HTML.Tag.HR);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
    /**
     * Show/hide characters tab
     */
    public Action insCharAction = new HTMLEditorAction(Local.getString("Insert character"),
            new ImageIcon(cl.getResource("resources/icons/char.png"))) {
        public void actionPerformed(ActionEvent e) {
            if (!isEditable()) {
                return;
            }
            if (!charTableShow) {
                addCharTablePanel();
                charTableShow = true;
                insCharActionB.setBorder(border2);
            } else {
                removeCharTablePanel();
                charTableShow = false;
                insCharActionB.setBorder(border1);
            }
            insCharActionB.setBorderPainted(charTableShow);
        }
    };

    /**
     * Show Find/Replace dialog
     */
    public Action getFindAction(final Component component) {
        return new HTMLEditorAction(Local.getString("Find & Replace"),
                new ImageIcon(cl.getResource("resources/icons/find.png"))) {
            public void actionPerformed(ActionEvent e) {
                // findReplaceDialog allows use of the Find feature when !isEditable();
                // disables Replace feature only
                findReplaceDialog(component);
            }
        };
    }
    /**
     * Insert line break
     */
    public BreakAction breakAction = new BreakAction();
    /**
     * Cut selection to the clipboard
     */
    public Action cutAction = new HTMLEditorKit.CutAction() {
        public void actionPerformed(ActionEvent e) {
            if (!isEditable()) {
                return;
            }
            super.actionPerformed(e);
        }
    };
    // public Action styleCopyAction = new HTMLEditorKit.CopyAction();
    /**
     * Copy selection to the clipbord
     */
    public Action copyAction = new HTMLEditorKit.CopyAction();
    /**
     * Paste clipboard content as HTML
     */
    public Action stylePasteAction = new HTMLEditorKit.PasteAction() {
        public void actionPerformed(ActionEvent e) {
            if (!isEditable()) {
                return;
            }
            super.actionPerformed(e);
        }
    };
    /**
     * Paste clipboard content as plain text
     */
    public Action pasteAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (!isEditable()) {
                return;
            }
            doPaste();
        }
    };
    /**
     * Undo previous operation
     */
    public UndoAction undoAction = new UndoAction();
    /**
     * Redo undone operation
     */
    public RedoAction redoAction = new RedoAction();
    JPopupMenu defaultPopupMenu = new JPopupMenu();
    JMenuItem jMenuItemUndo = new JMenuItem(undoAction);
    JMenuItem jMenuItemRedo = new JMenuItem(redoAction);
    JMenuItem jMenuItemCut = new JMenuItem(cutAction);
    JMenuItem jMenuItemCopy = new JMenuItem(copyAction);
    JMenuItem jMenuItemPaste = new JMenuItem(pasteAction);
    JMenuItem jMenuItemProp = new JMenuItem(getPropsAction(defaultPopupMenu));
    JMenuItem jMenuItemInsCell = new JMenuItem(insertTableCellAction);
    JMenuItem jMenuItemInsRow = new JMenuItem(insertTableRowAction);

    void showToolsPanel() {
        if (toolsPanelShow) {
            return;
        }
        this.add(toolsPanel, BorderLayout.SOUTH);
        toolsPanelShow = true;
    }

    void hideToolsPanel() {
        if (!toolsPanelShow) {
            return;
        }
        this.remove(charTablePanel);
        toolsPanelShow = false;
    }

    void addCharTablePanel() {
        showToolsPanel();
        toolsPanel.addTab(Local.getString("Characters"), charTablePanel);
    }

    void removeCharTablePanel() {
        toolsPanel.remove(charTablePanel);
        if (toolsPanel.getTabCount() == 0) {
            hideToolsPanel();
        }
    }

    private void doCopy() {
        Element el = document.getParagraphElement(editor.getSelectionStart());
        if (el.getName().toUpperCase().equals("P-IMPLIED")) {
            el = el.getParentElement();
        }
        String elName = el.getName();
        StringWriter sw = new StringWriter();
        String copy;
        java.awt.datatransfer.Clipboard clip = java.awt.Toolkit.getDefaultToolkit()
                .getSystemClipboard();
        try {
            editorKit.write(sw, document, editor.getSelectionStart(), editor.getSelectionEnd()
                    - editor.getSelectionStart());
            copy = sw.toString();
            copy = copy.split("<" + elName + "(.*?)>")[1];
            copy = copy.split("</" + elName + ">")[0];
            clip.setContents(new java.awt.datatransfer.StringSelection(copy.trim()), null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void doPaste() {
        Clipboard clip = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            Transferable content = clip.getContents(this);
            if (content == null) {
                return;
            }
            String txt = content.getTransferData(new DataFlavor(String.class, "String")).toString();
            document.replace(editor.getSelectionStart(), editor.getSelectionEnd()
                    - editor.getSelectionStart(), txt, editorKit.getInputAttributes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Resets the undo manager.
     */
    protected void resetUndoManager() {
        undo.discardAllEdits();
        undoAction.update();
        redoAction.update();
    }

    class UndoHandler implements UndoableEditListener {

        /**
         * Messaged when the Document has created an edit, the edit is added to
         * <code>undo</code>, an instance of UndoManager.
         */
        public void undoableEditHappened(UndoableEditEvent e) {
            undo.addEdit(e.getEdit());
            undoAction.update();
            redoAction.update();
        }
    }

    class UndoAction extends AbstractAction {

        public UndoAction() {
            super(Local.getString("Undo"));
            setEnabled(false);
            putValue(Action.SMALL_ICON, new ImageIcon(cl.getResource("resources/icons/undo16.png")));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        }

        public void actionPerformed(ActionEvent e) {
            try {
                if (!isEditable()) {
                    throw new CannotUndoException() {
                        public String getMessage() {
                            return "Cannot undo while document is read-only";
                        }
                    };
                }
                undo.undo();
            } catch (CannotUndoException ex) {
                System.out.println("Unable to undo: " + ex);
                ex.printStackTrace();
            }
            update();
            redoAction.update();
        }

        protected void update() {
            if (undo.canUndo() && isEditable()) {
                setEnabled(true);
                putValue(Action.SHORT_DESCRIPTION, undo.getUndoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.SHORT_DESCRIPTION, Local.getString("Undo"));
            }
        }
    }

    class RedoAction extends AbstractAction {

        public RedoAction() {
            super(Local.getString("Redo"));
            setEnabled(false);
            putValue(Action.SMALL_ICON, new ImageIcon(cl.getResource("resources/icons/redo16.png")));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + KeyEvent.SHIFT_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            try {
                if (!isEditable()) {
                    throw new CannotRedoException() {
                        public String getMessage() {
                            return "Cannot redo while document is read-only";
                        }
                    };
                }
                undo.redo();
            } catch (CannotRedoException ex) {
                System.out.println("Unable to redo: " + ex);
                ex.printStackTrace();
            }
            update();
            undoAction.update();
        }

        protected void update() {
            if (undo.canRedo() && isEditable()) {
                setEnabled(true);
                putValue(Action.SHORT_DESCRIPTION, undo.getRedoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.SHORT_DESCRIPTION, Local.getString("Redo"));
            }
        }
    }

    public class BlockAction extends AbstractAction {

        int _type;

        public BlockAction(int type, String name) {
            super(name);
            _type = type;
        }

        public void actionPerformed(ActionEvent e) {
            blockCB.setSelectedIndex(_type);
        }
    }

    public class InlineAction extends AbstractAction {

        int _type;

        public InlineAction(int type, String name) {
            super(name);
            _type = type;
        }

        public void actionPerformed(ActionEvent e) {
            inlineCB.setSelectedIndex(_type);
        }
    }

    void editor_caretUpdate(CaretEvent e) {
        currentCaret = e.getDot();
        // JLB addition to troubleshoot caret positioning in conjunction with ParaBreakAction
//	    if (e.getMark() != currentCaret)
//	        System.out.println("caret position: " + currentCaret + " - " + e.getMark());
//	    else
//	        System.out.println("caret position: " + currentCaret);
        // JLB end
        AttributeSet charattrs = null;
        if (editor.getCaretPosition() > 0) {
            try {
                charattrs = document.getCharacterElement(editor.getCaretPosition() - 1)
                        .getAttributes();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            charattrs = document.getCharacterElement(editor.getCaretPosition()).getAttributes();
        }
        if (charattrs.containsAttribute(StyleConstants.Bold, new Boolean(true))) {
            boldActionB.setBorder(border2);
            bold = true;
        } else if (bold) {
            boldActionB.setBorder(border1);
            bold = false;
        }
        boldActionB.setBorderPainted(bold);
        if (charattrs.containsAttribute(StyleConstants.Italic, new Boolean(true))) {
            italicActionB.setBorder(border2);
            italic = true;
        } else if (italic) {
            italicActionB.setBorder(border1);
            italic = false;
        }
        italicActionB.setBorderPainted(italic);
        if (charattrs.containsAttribute(StyleConstants.Underline, new Boolean(true))) {
            underActionB.setBorder(border2);
            under = true;
        } else if (under) {
            underActionB.setBorder(border1);
            under = false;
        }
        underActionB.setBorderPainted(under);
        inlineCBEventsLock = true;
        inlineCB.setEnabled(!charattrs.isDefined(HTML.Tag.A));
        if (charattrs.isDefined(HTML.Tag.EM)) {
            inlineCB.setSelectedIndex(I_EM);
        } else if (charattrs.isDefined(HTML.Tag.STRONG)) {
            inlineCB.setSelectedIndex(I_STRONG);
        } else if ((charattrs.isDefined(HTML.Tag.CODE)) || (charattrs.isDefined(HTML.Tag.SAMP))) {
            inlineCB.setSelectedIndex(I_CODE);
        } else if (charattrs.isDefined(HTML.Tag.SUP)) {
            inlineCB.setSelectedIndex(I_SUPERSCRIPT);
        } else if (charattrs.isDefined(HTML.Tag.SUB)) {
            inlineCB.setSelectedIndex(I_SUBSCRIPT);
        } else if (charattrs.isDefined(HTML.Tag.CITE)) {
            inlineCB.setSelectedIndex(I_CITE);
        } else if (charattrs.isDefined(HTML.Tag.FONT)) {
            inlineCB.setSelectedIndex(I_CUSTOM);
        } else {
            inlineCB.setSelectedIndex(I_NORMAL);
        }
        inlineCBEventsLock = false;

        Element pEl = document.getParagraphElement(editor.getCaretPosition());
        String pName = pEl.getName().toUpperCase();
        blockCBEventsLock = true;
        if (pName.equals("P-IMPLIED")) {
            pName = pEl.getParentElement().getName().toUpperCase();
        }
        // JLB NOTE: pName will be "HEAD" if the caret is not within the body of the document
        // this appears to only happen after having switched to source view and then back.
        if (pName.equals("HEAD")) {
//		    System.out.println("not in body");
        }
        // JLB end
        if (pName.equals("P")) {
            blockCB.setSelectedIndex(T_P);
        } else if (pName.equals("H1")) {
            blockCB.setSelectedIndex(T_H1);
        } else if (pName.equals("H2")) {
            blockCB.setSelectedIndex(T_H2);
        } else if (pName.equals("H3")) {
            blockCB.setSelectedIndex(T_H3);
        } else if (pName.equals("H4")) {
            blockCB.setSelectedIndex(T_H4);
        } else if (pName.equals("H5")) {
            blockCB.setSelectedIndex(T_H5);
        } else if (pName.equals("H6")) {
            blockCB.setSelectedIndex(T_H6);
        } else if (pName.equals("PRE")) {
            blockCB.setSelectedIndex(T_PRE);
        } else if (pName.equals("BLOCKQUOTE")) {
            blockCB.setSelectedIndex(T_BLOCKQ);
        }
        blockCBEventsLock = false;
        this.insertTableCellAction.update();
        this.insertTableRowAction.update();
    }

    void removeIfEmpty(Element elem) {
        if (elem.getEndOffset() - elem.getStartOffset() < 2) {
            try {
                document.remove(elem.getStartOffset(), elem.getEndOffset());
            } catch (Exception ex) {
                // ex.printStackTrace();
            }
        }
    }

    class ParaBreakAction extends AbstractAction {

        ParaBreakAction() {
            super("ParaBreakAction");
        }

        public void actionPerformed(ActionEvent e) {
            if (!isEditable()) {
                return;
            }

            // JLB added to debug cursor positioning
//	        System.out.println("caret position pre-break: " + editor.getCaretPosition());
            // JLB end

            int pos = editor.getCaretPosition();

            Element elem = document.getParagraphElement(pos);
            String elName = elem.getName().toUpperCase();
            String parentname = elem.getParentElement().getName();

            // JLB added to fix <P> insertion bugs
            HTML.Tag parentTag = HTML.getTag(parentname);
            if (parentname.equals("P-IMPLIED")) {
                parentTag = HTML.Tag.IMPLIED;
            } else if (parentname.equals("head")) // if caret is not within the body then it's useless to be here
            {
                return;
            }
            // JLB end

            if (parentname.toLowerCase().equals("li")) {
                if (elem.getEndOffset() - elem.getStartOffset() > 1) {
                    try {
                        document.insertAfterEnd(elem.getParentElement(), "<li></li>");
                        // Elem.getEndOffset can return a value larger than the length of the document
                        int newCaretPosition = Math.min(elem.getParentElement().getEndOffset(), document.getLength());
                        editor.setCaretPosition(newCaretPosition);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        document.remove(pos, 1);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    Element listParentElement = elem.getParentElement().getParentElement()
                            .getParentElement();
                    HTML.Tag listParentTag = HTML.getTag(listParentElement.getName());
                    // JLB added to support inserting when body tag is parent, two getParentElement() calls
                    // will find html tag which is not good
                    if (listParentTag.equals(HTML.Tag.HTML)) {
                        listParentTag = HTML.Tag.BODY;
                    }
                    // JLB end
                    String listParentTagName = listParentTag.toString();
                    if (listParentTagName.toLowerCase().equals("li")) {
                        Element listAncEl = listParentElement.getParentElement();
                        try {
                            editorKit.insertHTML(document, listAncEl.getEndOffset(),
                                    "<li><p></p></li>", 3, 0, HTML.Tag.LI);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        HTMLEditorKit.InsertHTMLTextAction pAction = new HTMLEditorKit.InsertHTMLTextAction(
                                "insertP", "<p></p>", listParentTag, HTML.Tag.P);
                        pAction.actionPerformed(e);
                    }
                }

            } else {

                // We make a linebreak though separating the text in two paragraphs
                // first we need to find the tag the surounding tag
                Element surrounding = elem;
                if (elem.getName().equalsIgnoreCase("P-IMPLIED")) {
                    surrounding = elem.getParentElement();
                }

                try {
                    // Get the part that is stored in the element _before_ the cursor
                    StringWriter sw = new StringWriter();
                    editorKit.write(sw, document, elem.getStartOffset(), pos - elem.getStartOffset());
                    String pre = Util.stripTagContentFromWriterOutput(sw.toString(), surrounding);

                    // To get the new position we use the length of the pre String
                    int newPos = elem.getStartOffset() + pre.length() + 1;

                    // Get the part that is stored in the element _after_ the cursor
                    sw = new StringWriter();
                    editorKit.write(sw, document, pos, elem.getEndOffset() - pos - 1);
                    String post = Util.stripTagContentFromWriterOutput(sw.toString(), surrounding);

                    String replacement = "<p>" + pre + "</p><p>" + post + "</p>";

                    document.setOuterHTML(elem, replacement);

                    // TODO find better solution that takes the lost blanks into account
                    // or does not lose the blanks in the first place

                    // JLB if above new logic is still broken consider surrounding it with if-else to the effect of...
                    //if (parentname.equals("BODY")) {
                    //} else {
                    // // elsewhere in body, insert after end of parent element
                    //  document.insertAfterEnd(elem.getParentElement(), "<p></p>");
                    //  editor.setCaretPosition(elem.getParentElement().getEndOffset());
                    //}
                    // JLB end

                    editor.setCaretPosition(newPos);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }
            // JLB added to debug cursor positioning
//	        System.out.println("caret position post-break: " + editor.getCaretPosition());
            // JLB end
        }
    }

    class BreakAction extends AbstractAction {

        BreakAction() {
            super(Local.getString("Insert break"), new ImageIcon(cl
                    .getResource("resources/icons/break.png")));
        }

        public void actionPerformed(ActionEvent e) {
            if (!isEditable()) {
                return;
            }
            String elName = document.getParagraphElement(editor.getCaretPosition()).getName();
            HTML.Tag tag = HTML.getTag(elName);
            if (elName.toUpperCase().equals("P-IMPLIED")) {
                tag = HTML.Tag.IMPLIED;
            }
            HTMLEditorKit.InsertHTMLTextAction hta = new HTMLEditorKit.InsertHTMLTextAction(
                    "insertBR", "<br>", tag, HTML.Tag.BR);
            hta.actionPerformed(e);
        }
    }

    class InsertTableRowAction extends AbstractAction {

        InsertTableRowAction() {
            super(Local.getString("Insert table row"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        }

        public void actionPerformed(ActionEvent e) {
            if (!isEditable()) {
                return;
            }
            String trTag = "<tr>";
            Element tr = document.getParagraphElement(editor.getCaretPosition()).getParentElement()
                    .getParentElement();
            for (int i = 0; i < tr.getElementCount(); i++) {
                if (tr.getElement(i).getName().toUpperCase().equals("TD")) {
                    trTag += "<td><p></p></td>";
                }
            }
            trTag += "</tr>";
            try {
                document.insertAfterEnd(tr, trTag);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public boolean isEnabled() {
            if (document == null) {
                return false;
            }
            return document.getParagraphElement(editor.getCaretPosition()).getParentElement()
                    .getName().toUpperCase().equals("TD");
        }

        public void update() {
            this.setEnabled(isEnabled());
        }
    }

    class InsertTableCellAction extends AbstractAction {

        InsertTableCellAction() {
            super(Local.getString("Insert table cell"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + KeyEvent.SHIFT_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            if (!isEditable()) {
                return;
            }
            String tdTag = "<td><p></p></td>";
            Element td = document.getParagraphElement(editor.getCaretPosition()).getParentElement();
            try {
                document.insertAfterEnd(td, tdTag);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public boolean isEnabled() {
            if (document == null) {
                return false;
            }
            return document.getParagraphElement(editor.getCaretPosition()).getParentElement()
                    .getName().toUpperCase().equals("TD");
        }

        public void update() {
            this.setEnabled(isEnabled());
        }
    }

    public String getContent() {
        try {
            return editor.getText();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Retrieve the current content as HTML
     *
     * @return HTML of the current content
     */
    public String getContentAsHTML() {
        if (this.sourceMode) {
            // Editing HTML source, return the current content
            return getContent();
        } else {
            // Get the current document in HTML form
            return getDocumentHtml();
        }
    }

    public void insertHTML(String html, int location) {
        // assumes editor is already set to "text/html" type
        try {
            HTMLEditorKit kit = (HTMLEditorKit) editor.getEditorKit();
            Document doc = editor.getDocument();
            StringReader reader = new StringReader(html);
            kit.read(reader, doc, location);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean toggleBold() {
        if (!bold) {
            boldActionB.setBorder(border2);
        } else {
            boldActionB.setBorder(border1);
        }
        bold = !bold;
        boldActionB.setBorderPainted(bold);
        new StyledEditorKit.BoldAction().actionPerformed(null);
        return bold;
    }

    public boolean toggleItalic() {
        if (!italic) {
            italicActionB.setBorder(border2);
        } else {
            italicActionB.setBorder(border1);
        }
        italic = !italic;
        italicActionB.setBorderPainted(italic);
        new StyledEditorKit.ItalicAction().actionPerformed(null);
        return italic;
    }

    public boolean toggleUnderline() {
        if (!under) {
            underActionB.setBorder(border2);
        } else {
            underActionB.setBorder(border1);
        }
        under = !under;
        underActionB.setBorderPainted(under);
        new StyledEditorKit.UnderlineAction().actionPerformed(null);
        return under;
    }

    public void insertUList() {
        String parentname = document.getParagraphElement(editor.getCaretPosition())
                .getParentElement().getName();
        HTML.Tag parentTag = HTML.getTag(parentname);
        HTMLEditorKit.InsertHTMLTextAction ulAction = new HTMLEditorKit.InsertHTMLTextAction(
                "insertUL", "<ul><li></li></ul>", parentTag, HTML.Tag.UL);
        ulAction.actionPerformed(null);
        list = true;
    }

    public void insertOList() {
        String parentname = document.getParagraphElement(editor.getCaretPosition())
                .getParentElement().getName();
        HTML.Tag parentTag = HTML.getTag(parentname);
        HTMLEditorKit.InsertHTMLTextAction olAction = new HTMLEditorKit.InsertHTMLTextAction(
                "insertOL", "<ol><li></li></ol>", parentTag, HTML.Tag.OL);
        olAction.actionPerformed(null);
        list = true;
    }

    public void alignLeft() {
        HTMLEditorKit.AlignmentAction aa = new HTMLEditorKit.AlignmentAction("leftAlign",
                StyleConstants.ALIGN_LEFT);
        aa.actionPerformed(null);
    }

    public void alignCenter() {
        HTMLEditorKit.AlignmentAction aa = new HTMLEditorKit.AlignmentAction("centerAlign",
                StyleConstants.ALIGN_CENTER);
        aa.actionPerformed(null);
    }

    public void alignRight() {
        HTMLEditorKit.AlignmentAction aa = new HTMLEditorKit.AlignmentAction("rightAlign",
                StyleConstants.ALIGN_RIGHT);
        aa.actionPerformed(null);
    }

    public void alignJustify() {
        HTMLEditorKit.AlignmentAction aa = new HTMLEditorKit.AlignmentAction("justifyAlign",
                StyleConstants.ALIGN_JUSTIFIED);
        aa.actionPerformed(null);
    }

    void setElementProperties(Component component, Element el, String id, String cls, String sty) {
        ElementDialog dlg = ElementDialog.getNewElementDialog(component);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = this.getSize();
        Point loc = this.getLocationOnScreen();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
                (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.setTitle(Local.getString("Object properties"));
        dlg.idField.setText(id);
        dlg.classField.setText(cls);
        dlg.styleField.setText(sty);
        dlg.setVisible(true);
        if (!dlg.isClosedWithOk()) {
            return;
        }
        SimpleAttributeSet attrs = new SimpleAttributeSet(el.getAttributes());
        if (dlg.idField.getText().length() > 0) {
            attrs.addAttribute(HTML.Attribute.ID, dlg.idField.getText());
        }
        if (dlg.classField.getText().length() > 0) {
            attrs.addAttribute(HTML.Attribute.CLASS, dlg.classField.getText());
        }
        if (dlg.styleField.getText().length() > 0) {
            attrs.addAttribute(HTML.Attribute.STYLE, dlg.styleField.getText());
        }
        document.setParagraphAttributes(el.getStartOffset(), 0, attrs, true);
    }

    public void elementPropertiesDialog(Component component) {
        AbstractDocument.BranchElement pEl = (AbstractDocument.BranchElement) document
                .getParagraphElement(editor.getCaretPosition());
//        System.out.println("--------------");
//        System.out.println(pEl.getName() + "<-" + pEl.getParentElement().getName());
        Element el = pEl.positionToElement(editor.getCaretPosition());
//        System.out.println(":" + el.getAttributes().getAttribute(StyleConstants.NameAttribute));
        AttributeSet attrs = el.getAttributes();

        int pos = editor.getCaretPosition();

        for (int i = 0; i < modules.size(); i++) {
            Module module = (Module) modules.get(i);

            if (module.applicableFor(document, pos)) {
                module.editProperties(component, this, document, pos, pEl);
                return;
            }
        }

        String id = "", cls = "", sty = "";
        AttributeSet pa = pEl.getAttributes();
        if (pa.getAttribute(HTML.Attribute.ID) != null) {
            id = pa.getAttribute(HTML.Attribute.ID).toString();
        }
        if (pa.getAttribute(HTML.Attribute.CLASS) != null) {
            cls = pa.getAttribute(HTML.Attribute.CLASS).toString();
        }
        if (pa.getAttribute(HTML.Attribute.STYLE) != null) {
            sty = pa.getAttribute(HTML.Attribute.STYLE).toString();
        }
        setElementProperties(component, pEl, id, cls, sty);
    }

    void blockCB_actionPerformed(ActionEvent e) {
        if (blockCBEventsLock) {
            return;
        }
        if (!isEditable()) {
            return;
        }
        setParagraphStyle(blockCB.getSelectedIndex());
    }

    public void setParagraphStyle(int pStyle) {
        HTML.Tag tag = null;
        switch (pStyle) {
            case T_P:
                tag = HTML.Tag.P;
                break;
            case T_H1:
                tag = HTML.Tag.H1;
                break;
            case T_H2:
                tag = HTML.Tag.H2;
                break;
            case T_H3:
                tag = HTML.Tag.H3;
                break;
            case T_H4:
                tag = HTML.Tag.H4;
                break;
            case T_H5:
                tag = HTML.Tag.H5;
                break;
            case T_H6:
                tag = HTML.Tag.H6;
                break;
            case T_PRE:
                tag = HTML.Tag.PRE;
                break;
            case T_BLOCKQ:
                tag = HTML.Tag.BLOCKQUOTE;
                break;
        }
        Element el = document.getParagraphElement(editor.getCaretPosition());
        if (el.getName().toUpperCase().equals("P-IMPLIED")) {
            Element pEl = el.getParentElement();
            String pElName = pEl.getName();
            String newName = tag.toString();
            StringWriter sw = new StringWriter();
            String copy;
            try {
                editorKit.write(sw, document, el.getStartOffset(), el.getEndOffset() - el.getStartOffset());
                copy = sw.toString();
                copy = copy.split("<" + pElName + "(.*?)>")[1];
                copy = copy.split("</" + pElName + ">")[0];
                document.setOuterHTML(pEl, "<" + newName + ">" + copy + "</" + newName + ">");
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        SimpleAttributeSet attrs = new SimpleAttributeSet(el.getAttributes());
        attrs.addAttribute(StyleConstants.NameAttribute, tag);
        if (editor.getSelectionEnd() - editor.getSelectionStart() > 0) {
            document.setParagraphAttributes(editor.getSelectionStart(), editor.getSelectionEnd()
                    - editor.getSelectionStart(), attrs, true);
        } else {
            document.setParagraphAttributes(editor.getCaretPosition(), 0, attrs, true);
        }
    }

    void inlineCB_actionPerformed(Component component, ActionEvent e) {
        if (inlineCBEventsLock) {
            return;
        }
        if (!isEditable()) {
            return;
        }
        setInlineStyle(component, inlineCB.getSelectedIndex());
    }

    public boolean setInlineStyle(Component component, int iStyle) {
        if (iStyle == I_NORMAL) {
            Element el = document.getCharacterElement(editor.getCaretPosition());
            SimpleAttributeSet attrs = new SimpleAttributeSet();
            attrs.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
            if (editor.getSelectionEnd() > editor.getSelectionStart()) {
                document.setCharacterAttributes(editor.getSelectionStart(), editor
                        .getSelectionEnd()
                        - editor.getSelectionStart(), attrs, true);
            } else {
                document.setCharacterAttributes(el.getStartOffset(), el.getEndOffset() - el.getStartOffset(),
                        attrs, true);
            }
            return true;
        }
        String text = "&nbsp;";
        if (editor.getSelectedText() != null) {
            text = editor.getSelectedText();
        }
        String tag = "";
        String att = "";
        switch (iStyle) {
            case I_EM:
                tag = "em";
                break;
            case I_STRONG:
                tag = "strong";
                break;
            case I_CODE:
                tag = "code";
                break;
            case I_SUPERSCRIPT:
                tag = "sup";
                break;
            case I_SUBSCRIPT:
                tag = "sub";
                break;
            case I_CITE:
                tag = "cite";
                break;
            case I_CUSTOM:
                tag = "font";
                att = setFontProperties(component, document.getCharacterElement(editor.getCaretPosition()), editor
                        .getSelectedText());
                if (att == null) {
                    return false;
                }
                break;
        }
        String html = "<" + tag + att + ">" + text + "</" + tag + ">";
        if (editor.getCaretPosition() == document.getLength()) {
            html += "&nbsp;";
        }
        try {
            // remove old text selection at the text (not HTML) level
            editor.replaceSelection("");
            // then insert HTML at this point
            switch (iStyle) {
                case I_CUSTOM:
                    // JLB added
                    Element elem = document.getCharacterElement(editor.getCaretPosition());
                    HTML.Tag sParentTag = HTML.getTag(elem.getParentElement().getName());
                    HTMLEditorKit.InsertHTMLTextAction pAction = new HTMLEditorKit.InsertHTMLTextAction("insertP", html, sParentTag, HTML.getTag(tag));
                    //System.out.println(sParentTag.toString());
                    pAction.actionPerformed(new ActionEvent(editor, 0, "insertP"));
                    break;
                // JLB end; original approach for all iStyle values is the default
                default:
                    editorKit.insertHTML(document, editor.getCaretPosition(), html, 0, 0, HTML.getTag(tag));
                    if (editor.getCaretPosition() == document.getLength()) {
                        editor.setCaretPosition(editor.getCaretPosition() - 1);
                    }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean fontDialog(Component component) {
        return setInlineStyle(component, I_CUSTOM);
    }

    String setFontProperties(Component component, Element el, String text) {
        FontDialog dlg = FontDialog.getNewFontDialog(component);
        Dimension dlgSize = dlg.getSize();
        Dimension frmSize = this.getSize();
        Point loc = this.getLocationOnScreen();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
                (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        AttributeSet ea = el.getAttributes();
        if (ea.isDefined(StyleConstants.FontFamily)) {
            dlg.fontFamilyCB.setSelectedItem(ea.getAttribute(StyleConstants.FontFamily).toString());
        }
        if (ea.isDefined(HTML.Tag.FONT)) {
            String s = ea.getAttribute(HTML.Tag.FONT).toString();
            String size = s.substring(s.indexOf("size=") + 5, s.indexOf("size=") + 6);
            dlg.fontSizeCB.setSelectedItem(size);
        }
        if (ea.isDefined(StyleConstants.Foreground)) {
            dlg.colorField.setText(Util.encodeColor((Color) ea
                    .getAttribute(StyleConstants.Foreground)));
            Util.setColorField(dlg.colorField);
            dlg.sample.setForeground((Color) ea.getAttribute(StyleConstants.Foreground));
        }
        if (text != null) {
            dlg.sample.setText(text);
        }
        dlg.setVisible(true);
        if (!dlg.isClosedWithOk()) {
            return null;
        }
        String attrs = "";
        if (dlg.fontSizeCB.getSelectedIndex() > 0) {
            attrs += "size=\"" + dlg.fontSizeCB.getSelectedItem() + "\"";
        }
        if (dlg.fontFamilyCB.getSelectedIndex() > 0) {
            attrs += "face=\"" + dlg.fontFamilyCB.getSelectedItem() + "\"";
        }
        if (dlg.colorField.getText().length() > 0) {
            attrs += "color=\"" + dlg.colorField.getText() + "\"";
        }
        if (attrs.length() > 0) {
            return " " + attrs;
        } else {
            return null;
        }
    }

    public void setDocument(Document doc) {
        this.document = (HTMLDocument) doc;
        initEditor();
    }

    public void initEditor() {
        editor.setDocument(document);
        sourcePane.setText(getDocumentHtml());
        if (addUndoManager) {
            // undo = new UndoManager();
            resetUndoManager();
            document.addUndoableEditListener(undoHandler);
        }
        editor.scrollRectToVisible(new Rectangle(0, 0, 0, 0));
        editor.setCaretPosition(0);
    }

    public boolean isDocumentChanged() {
        return undo.canUndo();
    }

    public void setStyleSheet(Reader r) {
        StyleSheet css = new StyleSheet();
        try {
            css.loadRules(r, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        editorKit.setStyleSheet(css);
    }

    public boolean findReplaceDialog(Component component) {
        FindDialog dlg = FindDialog.getNewFindDialog(component);
        Dimension dlgSize = dlg.getSize();
        Dimension frmSize = this.getSize();
        Point loc = this.getLocationOnScreen();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
                (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        if (editor.getSelectedText() != null) {
            dlg.txtSearch.setText(editor.getSelectedText());
        } else if (Context.get("LAST_SEARCHED_WORD") != null) {
            dlg.txtSearch.setText(Context.get("LAST_SEARCHED_WORD").toString());
        }
        dlg.setVisible(true);
        if (!dlg.isClosedWithOk()) {
            return false;
        }
        Context.put("LAST_SEARCHED_WORD", dlg.txtSearch.getText());

        dlg.txtReplace.setEnabled(isEditable());
        dlg.chkReplace.setEnabled(isEditable());

        String repl = null;
        if (dlg.chkReplace.isEnabled() && dlg.chkReplace.isSelected()) {
            repl = dlg.txtReplace.getText();
        }
        Finder finder = new Finder(this, dlg.txtSearch.getText(), dlg.chkWholeWord.isSelected(),
                dlg.chkCaseSens.isSelected(), dlg.chkRegExp.isSelected(), repl);
        finder.start();
        return true;
    }

    public URL getDocumentBase() {
        return documentBase;
    }

    public void setDocumentBase(URL url) {
        documentBase = url;
        updateDocumentBase();
    }

    protected void updateDocumentBase() {
        if (editor.getDocument() instanceof HTMLDocument) {
            ((HTMLDocument) editor.getDocument()).setBase(documentBase);
        }
    }
    Object lastSelectedObject = null;

    public void fireObjectChanged(Module module, Object o) {
        if (lastSelectedObject == o) {
            return;
        }
        lastSelectedObject = o;
        for (int i = 0; i < objectListeners.size(); i++) {
            ObjectListener objectListener = (ObjectListener) objectListeners.get(i);
            objectListener.objectChanged(new ObjectListenerEvent(module, o, lastSelectedObject));
        }
    }

    public void addObjectListener(ObjectListener listener) {
        objectListeners.add(listener);
    }

    public void removeObjectListener(ObjectListener listener) {
        objectListeners.remove(listener);
    }

    /*
     *Retrieve the currently selected text.
     *@param deleteSelection a flag to indicate if the selection should be
     *deleted while the text is retrieve; like the "Cut" operation to clipboard
     *@return a String containing the currently selected plain text
     *<p><code>null</code> if an error occurred
     */
    public String getHTMLEditorSelectedText(boolean deleteSelection) {
        if (deleteSelection) {
            String currentSelection = editor.getSelectedText();
            editor.replaceSelection(null);
            return currentSelection;
        } else {
            return editor.getSelectedText();
        }
    }

    /**
     * This method creates an Hyperlink which name will be given text and which
     * the URL will be the given one. The currently selected text will be
     * replaced by the newly created Hyperlink.
     *
     * @param strText the given text for the new Hyperlink
     * @param strURL the given URL for the new Hyperlink
     * @param newWindow <p>if <code>true</code> make this hyperlink open a new
     * window when selected. <p>if <code>false</code> make this hyperlink open
     * in the same window when selected.
     */
    public String makeTextAnHyperlink(String strText, String strURL, boolean newWindow) {
        try {
            AnchorModule anchor = new AnchorModule();
            Hashtable attrs = new Hashtable();
            if (newWindow) {
                attrs.put("target", "_blank");
            }

            JacinthTag newLink = anchor.insertLink(document, strURL, attrs, strText);
            addLinkHelper(newLink);
            anchor = null;
            return newLink.getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Helper method to make the currently selected text in the HTMLEditor an
     * hyperlink.
     */
    private void addLinkHelper(JacinthTag tag) {
        try {
            int pos = editor.getSelectionStart();

            // The following if else structure
            // is a fix for JEditorPane misbehavior on tables
            // Are we at the first position in cell of a table
            if (firstPositionOfCell()) {
                javax.swing.text.AbstractDocument.BranchElement pEl = (javax.swing.text.AbstractDocument.BranchElement) document.getParagraphElement(pos);
                Element td = pEl.getParentElement();
                AttributeSet tda = td.getAttributes();
                String tagStart = "<TD ";

                // Copy original attributes
                Enumeration enumeration = tda.getAttributeNames();
                do {
                    if (!enumeration.hasMoreElements()) {
                        break;
                    }
                    Object name = enumeration.nextElement();
                    if (!name.toString().equalsIgnoreCase("name")) {
                        tagStart = (new StringBuilder()).append(tagStart).append(name.toString()).append("=\"").append(tda.getAttribute(name).toString()).append("\" ").toString();
                    }
                } while (true);
                tagStart = (new StringBuilder()).append(tagStart.trim()).append(">").toString();

                // Get the original cell
                StringWriter sw = new StringWriter();
                editorKit.write(sw, document, td.getStartOffset(), td.getEndOffset() - td.getStartOffset());

                // Get the content of the original cell
                String tdContent = Util.stripTagContentFromWriterOutput(sw.toString(), td);

                // Include in existing paragraph if there exists one
                String newTd;
                if (tdContent.startsWith("<p>")) {
                    newTd = (new StringBuilder()).append(tagStart).append("<p>").append(tag.getContent()).append(tdContent.substring("<p>".length())).append("</TD>").toString();
                } else {
                    newTd = (new StringBuilder()).append(tagStart).append(tag.getContent()).append(tdContent).append("</TD>").toString();
                }
                document.setOuterHTML(pEl.getParentElement(), newTd);
            } else {
                editorKit.insertHTML(document, pos, tag.getContent(), tag.getPopDepth(), tag.getPushDepth(), tag.getType());
            }
            //update UI
            editor.replaceSelection("");
            editor.updateUI();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Allows another class to listen for the HTMLEditor mouse events.
     *
     * @param mouseAdapter A listener to catch HTMLEditor mouse events
     */
    public void addMouseListener(MouseAdapter mouseAdapter) {
        editor.addMouseListener(mouseAdapter);
    }

    /**
     * Allows another class to listen for the HTMLEditor Document events.
     *
     * @param documentListener A listener to catch HTMLEditor document events
     */
    public void addDocumentChangeListener(DocumentListener docListener) {
        editor.addDocumentListener(docListener);
    }

    /**
     * Trying to loosen the coupling of the HTMLEditor PopUp Menu. With this
     * method i can retrieve a copy, modify and then display the Menu.
     *
     * @return a modifiable copy of the HTMLEditor Default Menu
     */
    public JPopupMenu getHTMLEditorPopUpMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setFocusable(false);
        if (addUndoManager) {
            popupMenu.add(jMenuItemUndo);
            popupMenu.add(jMenuItemRedo);
            popupMenu.addSeparator();
        }
        popupMenu.add(jMenuItemCut);
        popupMenu.add(jMenuItemCopy);
        popupMenu.add(jMenuItemPaste);
        if (jMenuItemInsCell.getAction().isEnabled()) {
            popupMenu.addSeparator();
            popupMenu.add(jMenuItemInsCell);
            jMenuItemInsCell.setEnabled(true);
            popupMenu.add(jMenuItemInsRow);
            jMenuItemInsRow.setEnabled(true);
        }
        if (addObjectProperties) {
            popupMenu.addSeparator();
            popupMenu.add(jMenuItemProp);
        }
        return popupMenu;
    }

    /*
     *Retrieve the plain text being edited in the HTMLEditor.
     *@return a String containing the plain text being edited
     *<p><code>null</code> if an error occurred
     */
    public String getHTMLEditorContent() {
        try {
            return editor.getText(0,
                    editor.getDocument().getLength());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Overwrites the plain text being edited in the HTMLEditor.
     *
     * @param strText a String containing the plain text that will overwrite the
     * currently text in edition
     */
    public void setHTMLEditorContent(String text) {
        editor.setText(text);
    }
}