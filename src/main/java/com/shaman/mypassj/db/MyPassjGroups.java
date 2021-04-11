package com.shaman.mypassj.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Objects;


public class MyPassjGroups {
    private static MyPassjGroup rootGroup = null ;

    static public MyPassjGroup getRootGroup() {
        if (Objects.isNull(rootGroup)) createRootGroup();
        return rootGroup;
    }

    private static void createRootGroup() {
        MyPassjGroup root = new MyPassjGroup();
        root.New(0L, 0L,"ROOT",0, 0);
        rootGroup = root;
    }

    static public MyPassjGroup addGroup(MyPassjGroup parentGroup, Long id, String name, Integer orderNumber){
        MyPassjGroup group;
        if (id > 0)  {
            group = new MyPassjGroup(id, parentGroup.getId(), name, parentGroup.getLevel() + 1, orderNumber);
        }
        else {
            group = new MyPassjGroup();
            group.New(DataFile.getIdCounter(), parentGroup.getId(), name, parentGroup.getLevel() + 1, orderNumber);

        }
        parentGroup.getGroupList().add(group);
        return group;
    }

    static public void delGroup(MyPassjGroup parentGroup, MyPassjGroup group){
        parentGroup.getGroupList().remove(group);
    }

    static public String rootGroupToJson(){
        String res = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            res = mapper.writeValueAsString(rootGroup);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return res;
    }

    static public MyPassjGroup rootGroupFromJson(String jsonText){
        MyPassjGroup root= null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            root = mapper.readValue(jsonText, MyPassjGroup.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }
}