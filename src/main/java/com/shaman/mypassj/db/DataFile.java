package com.shaman.mypassj.db;

import com.shaman.mypassj.crypto.CryptoDB;

import java.io.*;

public class DataFile {

    public static DataFile dataFile;
    private final String rawFile;
    private final String rawFileGroups;
    private final String rawFileNotes;
    private final String rawFileLogins;
    private final String rawFileSettings;
    private final String rawFileIcons;
    private final String rawFileDocs;
    private final String cryptoFile;
    private final String passw;
    CryptoDB cryptoData;

    public String getFileSettings() { return rawFileSettings;}

    public DataFile(String rawFile, String rawFileGroups, String rawFileNotes, String rawFileLogins, String rawFileSettings, String rawFileIcons, String cryptoFile, String passw) {
        this.rawFile = rawFile;
        this.rawFileGroups = rawFileGroups;
        this.rawFileNotes = rawFileNotes;
        this.rawFileLogins = rawFileLogins;
        this.rawFileSettings = rawFileSettings;
        this.rawFileIcons = rawFileIcons;
        this.cryptoFile = cryptoFile;
        this.passw = passw;
        this.rawFileDocs = null;
    }

    public DataFile(String datafileName, String path, String passw) {
        this.passw = passw;
        if (path.charAt(path.length()-1) != '/') path += "/";
        this.rawFile = path + "._" + datafileName + ".sys";
        this.rawFileGroups = path + "._" + datafileName + "_groups.sys";
        this.rawFileNotes = path + "._" + datafileName + "_notes.sys";
        this.rawFileLogins = path + "._" + datafileName + "_logins.sys";
        this.rawFileSettings = path + "._" + datafileName + "_settings.sys";
        this.rawFileIcons = path + "._" + datafileName + "_icons.sys";
        this.rawFileDocs = path + "._" + datafileName + "_docs.sys";
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
    // Create Settings
        MyPassjSetting.writeSettings();

    // Create rootGroup
        MyPassjSetting.resetIdCounter("GROUP");
        MyPassjGroups.getRootGroup();
        DataFile.dataFile.writeData(MyPassjGroups.rootGroupToJson(), "GROUPS");
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

    public String readData(String fileType){
        String fileName = switch (fileType) {
            case "GROUPS" -> rawFileGroups;
            case "NOTES" -> rawFileNotes;
            case "LOGINS" -> rawFileLogins;
            case "SETTINGS" -> rawFileSettings;
            case "ICONS" -> rawFileIcons;
            case "DOCS" -> rawFileDocs;
            default -> rawFile;
        };
        try (FileInputStream f = new FileInputStream(fileName)) {
            byte[] strToBytes = f.readAllBytes();
            return new String(strToBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int writeData(String data, String fileType){
        String fileName = switch (fileType) {
            case "GROUPS" -> rawFileGroups;
            case "NOTES" -> rawFileNotes;
            case "LOGINS" -> rawFileLogins;
            case "SETTINGS" -> rawFileSettings;
            case "ICONS" -> rawFileIcons;
            case "DOCS" -> rawFileDocs;
            default -> rawFile;
        };
        try (FileOutputStream f = new FileOutputStream(fileName)) {
            byte[] strToBytes = data.getBytes();
            f.write(strToBytes);
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
