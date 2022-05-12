package com.zzq.ebook.controller;

import com.zzq.ebook.entity.User;
import com.zzq.ebook.service.UserService;
import com.zzq.ebook.utils.session.SessionUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zzq.ebook.utils.message.MsgCode;
import com.zzq.ebook.utils.message.MsgUtil;
import com.zzq.ebook.utils.message.Msg;
import com.zzq.ebook.constant.constant;

import java.util.Map;


//      接口名称                                接口说明：
//      /login           登录接口，接收POST请求，参数必须要包括username password，返回用户的完整信息。
//      /logout          登出接口，接收POST请求，参数无，返回是否登出成功，
//      /refreshToken    刷新令牌，接收POST请求，参数无，返回当前会话的用户名+请求信息。

//      /userinfo        用户信息接口，接收POST请求，参数包括用户名username，接口返回用户不包括密码的完整数据库表json字段对象，
//                       会检查当前的回话，判断用户信息，接口用于获取用户的个人信息、权限等




@RestController
public class loginControl {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public Msg function(@RequestBody Map<String, String> params){

        String username = params.get(constant.USERNAME);
        String password = params.get(constant.PASSWORD);

        System.out.println(username);
        System.out.println(password);

        User user = userService.checkUser(username,password);

        if(user != null){
            // 检测用户账户禁用，禁用账户直接不允许登录，给出提示
            if(user.getForbidlogin() == 1)
                return MsgUtil.makeMsg(MsgCode.LOGIN_USER_ERROR, MsgUtil.LOGIN_FAIL_ACCOUNT_FORBIDDEN);

            // 账户允许登录
            JSONObject obj = new JSONObject();
            obj.put(constant.USERNAME, user.getUsername());
            obj.put(constant.PRIVILEGE, user.getPrivilege());
            SessionUtil.setSession(obj);

            JSONObject data = JSONObject.fromObject(user);
            data.remove(constant.PASSWORD);
            return MsgUtil.makeMsg(MsgCode.SUCCESS, MsgUtil.LOGIN_SUCCESS_MSG, data);
        }

        return MsgUtil.makeMsg(MsgCode.LOGIN_USER_ERROR);
    }

    @RequestMapping("/logout")
    public Msg logout(){
        Boolean status = SessionUtil.removeSession();

        if(status){
            return MsgUtil.makeMsg(MsgCode.SUCCESS, MsgUtil.LOGOUT_SUCCESS_MSG);
        }
        return MsgUtil.makeMsg(MsgCode.ERROR, MsgUtil.LOGOUT_ERR_MSG);
    }

    @RequestMapping("/refreshToken")
    public Msg refreshToken(){
        JSONObject auth = SessionUtil.getAuth();

        if(auth == null){
            return MsgUtil.makeMsg(MsgCode.NOT_LOGGED_IN_ERROR);
        }
        else{
            return MsgUtil.makeMsg(MsgCode.SUCCESS, MsgUtil.LOGIN_SUCCESS_MSG, auth);
        }
    }
}
