package com.shaman.mypassj.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.Date;

public class MyPassjNotes {
    private static ArrayList<MyPassjNote> myPassjNotes = new ArrayList<>();


    public static void addNote(MyPassjNote note){
        myPassjNotes.add(note);
        saveNotes();
    }

    public static void editNote(MyPassjNote newNote){
        if (newNote != null) {
            MyPassjNote selNote = myPassjNotes.stream().filter(note -> note.getId() == newNote.getId()).findFirst().orElse(null);
            if (selNote != null) {
                selNote.setGroupid(newNote.getGroupid());
                selNote.setName(newNote.getName());
                selNote.setBody(newNote.getBody());
                selNote.setUpdateddt(newNote.getUpdateddt());
                saveNotes();
            }
        }
    }

    public static void deleteNote(long id){
        MyPassjNote selNote = myPassjNotes.stream().filter(note -> note.getId() == id).findFirst().orElse(null);
        if (selNote != null) {
            myPassjNotes.remove(selNote);
            saveNotes();
        }
    }
    public static ArrayList<MyPassjNote> getMyPassjNotes(){
        return myPassjNotes;
    }

    static public String notesToJson(){
        String res = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            res = mapper.writeValueAsString(myPassjNotes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return res;
    }

    static public void saveNotes(){
        DataFile.dataFile.writeData(notesToJson(), "NOTES");
        MyPassjSetting.writeSettings();
    }

    static public void readNotesFromDB(){
        notesFromJson(DataFile.dataFile.readData("NOTES"));
    }
    static public void notesFromJson(String jsonText){
        if (jsonText != null && !jsonText.equals("")) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                myPassjNotes = mapper.readValue(jsonText, new TypeReference<ArrayList<MyPassjNote>>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static public MyPassjNote getNode(Long id){
        return myPassjNotes.stream().filter(note -> note.getId() == id).findFirst().orElse(null);
    }
}
