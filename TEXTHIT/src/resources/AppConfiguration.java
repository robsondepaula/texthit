package resources;

/**
 * This class represents the application configuration.
 */
public class AppConfiguration {

    /**
     * Keeps track of the publisher server IP Address. Used to persist the
     * publishing location of the current HypertextMap.
     */
    private static String serverIpAddress = null;
    /**
     * Keeps track of the publisher server Login. Used to persist the publishing
     * location of the current HypertextMap.
     */
    private static String serverLogin = null;
    /**
     * Keeps track of the publisher server Path. Used to persist the publishing
     * location of the current HypertextMap.
     */
    private static String serverPath = null;

    public static void setServerLogin(String serverLogin) {
        AppConfiguration.serverLogin = serverLogin;
    }

    public static void setServerPath(String serverPath) {
        AppConfiguration.serverPath = serverPath;
    }

    public static String getServerLogin() {
        return serverLogin;
    }

    public static String getServerPath() {
        return serverPath;
    }

    /**
     * Configures the publisher server IP Address.
     *
     * @param newServerIpAddress a String containing the publisher server IP
     * address
     */
    public static void setServerIpAddress(String newServerIpAddress) {
        AppConfiguration.serverIpAddress = newServerIpAddress;
    }

    /**
     * Retrieves the configured publisher server IP address.
     *
     * @return a String containing the publisher server IP address
     */
    public static String getServerIpAddress() {
        return serverIpAddress;
    }
}
