package util;

/**
 * Abstract class for Communication
 */
public abstract class Communicator {

    /**
     * Send a file across the established communication.
     *
     * @param strPath the absolute path to the file to be sent
     * @return <code>true</code> if the file was sent <p><code>false</code> if
     * the operation failed
     */
    public abstract boolean sendFile(String strPath);

    /**
     * Finish using the established communication
     */
    public abstract void finish();
}
