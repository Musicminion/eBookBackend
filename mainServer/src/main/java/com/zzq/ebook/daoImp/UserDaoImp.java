package com.zzq.ebook.daoImp;

import com.zzq.ebook.dao.UserDao;
import com.zzq.ebook.entity.User;
import com.zzq.ebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    @Autowired
    UserRepository userRepository;

    @Override
    public User checkUser(String username, String password){
        return userRepository.checkUser(username,password);
    }
    @Override
    public User getUserByusername(String username){
        return userRepository.getOne(username);
    }
    @Override
    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    @Override
    public User saveOneUser(User oneUser){
        return userRepository.save(oneUser);
    }
}
