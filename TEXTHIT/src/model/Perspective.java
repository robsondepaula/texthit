package model;

/**
 * Model class for a Perspective.
 */
public class Perspective {

    Object color = null;
    String label = null;

    /**
     * Constructs a Perspective with the given color and label.
     *
     * @param color Associated color of this perspective
     * @param label Label that describes this perspective
     */
    public Perspective(Object color, String label) {
        this.color = color;
        this.label = label;
    }

    /**
     * Retrieves the object for this perspective
     *
     * @return an Object containing the color for this Perspective
     */
    public Object getColor() {
        return color;
    }

    /**
     * Retrieves the label for this Perspective
     *
     * @return a String containing the label for this Perspective
     */
    public String getLabel() {
        return label;
    }
}