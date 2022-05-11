package com.zzq.ebook.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "users")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class User {
    @Id
    private String username;

    private String password;
    private int privilege;
    private String name;
    private String email;
    private String useraddress;
    private int forbidlogin;

    public String getUsername(){
        return this.username;
    }
    public String getPassword() {
        return password;
    }
    public int getPrivilege() {
        return privilege;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getUseraddress() {
        return useraddress;
    }
    public int getForbidlogin() {
        return forbidlogin;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }
    public void setForbidlogin(int forbidlogin) {
        this.forbidlogin = forbidlogin;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
    }


//    public void setUsername(String username) {
//        this.username = username;
//    }
}

