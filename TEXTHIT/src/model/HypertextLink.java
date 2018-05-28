package model;

/**
 * Model class for an HypertextLink.
 */
public class HypertextLink {

    HypertextNode source = null;
    HypertextNode target = null;

    /**
     * Constructs an HypertextLink with the given Source and Destination
     * HypertextNodes ({@link HypertextNode}).
     *
     * @param source Originating HypertextNode
     * @param target Terminating HypertextNode
     */
    public HypertextLink(HypertextNode source, HypertextNode target) {
        this.source = source;
        this.target = target;
    }

    /**
     * Returns the source HypertextNode ({@link HypertextNode}) object for this
     * HypertextLink
     *
     * @return the source HypertextNode for this HypertextLink
     */
    public HypertextNode getSource() {
        return source;
    }

    /**
     * Returns the target HypertextNode ({@link HypertextNode}) object for this
     * HypertextLink
     *
     * @return the target HypertextNode for this HypertextLink
     */
    public HypertextNode getTarget() {
        return target;
    }
}
