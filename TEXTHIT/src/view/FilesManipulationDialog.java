package view;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * File Chooser wrapper with custom file filter and user interface text.
 */
public class FilesManipulationDialog {

    /**
     * Shows the choose file dialog.
     *
     * @param parentComponent the parent displayable component
     * @param strTitle the title for the choose file dialog
     * @param strApproveButton the label for the "OK" button
     * @param strFilterText helper text for the available file extensions
     * @param strExtensions an array containing the allowed file extensions to
     * be chosen
     * @param strPath the initial path shown by the choose file dialog
     * @param enableDirs allow the selection of directories
     * @return the chosen {@link File}
     */
    public static File showFileChooser(Component parentComponent,
            String strTitle,
            String strApproveButton,
            String strFilterText,
            String[] strExtensions,
            String strPath,
            boolean enableDirs) {

        applyFileChooserTweak();

        JFileChooser fc = new JFileChooser(strPath);
        //add a custom file filter and disable the default
        //(Accept All) file filter.
        fc.addChoosableFileFilter(new CustomFileFilter(strFilterText, strExtensions));
        fc.setAcceptAllFileFilterUsed(false);
        fc.setDialogTitle(strTitle);

        if (enableDirs) {
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        } else {
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        }

        //show the FileChooser
        int returnVal = fc.showDialog(parentComponent, strApproveButton);

        //Allow the user to choose a file accordingly to a FileFilter
        File file = null;
        //process the results
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
        }

        //reset the file chooser for the next time it's shown.
        fc.setSelectedFile(null);

        //returns the selected file
        return file;
    }

    /*
     * Unregisters the zipfldr.dll fixes JDialog slowness
     * issues. (Should be made optional)
     */
    public static void applyFileChooserTweak() {

        String osName = System.getProperty("os.name").toLowerCase();
        if (!osName.contains("windows")) {
            return;
        }
        try {
            System.out.print("Unregistering zipfldr.dll to speed up program (don't worry, windows will reset this)...");
            Runtime.getRuntime().exec("regsvr32 /s %windir%/system32/zipfldr.dll");
            System.out.println("success");
        } catch (Exception e) {
            System.out.println("failed");
        }
    }

    /**
     * Retrieves the given {@link File} extension.
     *
     * @param f the given File to retrieve the extension
     * @return a String containing the given File extension
     */
    private static String getExtension(File f) {
        //gets the extension of a file
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    /**
     * Implements a custom {@link FileFilter}. <p>Inner class of the
     * {@link FilesManipulationDialog}.
     */
    private static class CustomFileFilter extends FileFilter {
        //Customizes the file filter of a JFileChooser
        //Accept all directories and all htm, html or xhtml files.

        String filterText = null;
        String[] filterExt = null;

        /**
         * Constructs a custom file filter which will allow only the selection of
         * files with extensions that equals the given possible extensions.
         *
         * @param strFilterText helper text for the available {@link File}
         * extensions
         * @param strFilterExt an array containing the allowed File extensions
         * to be chosen
         */
        public CustomFileFilter(String strFilterText, String[] strFilterExt) {
            filterText = strFilterText;
            filterExt = strFilterExt;
        }

        /**
         * Query the custom {@link FileFilter} checks if the given {@link File}
         * has an acceptable extension.
         *
         * @return <code>true</code> if the File is acceptable
         * <p><code>false</code> if the File was filtered
         */
        @Override
        public boolean accept(File f) {
            String strExtension;
            int intRes;

            if (f.isDirectory()) {
                return true;
            }

            strExtension = FilesManipulationDialog.getExtension(f);
            intRes = 0;
            if ((strExtension != null) && (filterExt != null)) {
                for (int i = 0; i < filterExt.length; i++) {
                    if (strExtension.equals(filterExt[i])) {
                        intRes++;
                    }
                }

                if (intRes > 0) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        /**
         * Query the custom {@link FileFilter} and retrieves the helper text for
         * the available file extensions that can be chosen by the user.
         *
         * @return a String containing the helper text for the available file
         * extensions
         */
        @Override
        public String getDescription() {
            return filterText;
        }
    }
}
