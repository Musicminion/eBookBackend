package com.zzq.ebook.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;


@Document(collection = "userIcon")
public class UserIcon {
    @Id
    private String _id;

    private String username;
    private String iconBase64;

    public UserIcon(String username, String iconBase64) {
        this.username = username;
        this.iconBase64 = iconBase64;
    }

    public String getIconBase64() {
        return iconBase64;
    }

    public String getUsername() {
        return username;
    }

    public void setIconBase64(String iconBase64) {
        this.iconBase64 = iconBase64;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

