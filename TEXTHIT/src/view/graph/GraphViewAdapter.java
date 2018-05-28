package view.graph;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;

//API imports
import org.jgraph.JGraph;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

/**
 * Concrete GraphView.
 */
public class GraphViewAdapter extends GraphView {

    private GraphModel graphModel = null;
    private GraphLayoutCache graphView = null;
    private JGraph graph = null;
    private Point clickedVertexPoint = null;
    private Point clickedEdgePoint = null;

    @Override
    public Object getDisplayable() {
        return graph;
    }

    @Override
    public void initializeGraph() {
        //graphical representation of the model
        graphModel = new DefaultGraphModel();
        graphView = new GraphLayoutCache(graphModel,
                new DefaultCellViewFactory(),
                true);

        graph = new JGraph(graphModel, graphView);

        graph.updateUI();

        //uneditable jgraph
        graph.setEditable(false);

        //performance tunning and disabling unused and resource intensive features
        //graph.setPortsVisible(false);
        //graph.setGridEnabled(false);
        //graph.setTolerance(2);
        //graph.setDoubleBuffered(false);


        //individual node selection when inserted
        graph.getGraphLayoutCache().setSelectsAllInsertedCells(false);
        graph.getGraphLayoutCache().setSelectsLocalInsertedCells(false);

        //add graphical representation controller
        graph.addGraphSelectionListener(graphSelectionListener());

        //add customized marquee handler
        graph.setMarqueeHandler(new MyMarqueeHandler());
    }

    @Override
    public void stopGraph() {
        //clear graph objects
        clearGraph();

        //finishes the graph engine
        graphModel = null;
        graphView = null;
        graph = null;
    }

    /**
     * Perform cleanup removing all inserted Vertexes and Edges from the
     * GraphView.
     */
    private void clearGraph() {
        //removes all vertex and edges from the graph
        //getCells(boolean groups, boolean vertexes, boolean ports, boolean edges)
        Object[] cells = graph.getGraphLayoutCache().getCells(false, true, false, true);
        cells = graph.getDescendants(cells);
        graph.getModel().remove(cells);
        graph.getGraphLayoutCache().removeCells(cells);
    }

    @Override
    public void addVertex(final String vertexLabel) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //inserting a Vertex
                DefaultGraphCell cell = createVertex(vertexLabel,
                        20,
                        20 * getNumVertexes(), //poor man's layout
                        20,
                        20,
                        //default color is light gray
                        Color.LIGHT_GRAY);

                graph.getGraphLayoutCache().insert(cell);

                //vertex added
                if (getNumVertexes() > 1) {
                    setChanged();
                    notifyObservers(EVT_VERTEX_MAY_LINK);
                } else {
                    setChanged();
                    notifyObservers(EVT_VERTEX_ADDED);
                }
            }
        });
    }

    @Override
    public void addVertex(final String vertexLabel,
            final double xPosition,
            final double yPosition,
            final double width,
            final double height,
            final int colorR,
            final int colorG,
            final int colorB) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //intantiate the vertex color
                Color vertexColor = new Color(colorR, colorG, colorB);

                //inserting a Vertex
                DefaultGraphCell cell = createVertex(vertexLabel,
                        xPosition,
                        yPosition,
                        width,
                        height,
                        vertexColor);

                graph.getGraphLayoutCache().insert(cell);

                //vertex added
                if (getNumVertexes() > 1) {
                    setChanged();
                    notifyObservers(EVT_VERTEX_MAY_LINK);
                } else {
                    setChanged();
                    notifyObservers(EVT_VERTEX_ADDED);
                }
            }
        });
    }

    @Override
    public void updateVertexLabel(final String oldLabel, final String newLabel) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //get all Vertexes
                Object[] cells = graph.getGraphLayoutCache().getCells(false,
                        true, false, false);

                //find the target Vertex
                for (int i = 0; i < cells.length; i++) {
                    if (cells[i].toString().equals(oldLabel)) {
                        //updates the vertex label
                        DefaultGraphCell cell = (DefaultGraphCell) cells[i];

                        Map map = new HashMap();
                        GraphConstants.setValue(map, newLabel);
                        Map<DefaultGraphCell, Map> nested =
                                new HashMap<DefaultGraphCell, Map>();
                        nested.put(cell, map);
                        graph.getGraphLayoutCache().edit(nested);

                        //update
                        graph.repaint();
                        return;
                    }
                }
            }
        });
    }

    @Override
    public void addEdge(final String sourceVertexLabel,
            final String targetVertexLabel) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //get all Vertexes
                Object[] cells = graph.getGraphLayoutCache().getCells(false, true, false, false);
                Object sourceEdge = null;

                //find the source Vertex
                for (int i = 0; i < cells.length; i++) {
                    if (cells[i].toString().equals(sourceVertexLabel)) {
                        sourceEdge = cells[i];
                        break;
                    }
                }

                if (sourceEdge != null) {
                    //find the target Vertex
                    for (int i = 0; i < cells.length; i++) {
                        if (cells[i].toString().equals(targetVertexLabel)) {
                            //creates the new Edge
                            DefaultEdge newEdge = createEdge((DefaultGraphCell) sourceEdge, (DefaultGraphCell) cells[i]);

                            //add the newly created Edge to the graph
                            graph.getGraphLayoutCache().insert(newEdge);

                            //edge added
                            setChanged();
                            notifyObservers(EVT_EDGE_ADDED);
                            return;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void addEdgeFromSelectedSource(final String targetLabel) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //currently selected Vertex is the source Vertex
                Object cell = graph.getSelectionCell();

                if (cell != null) {
                    //get all Vertexes
                    Object[] cells = graph.getGraphLayoutCache().getCells(false,
                            true, false, false);

                    //find the target Vertex
                    for (int i = 0; i < cells.length; i++) {
                        if (cells[i].toString().equals(targetLabel)) {
                            //creates the new Edge
                            DefaultEdge edge = createEdge((DefaultGraphCell) cell,
                                    (DefaultGraphCell) cells[i]);
                            //add the newly created Edge to the graph
                            graph.getGraphLayoutCache().insert(edge);

                            //edge added
                            setChanged();
                            notifyObservers(EVT_EDGE_ADDED);
                            return;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void removeSelectedVertex() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //remove this Vertex Edges as necessary
                if (!graph.isSelectionEmpty()) {
                    //getCells(boolean groups, boolean vertexes, boolean ports, boolean edges)
                    Object[] cells = graph.getGraphLayoutCache().getCells(false, false, false, true);

                    //finds all Edges that has this Vertex as a source or target
                    for (int i = 0; i < cells.length; i++) {
                        Edge tmpEdge = (Edge) cells[i];
                        DefaultGraphCell source = (DefaultGraphCell) tmpEdge.getSource();
                        DefaultGraphCell target = (DefaultGraphCell) tmpEdge.getTarget();

                        if (source.getParent().toString().equals(getSelectedVertex().toString())
                                || target.getParent().toString().equals(getSelectedVertex().toString())) {
                            Object[] removableVertex = new Object[1];
                            removableVertex[0] = cells[i];
                            graph.getModel().remove(removableVertex);
                        }
                    }

                    //remove the Vertex
                    cells = graph.getSelectionCells();
                    cells = graph.getDescendants(cells);
                    graph.getModel().remove(cells);

                    if (getNumVertexes() == 0) {
                        //all vertexes removed
                        setChanged();
                        notifyObservers(EVT_ALL_VERTEXES_REMOVED);

                    }

                    if (getNumEdges() == 0) {
                        //all edges removed
                        setChanged();
                        notifyObservers(EVT_ALL_EDGES_REMOVED);
                    }
                }
            }
        });
    }

    @Override
    public void removeSelectedEdge() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //removes the currently selected Edge from the graph
                if (!graph.isSelectionEmpty()) {
                    Object[] cells = graph.getSelectionCells();
                    cells = graph.getDescendants(cells);
                    graph.getModel().remove(cells);

                    if (getNumEdges() == 0) {
                        //all edges removed
                        setChanged();
                        notifyObservers(EVT_ALL_EDGES_REMOVED);
                    }
                }
            }
        });
    }

    @Override
    public void removeEdge(final String srcVertex, final String tgtVertex) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //removes the given Edge from the graph
                Object[] cells = graph.getGraphLayoutCache().getCells(false, false, false, true);

                //find the Edge of the given source and target
                for (int i = 0; i < cells.length; i++) {
                    Edge tmpEdge = (Edge) cells[i];
                    DefaultGraphCell source = (DefaultGraphCell) tmpEdge.getSource();
                    DefaultGraphCell target = (DefaultGraphCell) tmpEdge.getTarget();

                    if (source.getParent().toString().equals(srcVertex)
                            && target.getParent().toString().equals(tgtVertex)) {
                        Object[] removableEdge = new Object[1];
                        removableEdge[0] = cells[i];
                        graph.getModel().remove(removableEdge);
                    }
                }

                if (getNumEdges() == 0) {
                    //all edges removed
                    setChanged();
                    notifyObservers(EVT_ALL_EDGES_REMOVED);
                }
            }
        });
    }

    @Override
    public String getSelectedVertexString() {
        try {
            return getSelectedVertex().toString();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getSelectedEdgeSourceString() {
        try {
            DefaultGraphCell source = (DefaultGraphCell) getSelectedEdge().getSource();
            return source.getParent().toString();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getSelectedEdgeTargetString() {
        try {
            DefaultGraphCell target = (DefaultGraphCell) getSelectedEdge().getTarget();

            return target.getParent().toString();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Object getLastClickedVertexPoint() {
        return clickedVertexPoint;
    }

    @Override
    public Object getLastClickedEdgePoint() {
        return clickedEdgePoint;
    }

    @Override
    public Object[] getThisGraphEdges() {
        //getCells(boolean groups, boolean vertixes, boolean ports, boolean edges)
        Object[] thisGraphEdges = graph.getGraphLayoutCache().getCells(false, false, false, true);

        return thisGraphEdges;
    }

    @Override
    public String getThisEdgeSourceVertexLabel(Object thisEdge) {
        try {
            Edge source = (Edge) thisEdge;
            DefaultGraphCell tmpCell = (DefaultGraphCell) source.getSource();

            return tmpCell.getParent().toString();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getThisEdgeTargetVertexLabel(Object thisEdge) {
        try {
            Edge source = (Edge) thisEdge;
            DefaultGraphCell tmpCell = (DefaultGraphCell) source.getTarget();

            return tmpCell.getParent().toString();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Object[] getThisGraphVertexes() {
        //getCells(boolean groups, boolean vertixes, boolean ports, boolean edges)
        Object[] thisGraphVertexes = graph.getGraphLayoutCache().getCells(false, true, false, false);

        return thisGraphVertexes;
    }

    @Override
    public String getThisVertexLabel(Object thisVertex) {
        try {
            DefaultGraphCell tmpCell = (DefaultGraphCell) thisVertex;

            return tmpCell.toString();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Object getThisVertexColor(Object thisVertex) {
        try {
            DefaultGraphCell tmpCell = (DefaultGraphCell) thisVertex;

            return tmpCell.getAttributes().get(GraphConstants.GRADIENTCOLOR);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setSelectedVertexColor(final int colorR,
            final int colorG,
            final int colorB) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (!graph.isSelectionEmpty()) {
                    //return currently selected Vertex
                    Object cell = graph.getSelectionCell();

                    //intantiate the vertex color
                    Color vertexColor = new Color(colorR, colorG, colorB);
                    try {
                        Map map = new HashMap();
                        GraphConstants.setGradientColor(map, vertexColor);
                        graph.getGraphLayoutCache().edit(new Object[]{cell},
                                map);

                        //update
                        graph.repaint();
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override
    public double getThisVertexPosition_X(Object thisVertex) {
        try {
            DefaultGraphCell tmpCell = (DefaultGraphCell) thisVertex;

            return graph.getCellBounds(tmpCell).getX();
        } catch (Exception e) {
            return 0.0;
        }
    }

    @Override
    public double getThisVertexPosition_Y(Object thisVertex) {
        try {
            DefaultGraphCell tmpCell = (DefaultGraphCell) thisVertex;

            return graph.getCellBounds(tmpCell).getY();
        } catch (Exception e) {
            return 0.0;
        }
    }

    @Override
    public double getThisVertexWidth(Object thisVertex) {
        try {
            DefaultGraphCell tmpCell = (DefaultGraphCell) thisVertex;

            return graph.getCellBounds(tmpCell).getWidth();
        } catch (Exception e) {
            return 0.0;
        }
    }

    @Override
    public double getThisVertexHeight(Object thisVertex) {
        try {
            DefaultGraphCell tmpCell = (DefaultGraphCell) thisVertex;

            return graph.getCellBounds(tmpCell).getHeight();
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * Installs a listener that allows the GraphView to notify its Observers of
     * important asynchronous events.
     *
     * @see #EVT_SELECTION_CLEARED
     * @see #EVT_VERTEX_MAY_LINK
     * @see #EVT_VERTEX_CANT_LINK
     * @see #EVT_EDGE_SELECTED
     */
    private GraphSelectionListener graphSelectionListener() {
        return new GraphSelectionListener() {
            @Override
            public void valueChanged(GraphSelectionEvent evt) {
                //return currently selected Cell
                Object cell = graph.getSelectionCell();

                //selection cleared
                if (cell == null) {
                    setChanged();
                    notifyObservers(EVT_SELECTION_CLEARED);
                    return;
                }

                //node actions updates
                if ((cell != null)
                        && (!(cell instanceof Edge))) {
                    if (getNumVertexes() > 1) {
                        setChanged();
                        notifyObservers(EVT_VERTEX_MAY_LINK);
                        return;
                    } else {
                        setChanged();
                        notifyObservers(EVT_VERTEX_CANT_LINK);
                        return;
                    }
                }

                //link actions updates
                if ((cell != null)
                        && (cell instanceof Edge)) {
                    setChanged();
                    notifyObservers(EVT_EDGE_SELECTED);
                    return;
                }
            }
        };
    }

    /**
     * Returns the currently selected Edge in the GraphView.
     *
     * @return the Edge that is currently selected <p><code>null</code> if an
     * error occurred or no Edge selected
     */
    private Edge getSelectedEdge() {
        //return currently selected Cell
        Object cell = graph.getSelectionCell();

        if ((cell != null) && (cell instanceof Edge)) {
            return (Edge) cell;
        }
        return null;
    }

    /**
     * Returns the currently selected Vertex in the GraphView.
     *
     * @return the Vertex that is currently selected <p><code>null</code> if an
     * error occurred or no Vertex selected
     */
    private Object getSelectedVertex() {
        //return currently selected Vertex
        Object cell = graph.getSelectionCell();

        if ((cell != null)
                && (!(cell instanceof Edge))) {
            return cell;
        }
        return null;
    }

    /**
     * Returns the number of Edges inserted in this GraphView.
     *
     * @return an int containing the number of Edges inserted is this GraphView
     */
    private int getNumEdges() {
        //getCells(boolean groups, boolean vertexes, boolean ports, boolean edges)
        Object[] cells = graph.getGraphLayoutCache().getCells(false, false, false, true);

        return cells.length;
    }

    /**
     * Returns the number of Vertexes inserted in this GraphView.
     *
     * @return an int containing the number of Vertexes inserted is this
     * GraphView
     */
    private int getNumVertexes() {
        //getCells(boolean groups, boolean verteces, boolean ports, boolean edges)
        Object[] cells = graph.getGraphLayoutCache().getCells(false, true, false, false);

        return cells.length;
    }

    /**
     * Create an Edge that connects the given source Vertex to the given target
     * Vertex.
     *
     * @param source the originating Vertex
     * @param target the destination Vertex
     * @return the newly created Edge that connects source and target
     */
    private DefaultEdge createEdge(DefaultGraphCell source, DefaultGraphCell target) {
        DefaultEdge edge = new DefaultEdge();

        // Set Arrow Style for edge
        int arrow = GraphConstants.ARROW_CLASSIC;
        GraphConstants.setLineEnd(edge.getAttributes(), arrow);
        GraphConstants.setEndFill(edge.getAttributes(), true);

        GraphConstants.setEditable(edge.getAttributes(), false);
        GraphConstants.setDisconnectable(edge.getAttributes(), false);

        edge.setSource(source.getChildAt(0));
        edge.setTarget(target.getChildAt(0));

        return edge;
    }

    /**
     * Create a Vertex with the given properties.
     *
     * @param name a String containing the desired label for the Vertex
     * @param x the desired horizontal axis Vertex position
     * @param y the desired vertical axis Vertex position
     * @param w the desired width for the Vertex
     * @param h the desired height for the Vertex
     * @param vertexColor the desired RGB {@link Color} for the Vertex
     * @return the newly created Vertex
     */
    private DefaultGraphCell createVertex(String name, double x,
            double y, double w, double h, Color vertexColor) {

        // Create vertex with the given name
        DefaultGraphCell cell = new DefaultGraphCell(name);

        // Set bounds
        GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(
                x, y, w, h));

        //AutoResize
        GraphConstants.setAutoSize(cell.getAttributes(), true);
        GraphConstants.setResize(cell.getAttributes(), true);

        GraphConstants.setGradientColor(cell.getAttributes(), vertexColor);
        GraphConstants.setOpaque(cell.getAttributes(), true);


        // Set a border
        GraphConstants.setBorder(cell.getAttributes(),
                BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.black),
                BorderFactory.createRaisedBevelBorder()));


        // Add a Floating Port
        cell.addPort();

        return cell;
    }

    /**
     * Constructs a custom handler for user interaction detection. Notifies the
     * GraphView observers of important events.
     *
     * @see #EVT_RIGHT_MOUSE_BUTTON_IN_VERTEX
     * @see #EVT_RIGHT_MOUSE_BUTTON_IN_EDGE
     * @see #EVT_DOUBLE_CLICK_VERTEX
     */
    private class MyMarqueeHandler extends BasicMarqueeHandler {

        @Override
        public boolean isForceMarqueeEvent(MouseEvent e) {
            // if right mouse button we want to notify the observer
            if (SwingUtilities.isRightMouseButton(e)) {
                // return immediately
                return true;
            } else {
                //we want to notify the observer about double clicks
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (e.getClickCount() == 2) {
                        // return immediately
                        return true;
                    }
                }
                // call superclass
                return super.isForceMarqueeEvent(e);
            }
        }

        /**
         * Listens for right mouse button clicks
         */
        @Override
        public void mousePressed(final MouseEvent e) {
            Object cell = null;
            clickedVertexPoint = null;
            clickedEdgePoint = null;

            // if right mouse button
            if (SwingUtilities.isRightMouseButton(e)) {
                // find cell in graphmodel coordinates
                cell = graph.getFirstCellForLocation(e.getX(), e.getY());

                if ((cell != null)
                        && (!(cell instanceof Edge))) {
                    //stores the location where the last vertex was clicked
                    clickedVertexPoint = e.getPoint();

                    setChanged();
                    notifyObservers(EVT_RIGHT_MOUSE_BUTTON_IN_VERTEX);
                    return;
                }

                if ((cell != null)
                        && (cell instanceof Edge)) {
                    //stores the location where the last vertex was clicked
                    clickedEdgePoint = e.getPoint();

                    setChanged();
                    notifyObservers(EVT_RIGHT_MOUSE_BUTTON_IN_EDGE);
                    return;
                }
            } else {
                cell = graph.getFirstCellForLocation(e.getX(), e.getY());

                //notify the observer about vertex double clicks
                if ((e.getClickCount() == 2) && (cell != null)
                        && (!(cell instanceof Edge))) {
                    setChanged();
                    notifyObservers(EVT_DOUBLE_CLICK_VERTEX);
                    return;
                }

                // call superclass
                super.mousePressed(e);
            }
        }
    }
}
