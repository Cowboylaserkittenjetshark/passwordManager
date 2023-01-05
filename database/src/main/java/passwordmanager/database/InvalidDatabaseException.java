package passwordmanager.database;

public class InvalidDatabaseException extends Exception {
    public InvalidDatabaseException(String message) {
        super(message);
    }

    public InvalidDatabaseException(String message, Throwable exception) {
        super(message, exception);
    }
}
