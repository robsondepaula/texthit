/*
 * Created on 16.01.2007
 * @author Stephan Richard Palm
 * Copyright by Stephan Richard Palm
 * 
 * 
 * Colors after the CSS-3-specification.
 */
package net.sf.jacinth.color;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HTMLColors {

    public static Map htmlColors = new HashMap();
    public static Map htmlColorsOrg = new HashMap(){{
        put("AliceBlue", "#F0F8FF");
        put("AntiqueWhite", "#FAEBD7");
        put("Aqua", "#00FFFF");
        put("Aquamarine", "#7FFFD4");
        put("Azure", "#F0FFFF");
        put("Beige", "#F5F5DC");
        put("Bisque", "#FFE4C4");
        put("Black", "#000000");
        put("BlanchedAlmond", "#FFEBCD");
        put("Blue", "#0000FF");
        put("BlueViolet", "#8A2BE2");
        put("Brown", "#A52A2A");
        put("BurlyWood", "#DEB887");
        put("CadetBlue", "#5F9EA0");
        put("Chartreuse", "#7FFF00");
        put("Chocolate", "#D2691E");
        put("Coral", "#FF7F50");
        put("CornflowerBlue", "#6495ED");
        put("Cornsilk", "#FFF8DC");
        put("Crimson", "#DC143C");
        put("Cyan", "#00FFFF");
        put("DarkBlue", "#00008B");
        put("DarkCyan", "#008B8B");
        put("DarkGoldenrod", "#B8860B");
        put("DarkGray", "#A9A9A9");
        put("DarkGreen", "#006400");
        put("DarkKhaki", "#BDB76B");
        put("DarkMagenta", "#8B008B");
        put("DarkOliveGreen", "#556B2F");
        put("DarkOrange", "#FF8C00");
        put("DarkOrchid", "#9932CC");
        put("DarkRed", "#8B0000");
        put("DarkSalmon", "#E9967A");
        put("DarkSeaGreen", "#8FBC8F");
        put("DarkSlateBlue", "#483D8B");
        put("DarkSlateGray", "#2F4F4F");
        put("DarkTurquoise", "#00CED1");
        put("DarkViolet", "#9400D3");
        put("DeepPink", "#FF1493");
        put("DeepSkyBlue", "#00BFFF");
        put("DimGray", "#696969");
        put("DodgerBlue", "#1E90FF");
        put("FireBrick", "#B22222");
        put("FloralWhite", "#FFFAF0");
        put("ForestGreen", "#228B22");
        put("Fuchsia", "#FF00FF");
        put("Gainsboro", "#DCDCDC");
        put("GhostWhite", "#F8F8FF");
        put("Gold", "#FFD700");
        put("Goldenrod", "#DAA520");
        put("Gray", "#808080");
        put("Green", "#008000");
        put("GreenYellow", "#ADFF2F");
        put("Honeydew", "#F0FFF0");
        put("HotPink", "#FF69B4");
        put("IndianRed", "#CD5C5C");
        put("Indigo", "#4B0082");
        put("Ivory", "#FFFFF0");
        put("Khaki", "#F0E68C");
        put("Lavender", "#E6E6FA");
        put("LavenderBlush", "#FFF0F5");
        put("LawnGreen", "#7CFC00");
        put("LemonChiffon", "#FFFACD");
        put("LightBlue", "#ADD8E6");
        put("LightCoral", "#F08080");
        put("LightCyan", "#E0FFFF");
        put("LightGoldenrodYellow", "#FAFAD2");
        put("LightGreen", "#90EE90");
        put("LightGray", "#D3D3D3");
        put("LightPink", "#FFB6C1");
        put("LightSalmon", "#FFA07A");
        put("LightSeaGreen", "#20B2AA");
        put("LightSkyBlue", "#87CEFA");
        put("LightSlateGray", "#778899");
        put("LightSteelBlue", "#B0C4DE");
        put("LightYellow", "#FFFFE0");
        put("Lime", "#00FF00");
        put("LimeGreen", "#32CD32");
        put("Linen", "#FAF0E6");
        put("Magenta", "#FF00FF");
        put("Maroon", "#800000");
        put("MediumAquamarine", "#66CDAA");
        put("MediumBlue", "#0000CD");
        put("MediumOrchid", "#BA55D3");
        put("MediumPurple", "#9370DB");
        put("MediumSeaGreen", "#3CB371");
        put("MediumSlateBlue", "#7B68EE");
        put("MediumSpringGreen", "#00FA9A");
        put("MediumTurquoise", "#48D1CC");
        put("MediumVioletRed", "#C71585");
        put("MidnightBlue", "#191970");
        put("MintCream", "#F5FFFA");
        put("MistyRose", "#FFE4E1");
        put("Moccasin", "#FFE4B5");
        put("NavajoWhite", "#FFDEAD");
        put("Navy", "#000080");
        put("OldLace", "#FDF5E6");
        put("Olive", "#808000");
        put("OliveDrab", "#6B8E23");
        put("Orange", "#FFA500");
        put("OrangeRed", "#FF4500");
        put("Orchid", "#DA70D6");
        put("PaleGoldenrod", "#EEE8AA");
        put("PaleGreen", "#98FB98");
        put("PaleTurquoise", "#AFEEEE");
        put("PaleVioletRed", "#DB7093");
        put("PapayaWhip", "#FFEFD5");
        put("PeachPuff", "#FFDAB9");
        put("Peru", "#CD853F");
        put("Pink", "#FFC0CB");
        put("Plum", "#DDA0DD");
        put("PowderBlue", "#B0E0E6");
        put("Purple", "#800080");
        put("Red", "#FF0000");
        put("RosyBrown", "#BC8F8F");
        put("RoyalBlue", "#4169E1");
        put("SaddleBrown", "#8B4513");
        put("Salmon", "#FA8072");
        put("SandyBrown", "#F4A460");
        put("SeaGreen", "#2E8B57");
        put("Seashell", "#FFF5EE");
        put("Sienna", "#A0522D");
        put("Silver", "#C0C0C0");
        put("SkyBlue", "#87CEEB");
        put("SlateBlue", "#6A5ACD");
        put("SlateGray", "#708090");
        put("Snow", "#FFFAFA");
        put("SpringGreen", "#00FF7F");
        put("SteelBlue", "#4682B4");
        put("Tan", "#D2B48C");
        put("Teal", "#008080");
        put("Thistle", "#D8BFD8");
        put("Tomato", "#FF6347");
        put("Turquoise", "#40E0D0");
        put("Violet", "#EE82EE");
        put("Wheat", "#F5DEB3");
        put("White", "#FFFFFF");
        put("WhiteSmoke", "#F5F5F5");
        put("Yellow", "#FFFF00");
        put("YellowGreen", "#9ACD32");
        
        // Alternative writing
        put("DarkGrey", "#A9A9A9");
        put("DarkSlateGrey", "#2F4F4F");
        put("DimGrey", "#696969");
        put("Grey", "#808080");
        put("LightGrey", "#D3D3D3");
        put("LightSlateGrey", "#778899");
        put("SlateGrey", "#708090");
    }};
    
    static {
        Iterator i = htmlColorsOrg.entrySet().iterator(); 
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            htmlColors.put(entry.getKey().toString().toLowerCase(), Color.decode("" + Integer.parseInt(entry.getValue().toString().substring(1), 16)));
        }
    }
    
}
