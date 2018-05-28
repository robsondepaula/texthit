package util.html;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import net.sf.jacinth.HTMLEditor;

public class HTMLEditorPanel extends JPanel {

    public HTMLEditorPanel(HTMLEditor editor) {
        setLayout(new BorderLayout());
        JToolBar editToolbar = editor.getEditToolbar(this);
        JToolBar formatToolbar = editor.getFormatToolbar(this);

        JPanel secondToolbarPanel = new JPanel();
        secondToolbarPanel.setLayout(new BorderLayout());
        secondToolbarPanel.add(formatToolbar, BorderLayout.PAGE_START);
        secondToolbarPanel.add(editor);

        add(editToolbar, BorderLayout.PAGE_START);
        add(secondToolbarPanel);
    }
}
