package passwordmanager.app;

import java.io.Console;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.crypto.BadPaddingException;

import com.password4j.types.Argon2;

import passwordmanager.database.InvalidDatabaseException;
import passwordmanager.database.PasswordDatabase;

public class App {
    public static void main(String[] args) {
        final String configDirPath = Util.getConfigLocation();
        final Path dbPath = Paths.get(configDirPath, "jpass.db");
        final Scanner scan = new Scanner(System.in);
        final PasswordDatabase database;
        Console console = System.console();
        char[] password;
        if (Files.notExists(dbPath)) {
            System.out.println("Didn't find a database file at " + dbPath.toString() + ". Creating a new one.");
            password = console.readPassword("Enter a new password: ");
            try {
                Files.createDirectories(Paths.get(configDirPath));
                Files.createFile(dbPath);
                database = new PasswordDatabase(dbPath.toFile(), password, 15, 1, 2, 32, Argon2.ID);
            } catch (IOException | InvalidDatabaseException e) {
                scan.close();
                throw new RuntimeException("Failed trying to create new database file", e);
            }
        } else {
            try {
                password = console.readPassword("Enter password to unlock: ");
                database = new PasswordDatabase(dbPath.toFile(), password);
            } catch (InvalidDatabaseException e) {
                throw new RuntimeException("Failed trying to import existing database file", e);
            } catch (BadPaddingException e) {
                throw new RuntimeException("Provided password was incorrect", e);
        }
        scan.close();
    }
    /* TODO Add main loop
     * - Display all accounts
     * - Prompt to:
     *      - Search
     *      - Sort
     *      - Show details of account <i>
     *      - Exit
     */
}
