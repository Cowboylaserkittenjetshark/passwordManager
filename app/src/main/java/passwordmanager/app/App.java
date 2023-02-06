package passwordmanager.app;

import java.io.Console;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import com.password4j.types.Argon2;
import passwordmanager.database.Account;
import passwordmanager.database.InvalidDatabaseException;
import passwordmanager.database.PasswordDatabase;

public class App {
    public static void main(String[] args) {
        final PasswordDatabase database;
        final String configDirPath = Util.getConfigLocation();
        final Path dbPath = Paths.get(configDirPath, "jpass.db");
        final Console console = System.console();
        final char[] password;

        // Access database
        if (Files.notExists(dbPath)) {
            System.out.println("Didn't find a database file at " + dbPath.toString()
                    + ". Creating a new one.");
            password = console.readPassword("Enter a new password: ");
            try {
                Files.createDirectories(Paths.get(configDirPath));
                Files.createFile(dbPath);
                database = new PasswordDatabase(dbPath.toFile(), password, 15, 1, 2, 32, Argon2.ID);
            } catch (IOException | InvalidDatabaseException e) {
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
        }

        // Main menu
        final Scanner scan = new Scanner(System.in);
        String filter = "";

        boolean runLoop = true;
        while (runLoop) {
            int index = 1;
            for (Account currAccount : database.search(filter)) {
                System.out.println(index + " " + currAccount.accountName);
                index++;
            }
            System.out.print(
                    "Enter an account number to view or [A]dd [R]emove [F]ilter [S]ort [E]xit: ");
            if (scan.hasNextInt()) { // View when an int is entered
                int input = scan.nextInt();
                scan.nextLine();
                Account accountAtIndex = database.search(filter).get(input - 1);
                System.out.println();
                System.out.println(accountAtIndex.accountName);
                System.out.println("Username: " + accountAtIndex.username);
                System.out.println("Password: " + accountAtIndex.password);
                System.out.println();
                continue;
            } else { // Other functions when a non-int is entered
                String input = scan.nextLine().toLowerCase();
                if (input.matches("\\b[A|a]((dd\\b)|\\b)")) { // Add
                    String accountName;
                    String username;
                    char[] accountPassword;
                    System.out.println("Adding a new account");
                    System.out.print("Account name: ");
                    accountName = scan.nextLine();
                    System.out.print("Account username or email: ");
                    username = scan.nextLine();
                    boolean passwordsMatch = false;
                    while (!passwordsMatch) {
                        accountPassword = console.readPassword("Enter account password: ");
                        char[] passwordCheck = console.readPassword("Re-enter account password: ");
                        if (Arrays.equals(accountPassword, passwordCheck)) {
                            passwordsMatch = true;
                        } else {
                            System.out.println("Passwords do not match. Try again");
                        }
                    }
                    Account newAccount = new Account(accountName, username, new String(password));
                    try {
                        database.addAccount(newAccount);
                    } catch (InvalidDatabaseException e) {
                        scan.close();
                        throw new RuntimeException("Failed trying to modify database", e);
                    }
                } else if (input.matches("\\b[R|r]((emove\\b)|\\b)")) { // Remove
                    ArrayList<Account> filteredAccountList = database.search(filter);
                    int indexToRemove = -1;
                    boolean runRemoveLoop = true;
                    while (runRemoveLoop) {
                        System.out.print("Enter index of the account to be removed: ");
                        if (scan.hasNextInt()) {
                            indexToRemove = scan.nextInt();
                            scan.nextLine();
                            if (!(indexToRemove > filteredAccountList.size())
                                    && !(indexToRemove < 1)) {
                                runRemoveLoop = false;
                            } else {
                                System.out.println(
                                        "Bad input, enter the integer index of the account to remove");
                            }
                        }
                    }
                    Account accountToRemove = filteredAccountList.get(indexToRemove - 1);
                    try {
                        database.removeAccount(accountToRemove);
                    } catch (InvalidDatabaseException e) {
                        scan.close();
                        throw new RuntimeException("Failed trying to modify database", e);
                    }
                } else if (input.matches("\\b[F|f]((ilter\\b)|\\b)")) {
                    System.out.print("Enter filter term: ");
                    filter = scan.nextLine();
                } else if (input.matches("\\b[S|s]((ort\\b)|\\b)")) {
                    System.out.print("Sort by Creation [T]ime, [A]ccount name, or [U]sername: ");
                    input = scan.nextLine();
                    Comparator chosenComparator;
                    System.out.println("Ascending or descending: ");
                    if (input.matches("\\b[T|t]((ime\\b)|\\b)")) {
                        chosenComparator = new Account.TimeComparator();
                    } else if (input.matches("\\b[A|a]((ccount\\b)|\\b)")) {
                        chosenComparator = new Account.AccountNameComparator();
                    } else if (input.matches("\\b[U|u]((sername\\b)|\\b)")) {
                        chosenComparator = new Account.AccountNameComparator();
                    } else {
                        System.out.println();
                        continue;
                    }
                    // TODO Do sorting internally in db
                    // TODO Ascending descending sort?
                    database.sortAccounts(chosenComparator);
                } else if (input.matches("\\b[E|e]((xit\\b)|\\b)")) {
                    runLoop = false;
                }
            }

            System.out.println();
        }
        scan.close();
        System.exit(0);
    }
}
