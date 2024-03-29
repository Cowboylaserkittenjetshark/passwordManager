package passwordmanager.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import java.util.ArrayList;
import java.util.Comparator;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import com.password4j.types.Argon2;

public class PasswordDatabase {
  private final File databaseFile;
  private final char[] databasePassword;
  private final ArrayList<Account> accountList;

  public PasswordDatabase(File databaseFile, char[] databasePassword, int memory, int iterations,
      int parallelization, int length, Argon2 type) throws InvalidDatabaseException {
    this.databaseFile = databaseFile;
    this.databasePassword = databasePassword;
    this.accountList = new ArrayList<Account>();
    initializeDatatbase(memory, iterations, parallelization, length, type);
  }

  public PasswordDatabase(File databaseFile, char[] databasePassword)
      throws InvalidDatabaseException, BadPaddingException {
    this.databaseFile = databaseFile;
    this.databasePassword = databasePassword;
    this.accountList = importDatabase();
  }

  public static PasswordDatabase getInstanceExisting(Path dbPath, char[] password) throws BadPaddingException, InvalidDatabaseException {
    return new PasswordDatabase(dbPath.toFile(), password);
  }

  public static PasswordDatabase getInstanceNew(Path dbPath, char[] password) throws InvalidDatabaseException {
    return new PasswordDatabase(dbPath.toFile(), password,15, 1, 2, 32, Argon2.ID);
  }

  private ArrayList<Account> importDatabase() throws InvalidDatabaseException, BadPaddingException {
    EncryptedDatabase database = readDatabseFile();
    String accountListJSON = null;
    try {
      accountListJSON = database.decrypt(this.databasePassword);
    } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
        | InvalidAlgorithmParameterException | IllegalBlockSizeException
        | InvalidKeySpecException e) {
      throw new InvalidDatabaseException("Database contains invalid encryption parameters", e);
    }
    /*
     * TODO Add citation to the GSON Javadoc for the TypeToken line
     */
    TypeToken<ArrayList<Account>> accountListType = new TypeToken<ArrayList<Account>>() {};
    ArrayList<Account> imported = new Gson().fromJson(accountListJSON, accountListType.getType());
    return imported;
  }

  private void exportDatabase() throws InvalidDatabaseException {
    EncryptedDatabase database = readDatabseFile();
    String accountListJSON = new Gson().toJson(this.accountList);
    try {
      database.encrypt(this.databasePassword, accountListJSON);
    } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
        | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException
        | InvalidKeySpecException e) {
      throw new InvalidDatabaseException("Database contains invalid encryption parameters", e);
    }
    try (BufferedWriter fileWrite = new BufferedWriter(new FileWriter(this.databaseFile))) {
      fileWrite.write(new Gson().toJson(database));
      fileWrite.close();
    } catch (IOException e) {
      throw new InvalidDatabaseException("Cannot access database at " + this.databaseFile.getName(),
          e);
    }
  }

  private EncryptedDatabase readDatabseFile() throws InvalidDatabaseException {
    try (BufferedReader fileRead = new BufferedReader(new FileReader(this.databaseFile))) {
      EncryptedDatabase database = new Gson().fromJson(fileRead, EncryptedDatabase.class);
      if (database == null)
        throw new InvalidDatabaseException("Database not initialized");
      return database;
    } catch (JsonIOException | IOException e) {
      throw new InvalidDatabaseException("Cannot access database at " + this.databaseFile.getName(),
          e);
    }
  }

  private void initializeDatatbase(int memory, int iterations, int parallelization, int length,
      Argon2 type) throws InvalidDatabaseException {
    String accountListJSON = new Gson().toJson(this.accountList);
    EncryptedDatabase database =
        new EncryptedDatabase(null, memory, iterations, parallelization, length, type);
    try {
      database.encrypt(this.databasePassword, accountListJSON);
    } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
        | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException
        | InvalidKeySpecException e) {
      throw new InvalidDatabaseException("Invalid encryption parameters", e);
    }
    try (BufferedWriter fileWrite = new BufferedWriter(new FileWriter(this.databaseFile))) {
      fileWrite.write(new Gson().toJson(database));
      fileWrite.close();
    } catch (IOException e) {
      throw new InvalidDatabaseException("Cannot create database at " + this.databaseFile.getName(),
          e);
    }
  }

  public boolean addAccount(Account account) throws InvalidDatabaseException {
    boolean exitCode = this.accountList.add(account);
    exportDatabase();
    return exitCode;
  }

  public Account removeAccount(int accountIndex) throws InvalidDatabaseException {
    Account removed = this.accountList.remove(accountIndex);
    exportDatabase();
    return removed;
  }

  public boolean removeAccount(Account account) throws InvalidDatabaseException {
    boolean removed = this.accountList.remove(account);
    exportDatabase();
    return removed;
  }

  public ArrayList<Account> getAccounts() {
    return this.accountList;
  }

  public Account getAccount(int index) {
    return this.accountList.get(index);
  }

  public String getAccountName(int index) {
    return this.accountList.get(index).accountName;
  }

  public String getPassword(int index) {
    return this.accountList.get(index).password;
  }

  public String getUsername(int index) {
    return this.accountList.get(index).username;
  }

  public ArrayList<Account> search(String searchString) {
    ArrayList<Account> searchResults = new ArrayList<Account>();
    for (Account currAccount : accountList) {
      if (currAccount.accountName.contains(searchString)
          || currAccount.username.contains(searchString)) {
        searchResults.add(currAccount);
      }
    }
    return searchResults;
  }

  // TODO Implement sort (By ABC, time)
  public void sortAccounts(Comparator<Account> comparator) {
    this.accountList.sort(comparator);
  }

  // TODO Implement dump (Return as Account[] ?)
}
