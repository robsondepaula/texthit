package util.html;

/**
 * Concrete factory for XMLManagerAdapter creation
 */
public class HTMLManagerFactory extends AbstractHTMLManagerFactory {

    @Override
    public HTMLManagerAdapter create() {
        return new HTMLManagerAdapter();
    }
}
