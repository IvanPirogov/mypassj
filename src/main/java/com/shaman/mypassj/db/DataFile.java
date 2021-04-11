package com.shaman.mypassj.db;

import com.shaman.mypassj.crypto.CryptoDB;

import java.io.File;
import java.io.IOException;

public class DataFile {
    private  static Long idCounter = 0L;
    private final String datafileName;
    private final String rawFile;
    private final String cryptoFile;
    private final String path;
    private final String passw;
    CryptoDB cryptoData;

    public DataFile(String datafileName, String rawFile, String cryptoFile, String path, String passw) {
        this.datafileName = datafileName;
        this.rawFile = rawFile;
        this.cryptoFile = cryptoFile;
        this.path = path;
        this.passw = passw;
    }

    public static Long getIdCounter() {
        return ++idCounter;
    }

    public DataFile(String datafileName, String path, String passw) {
        this.passw = passw;
        this.datafileName = datafileName;
        if (path.charAt(path.length()-1) != '/') path += "/";
        this.path = path;
        this.rawFile = path + "._" + datafileName + ".sys";
        this.cryptoFile = path + "" + datafileName + ".mpj";
        cryptoData = new CryptoDB(passw, rawFile, cryptoFile);
    }

    public int OpenDatafile(){
        File f = new File(cryptoFile);
        if (f.exists()) {
            cryptoData.Decrypt();
        } else {
            return 1; // File is not exists or directory is not correctly
        }
        return 0;  // File is OK
    }

    public int SaveDataFile(){
        cryptoData.Encrypt();
        return  0;
    }

    public int CreateDatafile(){
        File f = new File(cryptoFile);
        if (f.exists())
            System.out.println("Exists");
        else
            try{
                if(! f.createNewFile()) return 1;
            } catch (IOException e) {
                e.printStackTrace();
                return 1;
            }
        return 0;
    }

    public int CloseDatafile(){
        File f = new File(rawFile);
        if (! f.delete()) return 1;
        return 0;
    }

    public String readData(){
        return null;
    }

    public int writeData(String data){

        return 0;

    }
}
