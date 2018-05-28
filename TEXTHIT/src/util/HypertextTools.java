package util;

import java.io.File;
import java.net.URI;
import java.net.URL;
import util.xhtml.XHTMLManager;
import util.xhtml.XHTMLManagerFactory;

/**
 * This class performs transcoding of the XHTML and HTML languages.
 */
public class HypertextTools {

    private static XHTMLManagerFactory xMngFactory = null;
    private static XHTMLManager xMng = null;

    /**
     * Convert XHTML to HTML code.
     *
     * @param strSrcPath the absolute path to the source XHTML file
     * @return a String containing the transformed HTML code
     * <p><code>null</code> if an error occurred
     */
    public static String transformXHTML_To_HTML(String strSrcPath) {
        //instantiate factory only once
        if (xMngFactory == null) {
            xMngFactory = new XHTMLManagerFactory();
        }
        //instantiate adapter only once
        if (xMng == null) {
            xMng = xMngFactory.create();
        }

        return xMng.transformXHTML_To_HTML(strSrcPath);
    }

    /**
     * Transcode HTML code to XHTML.
     *
     * @param strSrcCode a String containing the HTML sourcecode
     * @return a String containing the transcoded XHTML code
     * <p><code>null</code> if an error occurred
     */
    public static String transformHTML_To_XHTML(String strSrcCode) {
        //instantiate factory only once
        if (xMngFactory == null) {
            xMngFactory = new XHTMLManagerFactory();
        }
        //instantiate adapter only once
        if (xMng == null) {
            xMng = xMngFactory.create();
        }

        return xMng.transformHTML_To_XHTML(strSrcCode);
    }

    /**
     * Constructs an URL to access a file from a web browser application.
     *
     * @param strPath the file system absolute path
     * @return a valid URL to access the given file from a web browser
     * application. <p><code>null</code> if an error occurred
     */
    public static String filePathToURL(String strPath) {
        // convert the file object to a URL
        URL url = null;
        try {
            // create a file object
            File file = new File(strPath);
            // the file need not exist
            url = file.toURI().toURL();
            //encode all the non-US characters
            URI uri = new URI(url.toString());
            return uri.toASCIIString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Obtain a filepath from the given URL.
     *
     * @param strURL a valid URL to access the given file from a web browser
     * application.
     * @return the file system absolute path <p><code>null</code> if an error
     * occurred
     */
    public static String urlToFilePath(String strURL) {
        // convert the URL to a file system absolute path
        try {
            // create a file object
            File file = new File(new URI(strURL));
            // the file need not exist. It is made into an absolute path
            // by prefixing the current working directory
            return file.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }
}
