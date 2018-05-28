package util;

import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * This class establishes an FTP communication
 */
public class FTPCommunicator extends Communicator {

    private static FTPClient ftp = null;

    /*
     *Constructs the FTP Communicator
     *@param str_IP_Address a String containing the IP address of the FTP server
     *@param strUser the user that will login in the FTP server
     *@param strPasswd the password for the given user
     */
    public FTPCommunicator(String str_IP_Address,
            String strUser,
            String strPasswd) throws Exception {
        //instantiate the client
        ftp = new FTPClient();
        ftp.connect(str_IP_Address);

        //verify connection
        if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
            ftp.login(strUser, strPasswd);
        } else {
            //error with the connection
            ftp.disconnect();
            throw new Exception("FTP connection error");
        }
    }

    @Override
    public boolean sendFile(String strPath) {
        //retrieve filename from node storage path
        String strURL =
                HypertextTools.filePathToURL(strPath);
        String fileName =
                strURL.substring(strURL.lastIndexOf("/"));
        //prepare to read the local source
        InputStream input = null;
        try {
            input = new FileInputStream(strPath);
        } catch (Exception e) {
            return false;
        }
        try {
            //prepare to attempt the file transfer
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        } catch (Exception e) {
            try {
                input.close();
            } catch (Exception ex) {
            }
            return false;
        }
        try {
            //send the file
            ftp.storeFile(fileName, input);
        } catch (Exception e) {
            try {
                input.close();
            } catch (Exception ex) {
            }
            return false;
        }
        try {
            input.close();
        } catch (Exception e) {
        }
        return true;
    }

    /*
     *Disconnects the FTP Communicator from the server.
     */
    @Override
    public void finish() {
        try {
            ftp.disconnect();
        } catch (Exception e) {
        }
        //nullify
        ftp = null;
    }
}
