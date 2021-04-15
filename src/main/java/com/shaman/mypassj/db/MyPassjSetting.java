package com.shaman.mypassj.db;

import org.ini4j.*;

import java.io.File;
import java.io.FileWriter;

public class MyPassjSetting {
    private  static Long idGroupCounter = 0L;
    private  static Long idNoteCounter = 0L;
    private  static Long idLoginCounter = 0L;
    private  static Long idIconCounter = 0L;
    private  static Long idDocCounter = 0L;
    private  static String path;
    private  static String dataFileName;

    public static Long getIdCounter(String counterType) {
        return switch (counterType) {
            case "GROUP" -> ++idGroupCounter;
            case "NOTE" -> ++idNoteCounter;
            case "LOGIN" -> ++idLoginCounter;
            case "ICON" -> ++idIconCounter;
            case "DOC" -> ++idDocCounter;
            default -> -1L;
        };
    }

    public static void resetIdCounter(String counterType) {
        switch (counterType) {
            case "GROUP" -> idGroupCounter = 0L;
            case "NOTE" -> idNoteCounter = 0L;
            case "LOGIN" -> idLoginCounter = 0L;
            case "ICON" -> idIconCounter = 0L;
            case "DOC" -> idDocCounter = 0L;
        };
    }

    public static String getWorkDir() { return path;}
    public static String getDataFileName() { return dataFileName;}


    public static void writeSettings() {
        try {
            File f = new File(DataFile.dataFile.getFileSettings());
            if (!f.exists())f.createNewFile();
            Wini ini = new Wini(f);
            ini.put("counters", "idGroupCounter", idGroupCounter);
            ini.put("counters", "idNoteCounter", idNoteCounter);
            ini.put("counters", "idLoginCounter", idLoginCounter);
            ini.put("counters", "idIconCounter", idIconCounter);
            ini.put("counters", "idDocCounter", idDocCounter);
            ini.store();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void removeSectionsSettings(String sectionName) {
        try {
            Wini ini = new Wini(new File(DataFile.dataFile.getFileSettings()));
            ini.remove(sectionName);
            ini.store();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void readInnerSettings(){
        try{
            Wini ini = new Wini(new File(DataFile.dataFile.getFileSettings()));
            idGroupCounter = ini.get("counters", "idGroupCounter", Long.class);
            idNoteCounter = ini.get("counters", "idNoteCounter", Long.class);
            idLoginCounter = ini.get("counters", "idLoginCounter", Long.class);
            idIconCounter = ini.get("counters", "idIconCounter", Long.class);
            idDocCounter = ini.get("counters", "idDocCounter", Long.class);
        }catch(Exception e){
            System.err.println(e.getMessage());
        }

    }

    public static void readOuterSettings(){

    }
}
