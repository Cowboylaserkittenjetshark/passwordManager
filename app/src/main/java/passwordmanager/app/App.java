package passwordmanager.app;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        final String CONFIG_DIR = getConfigLocation();
        if (Files.notExists(Paths.get(CONFIG_DIR, "jpass.conf"))) {
            try {
                Files.createDirectories(Paths.get(CONFIG_DIR));
                Files.createFile(Paths.get(CONFIG_DIR, "jpass.conf"));
                storeProperties();
            } catch (IOException e) {
                throw new RuntimeException("Failed trying to initialize config file", e);
            }
        }
        loadProperties(Paths.get(CONFIG_DIR, "jpass.conf"));
        Scanner scan = new Scanner(System.in);
        Console console = System.console();
        char[] password = console.readPassword("Enter password to unlock: ");
        // TODO Wipe array?
        console.printf(new String(password)); // TODO Remove password echo
        console.printf("Config directory: %s", getConfigLocation());
        // PasswordDatabase database = new PasswordDat
    }

    private static Properties loadProperties(Path confPath) throws IOException {
        Properties props = new Properties();
        Path configFilePath = Paths.get(getConfigLocation());
        BufferedReader bufferedConfigReader = Files.newBufferedReader(configFilePath);
        props.load(bufferedConfigReader);
        bufferedConfigReader.close();
        return props;
    }

    private static void storeProperties() {
        
    }

    private static String getConfigLocation() {
        String osProp = System.getProperty("os.name");
        StringBuilder configPathBuilder = new StringBuilder();
        if (osProp.matches("Windows]")) {
            configPathBuilder.append(System.getenv("APPDATA"));
            configPathBuilder.append(File.separator);
            configPathBuilder.append("jpass");
        } else if (osProp.matches("Linux|SunOS|FreeBSD")) {
            String xdgConfigHome = System.getenv("XDG_CONFIG_HOME");
            if (!xdgConfigHome.equals(null)) {
                configPathBuilder.append(xdgConfigHome);
            } else {
                configPathBuilder.append(System.getenv("HOME"));
                configPathBuilder.append(File.separator);
                configPathBuilder.append(".config");
            }
            configPathBuilder.append(File.separator);
            configPathBuilder.append("jpass");
        } else if (osProp.matches("Mac")) {
            configPathBuilder.append(System.getProperty("user.home"));
            configPathBuilder.append(File.separator);
            configPathBuilder.append("Library");
            configPathBuilder.append(File.separator);
            configPathBuilder.append("Application Support");
            configPathBuilder.append(File.separator);
            configPathBuilder.append("jpass");
        } else {
            configPathBuilder.append(System.getProperty("user.home"));
            configPathBuilder.append(File.separator);
            configPathBuilder.append(".jpass");
        }
        return configPathBuilder.toString();
    }
}
