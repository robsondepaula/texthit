package view.graph;

/**
 * Concrete factory for GraphViewAdapter creation
 */
public class GraphViewFactory extends AbstractGraphViewFactory {

    @Override
    public GraphViewAdapter create() {
        return new GraphViewAdapter();
    }
}
