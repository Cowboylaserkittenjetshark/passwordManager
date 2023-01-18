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

import org.junit.jupiter.api.Test;

import com.password4j.jca.providers.Password4jProvider;
import com.password4j.jca.spec.Argon2KeySpec;
import com.password4j.types.Argon2;

public class StandaloneTest {
    @Test
    void doCrypt() throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        String password = "password";
        int memory = 15;
        int iterations = 1;
        int parallelization = 2;
        int length = 32;
        byte[] salt = new byte[16];
        byte[] ivB = new byte[16];
        new SecureRandom().nextBytes(salt);
        new SecureRandom().nextBytes(ivB);
        Password4jProvider.enableUnlimited();
        SecretKeyFactory argonKeyFactory = SecretKeyFactory.getInstance("argon2");
        Argon2KeySpec argonSpec = new Argon2KeySpec(password.toCharArray(), salt, memory, iterations, parallelization,
                length, Argon2.ID);
        SecretKey key = new SecretKeySpec(argonKeyFactory.generateSecret(argonSpec).getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec initVector = new IvParameterSpec(ivB);
        cipher.init(Cipher.ENCRYPT_MODE, key, initVector);
        byte[] encOpOut = cipher.doFinal(("toBeEnc").getBytes());
        System.out.print(encOpOut);
    }
}
