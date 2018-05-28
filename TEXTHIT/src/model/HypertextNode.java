package model;

/**
 * Model class for an HypertextNode.
 */
public class HypertextNode {

    public static final int NODE_TYPE_STANDARD = 1;
    public static final int NODE_TYPE_READY_ONLY = 2;
    private String nodeTitle = null;
    private String nodeURL = null;
    private int nodeType = NODE_TYPE_STANDARD;

    /**
     * Constructs a standard HypertextNode with the given Title and URL.
     *
     * @param nodeTitle The title for thi HypertextNode
     * @param nodeURL An URL representing the storage location of this
     * HypertextNode
     */
    public HypertextNode(String nodeTitle, String nodeURL) {
        this.nodeURL = nodeURL;
        this.nodeTitle = nodeTitle;
        this.nodeType = NODE_TYPE_STANDARD;
    }

    /**
     * Constructs an HypertextNode with the given Title, URL and type.
     *
     * @param nodeTitle The title for thi HypertextNode
     * @param nodeURL An URL representing the storage location of this
     * @param nodeType an int specifying this Node's type HypertextNode
     */
    public HypertextNode(String nodeTitle, String nodeURL, int nodeType) {
        this.nodeURL = nodeURL;
        this.nodeTitle = nodeTitle;
        this.nodeType = nodeType;
    }

    /**
     * Retrieves the title for this HypertextNode.
     *
     * @return The node title in String format
     */
    public String getNodeTitle() {
        return nodeTitle;
    }

    /**
     * Insert the given title as the new title for this HypertextNode.
     *
     * @param nodeTitle the new title for this HypertextNode
     */
    public void setNodeTitle(String nodeTitle) {
        this.nodeTitle = nodeTitle;
    }

    /**
     * Returns the URL for this HypertextNode.
     *
     * @return The node URL in String format
     */
    public String getNodeURL() {
        return nodeURL;
    }

    /**
     * Insert the given URL as the new URL for this HypertextNode.
     *
     * @param nodeURL the new URL for this HypertextNode
     */
    public void setNodeURL(String nodeURL) {
        this.nodeURL = nodeURL;
    }

    /**
     * Retrieves the type for this HypertextNode.
     *
     * @return an int corresponding to the Node type.
     */
    public int getNodeType() {
        return nodeType;
    }
}
