package view.xhtml;

/**
 * Concrete factory for XHTMLViewAdapter creation
 */
public class XHTMLViewFactory extends AbstractXHTMLViewFactory {

    @Override
    public XHTMLViewAdapter create() {
        return new XHTMLViewAdapter();
    }
}
