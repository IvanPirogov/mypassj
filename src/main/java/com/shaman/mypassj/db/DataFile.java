package com.shaman.mypassj.db;

import com.shaman.mypassj.crypto.CryptoDB;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.json.JSONObject;


import java.io.*;
import java.util.Optional;

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
    public CryptoDB cryptoData;

    public String getFileSettings() {
        return rawFileSettings;
    }

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
        if (path.charAt(path.length() - 1) != '/') path += "/";
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

    public int OpenDatafile() {
        File f = new File(cryptoFile);
        if (f.exists()) {
            if (cryptoData.Decrypt() == 0) {
                unpackDataFromFile();
                MyPassjSetting.readInnerSettings();
            } else return 1;
        } else {
            return 1;
        }
        return 0;
    }

    public int SaveDataFile() {
        packDataToFile();
        cryptoData.Encrypt();
        deleteSysDatafiles();
        return 0;
    }

    public int CreateDatafile() {
        // Create Settings
        MyPassjSetting.resetAllIdCounters();
        MyPassjSetting.writeInnerSettings();

        // Create rootGroup
        MyPassjGroups.createRootGroup();
        dataFile.writeData(MyPassjGroups.rootGroupToJson(), "GROUPS");

        // Create Notes
        dataFile.writeData("", "NOTES");

        // Create Logins
        dataFile.writeData("", "LOGINS");

        // Create CryptoFile
        File f = new File(cryptoFile);
        if (f.exists()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Create a new DB");
            String s = "\nDatafile with this name exists.";
            s = s + "\n\nRecreate the Datafile?";
            alert.setContentText(s);
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                try {
                    if (!f.createNewFile()) return 1;
                } catch (IOException e) {
                    e.printStackTrace();
                    return 1;
                }
            } else {
                return 1;
            }
        } else
            try {
                if (!f.createNewFile()) return 1;
            } catch (IOException e) {
                e.printStackTrace();
                return 1;
            }
        return 0;
    }

    private void packDataToFile() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("SETTINGS",readData("SETTINGS"));
        jsonObject.put("GROUPS",readData("GROUPS"));
        jsonObject.put("LOGINS",readData("LOGINS"));
        jsonObject.put("NOTES",readData("NOTES"));
        writeData(jsonObject.toString(),"");
    }

    private void unpackDataFromFile() {
        JSONObject jsonObject = new JSONObject(readData(""));
        writeData( jsonObject.get("SETTINGS").toString(), "SETTINGS");
        writeData( jsonObject.get("GROUPS").toString(), "GROUPS");
        writeData( jsonObject.get("LOGINS").toString(), "LOGINS");
        writeData( jsonObject.get("NOTES").toString(), "NOTES");
    }

    public void deleteSysDatafiles(){
        File f = new File(rawFileGroups);
        if (f.exists()) f.delete();
        f = new File(rawFileNotes);
        if (f.exists()) f.delete();
        f = new File(rawFileLogins);
        if (f.exists()) f.delete();
        f = new File(rawFileSettings);
        if (f.exists()) f.delete();
        f = new File(rawFile);
        if (f.exists()) f.delete();
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
        if(new File(fileName).exists()) {
            try (FileInputStream f = new FileInputStream(fileName)) {
                byte[] strToBytes = f.readAllBytes();
                if (strToBytes == null) return null;
                else return new String(strToBytes);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }else return null;
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
