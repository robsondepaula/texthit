package view.xhtml;

/**
 * Abstract factory for XHTMLViewAdapter creation
 */
public abstract class AbstractXHTMLViewFactory {

    /**
     * Creates a concrete XHTMLViewAdapter ({@link XHTMLViewAdapter})
     *
     * @return a concrete instance of an XHTMLViewAdapter
     */
    public abstract XHTMLViewAdapter create();
}
