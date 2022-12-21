package passwordmanager.database;

// WIP: Remove glob imports
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
    this.accountList = importDatabase();
  }

  private ArrayList<Account> importDatabase() {
    char[] password; // Get password from somewhere
    Gson jsonProcessor = new Gson();
    BufferedReader fileRead = new BufferedReader(new FileReader(this.databaseFile));
    EncryptedDatabase database = jsonProcessor.fromJson(fileRead, EncryptedDatabase.class);
    fileRead.close();
    String asJson = CryptUtil.decrypt(password, database.initVector, database.salt, database.memory, database.iterations, database.parallelization, database.length, database.type, database.accountList);
    TypeToken<ArrayList<Account>> accountListType = new TypeToken<ArrayList<Account>>(){}.getType() 
    return new Gson().fromJson(asJson, accountListType.getType());
  }

  private void exportDatabase()
}
