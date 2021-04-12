package com.shaman.mypassj.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.TreeItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
        }
        return root;
    }

    static public TreeItem<MyPassjGroup> buildTreeItem(){
        TreeItem<MyPassjGroup> resTeeItem = null;
//  begin test
        rootGroup = new MyPassjGroup(0L, 0L, "ROOT", 0,0); // test
        rootGroup.getGroupList().add(new MyPassjGroup(1L, 0L,  "Work", 1,1));  // test
        MyPassjGroup second = new MyPassjGroup(2L, 0L,  "Home", 1,2);
        second.getGroupList().add(new MyPassjGroup(4L, 2L,  "Room", 2,1));
        rootGroup.getGroupList().add(second);  // test
        rootGroup.getGroupList().add(new MyPassjGroup(3L, 0L,  "Wife", 1,3));  //test
//  end test
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
        System.out.println(rootGroupToJson());
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
}