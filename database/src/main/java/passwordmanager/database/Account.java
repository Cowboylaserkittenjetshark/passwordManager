package passwordmanager.database;

public class Account {
    protected String accountName;
    protected String username;
    protected String password;

    protected Account(String accountName, String username, String password) {
        this.accountName = accountName;
        this.username = username;
        this.password = password;
    }
}
