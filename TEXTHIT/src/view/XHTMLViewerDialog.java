package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

import view.xhtml.XHTMLView;
import view.xhtml.XHTMLViewFactory;

/**
 * Constructs a XHTML Viewer to preview XHTML files.
 */
public class XHTMLViewerDialog extends JDialog {

    private JScrollPane aboutJScrollPane;
    private JButton closeButton;
    private boolean isVisible = false;
    /**
     * Concrete XHTMLView ({@link view.xhtml.XHTMLView}) used to render the
     * XHTML file
     */
    private XHTMLView xhtmlView = null;

    /**
     * Constructs the XHTML viewer dialog. Instantiates a concrete XHTMLView
     * ({@link view.xhtml.XHTMLView}) to render the given URL.
     *
     * @param titleText the text for the dialog title property
     * @param buttonLabel the text for the dismiss dialog button
     * @param url the url for the XHTML to be rendered
     * @param width the dialog window width
     * @param height the dialog window height
     */
    public XHTMLViewerDialog(String titleText, String buttonLabel, String url,
            int width, int height) throws Exception {
        super();
        setTitle(titleText);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeDialog();
            }
        });

        XHTMLViewFactory xFactory = new XHTMLViewFactory();
        xhtmlView = xFactory.create();
        xhtmlView.setDimensions(width, height);

        //gets the displayable component and shows it in the main window
        aboutJScrollPane = new JScrollPane((Component) xhtmlView.getDisplayable());
        aboutJScrollPane.setPreferredSize(new Dimension(width, height));

        getContentPane().add(aboutJScrollPane, "Center");
        closeButton = new JButton(buttonLabel);
        getContentPane().add(closeButton, "South");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                closeDialog();
            }
        });

        //loads the url
        if (xhtmlView.loadPage(url)) {
            pack();
            setSize(width, height);

            //centering in the screen
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((screen.width - width) / 2, (screen.height - height) / 2);
        } else {
            throw new Exception("XHTML View unnavailable");
        }
    }

    /**
     * Constructs the XHTML viewer dialog. Instantiates a concrete XHTMLView
     * ({@link view.xhtml.XHTMLView}) to render the given XHTML contents.
     *
     * @param width the dialog window width
     * @param height the dialog window height
     * @param titleText the text for the dialog title property
     * @param buttonLabel the text for the dismiss dialog button
     * @param xhtmlContents the XHTML contents that will be rendered
     */
    public XHTMLViewerDialog(int width, int height, String titleText,
            String buttonLabel, String xhtmlContents) throws Exception {
        super();
        setTitle(titleText);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeDialog();
            }
        });

        XHTMLViewFactory xFactory = new XHTMLViewFactory();
        xhtmlView = xFactory.create();
        xhtmlView.setDimensions(width, height);

        //gets the displayable component and shows it in the main window
        aboutJScrollPane = new JScrollPane((Component) xhtmlView.getDisplayable());
        aboutJScrollPane.setPreferredSize(new Dimension(width, height));
        //avoids user actions to be threated by the API
        aboutJScrollPane.setEnabled(false);

        getContentPane().add(aboutJScrollPane, "Center");
        closeButton = new JButton(buttonLabel);
        getContentPane().add(closeButton, "South");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                closeDialog();
            }
        });

        //loads the XHTML contents in the renderer
        if (xhtmlView.loadDocument(xhtmlContents)) {
            pack();
            setSize(width, height);

            //centering in the screen
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((screen.width - width) / 2, (screen.height - height) / 2);
        } else {
            throw new Exception("XHTML View unnavailable");
        }
    }

    /**
     * Changes the visibility of the XHTMLViewerDialog.
     *
     * @param isVisible if <code>true</code> shows the XHTMLViewerDialog. *
     * *<p>if <code>false</code> hides it
     */
    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        this.isVisible = isVisible;
    }

    /**
     * Retrieves the current visibility state of the XHTMLViewerDialog.
     *
     * @return <code>true</code> if the XHTMLViewerDialog is visible.
     * <p><code>false</code> if hidden
     */
    public boolean getVisibility() {
        return isVisible;
    }

    private void closeDialog() {
        setVisible(false);
        isVisible = false;
        xhtmlView.unload();
    }
}
