package resources;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class encapsulates a {@link ResourceBundle} for internationalization
 * purposes.
 */
public class ViewInternationalization {

    private Locale currentLocale = null;
    private ResourceBundle messages = null;

    /**
     * Constructs the class using the given file as a {@link ResourceBundle}.
     * <p>A locale code is appended in the end of the given file name. This can
     * be useful for multi-language applications. <p>If no localized message
     * file is available use default language
     *
     * @param propertiesFileName the file containing key X value pairs
     */
    public ViewInternationalization(String propertiesFileName) {
        try {
            //attempts internationalization
            currentLocale = Locale.getDefault();
            messages = ResourceBundle.getBundle(propertiesFileName, currentLocale);
        } catch (Exception e) {
            //localized messages not found. Apply defaults
            currentLocale = new Locale("en", "US");
            messages = ResourceBundle.getBundle(propertiesFileName, currentLocale);
        }
    }

    /**
     * Retrieves a value for the given key.
     *
     * @param strKey the key for the desired value
     * @return the desired value stored in the given key <p><code>null</code> if
     * the key is invalid or has no value stored in it
     */
    public String getString(String strKey) {
        try {
            return messages.getString(strKey);
        } catch (Exception e) {
            return null;
        }
    }
}
