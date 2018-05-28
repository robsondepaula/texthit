package util.xhtml;

/**
 * Abstract factory for XHTMLManagerAdapter creation
 */
public abstract class AbstractXHTMLManagerFactory {

    /**
     * Creates a concrete XHTMLManagerAdapter ({@link XHTMLManagerAdapter})
     *
     * @return a concrete instance of an XHTMLManagerAdapter
     */
    public abstract XHTMLManagerAdapter create();
}
