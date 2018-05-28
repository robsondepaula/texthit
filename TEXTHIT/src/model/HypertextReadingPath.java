package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class for an HypertexReadingPath.
 */
public class HypertextReadingPath {

    private List<HypertextNode> readingPath = null;
    private String pathName = null;

    /**
     * Constructs an HypertextReadingPath
     */
    public HypertextReadingPath() {
        readingPath = new ArrayList<HypertextNode>();
    }

    /**
     * This method will insert a given HypertextNode ({@link HypertextNode})
     * into the ReadingPath. <p>HypertextNodes are inserted at the end of a
     * Linear List.
     *
     * @param node The HypertextNode to be added to the ReadingPath
     */
    public void addNode(HypertextNode node) {
        //add node in the end of the list
        readingPath.add(node);
    }

    /**
     * This method will replace an existing HypertextNode
     * ({@link HypertextNode}) at the specified index with the given
     * HypertextNode.
     *
     * @param node The HypertextNode to replace another in the ReadingPath
     * @param index The index of the HypertextNode being replaced in the
     * ReadingPath
     */
    public void replaceNode(HypertextNode node, int index) {
        //add node at the provided index
        readingPath.set(index, node);
    }

    /**
     * This method will remove an existing HypertextNode ({@link HypertextNode})
     * at the specified index. The Linear List is mantained correct by removing
     * the gap left by the removed HypertextNode from the List.
     *
     * @param index The index of the HypertextNode being removed from the
     * ReadingPath
     */
    public void removeNode(int index) {
        readingPath.remove(index);
    }

    /**
     * Builds a list containing all HypertextNodes ({@link HypertextNode}) of
     * this ReadingPath in the order they were inserted.
     *
     * @return a list containing all nodes of this path in order of insertion
     */
    public HypertextNode[] getPath() {
        //retrive this ReadingPath components
        HypertextNode[] path = null;

        int pathSize = readingPath.size();
        if (pathSize > 0) {
            path = new HypertextNode[pathSize];

            for (int i = 0; i < pathSize; i++) {
                path[i] = (HypertextNode) readingPath.get(i);
            }
        }
        return path;
    }

    /**
     * Retrieves the name for this ReadingPath.
     *
     * @return the name for this ReadingPath
     */
    public String getPathName() {
        return pathName;
    }

    /**
     * Updates the name of this ReadingPath with the given one.
     *
     * @param pathName new ReadingPath name
     */
    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    /**
     * Retrieves the number of HypertextNodes ({@link HypertextNode}) for this
     * ReadingPath.
     *
     * @return the number of HypertextNodes in this ReadingPath
     */
    public int size() {
        return readingPath.size();
    }

    /**
     * Retrieve this reading path root node.
     *
     * @return this path's root node <p><code>null</code> if an error occurred
     * or no root node
     */
    public HypertextNode getRootNode() {
        HypertextNode root = null;

        try {
            root = (HypertextNode) readingPath.get(0);
        } catch (Exception e) {
            root = null;
        }

        return root;
    }
}
