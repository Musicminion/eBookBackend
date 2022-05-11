package com.zzq.ebook.dao;

import com.zzq.ebook.entity.User;
import com.zzq.ebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public interface UserDao {

    User checkUser(String username, String password);
}
