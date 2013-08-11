package no.burkow.crypt;

import android.util.Base64;
import android.util.Log;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 8;

    private String password;

    private Cipher encryptCipher;
    private byte[] iv;
    private byte[] salt;

    private Cipher decryptCipher;
    private byte[] decryptIv;
    private byte[] decryptSalt;
    private static final String TAG = "Crypt";

    public Encryption(String password) throws Exception {
        this.password = password;

        SecureRandom random = new SecureRandom();
        salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        decryptSalt = salt.clone();

        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        SecretKey key = new SecretKeySpec(keyBytes, "AES");

        encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        iv = new byte[encryptCipher.getBlockSize()];
        random.nextBytes(iv);
        decryptIv = iv.clone();

        IvParameterSpec ivParams = new IvParameterSpec(iv);
        encryptCipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
        decryptCipher.init(Cipher.DECRYPT_MODE, key, ivParams);
    }

    public String encrypt(String plaintext) throws Exception {
        byte[] cipherText = encryptCipher.doFinal(plaintext.getBytes("UTF-8"));
        return Base64.encodeToString(salt, Base64.DEFAULT) + "]" + Base64.encodeToString(iv, Base64.DEFAULT) + "]" + Base64.encodeToString(cipherText, Base64.DEFAULT);
    }

    public String decrypt(String cipherText) throws Exception{
        String[] fields = cipherText.split("]");
        byte[] newSalt = Base64.decode(fields[0], Base64.DEFAULT);
        byte[] newIv = Base64.decode(fields[1], Base64.DEFAULT);
        byte[] payload = Base64.decode(fields[2], Base64.DEFAULT);

        if (Arrays.equals(decryptSalt, newSalt) && Arrays.equals(decryptIv, newIv) && decryptCipher != null) {
            Log.d(TAG, "CACHE HIT :D");
            byte[] plainText = decryptCipher.doFinal(payload);
            return new String(plainText, "UTF-8"); // UTF8 ?
        }

        decryptSalt = newSalt;
        decryptIv = newIv;

        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), decryptSalt, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        SecretKey key = new SecretKeySpec(keyBytes, "AES");

        decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParams = new IvParameterSpec(decryptIv);
        decryptCipher.init(Cipher.DECRYPT_MODE, key, ivParams);

        byte[] plainText = decryptCipher.doFinal(payload);
        return new String(plainText, "UTF-8");
    }
}