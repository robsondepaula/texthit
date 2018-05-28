package util.xhtml;

/**
 * Concrete factory for XHTMLManagerAdapter creation
 */
public class XHTMLManagerFactory extends AbstractXHTMLManagerFactory {

    @Override
    public XHTMLManagerAdapter create() {
        return new XHTMLManagerAdapter();
    }
}
