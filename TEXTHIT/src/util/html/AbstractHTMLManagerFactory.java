package util.html;

/**
 * Abstract factory for HTMLManagerAdapter creation
 */
public abstract class AbstractHTMLManagerFactory {

    /**
     * Creates a concrete HTMLManagerAdapter ({@link HTMLManagerAdapter})
     *
     * @return a concrete instance of an HTMLManagerAdapter
     */
    public abstract HTMLManagerAdapter create();
}
