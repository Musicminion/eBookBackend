package com.zzq.ebook.controller;

import com.zzq.ebook.entity.User;
import com.zzq.ebook.service.BookService;
import com.zzq.ebook.service.OrderService;
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


// -------------------------------------接-----------口-----------表-----------------------------------------------------
//      接口名称                                接口说明：
//      /login           登录接口，接收POST请求，参数必须要包括username password，返回用户的完整信息。
//      /logout          登出接口，接收POST请求，参数无，返回是否登出成功，
//      /checkSession    检查当前会话的Session，接收POST请求，返回当前是否处于登录的窗台
//      /register        注册接口，接收POST请求，接收的参数如下：是否同意用户协议，用户名，密码，邮件，用户住址信息
// ---------------------------------------------------------------------------------------------------------------------


@RestController
public class loginControl {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public Msg login(@RequestBody Map<String, String> params){
        // 解析参数
        String username = params.get(constant.USERNAME);
        String password = params.get(constant.PASSWORD);
        return userService.loginUserCheck(username,password);
    }

    @RequestMapping("/logout")
    public Msg logout(){
        Boolean status = SessionUtil.removeSession();

        if(status){
            return MsgUtil.makeMsg(MsgCode.SUCCESS, MsgUtil.LOGOUT_SUCCESS_MSG);
        }
        return MsgUtil.makeMsg(MsgCode.ERROR, MsgUtil.LOGOUT_ERR_MSG);
    }

    @RequestMapping("/checkSession")
    public Msg checkSession(){
        JSONObject auth = SessionUtil.getAuth();

        if(auth == null){
            return MsgUtil.makeMsg(MsgCode.NOT_LOGGED_IN_ERROR);
        }
        else{
            return MsgUtil.makeMsg(MsgCode.SUCCESS, MsgUtil.LOGIN_SUCCESS_MSG, auth);
        }
    }

}
