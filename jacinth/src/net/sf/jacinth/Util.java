package net.sf.jacinth;

import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.text.Element;

import net.sf.jacinth.color.HTMLColors;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Util {
    
    /**
     * Make Java color from an html color string.
     * @param color the html color string to decode
     * @param defaultColor the color that we take, if we can't decode the color
     */
    public static Color decodeColor(String color, Color defaultColor) {
        String colorVal = "";
        
        // Look for color in W3C Standarts list
        if (HTMLColors.htmlColors.containsKey(color.toLowerCase()))
            return (Color)HTMLColors.htmlColors.get(color.toLowerCase());

        // Try to get color from number
        if (color.length() > 0) {
            colorVal = color.trim();
            if (colorVal.startsWith("#"))
                colorVal = colorVal.substring(1);            
            try {
                colorVal = new Integer(Integer.parseInt(colorVal, 16)).toString();
                return Color.decode(colorVal.toLowerCase());
            }
            catch (Exception ex) {
//		    ex.printStackTrace();
	    }
        }

        // If nothing goes use default color
        return defaultColor;
    }
    
    public static String encodeColor(Color color) {        
        return "#"+Integer.toHexString(color.getRGB()-0xFF000000).toUpperCase();  
    }

    public static Color decodeColor(String color) {
        return decodeColor(color, Color.white);
    }

    public static void setColorField(JTextField field) {
        Color c = Util.decodeColor(field.getText(), Color.black);
        field.setForeground(c);
        //field.setForeground(new Color(~c.getRGB()));
    }

//    /**
//     * @deprecated
//     */
//    public static String stripTagFromWriterOutput(String str, String tag) {
//        String uCopy = str.toLowerCase();
//        String tagEnd = "</" + tag + ">";
//        
//        int start = uCopy.indexOf("<" + tag);
//        int end = uCopy.lastIndexOf(tagEnd);
//
//        return str.substring(start, end + tagEnd.length());
//    }
//
//    /**
//     * @deprecated
//     */
//    public static String stripTagContentFromWriterOutput(String str, String tag) {
//        String uCopy = str.toLowerCase();
//        int start = uCopy.indexOf("<" + tag);
//        if (start == -1)
//            return null;
//        start = uCopy.indexOf(">", start);
//        int end = uCopy.lastIndexOf("</" + tag + ">");
//        if (end == -1)
//            return null;
//        return str.substring(start + 1, end).trim();
//    }
    
    /**
     * Describes how many of the surrounding tags are of the same type
     * <td>...<td></td>...<td>  => 2
     * <td></td>  => 1
     * 
     * The only known use is as help function for stripTagContentFromWriterOutput
     * 
     * @param element
     */
    private static int relativeDepth(Element element) {
        if (element.getName().equalsIgnoreCase("body"))
            return 1;
        
        int ret = 0;
        Element work = element;

        while (!work.getName().equalsIgnoreCase("body")) {
            if (work.getName().equals(element.getName()))
                ret++;
            work = work.getParentElement();
        }
        
        return ret;
    }
    
    public static String stripTagContentFromWriterOutput(String str, Element element) {
        String uCopy = str.toLowerCase();
        String tag = element.getName();
        int depth = relativeDepth(element);
        
        int start = 0;
        for (int i = 0; i < depth; i++) {
            start = uCopy.indexOf("<" + tag, start + 1);
            if (start == -1)
                return null;
        }
        start = uCopy.indexOf(">", start);

        int end = uCopy.length();
        for (int i = 0; i < depth; i++) {
            end = uCopy.lastIndexOf("</" + tag + ">", end - 1);
            if (end == -1)
                return null;
        }
        
        return str.substring(start + 1, end).trim();
    }
}