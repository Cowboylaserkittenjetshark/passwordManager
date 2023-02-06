package passwordmanager.database;

import java.util.Comparator;

public class Account {
    public String accountName;
    public String username;
    public String password;
    public long time;

    public Account(String accountName, String username, String password) {
        this.time = System.currentTimeMillis();
        this.accountName = accountName;
        this.username = username;
        this.password = password;
    }

    public static class AccountNameComparator implements Comparator<Account> {
        @Override
        public int compare(Account account0, Account account1) {
            return account0.accountName.compareTo(account1.accountName);
        }
    }

    public static class UsernameComparator implements Comparator<Account> {
        @Override
        public int compare(Account account0, Account account1) {
            return account0.username.compareTo(account1.username);
        }
    }

    public static class TimeComparator implements Comparator<Account> {
        @Override
        public int compare(Account account0, Account account1) {
            return Long.compare(account0.time, account1.time);
        }

    }
}
