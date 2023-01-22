package com.zzq.ebook.dao;

import com.zzq.ebook.entity.User;
import com.zzq.ebook.entity.UserIcon;

import java.util.List;

public interface UserDao {

    User checkUser(String username, String password);

    User getUserByUsername(String username);

    List<User> getAllUser();

    User saveOneUser(User oneuser);

    UserIcon getUserIconByUsername(String username);
}
