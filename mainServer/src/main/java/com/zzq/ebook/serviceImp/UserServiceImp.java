package com.zzq.ebook.serviceImp;

import com.zzq.ebook.constant.constant;
import com.zzq.ebook.entity.UserIcon;
import com.zzq.ebook.utils.message.MsgCode;
import com.zzq.ebook.utils.message.MsgUtil;
import com.zzq.ebook.utils.message.Msg;
import com.zzq.ebook.dao.UserDao;
import com.zzq.ebook.entity.User;
import com.zzq.ebook.service.UserService;
import com.zzq.ebook.utils.session.SessionUtil;
import net.sf.json.JSONObject;
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
    public Msg loginUserCheck(String username, String password){
        User user = userDao.checkUser(username,password);
        if(user != null){
            // 检测用户账户禁用，禁用账户直接不允许登录，给出提示
            // 返回禁用了账户的信息
            if(user.getForbidlogin() == 1)
                return MsgUtil.makeMsg(MsgCode.LOGIN_USER_ERROR, MsgUtil.LOGIN_FAIL_ACCOUNT_FORBIDDEN);

            // 账户允许登录 写入 Session
            JSONObject obj = new JSONObject();
            obj.put(constant.USERNAME, user.getUsername());
            obj.put(constant.PRIVILEGE, user.getPrivilege());
            SessionUtil.setSession(obj);

            user.setUserIcon(new UserIcon(username,"none"));
            JSONObject data = JSONObject.fromObject(user);
            data.remove(constant.PASSWORD);
            data.remove("childOrderItem");
            // 返回允许成功信息
            return MsgUtil.makeMsg(MsgCode.SUCCESS, MsgUtil.LOGIN_SUCCESS_MSG, data);
        }

        // 返回不允许登录信息
        return MsgUtil.makeMsg(MsgCode.LOGIN_USER_ERROR);
    }


    @Override
    public User getUserByusername(String username){
        return userDao.getUserByUsername(username);
    }

    @Override
    public boolean checkUserIfExist(String username){
        User user = userDao.getUserByUsername(username);
        return user != null;
    }

    @Override
    public List<User> getAllUser(){
        return userDao.getAllUser();
    }

    @Override
    public boolean  setUserLoginPermit(String setObjUser, int setObjState){
        User user = userDao.getUserByUsername(setObjUser);
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
        if(userDao.getUserByUsername(username) != null)
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

    public UserIcon getUserIconByUsername(String username){

        return userDao.getUserIconByUsername(username);
    }
}
