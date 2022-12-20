package passwordmanager.database;

public class EncryptedDatabase {
	// AES encrypted list of accounts 
	protected byte[] accountList;
	// Argon2 parameters
	protected final byte[] salt;
	protected final int memory;
	protected final int iterations;
	protected final int parallelization;
	protected final int length;

	protected EncryptedDatabase(byte[] accountList, byte[] salt, int memory, int iterations, int parallelization, int length) {
		this.accountList = accountList;
		this.salt = salt;
		this.memory = memory;
		this.iterations = iterations;
		this.parallelization = parallelization;
		this.length = length;
	}
}
