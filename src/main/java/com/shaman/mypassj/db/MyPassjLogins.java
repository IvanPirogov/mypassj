package com.shaman.mypassj.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class MyPassjLogins {
    static ArrayList<MyPassjLogin> myPassjLogins = null;


    public static void addLogin(MyPassjLogin login){
        myPassjLogins.add(login);
        saveLogins();
    }

    public static void editLogin(MyPassjLogin newLogin){
        if (newLogin != null) {
            System.out.println(newLogin.getId());
            MyPassjLogin selLogin = myPassjLogins.stream().filter(login -> login.getId() == newLogin.getId()).findFirst().orElse(null);
            if (selLogin != null) {
                selLogin.setGroupid(newLogin.getGroupid());
                selLogin.setName(newLogin.getName());
                selLogin.setLogin(newLogin.getLogin());
                selLogin.setUpdateddt(newLogin.getUpdateddt());
                selLogin.setMemo(newLogin.getMemo());
                selLogin.setPassword(newLogin.getPassword());
                selLogin.setPort(newLogin.getPort());
                selLogin.setSource(newLogin.getSource());
                saveLogins();
            }
        }
    }

    public static void deleteLogin(long id){
        MyPassjLogin selLogin = myPassjLogins.stream().filter(login -> login.getId() == id).findFirst().orElse(null);
        if (selLogin != null) {
            myPassjLogins.remove(selLogin);
            saveLogins();
        }
    }
    public static ArrayList<MyPassjLogin> getMyPassjLogins(){
        return myPassjLogins;
    }

    static public String loginsToJson(){
        String res = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            res = mapper.writeValueAsString(myPassjLogins);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return res;
    }

    static public void saveLogins(){
        DataFile.dataFile.writeData(loginsToJson(), "LOGINS");
        MyPassjSetting.writeInnerSettings();
    }

    static public void readLoginsFromDB(){
        loginsFromJson(DataFile.dataFile.readData("LOGINS"));
    }
    static public void loginsFromJson(String jsonText){
        if (jsonText != null && !jsonText.equals("")) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                myPassjLogins = mapper.readValue(jsonText, new TypeReference<ArrayList<MyPassjLogin>>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else myPassjLogins = new ArrayList<>();
    }

    static public MyPassjLogin getNode(Long id){
        return myPassjLogins.stream().filter(login -> login.getId() == id).findFirst().orElse(null);
    }
}
