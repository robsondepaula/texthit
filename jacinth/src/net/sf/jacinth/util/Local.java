/**
 * Local.java
 * Created on 05.09.2003, 16:43:39 Alex
 * Package: org.openmechanics.htmleditor
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 OpenMechanics.org
 *
 * Modified to add ResourceBundle based internationalization support.
 * @author Robson de Paula, depaula@dca.fee.unicamp.br
 * on 03/03/2007
 */
package net.sf.jacinth.util;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *This class encapsulates a {@link ResourceBundle} for internationalization
 *purposes.
 */
public class Local {
    private static Locale currentLocale = null;
    private static ResourceBundle messages = null;
    
    /**
     *Retrieves a value for the given key.
     *@param key the key for the desired value
     *@return the desired value stored in the given key
     *<p>the given key String if the MessagesBundle has no value stored for it
     */
    public static String getString(String key) {
        if (messages == null){
            try{
                //attempts internationalization
                currentLocale = Locale.getDefault();
                messages =
                        ResourceBundle.getBundle("net.sf.jacinth.util.MessagesBundle",
                        currentLocale);
            }catch(Exception e){
                //Resource Bundle file not found
                messages = null;
            }
        }
        
        try{
            return messages.getString(key);
        }catch(Exception e){
            //internationalization failed, apply defaults
            return key;
        }
    }
}
