package com.shaman.mypassj.db;

import java.util.ArrayList;

public class MyPassjGroup {
    private Long id;
    private Long parentid;
    private String name;
    private Integer level;
    private Integer orderNumber;
    private ArrayList<MyPassjGroup> groupList = new ArrayList<>();

    public MyPassjGroup(){}
    public MyPassjGroup(Long id, Long parentid, String name, Integer level, Integer orderNumber) {
        this.id = id;
        this.parentid = parentid;
        this.name = name;
        this.level = level;
        this.orderNumber = orderNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentid() {
        return parentid;
    }

    public void setParentid(Long parentid) {
        this.parentid = parentid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public ArrayList<MyPassjGroup> getGroupList() {
        return groupList;
    }

    public void setGroupList(ArrayList<MyPassjGroup> groupList) {
        this.groupList = groupList;
    }

    public boolean New(Long id, Long parentid, String name, Integer level, Integer orderNumber) {
        this.id = id;
        this.parentid = parentid;
        this.name = name;
        this.level = level;
        this.orderNumber = orderNumber;
        return true;
    }

    @Override
    public String toString() { return  name; }
}