package util.xml;

/**
 * Abstract factory for XMLManagerAdapter creation
 */
public abstract class AbstractXMLManagerFactory {

    /**
     * Creates a concrete XMLManagerAdapter ({@link XMLManagerAdapter})
     *
     * @return a concrete instance of an XMLManagerAdapter
     */
    public abstract XMLManagerAdapter create();
}
