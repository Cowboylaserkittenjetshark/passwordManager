package passwordmanager.database;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.password4j.jca.providers.Password4jProvider;
import com.password4j.jca.spec.Argon2KeySpec;
import com.password4j.types.Argon2;

public class EncryptedDatabase {
	// AES info
	protected byte[] accountList;
	protected byte[] initVector;
	// Argon2 parameters
	protected final byte[] salt;
	protected final int memory;
	protected final int iterations;
	protected final int parallelization;
	protected final int length;
	protected final Argon2 type;

	protected EncryptedDatabase(byte[] accountList, int memory, int iterations, int parallelization, int length, Argon2 type) {
		this.accountList = accountList;
		this.initVector = getRandomBytes(16);
		this.salt = getRandomBytes(16);
		this.memory = memory;
		this.iterations = iterations;
		this.parallelization = parallelization;
		this.length = length;
		this.type = type;
	}

	protected String decrypt(char[] password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        SecretKey key = getSecretKey(password, this.salt, this.memory, this.iterations, this.parallelization, this.length, this.type);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(this.initVector));
        return new String(cipher.doFinal(accountList));
    }
  
    protected void encrypt(char[] password, String accountListJSON) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
      SecretKey key = getSecretKey(password, this.salt, this.memory, this.iterations, this.parallelization, this.length, this.type);
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	  this.initVector = getRandomBytes(16);
      cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(this.initVector));
      this.accountList = cipher.doFinal(accountListJSON.getBytes());
    }

	// TODO Talk about JCA
    private static SecretKey getSecretKey(char[] password, byte[] salt, int memory, int iterations, int parallelization, int length, Argon2 type) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Password4jProvider.enable();
        SecretKeyFactory argonKeyFactory = SecretKeyFactory.getInstance("argon2");
        Argon2KeySpec argonSpec = new Argon2KeySpec(password, salt, memory, iterations, parallelization, length, Argon2.ID);
        return new SecretKeySpec(argonKeyFactory.generateSecret(argonSpec).getEncoded(), "AES");
    }

	private static byte[] getRandomBytes(int length) {
        byte[] randomBytes = new byte[length];
        new SecureRandom().nextBytes(randomBytes);
        return randomBytes;
    }

	public String toString() {
		return ("" + memory + " " + iterations + " " + parallelization + " " + length);
	}
 }
