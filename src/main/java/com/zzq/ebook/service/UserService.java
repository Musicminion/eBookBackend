package com.zzq.ebook.service;

import com.zzq.ebook.entity.User;

public interface UserService {

    public User checkUser(String username, String password);

    public User getUserByusername(String username);

}
