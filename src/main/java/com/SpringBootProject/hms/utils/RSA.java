package com.SpringBootProject.hms.utils;

import com.SpringBootProject.hms.exceptions.CustomException;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
@Configuration
public class RSA {
    protected PrivateKey privateKey;
    protected PublicKey publicKey;
    private final SecureRandom random = new SecureRandom();

    @PostConstruct
    public void generateKeyPair(){
        try{
            File publicKeyFile = new File("public.key");
            File privateKeyFile = new File("private.key");
            if(!publicKeyFile.exists() || !privateKeyFile.exists()) {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(1028,random);      //514, 1028, 2056
                KeyPair keyPair = keyPairGenerator.generateKeyPair();
                this.privateKey = keyPair.getPrivate();
                this.publicKey = keyPair.getPublic();
                //-------------------WRITE KEYS TO FILE----------------------------
                try (FileOutputStream fos = new FileOutputStream("public.key")) {
                    fos.write(publicKey.getEncoded());
                }
                try (FileOutputStream fos = new FileOutputStream("private.key")) {
                    fos.write(privateKey.getEncoded());
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        File publicKeyFile = new File("public.key");
        if(publicKeyFile.exists()){
            byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            return keyFactory.generatePublic(publicKeySpec);
        }else {
            generateKeyPair();
            return getPublicKey();
        }

    }

    public PrivateKey getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        File privateKeyFile = new File("private.key");
        if(privateKeyFile.exists()){
            byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        EncodedKeySpec privateKeySpec = new X509EncodedKeySpec(privateKeyBytes);
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            return keyFactory.generatePrivate(privateKeySpec);
        }else {
            generateKeyPair();
            return getPrivateKey();
        }
    }

    public String rsaEncrypt(String plainData) throws CustomException {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, this.getPublicKey());
            byte[] encryptBytes=cipher.doFinal(plainData.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptBytes);
        }catch (Exception e){
            System.out.println("rsaEncryption: "+e.getMessage());
            throw new CustomException("error in rsa encryption");
        }
    }
    public String rsaDecrypt(String encryptedData) throws CustomException {
        try{
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, this.getPrivateKey());
            byte[] decryptedBytes=cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        }catch (Exception e){
            System.out.println("rsaDecryption: "+e.getMessage());
            throw new CustomException("error in rsa decryption");
        }
    }
}
