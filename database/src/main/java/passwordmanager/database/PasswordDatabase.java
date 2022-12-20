package passwordmanager.database;

// WIP: Remove glob imports
import com.password4j.*;
import com.password4j.jca.*;
import com.google.gson.Gson;
// IO
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
// Util
import java.util.ArrayList;

public class PasswordDatabase {
  private final File databaseFile;
  private final ArrayList<Account> accountList;

  public PasswordDatabase(File databaseFile) {
    this.databaseFile = databaseFile;
    this.accountList = importDatabase(this.databaseFile);
  }

  private ArrayList<Account> importDatabase(File databaseFile) {
    Gson jsonProcessor = new Gson();
    FileInputStream fileIn = new FileInputStream(databaseFile);
    BufferedReader fileRead = new BufferedReader(new FileReader(databaseFile));
    EncryptedDatabase database = jsonProcessor.fromJson(fileRead, EncryptedDatabase.class);
    return decryptDatabase(database.accountList, database.salt, database.memory, database.iterations, database.parallelization, database.length);
  }

  private ArrayList<Account> decryptDatabase(byte[] accountList, byte[] salt, int memory, int iterations, int parallelization, int length) {
    
  }
}
