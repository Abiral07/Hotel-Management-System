package com.SpringBootProject.hms.utils;

import com.SpringBootProject.hms.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
@Configuration
public class AesEncryption {
    private SecretKey key;
    @Value("${aes.encryption.key}")          //n = 128 || 192 || 256
    String encryptionKey;

    public SecretKey getKey() {
        if (key == null) {
            key = new SecretKeySpec(encryptionKey.getBytes(), "AES");
        }

        return key;
    }

    //------------------ENCRYPT--------------------
    public String encrypt(String algorithm, String input) throws CustomException {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, this.getKey(), new IvParameterSpec(new byte[16]));
            byte[] cipherText = cipher.doFinal(input.getBytes());
            return Base64.getEncoder().encodeToString(cipherText);
        }catch (Exception e){
            System.out.println("AESencryption: ");
            throw new CustomException("aes encryptiopn error");
        }
    }
    //------------------DECRYPT-------------------
    public String decrypt(String algorithm, String cipherText) throws CustomException {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, this.getKey(), new IvParameterSpec(new byte[16]));
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return new String(plainText);
        }catch (Exception e){
            System.out.println("AESdecryption: ");
            throw new CustomException("aes decryptiopn error");
        }
    }
}
