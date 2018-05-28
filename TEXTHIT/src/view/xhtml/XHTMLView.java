package view.xhtml;

/**
 * Abstract XHTMLView.
 */
public abstract class XHTMLView extends java.util.Observable {

    /**
     * Loads the given URL and renders the XHTML pointed by it.
     *
     * @param strURL the URL pointing to the XHTML to be rendered
     */
    public abstract boolean loadPage(String strURL);

    /**
     * Loads the given source code and renders the corresponding XHTML.
     *
     * @param srcCode the XHTML source code to be rendered
     */
    public abstract boolean loadDocument(String srcCode);

    /**
     * Unloads the XHTML.
     *
     */
    public abstract void unload();

    /**
     * Set the dimensions of the XHTMLView rendering area.
     *
     * @param width the width of the rendering area
     * @param height the height of the rendering area
     */
    public abstract void setDimensions(int width, int height);

    /**
     * Retrieves the XHTMLView displayable component that renders the given
     * XHTML.
     *
     * @return the XHTMLView displayable component.
     */
    public abstract Object getDisplayable();

    /**
     * Install a MouseListener to receive mouse events from the XHTMLView
     * rendering area.
     *
     * @param mListener a mouse listener to receive the mouse events created in
     * the XHTMLView rendering area
     */
    public abstract void addMouseListener(java.awt.event.MouseListener mListener);
}
