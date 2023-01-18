package passwordmanager.database;

import org.junit.jupiter.api.Test;

import com.password4j.types.Argon2;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class DatabaseTests {
    @Test void testInitialization() throws IOException {
        // TODO Use proper constructor to initialize the database
        String password = "password";
        File databaseFile = new File("test.db");
        databaseFile.createNewFile();
        try {
            PasswordDatabase testPDB = new PasswordDatabase(databaseFile, password.toCharArray(), 15, 1, 2, 32,Argon2.ID);
        } catch (InvalidDatabaseException e) {
            fail(e);
        }
    }
    // TODO Test adding/removing
    // TODO Test sorting
}
