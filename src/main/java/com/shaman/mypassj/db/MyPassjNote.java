package com.shaman.mypassj.db;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Date;

public class MyPassjNote {
    private Long id;
    private Long groupid;
    private String name;
    private String body;
    private Date createddt;
    private Date updateddt;


    public MyPassjNote(){

    }

    public MyPassjNote(Long id, Long groupid, String name, String body, Date createddt, Date updateddt) {
        this.id = id;
        this.groupid = groupid;
        this.name = name;
        this.body = body;
        this.createddt = createddt;
        this.updateddt = updateddt;
    }


    public void New(Long id, Long groupid, String name, String body) {
        this.id = id;
        this.groupid = groupid;
        this.name = name;
        this.body = body;
        this.updateddt =  new Date();
        this.createddt =  new Date();
    }

    public void Edit(Long groupid, String name, String body) {
        this.groupid = groupid;
        this.name = name;
        this.body = body;
        this.updateddt =  new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupid() {
        return groupid;
    }

    public void setGroupid(Long groupid) {
        this.groupid = groupid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public Date getCreateddt() {
        return createddt;
    }

    public void setCreateddt(Date createddt) {
        this.createddt = createddt;
    }

    public Date getUpdateddt() {
        return updateddt;
    }

    public void setUpdateddt(Date updateddt) {
        this.updateddt = updateddt;
    }
}
