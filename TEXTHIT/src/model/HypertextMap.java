package model;

import controller.AuthoringToolWindowController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Main Application Model class.
 */
public class HypertextMap {

    private List<HypertextNode> thisMapNodes = null;
    private List<HypertextLink> thisMapLinks = null;
    private List<HypertextReadingPath> thisMapPaths = null;
    private List<Perspective> thisMapPerspectives = null;
    /**
     * General Map description.
     */
    private String mapDescription = "";

    /**
     * Constructs an empty HypertextMap.
     */
    public HypertextMap() {
        //creates a new Map
        thisMapNodes = new ArrayList<HypertextNode>();
        thisMapLinks = new ArrayList<HypertextLink>();
        thisMapPaths = new ArrayList<HypertextReadingPath>();
        thisMapPerspectives = new ArrayList<Perspective>();
    }

    /**
     * This method will instantiate an HypertextNode ({@link HypertextNode}) of
     * a given title and URL. The instantiated HypertextNode will be inserted in
     * the map.
     *
     * @param nodeTitle A unique title for the HypertextNode
     * @param nodeURL The unique URL for the HypertextNode
     * @return the newly created HypertextNode <p><code>null</code> in case this
     * HypertextNode already exists in the map or an error ocurred
     */
    public synchronized HypertextNode addNode(String nodeTitle, String nodeURL) {
        HypertextNode existingNode = null;

        //traverses the list looking for a colision
        for (Iterator iterNodes = thisMapNodes.iterator(); iterNodes.hasNext();) {
            existingNode = (HypertextNode) (iterNodes.next());
            if (existingNode.getNodeURL().equals(nodeURL)
                    || existingNode.getNodeTitle().equals(nodeTitle)) {
                //a colision ocurred
                return null;
            }
        }


        //adds only nodes with an unique nodeURL and nodeTitle to the map
        HypertextNode node = new HypertextNode(nodeTitle, nodeURL);
        thisMapNodes.add(node);

        return node;
    }

    /**
     * This method will instantiate an HypertextNode ({@link HypertextNode}) of
     * a given title, URL and type. The instantiated HypertextNode will be
     * inserted in the map.
     *
     * @param nodeTitle A unique title for the HypertextNode
     * @param nodeURL The unique URL for the HypertextNode
     * @return the newly created HypertextNode <p><code>null</code> in case this
     * HypertextNode already exists in the map or an error ocurred
     */
    public synchronized HypertextNode addNode(String nodeTitle, String nodeURL,
            int nodeType) {
        HypertextNode existingNode = null;

        //traverses the list looking for a colision
        for (Iterator iterNodes = thisMapNodes.iterator(); iterNodes.hasNext();) {
            existingNode = (HypertextNode) (iterNodes.next());
            if (existingNode.getNodeURL().equals(nodeURL)
                    || existingNode.getNodeTitle().equals(nodeTitle)) {
                //a colision ocurred
                return null;
            }
        }


        //adds only nodes with an unique nodeURL and nodeTitle to the map
        HypertextNode node = new HypertextNode(nodeTitle, nodeURL, nodeType);
        thisMapNodes.add(node);

        return node;
    }

    /**
     * This method will remove an HypertextNode ({@link HypertextNode}) of a
     * given title from the map.
     *
     * @param nodeTitle The title of the HypertextNode to be removed
     */
    public synchronized void removeNode(String nodeTitle) {
        //removes a node from the map
        HypertextNode removableNode = null;
        HypertextLink removableLink = null;

        //traverses the list looking for a match
        for (Iterator iterTarget = thisMapNodes.iterator(); iterTarget.hasNext();) {
            removableNode = (HypertextNode) (iterTarget.next());

            if (removableNode.getNodeTitle().equals(nodeTitle)) {
                //found the target node, remove links as needed
                for (Iterator iterLinks = thisMapLinks.iterator(); iterLinks.hasNext();) {
                    removableLink = (HypertextLink) (iterLinks.next());

                    if ((removableLink.getSource() == removableNode)
                            || (removableLink.getTarget() == removableNode)) {

                        thisMapLinks.remove(removableLink);
                        //updates the iterator
                        iterLinks = thisMapLinks.iterator();
                    }
                }

                //remove the node
                thisMapNodes.remove(removableNode);

                //removal done
                break;
            }
        }
    }

    /**
     * This method will return an HypertextNode ({@link HypertextNode}) of a
     * given title from the map.
     *
     * @param nodeTitle The title of the HypertextNode
     * @return the HypertextNode of the given title <p><code>null</code> when no
     * HypertextNode of the given title exists in the map
     */
    public HypertextNode getNode(String nodeTitle) {
        //traverses the list looking for a match
        for (Iterator iterTarget = thisMapNodes.iterator(); iterTarget.hasNext();) {
            HypertextNode currentNode = (HypertextNode) (iterTarget.next());

            if (currentNode.getNodeTitle().equals(nodeTitle)) {
                //found the target node
                return currentNode;
            }
        }
        return null;
    }

    /**
     * This method will return an HypertextNode ({@link HypertextNode}) of a
     * given URL from the map.
     *
     * @param nodeURL The URL of the HypertextNode
     * @return the HypertextNode of the given title <p><code>null</code> when no
     * HypertextNode of the given URL exists in the map
     */
    public HypertextNode getNodeByURL(String nodeURL) {
        //traverses the list looking for a match
        for (Iterator iterTarget = thisMapNodes.iterator(); iterTarget.hasNext();) {
            HypertextNode currentNode = (HypertextNode) (iterTarget.next());

            if (currentNode.getNodeURL().equals(nodeURL)) {
                //found the target node
                return currentNode;
            }
        }
        return null;
    }

    /**
     * This method will remove from the map an HypertextLink
     * ({@link HypertextLink}).
     *
     * @param sourceTitle The title of the source HypertextNode of the removable
     * HypertextLink
     * @param targetTitle The title of the target HypertextNode of the removable
     * HypertextLink
     */
    public synchronized void removeLink(String sourceTitle, String targetTitle) {
        HypertextNode sourceNode = null;
        HypertextNode targetNode = null;
        HypertextLink removableLink = null;

        //traverses the list looking for the target
        for (Iterator iterTarget = thisMapNodes.iterator(); iterTarget.hasNext();) {
            targetNode = (HypertextNode) (iterTarget.next());

            if (targetNode.getNodeTitle().equals(targetTitle)) {
                //found the target node
                break;
            }
        }

        //traverses the list looking for the source
        for (Iterator iterSource = thisMapNodes.iterator(); iterSource.hasNext();) {
            sourceNode = (HypertextNode) (iterSource.next());

            if (sourceNode.getNodeTitle().equals(sourceTitle) && (targetNode != null)) {
                //found the source node, traverse the links list and remove the correct one
                for (Iterator iterLinks = thisMapLinks.iterator(); iterLinks.hasNext();) {
                    removableLink = (HypertextLink) (iterLinks.next());

                    if ((removableLink.getSource() == sourceNode) && (removableLink.getTarget() == targetNode)) {
                        thisMapLinks.remove(removableLink);
                        //removal done
                        return;
                    }
                }
                //finished traversing the links list and the link wasn`t found
                return;
            }
        }
    }

    /**
     * This method will add an unique HypertextLink ({@link HypertextLink})
     * between a source and a target HypertextNode ({@link HypertextNode}).
     *
     * @param sourceTitle The title for the source HypertextNode
     * @param targetTitle The title for the target HypertextNode
     * @return <code>true</code> on success <p><code>false</code> in case this
     * HypertextLink already exists in the map or an error ocurred
     */
    public synchronized boolean addLink(String sourceTitle, String targetTitle) {
        HypertextNode sourceNode = null;
        HypertextNode targetNode = null;
        HypertextLink existingLink = null;

        //traverses the list looking for a colision
        for (Iterator iterLinks = thisMapLinks.iterator(); iterLinks.hasNext();) {
            existingLink = (HypertextLink) (iterLinks.next());
            if ((existingLink.getSource().getNodeTitle().equals(sourceTitle))
                    && (existingLink.getTarget().getNodeTitle().equals(targetTitle))) {
                //link already exists
                return false;
            }
        }

        //traverses the list looking for the target
        for (Iterator iterTarget = thisMapNodes.iterator(); iterTarget.hasNext();) {
            targetNode = (HypertextNode) (iterTarget.next());

            if (targetNode.getNodeTitle().equals(targetTitle)) {
                //found the target node
                break;
            }
        }

        //traverses the list looking for the source
        for (Iterator iterSource = thisMapNodes.iterator(); iterSource.hasNext();) {
            sourceNode = (HypertextNode) (iterSource.next());

            if (sourceNode.getNodeTitle().equals(sourceTitle)) {
                //found the source node
                if (targetNode != null) {
                    HypertextLink newLink = new HypertextLink(sourceNode, targetNode);
                    thisMapLinks.add(newLink);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    /**
     * Returns a list containing all HypertextNodes ({@link HypertextNode}) from
     * this map except the given one.
     *
     * @param givenNodeTitle The title for the HypertextNode that will not be
     * present in the list of HypertextNodes from this map
     * @return a list containing every HypertextNode in the map except the given
     * one
     */
    public String[] getAvailableLinkDestinations(String givenNodeTitle) {
        try {
            HypertextNode node = null;
            int i = (givenNodeTitle == null) ? (thisMapNodes.size()) : (thisMapNodes.size() - 1);
            //creates the destination list
            String[] targetList = new String[i];

            i = 0;
            //traverses the list
            for (Iterator iter = thisMapNodes.iterator(); iter.hasNext();) {
                node = (HypertextNode) (iter.next());

                String nodeTitle = node.getNodeTitle();
                if (!AuthoringToolWindowController.ORIGINAL_TEXT.equals(nodeTitle) && !(nodeTitle.equals(givenNodeTitle))) {
                    targetList[i] = node.getNodeTitle();
                    i++;
                }
            }

            return (i > 0) ? targetList : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Inspects all HypertextLinks ({@link HypertextLink}) of the given
     * HypertextNode ({@link HypertextNode}). Builds a list of all
     * HypertextNodes connected to the given one.
     *
     * @param sourceNodeTitle The title of the source HypertextNode
     * @return a list containing every destination HypertextNode in the map that
     * has a HypertextLink with the source one <p><code>null</code> if an error
     * occurred or no link destinations available
     */
    public String[] getNodeLinkDestinations(String sourceNodeTitle) {
        HypertextNode node = null;
        List<HypertextLink> linksList = null;
        String[] targetList = null;
        HypertextLink candidateLink = null;

        try {
            //traverses the list
            for (Iterator iter = thisMapNodes.iterator(); iter.hasNext();) {
                node = (HypertextNode) (iter.next());

                if (node.getNodeTitle().equals(sourceNodeTitle)) {
                    //found the source node
                    linksList = new ArrayList<HypertextLink>();

                    //traverses its links list
                    for (Iterator iterLinks = thisMapLinks.iterator(); iterLinks.hasNext();) {
                        candidateLink = (HypertextLink) (iterLinks.next());

                        if (candidateLink.getSource() == node) {
                            //the candidate link connects the source with a target
                            linksList.add(candidateLink);
                        }
                    }
                    break;
                }
            }

            //avoid waste of memory space
            targetList = new String[linksList.size()];

            for (int i = 0; i < targetList.length; i++) {
                HypertextLink tmpLink = (HypertextLink) linksList.get(i);
                targetList[i] = tmpLink.getTarget().getNodeTitle();
            }
        } catch (Exception e) {
            targetList = null;
        }

        //cleanup
        linksList = null;

        return targetList;
    }

    /**
     * Builds a list containing all HypertextNodes ({@link HypertextNode}) of
     * this map that are root nodes of a Reading Path.
     *
     * @return a list containing all root nodes <p><code>null</code> if an error
     * occurred or no root nodes in the map
     */
    public String[] getRootNodes() {
        HypertextReadingPath path = null;
        ArrayList<String> rootNodes = null;
        String[] strRootList = null;
        int i = 0;

        rootNodes = new ArrayList<String>();

        //traverses this map's Reading Path List
        for (Iterator iter = thisMapPaths.iterator(); iter.hasNext();) {
            path = (HypertextReadingPath) (iter.next());
            //add root node title to the list
            rootNodes.add(path.getRootNode().getNodeTitle());
        }

        //avoid waste of memory space
        strRootList = new String[rootNodes.size()];
        for (Iterator iter = rootNodes.iterator(); iter.hasNext();) {
            strRootList[i] = (String) iter.next();
            i++;
        }
        //cleanup
        rootNodes = null;

        return strRootList;
    }

    /**
     * Purge this HypertextMap.
     */
    public synchronized void dispose() {
        try {
            HypertextNode node = null;
            HypertextLink link = null;
            HypertextReadingPath path = null;
            Perspective perspective = null;

            for (Iterator iter = thisMapNodes.iterator(); iter.hasNext();) {
                node = (HypertextNode) (iter.next());
                thisMapNodes.remove(node);
                iter = thisMapNodes.iterator();
            }

            for (Iterator iter = thisMapLinks.iterator(); iter.hasNext();) {
                link = (HypertextLink) (iter.next());
                thisMapLinks.remove(link);
                iter = thisMapLinks.iterator();
            }

            for (Iterator iter = thisMapPaths.iterator(); iter.hasNext();) {
                path = (HypertextReadingPath) (iter.next());
                thisMapPaths.remove(path);
                iter = thisMapPaths.iterator();
            }

            for (Iterator iter = thisMapPerspectives.iterator(); iter.hasNext();) {
                perspective = (Perspective) (iter.next());
                thisMapPerspectives.remove(perspective);
                iter = thisMapPerspectives.iterator();
            }

        } catch (Exception e) {
        }

        thisMapNodes = null;
        thisMapLinks = null;
        thisMapPaths = null;
        thisMapPerspectives = null;
    }

    /**
     * This method will add an HypertextReadingPath
     * ({@link HypertextReadingPath}) to the map.
     *
     * @param newPath The HypertextReadingPath being added to the map
     */
    public synchronized void addPath(HypertextReadingPath newPath) {
        thisMapPaths.add(newPath);
    }

    /**
     * This method will remove a given HypertextReadingPath
     * ({@link HypertextReadingPath}) from the map.
     *
     * @param pathName The HypertextReadingPath being removed from the map
     * @return <code>true</code> if the path was successfuly removed       *<p> <code>false</code> if the path was not found or an error
     * occurred
     */
    public synchronized boolean removePath(String pathName) {
        HypertextReadingPath existingPath = null;

        //traverses the list looking for the removable path
        for (Iterator iterPaths = thisMapPaths.iterator(); iterPaths.hasNext();) {
            existingPath = (HypertextReadingPath) (iterPaths.next());
            if (existingPath.getPathName().equals(pathName)) {
                //found it
                thisMapPaths.remove(existingPath);
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves all HypertextNodes ({@link HypertextNode}) of this map.
     *
     * @return {@link List} containing the HypertextNodes of this map
     */
    public List getThisMapNodes() {
        return thisMapNodes;
    }

    /**
     * Retrieves all HypertextLinks ({@link HypertextLink}) of this map.
     *
     * @return {@link List} containing the HypertextLinks of this map
     */
    public List getThisMapLinks() {
        return thisMapLinks;
    }

    /**
     * Retrieves all HypertextReadingPaths ({@link HypertextReadingPath}) of
     * this map.
     *
     * @return {@link List} containing the HypertextReadingPaths of this map
     */
    public List getThisMapReadingPaths() {
        return thisMapPaths;
    }

    /**
     * This method will retrieve an HypertextReadingPath
     * ({@link HypertextReadingPath}) of the given name from the map.
     *
     * @param readingPathName The name of the HypertextReadingPath
     * @return {@link HypertextReadingPath} of the given name from the map       *<p> <code>null</code> if the path was not found or an error
     * occurred
     */
    public HypertextReadingPath getHypertextReadingPath(String readingPathName) {
        HypertextReadingPath path = null;

        for (Iterator iter = thisMapPaths.iterator(); iter.hasNext();) {
            HypertextReadingPath tmpPath = (HypertextReadingPath) (iter.next());
            if (tmpPath.getPathName().equals(readingPathName)) {
                path = tmpPath;
                break;
            }
        }
        return path;
    }

    /**
     * This method will check if a source HypertextNode (@link HypertextNode) has
     *an HypertextLink ({@link HypertextLink}) with a target HypertextNode.
     *
     * @param sourceNode The source HypertextNode
     * @param targetNode The target HypertextNode
     * @return <code>true</code> if the HypertextNodes are connected by an
     * HypertextLink <p><code>false</code> if no connection exists
     */
    public boolean linkBetweenNodesExist(HypertextNode sourceNode, HypertextNode targetNode) {
        HypertextLink link = null;

        //traverses this map links looking for a match
        for (Iterator iter = thisMapLinks.iterator(); iter.hasNext();) {
            link = (HypertextLink) (iter.next());
            if (nodeEquals(link.getSource(), sourceNode)
                    && nodeEquals(link.getTarget(), targetNode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method will check if an HypertextNode ({@link HypertextNode}) "A"
     * equals an HypertextNode "B". Equals in the same context of the
     * <code>String.equals()</code> method.
     *
     * @param nodeA The A HypertextNode
     * @param nodeB The B HypertextNode
     * @return <code>true</code> if A equals B <p><code>false</code> if A is not
     * equal B
     */
    private boolean nodeEquals(HypertextNode nodeA, HypertextNode nodeB) {
        if (nodeA.getNodeTitle().equals(nodeB.getNodeTitle())
                && nodeA.getNodeURL().equals(nodeB.getNodeURL())) {
            return true;
        }
        return false;
    }

    /**
     * Retrieves the description for this HypertextMap.
     *
     * @return The HypertextMap description in String format
     */
    public String getMapDescription() {
        return mapDescription;
    }

    /**
     * Insert the given description as the new description for this
     * HypertextMap.
     *
     * @param mapDescription the new description for this HypertextMap
     */
    public void setMapDescription(String mapDescription) {
        this.mapDescription = mapDescription;
    }

    /**
     * Insert the given Perspective ({@link Perspective}) in the given index on
     * this HypertextMap.
     *
     * @param index the index for the perspective
     * @param perspective the perspective to be inserted
     */
    public void addPerspective(int index, Perspective perspective) {
        thisMapPerspectives.add(index, perspective);
    }

    /**
     * Update the Perspective ({@link Perspective}) in the given index on this
     * HypertextMap with the given information.
     *
     * @param index the index for the perspective
     * @param perspective the updated perspective
     */
    public void updatePerspective(int index, Perspective perspective) {
        thisMapPerspectives.set(index, perspective);
    }

    /**
     * Remove the Perspective ({@link Perspective}) on the given index off this
     * HypertextMap.
     *
     * @param index the index for the perspective being remove
     */
    public void removePerspective(int index) {
        thisMapPerspectives.remove(index);
    }

    /**
     * Retrieves all Perspectives ({@link Perspective}) of this map.
     *
     * @return {@link List} containing the Perspectives of this map
     */
    public List getThisMapPerspectives() {
        return thisMapPerspectives;
    }
}
