package passwordmanager.database;

import javax.crypto.spec.IvParameterSpec;

import com.password4j.types.Argon2;

public class EncryptedDatabase {
	// AES info
	protected byte[] accountList;
	protected IvParameterSpec initVector;
	// Argon2 parameters
	protected final byte[] salt;
	protected final int memory;
	protected final int iterations;
	protected final int parallelization;
	protected final int length;
	protected final Argon2 type;

	protected EncryptedDatabase(byte[] accountList, IvParameterSpec initVector, byte[] salt, int memory, int iterations, int parallelization, int length, Argon2 type) {
		this.accountList = accountList;
		this.initVector = initVector;
		this.salt = salt;
		this.memory = memory;
		this.iterations = iterations;
		this.parallelization = parallelization;
		this.length = length;
		this.type = type;
	}

	protected void rotate(byte[] accountList) {
		this.accountList = accountList;
		this.initVector = CryptUtil.getInitVector();
	}
 }
