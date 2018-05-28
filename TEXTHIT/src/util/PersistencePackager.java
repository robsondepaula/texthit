package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

/**
 * This class compresses and decompresses files.
 */
public class PersistencePackager {

    public static final String fileSeparator = System.getProperty("file.separator");
    /**
     * Files manipulation memory buffer size
     */
    private static final int BUFFER_SIZE = 2048;
    private FileOutputStream destFile = null;
    private ZipOutputStream outputZip = null;
    private ZipFile srcFile = null;

    /**
     * Create a package of the given name and prepare to add files to it.
     *
     * @param strPath the absolute path for the package output
     * @throws Exception If errors prevented the package generation
     */
    public void setPackagerForCompress(String strPath)
            throws Exception {
        destFile = new FileOutputStream(strPath);
        outputZip = new ZipOutputStream(new BufferedOutputStream(destFile));
        //legacy encoding to support non-US characters in filenames
        outputZip.setEncoding("Cp437");
    }

    /**
     * Prepare to decompress files from the given package.
     *
     * @param strPath the absolute path for the package input
     * @throws Exception If errors prevented processing the package
     */
    public void setPackagerForDecompress(String strPath) throws Exception {
        //legacy encoding to support non-US characters in filenames
        srcFile = new ZipFile(strPath, "Cp437");
    }

    /**
     * Compresses the given file and add it to the created package.
     *
     * @param strPath the absolute path for the file been added to the package
     * @param strEntryName the relative path for the file within the package.
     * Enables user to preserve or not the original file path. Also allows to
     * add subdirectories in the package.
     * @throws Exception If errors prevented processing the file
     */
    public void addToPackage(String strPath, String strEntryName) {
        byte[] dataArray = null;
        int dataSize = 0;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(strPath);
        } catch (Exception e) {
        }
        BufferedInputStream bufInput =
                new BufferedInputStream(inputStream, BUFFER_SIZE);
        ZipEntry newEntry = new ZipEntry(strEntryName);

        try {
            outputZip.putNextEntry(newEntry);
        } catch (Exception e) {
        }
        dataArray = new byte[BUFFER_SIZE];
        try {
            while ((dataSize = bufInput.read(dataArray, 0, BUFFER_SIZE)) != -1) {
                outputZip.write(dataArray, 0, dataSize);
            }
        } catch (Exception e) {
        }

        try {
            bufInput.close();
        } catch (Exception e) {
        }
        try {
            inputStream.close();
        } catch (Exception e) {
        }
    }

    /**
     * Decompresses the given file from the package in the current directory.
     *
     * @param strFileName the name of the file been decompressed from the
     * package
     * @throws Exception If errors prevented processing the file
     */
    public void extractFromPackage(String strFileName) {
        Enumeration entries = srcFile.getEntries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            if (entry.getName().equals(strFileName)) {
                InputStream in = null;
                try {
                    in = srcFile.getInputStream(entry);
                } catch (Exception e) {
                }
                BufferedOutputStream out = null;
                try {
                    out = new BufferedOutputStream(new FileOutputStream(entry.getName()));
                } catch (Exception e) {
                }

                byte[] buffer = new byte[BUFFER_SIZE];
                int len;
                try {
                    while ((len = in.read(buffer)) >= 0) {
                        out.write(buffer, 0, len);
                    }
                } catch (Exception e) {
                }

                try {
                    in.close();
                } catch (Exception e) {
                }
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Finish using the current package.
     *
     * @see #setPackagerForCompress(String strPath)
     * @see #setPackagerForDecompress(String strPath)
     */
    public void closePackage() {
        try {
            outputZip.close();
        } catch (Exception e) {
        }
        try {
            destFile.close();
        } catch (Exception e) {
        }
        try {
            srcFile.close();
        } catch (Exception e) {
        }
    }
}
