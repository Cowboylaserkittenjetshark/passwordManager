package passwordmanager.database;

import java.security.SecureRandom;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;

import com.password4j.jca.providers.Password4jProvider;
import com.password4j.jca.spec.Argon2KeySpec;
import com.password4j.types.Argon2;

public class CryptUtil {
    protected static String decrypt(char[] password, IvParameterSpec initVector, byte[] salt, int memory, int iterations, int parallelization, int length, Argon2 type, byte[] accountList) {
        Password4jProvider.enable();
        SecretKeyFactory argonKeyFactory = SecretKeyFactory.getInstance("argon2");
        Argon2KeySpec argonSpec = new Argon2KeySpec(password, salt, memory, iterations, parallelization, length, Argon2.ID);
        SecretKey key = argonKeyFactory.generateSecret(argonSpec);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, initVector);
        return new String(cipher.doFinal(accountList));
    }
  
    static protected byte[] encrypt(byte[] accountList, byte[] salt, int memory, int iterations, int parallelization, int length) {
      
    }

    protected static IvParameterSpec getInitVector() {
        byte[] ivBytes = new byte[16];
        new SecureRandom().nextBytes(ivBytes);
        return new IvParameterSpec(ivBytes);
    }


}
