package passwordmanager.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import javax.crypto.BadPaddingException;
import org.junit.jupiter.api.Test;

import com.password4j.types.Argon2;

public class DatabaseTests {
    @Test void testInitialization() throws IOException, BadPaddingException {
        String password = "password";
        File databaseFile = new File("test.db");
        databaseFile.createNewFile();
        try {
            PasswordDatabase testPDB = new PasswordDatabase(databaseFile, password.toCharArray(), 15, 1, 2, 32,Argon2.ID);
        } catch (InvalidDatabaseException e) {
            fail(e);
        }
        try {
            PasswordDatabase testPDB = new PasswordDatabase(new File("test.db"), password.toCharArray());
            testPDB.addAccount(new Account("ServiceName", "Cowboylaserkittenjetshark", "password1234"));
        } catch (InvalidDatabaseException e) {
            fail(e);
        }

//
        try {
            PasswordDatabase testPDB = new PasswordDatabase(new File("test.db"), password.toCharArray());
            Account testAcc = new Account("ServiceName", "Cowboylaserkittenjetshark", "password1234");
            Account fromDB = testPDB.getAccount(0);
            assertEquals(testAcc.accountName, fromDB.accountName);
            assertEquals(testAcc.accountName, testPDB.getAccountName(0));
            assertEquals(testAcc.username, fromDB.username);
            assertEquals(testAcc.username, testPDB.getUsername(0));
            assertEquals(testAcc.password, fromDB.password);
            assertEquals(testAcc.password, testPDB.getPassword(0));
            assertFalse(testAcc.time < fromDB.time);
        } catch (InvalidDatabaseException e) {
            fail(e);
        }
    }
    // TODO Test sorting
}
