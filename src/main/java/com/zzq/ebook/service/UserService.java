package com.zzq.ebook.service;

import com.zzq.ebook.entity.User;
import com.zzq.ebook.utils.message.Msg;

import java.util.List;

public interface UserService {

    // 通用功能 普通用户功能等
    // 检查用户的用户名密码
    public User checkUser(String username, String password);
    public Msg loginUserCheck(String username, String password);

    // 检查用户是否存在
    public boolean checkUserIfExist(String username);

    // 通过用户名获取用户的实体
    public User getUserByusername(String username);

    // 注册一个用户
    public int registerUser(String email,String location,String password,String phone,
                            String username, String confirm,String agreement);

    // 管理员功能
    // 请求数据库中的 所有的用户列表
    public List<User> getAllUser();

    public boolean setUserLoginPermit(String setObjUser, int setObjState);
}
