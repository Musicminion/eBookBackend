package com.zzq.ebook.entity;


import com.zzq.ebook.entity.UserIcon;
import com.fasterxml.jackson.annotation.*;
import javax.persistence.*;
import java.util.List;

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

    private String telephone;
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
    public String getEmail() { return email; }
    public String getUseraddress() {
        return useraddress;
    }
    public int getForbidlogin() {
        return forbidlogin;
    }

    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public void setUsername(String username){
        this.username = username;
    }

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "belonguser")
    private List<OrderItem> childOrderItem;
    public List<OrderItem> getChildOrderItem() {
        return childOrderItem;
    }
    public void setChildOrderItem(List<OrderItem> childOrderItem) {
        this.childOrderItem = childOrderItem;
    }



    @Transient
    private UserIcon userIcon;
    @Transient
    public UserIcon getUserIcon() {
        return userIcon;
    }
    @Transient
    public void setUserIcon(UserIcon userIcon) {
        this.userIcon = userIcon;
    }
}

