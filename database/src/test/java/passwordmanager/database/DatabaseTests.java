package passwordmanager.database;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class DatabaseTests {
    @Test void testInitialization() throws IOException {
        fail("Not implemented"); // TODO Use proper constructor to initialize the database
        String password = "password";
        File databaseFile = new File("test.db");
        databaseFile.createNewFile();
        try {
            PasswordDatabase testPDB = new PasswordDatabase(databaseFile, password.toCharArray());
        } catch (InvalidDatabaseException e) {
            fail(e);
        }
    }
    // TODO Test adding/removing
    // TODO Test sorting
}
