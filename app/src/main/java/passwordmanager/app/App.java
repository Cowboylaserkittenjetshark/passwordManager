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

import com.password4j.types.Argon2;

import passwordmanager.database.InvalidDatabaseException;
import passwordmanager.database.PasswordDatabase;

public class App {
    public static void main(String[] args) {
        final String configDirPath = getConfigLocation();
        final Path dbPath = Paths.get(configDirPath, "jpass.db");
        final Scanner scan = new Scanner(System.in);
        final PasswordDatabase database;
        Console console = System.console();
        char[] password;
        if (Files.notExists(dbPath)) {
            System.out.println("Didn't find a database file at " + dbPath.toString() + ". ") // TODO New DB prompt
            password = console.readPassword("Enter a new password: ");
            try {
                Files.createDirectories(Paths.get(configDirPath));
                Files.createFile(dbPath);
                database = new PasswordDatabase(dbPath.toFile(), password, 15, 1, 2, 32, Argon2.ID);
            } catch (IOException|InvalidDatabaseException e) {
                scan.close();
                throw new RuntimeException("Failed trying to create new database file", e);
            }
        }
        else {
            try {
                database = new PasswordDatabase(dbPath.toFile(), password);
            } catch (InvalidDatabaseException e) {
                scan.close();
                throw new RuntimeException("Failed trying to import existing database file", e);
            }
        }

        scan.close();
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
