package com.zzq.ebook.daoImp;

import com.zzq.ebook.dao.UserDao;
import com.zzq.ebook.entity.User;
import com.zzq.ebook.entity.UserIcon;
import com.zzq.ebook.repository.UserIconRepository;
import com.zzq.ebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserIconRepository userIconRepository;

    @Override
    public User checkUser(String username, String password){
        return userRepository.checkUser(username,password);
    }
    @Override
    public User getUserByUsername(String username){
        User user = userRepository.getOne(username);
        UserIcon userIcon = userIconRepository.findUserIconByUsername(username);
        if(userIcon != null){
            user.setUserIcon(userIcon);
        }
        return user;
    }
    @Override
    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    @Override
    public User saveOneUser(User oneUser){
        return userRepository.save(oneUser);
    }


    // 测试用的
    @Override
    public UserIcon getUserIconByUsername(String username){
        return userIconRepository.findUserIconByUsername(username);
    }
}
