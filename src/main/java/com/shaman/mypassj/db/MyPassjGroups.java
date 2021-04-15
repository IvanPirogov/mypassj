package com.shaman.mypassj.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.Objects;


public class MyPassjGroups {
    private static MyPassjGroup rootGroup = null ;

    static public MyPassjGroup getRootGroup() {
        if (Objects.isNull(rootGroup)) createRootGroup();
        return rootGroup;
    }

    private static void createRootGroup() {
        rootGroup= new MyPassjGroup();
        rootGroup.New(MyPassjSetting.getIdCounter("GROUP"), 0L,"ROOT",0, 0);
    }

    static public MyPassjGroup addGroup(MyPassjGroup parentGroup, Long id, String name, Integer orderNumber){
        MyPassjGroup group;
        if (id > 0)  {
            group = new MyPassjGroup(id, parentGroup.getId(), name, parentGroup.getLevel() + 1, orderNumber);
        }
        else {
            group = new MyPassjGroup();
            group.New(MyPassjSetting.getIdCounter("GROUP"), parentGroup.getId(), name, parentGroup.getLevel() + 1, orderNumber);

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
        }
        return root;
    }

    static public TreeItem<MyPassjGroup> buildTreeItem(){
        TreeItem<MyPassjGroup> resTeeItem = null;
        rootGroup = rootGroupFromJson(DataFile.dataFile.readData("GROUPS"));
        if (rootGroup != null) {
            resTeeItem = new TreeItem<>(rootGroup);
            resTeeItem.setExpanded(true);
            if (rootGroup.getGroupList() != null) {
                addTreeItemArray(rootGroup.getGroupList(), resTeeItem);
            }
        }
        return resTeeItem;
    }

     private static TreeItem<MyPassjGroup> addTreeItemArray(ArrayList<MyPassjGroup> groupList, TreeItem<MyPassjGroup> resTeeItem){
        for(MyPassjGroup group : groupList) {
            TreeItem<MyPassjGroup> item = new TreeItem<>(group);
            resTeeItem.getChildren().add(item);
            if (group.getGroupList() != null) {
                addTreeItemArray(group.getGroupList(), item);
            }
        }
        return resTeeItem;
    }

    static public void buildGroupObjects(TreeItem<MyPassjGroup> rootTreeItem){
        Integer childOrderNumber = 1;
        rootGroup = rootTreeItem.getValue();
        rootGroup.getGroupList().removeAll(rootGroup.getGroupList());
        for (TreeItem<MyPassjGroup> childItem : rootTreeItem.getChildren()) {
            addGroupObjects(childItem, rootGroup, childOrderNumber++);
        }
    }

    static private void addGroupObjects(TreeItem<MyPassjGroup> treeItem,MyPassjGroup parentGroup, Integer orderNumber){
        Integer childOrderNumber = 1;
        MyPassjGroup group = treeItem.getValue();
        group.setLevel(parentGroup.getLevel()+1);
        group.setOrderNumber(orderNumber);
        group.setParentid(parentGroup.getId());
        parentGroup.getGroupList().add(group);
        group.getGroupList().removeAll(group.getGroupList());
        for (TreeItem<MyPassjGroup> childItem : treeItem.getChildren()) {
            addGroupObjects(childItem, group, childOrderNumber++);
        }
    }

    static public void saveGroups(TreeItem<MyPassjGroup> rootTreeItem){
        buildGroupObjects(rootTreeItem);
        DataFile.dataFile.writeData(rootGroupToJson(), "GROUPS");
        MyPassjSetting.writeSettings();
    }
}