package com.zzq.ebook.serviceImp;

import com.zzq.ebook.dao.UserDao;
import com.zzq.ebook.entity.User;
import com.zzq.ebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService {


    @Autowired
    private UserDao userDao;

    @Override
    public User checkUser(String username, String password){
        return userDao.checkUser(username,password);
    }

    @Override
    public User getUserByusername(String username){
        return userDao.getUserByusername(username);
    }

    @Override
    public boolean checkUserIfExist(String username){
        User user = userDao.getUserByusername(username);
        return user != null;
    }

    @Override
    public List<User> getAllUser(){
        return userDao.getAllUser();
    }

    @Override
    public boolean  setUserLoginPermit(String setObjUser, int setObjState){
        User user = userDao.getUserByusername(setObjUser);
        if(user != null){
            user.setForbidlogin(setObjState);
            userDao.saveOneUser(user);
            return true;
        }
        else {
            System.out.println("没有获取到用户实体");
            return false;
        }
    }
    @Override
    public int registerUser(String email,String location,String password,String phone,
                            String username, String confirm,String agreement){






        return 0;
    }
}
