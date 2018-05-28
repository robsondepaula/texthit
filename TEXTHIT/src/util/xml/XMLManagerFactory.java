package util.xml;

/**
 * Concrete factory for XMLManagerAdapter creation
 */
public class XMLManagerFactory extends AbstractXMLManagerFactory {

    @Override
    public XMLManagerAdapter create() {
        return new XMLManagerAdapter();
    }
}
