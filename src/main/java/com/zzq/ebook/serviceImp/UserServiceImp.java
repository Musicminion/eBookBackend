package com.zzq.ebook.serviceImp;

import com.zzq.ebook.dao.UserDao;
import com.zzq.ebook.entity.User;
import com.zzq.ebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
        // 为了安全，后端再检查一下用户名有没有重复，重复了的话，就返回错误
        if(userDao.getUserByusername(username) != null)
            return -1;
        if(!Objects.equals(password, confirm))
            return -2;

        System.out.println(agreement);

        User newUser = new User();

        newUser.setUsername(username);
        newUser.setName(username);
        newUser.setEmail(email);
        newUser.setUseraddress(location);
        newUser.setPassword(password);
        newUser.setTelephone(phone);
        newUser.setForbidlogin(0);
        newUser.setPrivilege(2);

        userDao.saveOneUser(newUser);
        return 0;
    }
}
