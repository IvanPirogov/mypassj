package com.shaman.mypassj.db;

import java.util.Date;

public class MyPassjLogin {
    private Long id;
    private Long groupid;
    private String name;
    private String source;
    private String login;
    private String password;
    private String port;
    private String memo;
    private Date createddt;
    private Date updateddt;

    public MyPassjLogin() {}

    public MyPassjLogin(Long id, Long groupid, String name, String source, String login, String password, String port, String memo, Date createddt, Date updateddt) {
        this.id = id;
        this.groupid = groupid;
        this.name = name;
        this.source = source;
        this.login = login;
        this.password = password;
        this.port = port;
        this.memo = memo;
        this.createddt = createddt;
        this.updateddt = updateddt;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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
