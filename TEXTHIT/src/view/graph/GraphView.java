package view.graph;

/**
 * Abstract GraphView.
 */
public abstract class GraphView extends java.util.Observable {

    /**
     * Event number that is used to signal a Vertex insertion
     */
    public static final Integer EVT_VERTEX_ADDED = new Integer(1);
    /**
     * Event number that is used to signal an Edge insertion
     */
    public static final Integer EVT_EDGE_ADDED = new Integer(2);
    /**
     * Event number that is used to signal that all Vertex were removed from the
     * GraphView
     */
    public static final Integer EVT_ALL_VERTEXES_REMOVED = new Integer(3);
    /**
     * Event number that is used to signal that all Edges were removed from the
     * GraphView
     */
    public static final Integer EVT_ALL_EDGES_REMOVED = new Integer(4);
    /**
     * Event number that is used to signal that there is no element selected in
     * the GraphView
     */
    public static final Integer EVT_SELECTION_CLEARED = new Integer(5);
    /**
     * Event number that is used to signal that there is at least two Vertexes
     * in the GraphView
     */
    public static final Integer EVT_VERTEX_MAY_LINK = new Integer(6);
    /**
     * Event number that is used to signal that there is less than two Vertexes
     * in the GraphView
     */
    public static final Integer EVT_VERTEX_CANT_LINK = new Integer(7);
    /**
     * Event number that is used to signal that there is an Edge selected in the
     * GraphView
     */
    public static final Integer EVT_EDGE_SELECTED = new Integer(8);
    /**
     * Event number that is used to signal that the right mouse button was
     * clicked in a Vertex
     */
    public static final Integer EVT_RIGHT_MOUSE_BUTTON_IN_VERTEX = new Integer(9);
    /**
     * Event number that is used to signal that the right mouse button was
     * clicked in an Edge
     */
    public static final Integer EVT_RIGHT_MOUSE_BUTTON_IN_EDGE = new Integer(10);
    /**
     * Event number that is used to signal that the left mouse button was
     * fouble-clicked in a Vertex
     */
    public static final Integer EVT_DOUBLE_CLICK_VERTEX = new Integer(11);

    /**
     * Add a Vertex in the GraphView.
     *
     * @param vertexLabel a String containing the desired label for the Vertex
     */
    public abstract void addVertex(String vertexLabel);

    /**
     * Add a Vertex in the GraphView.
     *
     * @param vertexLabel a String containing the desired label for the Vertex
     * @param xPosition the desired horizontal axis Vertex position
     * @param yPosition the desired vertical axis Vertex position
     * @param width the desired width for the Vertex
     * @param height the desired height for the Vertex
     * @param colorR the desired Red color component as in RGB color
     * @param colorG the desired Green color component as in RGB color
     * @param colorB the desired Blue color component as in RGB color
     */
    public abstract void addVertex(String vertexLabel,
            double xPosition,
            double yPosition,
            double width,
            double height,
            int colorR,
            int colorG,
            int colorB);

    /**
     * Set the given color in the currently selected vertex
     *
     * @param colorR the desired Red color component as in RGB color
     * @param colorG the desired Green color component as in RGB color
     * @param colorB the desired Blue color component as in RGB color
     */
    public abstract void setSelectedVertexColor(final int colorR,
            final int colorG,
            final int colorB);

    /**
     * Updates a Vertex label.
     *
     * @param oldLabel a String containing the label to be changed
     * @param newLabel the new label of the vertex
     */
    public abstract void updateVertexLabel(String oldLabel, String newLabel);

    /**
     * Add an Edge in the GraphView.
     *
     * @param sourceVertexLabel a String containing the label for the source
     * Vertex for this Edge
     * @param targetVertexLabel a String containing the label for the target
     * Vertex for this Edge
     */
    public abstract void addEdge(String sourceVertexLabel,
            String targetVertexLabel);

    /**
     * Add an edge from the current selected vertex in the {@link GraphView} to
     * the given vertex.
     *
     * @param targetLabel the label of the destination vertex that will be
     * connected with the selected one by the new edge
     */
    public abstract void addEdgeFromSelectedSource(String targetLabel);

    /**
     * Removes the currently selected Vertex from the GraphView.
     */
    public abstract void removeSelectedVertex();

    /**
     * Removes the the Edge of the given source and target from the GraphView.
     */
    public abstract void removeEdge(String srcVertex, String tgtVertex);

    /**
     * Removes the currently selected Edge from the GraphView.
     */
    public abstract void removeSelectedEdge();

    /**
     * Query the {@link GraphView} an returns the title of the currently
     * selected Vertex.
     *
     * @return a String containing the title of the selected Vertex
     */
    public abstract String getSelectedVertexString();

    /**
     * Returns the title of the source Vertex for the currently selected Edge.
     *
     * @return a String containing the title of the source Vertex for the
     * selected Edge
     */
    public abstract String getSelectedEdgeSourceString();

    /**
     * Query the {@link GraphView} an returns the title of the target Vertex for
     * the currently selected Edge.
     *
     * @return a String containing the title of the target Vertex for the
     * selected Edge
     */
    public abstract String getSelectedEdgeTargetString();

    /**
     * Perform setup operations in the GraphView and ready it for display.
     */
    public abstract void initializeGraph();

    /**
     * Gracefuly disposes the GraphView
     */
    public abstract void stopGraph();

    /**
     * Retrieves the GraphView displayable component used to show vertexes and
     * edges to the user.
     *
     * @return the GraphView displayable component.
     */
    public abstract Object getDisplayable();

    /**
     * Retrieves the last point where the mouse was clicked in a Vertex.
     *
     * @return an Object carrying a {@link java.awt.Point} class with the
     * desired dimensions.
     */
    public abstract Object getLastClickedVertexPoint();

    /**
     * Retrieves the last point where the mouse was clicked in an Edge.
     *
     * @return an Object carrying a {@link java.awt.Point} class with the
     * desired dimensions.
     */
    public abstract Object getLastClickedEdgePoint();

    /**
     * Builds an array containing all Vertexes that were inserted in this
     * GraphView.
     *
     * @return an array Object containing all Vertexes that were inserted in
     * this GraphView
     */
    public abstract Object[] getThisGraphVertexes();

    /**
     * Inspects the given Vertex object and return its label property.
     *
     * @param thisVertex the Vertex object under inspection
     * @return a String containing the label property of the given Vertex
     */
    public abstract String getThisVertexLabel(Object thisVertex);

    /**
     * Inspects the given Vertex object and return its color property.
     *
     * @param thisVertex the Vertex object under inspection
     * @return an Object carrying a {@link java.awt.Color} class with its RGB
     * color representation
     */
    public abstract Object getThisVertexColor(Object thisVertex);

    /**
     * Inspects the given Vertex object and return its horizontal axis position
     * property.
     *
     * @param thisVertex the Vertex object under inspection
     * @return the horizontal axis position for this Vertex
     */
    public abstract double getThisVertexPosition_X(Object thisVertex);

    /**
     * Inspects the given Vertex object and return its vertical axis position
     * property.
     *
     * @param thisVertex the Vertex object under inspection
     * @return the vertical axis position for this Vertex
     */
    public abstract double getThisVertexPosition_Y(Object thisVertex);

    /**
     * Inspects the given Vertex object and return its width property.
     *
     * @param thisVertex the Vertex object under inspection
     * @return the width of this Vertex
     */
    public abstract double getThisVertexWidth(Object thisVertex);

    /**
     * Inspects the given Vertex object and return its height property.
     *
     * @param thisVertex the Vertex object under inspection
     * @return the height of this Vertex
     */
    public abstract double getThisVertexHeight(Object thisVertex);

    /**
     * Builds an array containing all Edges that were inserted in this
     * GraphView.
     *
     * @return an array Object containing all Edges that were inserted in this
     * GraphView
     */
    public abstract Object[] getThisGraphEdges();

    /**
     * Inspects the given Edge object and return its source vertex label
     * property.
     *
     * @param thisEdge the Edge object under inspection
     * @return a String containing the label for this Edge source vertex
     */
    public abstract String getThisEdgeSourceVertexLabel(Object thisEdge);

    /**
     * Inspects the given Edge object and return its target vertex label
     * property.
     *
     * @param thisEdge the Edge object under inspection
     * @return a String containing the label for this Edge target vertex
     */
    public abstract String getThisEdgeTargetVertexLabel(Object thisEdge);
}
