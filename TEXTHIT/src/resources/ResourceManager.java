package resources;

import java.io.InputStream;

/**
 * This class accesses the application's jar package. It is used to help other
 * classes to read template and configuration files for example.
 */
public class ResourceManager {

    /**
     * Retrieves the given file from the application's jar package.
     *
     * @param strResName the name of the file to be retrieved
     * @return An {@link InputStream} to the file
     */
    public static InputStream getResourceInputStream(String strResName) {
        InputStream inputStream = ResourceManager.class.getResourceAsStream(strResName);
        return inputStream;
    }
}
