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
import passwordmanager.database.Account;
import passwordmanager.database.InvalidDatabaseException;
import passwordmanager.database.PasswordDatabase;

public class App {
    public static void main(String[] args) {
        final PasswordDatabase database;
        final String configDirPath = Util.getConfigLocation();
        final Path dbPath = Paths.get(configDirPath, "jpass.db");
        final Console console = System.console();
        char[] password;

        // Access database
        if (Files.notExists(dbPath)) {
            System.out.println(Util.color(">>", Util.PROMPT) + " Didn't find a database file at " + dbPath.toString()
                    + ". Creating a new one.");
            boolean passwordsMatch = false;
            do {
                password = console.readPassword(Util.color(">>", Util.PROMPT) + " Enter new password: ");
                char[] passwordCheck = console.readPassword(Util.color(">>", Util.PROMPT) + " Re-enter password: ");
                if (Arrays.equals(password, passwordCheck)) {
                    passwordsMatch = true;
                } else {
                    System.out.println(Util.color("Passwords do not match. Try again", Util.PROBLEM));
                }
            } while (!passwordsMatch);
            try {
                Files.createDirectories(Paths.get(configDirPath));
                Files.createFile(dbPath);
                database = PasswordDatabase.getInstanceNew(dbPath, password);
            } catch (IOException | InvalidDatabaseException e) {
                throw new RuntimeException("Failed trying to create new database file", e);
            }
        } else {
            try {
                password = console.readPassword(Util.color(">>", Util.PROMPT) + " Enter password to unlock: ");
                database = PasswordDatabase.getInstanceExisting(dbPath, password);
            } catch (InvalidDatabaseException e) {
                throw new RuntimeException("Failed trying to import existing database file", e);
            } catch (BadPaddingException e) {
                throw new RuntimeException("Provided password was incorrect", e);
            }
        }

        // Main menu
        final Scanner scan = new Scanner(System.in);
        String filter = "";

        System.out.println();
        boolean runLoop = true;
        while (runLoop) {
            int index = 1;
            for (Account currAccount : database.search(filter)) {
                System.out.println(Util.color(index, Util.LIST) + " " + currAccount.accountName);
                index++;
            }
            System.out.print(Util.color(">>", Util.PROMPT) + " Enter an account number to view or [A]dd [R]emove [F]ilter [S]ort [E]xit: ");
            if (scan.hasNextInt()) { // View when an int is entered
                int input = scan.nextInt();
                scan.nextLine();
                Account accountAtIndex = database.search(filter).get(input - 1);
                System.out.println();
                System.out.printf("\t%s %s\n", Util.color("Account:", Util.DETAILS),accountAtIndex.accountName);
                System.out.printf("\t%s %s\n", Util.color("Username:", Util.DETAILS), accountAtIndex.username);
                System.out.printf("\t%s %s\n", Util.color("Password:", Util.DETAILS), accountAtIndex.password);
                System.out.println();
                System.out.println(Util.color(">>", Util.PROMPT) + " Press Enter to continue");
                scan.nextLine();
                continue;
            } else { // Other functions when a non-int is entered
                String input = scan.nextLine().toLowerCase();
                if (input.matches("\\b[A|a]((dd\\b)|\\b)")) { // Add
                    String accountName;
                    String username;
                    char[] accountPassword;
                    System.out.println(Util.color(">>", Util.PROMPT) + " Adding a new account");
                    System.out.print(Util.color(">>", Util.PROMPT) + " Account name: ");
                    accountName = scan.nextLine();
                    System.out.print(Util.color(">>", Util.PROMPT) + " Account username or email: ");
                    username = scan.nextLine();
                    boolean passwordsMatch = false;
                    do {
                        accountPassword = console.readPassword(Util.color(">>", Util.PROMPT) + " Enter account password: ");
                        char[] passwordCheck = console.readPassword(Util.color(">>", Util.PROMPT) + " Re-enter account password: ");
                        if (Arrays.equals(accountPassword, passwordCheck)) {
                            passwordsMatch = true;
                        } else {
                            System.out.println(Util.color("Passwords do not match. Try again", Util.PROBLEM));
                        }
                    } while (!passwordsMatch);
                    Account newAccount = new Account(accountName, username, new String(accountPassword));
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
                        System.out.print(Util.color(">>", Util.PROMPT) + " Enter index of the account to be removed: ");
                        if (scan.hasNextInt()) {
                            indexToRemove = scan.nextInt();
                            scan.nextLine();
                            if (!(indexToRemove > filteredAccountList.size())
                                    && !(indexToRemove < 1)) {
                                runRemoveLoop = false;
                            } else {
                                System.out.println(Util.color("Bad input, enter the integer index of the account to remove", Util.PROBLEM));
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
                    System.out.print(Util.color(">>", Util.PROMPT) + " Enter filter term: ");
                    filter = scan.nextLine();
                } else if (input.matches("\\b[S|s]((ort\\b)|\\b)")) {
                    System.out.print(Util.color(">>", Util.PROMPT) + " Sort by Creation [T]ime, [A]ccount name, or [U]sername: ");
                    input = scan.nextLine();
                    Comparator chosenComparator;
                    // System.out.println(">> Ascending or descending: "); TODO A / D?
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
