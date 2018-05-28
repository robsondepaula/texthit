package view.graph;

/**
 * Abstract factory for GraphViewAdapter creation
 */
public abstract class AbstractGraphViewFactory {

    /**
     * Creates a concrete GraphViewAdapter ({@link GraphViewAdapter})
     *
     * @return a concrete instance of an GraphViewAdapter
     */
    public abstract GraphViewAdapter create();
}
