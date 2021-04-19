package com.shaman.mypassj.crypto;

import com.shaman.mypassj.controllers.MainController;
import javafx.scene.control.Alert;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoDB {
    private IvParameterSpec iv;
    private SecretKeySpec key;
    private String rawFile;
    private String cryptoFile;

    public void createKey(String password){
        try {
            final MessageDigest md = MessageDigest.getInstance("md5");
            md.update(password.getBytes(StandardCharsets.UTF_8));
            final byte[] hashkey = md.digest();
            this.key = new SecretKeySpec(hashkey, "AES");
            StringBuilder iv = new StringBuilder(password);
            iv.reverse();
            final MessageDigest md1 = MessageDigest.getInstance("md5");
            md1.update(iv.toString().getBytes(StandardCharsets.UTF_8));
            final byte[] hashiv = md.digest();
            this.iv = new IvParameterSpec(hashiv);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public CryptoDB(String password,String rawFile, String cryptoFile) {
        createKey(password);
        this.cryptoFile = cryptoFile;
        this.rawFile = rawFile;

    }

    public int Encrypt(){
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key,iv);
            byte[] input = new byte[64];
            int bytesRead;
            FileInputStream fis = new FileInputStream(rawFile);
            FileOutputStream fos = new FileOutputStream(cryptoFile);
            while ((bytesRead = fis.read(input)) != -1) {
                byte[] output = cipher.update(input, 0, bytesRead);
                if (output != null) fos.write(output);
            }
            byte[] output = cipher.doFinal();
            if (output != null) fos.write(output);
            fis.close();
            fos.flush();
            fos.close();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }

    }

     public int Decrypt(){
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key,iv);
            FileInputStream fis = new FileInputStream(cryptoFile);
            FileOutputStream fos = new FileOutputStream(rawFile);
            byte[] in = new byte[64];
            int read;
            while ((read = fis.read(in)) != -1) {
                byte[] output = cipher.update(in, 0, read);
                if (output != null) fos.write(output);
            }
            byte[] output = cipher.doFinal();
            if (output != null) fos.write(output);
            fis.close();
            fos.flush();
            fos.close();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Can not decrypt DB");
            alert.setContentText("Try to enter a correct password!");
            alert.showAndWait();
            return 1;
        }
    }

}
