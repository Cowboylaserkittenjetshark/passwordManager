package passwordmanager.database;

// WIP: Remove glob imports
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

// IO
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
// Util
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class PasswordDatabase {
  private final File databaseFile;
  private final ArrayList<Account> accountList;

  public PasswordDatabase(File databaseFile) {
    this.databaseFile = databaseFile;
    this.accountList = importDatabase();
  }

  private ArrayList<Account> importDatabase() {
    char[] password = ("replaceMe").toCharArray(); // Get password from somewhere
    EncryptedDatabase database = readDatabseFile();
    String asJson = "{}"; // TODO
    try {
      asJson = CryptUtil.decrypt(password, database.initVector, database.salt, database.memory, database.iterations, database.parallelization, database.length, database.type, database.accountList);
    } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
        | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException
        | InvalidKeySpecException e) {
      // TODO Handle decrypt exceptions
      e.printStackTrace();
    }
    TypeToken<ArrayList<Account>> accountListType = new TypeToken<ArrayList<Account>>() {};
    return new Gson().fromJson(asJson, accountListType.getType());
  }

  private void exportDatabase() {
    char[] password = ("replaceMe").toCharArray(); // Get password from somewhere
    EncryptedDatabase database = readDatabseFile();
    String asJson = new Gson().toJson(this.accountList);
    byte[] accountListEnc = new byte[1]; // TODO Default val for accountListEnc byte[]
    try {
      accountListEnc = CryptUtil.encrypt(password, database.initVector, database.salt, database.memory, database.iterations, database.parallelization, database.length, database.type, asJson);
    } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
        | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException
        | InvalidKeySpecException e) {
      // TODO Handle encryption exceptions
      e.printStackTrace();
    }
    database.rotate(accountListEnc);
    try (BufferedWriter fileWrite = new BufferedWriter( new FileWriter(this.databaseFile))) {
      fileWrite.write(new Gson().toJson(database));
      fileWrite.close();
    } catch (IOException e) {
      // TODO Handle IOException on export
      e.printStackTrace();
    }
  }

  private EncryptedDatabase readDatabseFile() {
    try (BufferedReader fileRead = new BufferedReader(new FileReader(this.databaseFile))) {
      EncryptedDatabase database = new Gson().fromJson(fileRead, EncryptedDatabase.class);
      fileRead.close();
      return database;
    } catch (JsonSyntaxException | JsonIOException | IOException e) {
      // TODO Handle IOException of read
      e.printStackTrace();
    }
  }
}
