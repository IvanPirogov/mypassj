package com.shaman.mypassj.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoDB {
    private IvParameterSpec iv;
    private SecretKeySpec key;
    private String rawFile;
    private String cryptoFile;
    public CryptoDB(String password,String rawFile, String cryptoFile) {
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

            this.cryptoFile = cryptoFile;
            this.rawFile = rawFile;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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
            System.out.println("File Encrypted.");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
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
            System.out.println("File Decrypted.");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
