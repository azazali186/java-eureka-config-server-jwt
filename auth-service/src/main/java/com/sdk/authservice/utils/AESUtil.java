package com.sdk.authservice.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtil {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY = "Aj189628@";    
    private static final String AES = "Aj189628@";

    private static final int IV_SIZE = 256; // AES block size

    public static String encrypt(String value) throws Exception {
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), AES);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv); // generate random IV
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

        byte[] encrypted = cipher.doFinal(value.getBytes());
        byte[] encryptedIVAndText = new byte[IV_SIZE + encrypted.length];
        System.arraycopy(iv, 0, encryptedIVAndText, 0, IV_SIZE);
        System.arraycopy(encrypted, 0, encryptedIVAndText, IV_SIZE, encrypted.length);

        return Base64.getEncoder().encodeToString(encryptedIVAndText);
    }

    public static String decrypt(String encryptedIvText) throws Exception {
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), AES);
        byte[] ivAndEncryptedText = Base64.getDecoder().decode(encryptedIvText);
        byte[] iv = new byte[IV_SIZE];
        System.arraycopy(ivAndEncryptedText, 0, iv, 0, iv.length);
        byte[] encryptedText = new byte[ivAndEncryptedText.length - IV_SIZE];
        System.arraycopy(ivAndEncryptedText, IV_SIZE, encryptedText, 0, encryptedText.length);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

        return new String(cipher.doFinal(encryptedText));
    }
}

