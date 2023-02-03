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

import passwordmanager.database.PasswordDatabase;

public class App {
    public static void main(String[] args) {
        final String CONFIG_DIR = getConfigLocation();
        Scanner scan = new Scanner(System.in);
        Console console = System.console();
        char[] password = console.readPassword("Enter password to unlock: ");
        if (Files.notExists(Paths.get(CONFIG_DIR, "jpass.db"))) {
            try {
                Files.createDirectories(Paths.get(CONFIG_DIR));
                Files.createFile(Paths.get(CONFIG_DIR, "jpass.db"));
                PasswordDatabase database = new PasswordDatabase(Paths.get(CONFIG_DIR, "jpass.db").toFile(), password, 0, 0, 0, 0, null)
            } catch (IOException e) {
                throw new RuntimeException("Failed trying to create new database file", e);
            }
        }
        else {
            // Impoort existing db
        }
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
