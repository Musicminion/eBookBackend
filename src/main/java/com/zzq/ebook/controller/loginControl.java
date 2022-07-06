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

        String username = params.get(constant.USERNAME);
        String password = params.get(constant.PASSWORD);

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

            // 登录成功
            return MsgUtil.makeMsg(MsgCode.SUCCESS, MsgUtil.LOGIN_SUCCESS_MSG, data);
        }

        // 登录失败
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

    //agreement: true
    //confirm: "1231231"
    //email: "zhangziqian@sjtu.edu.cn"
    //location: "123123123"
    //password: "1231231"
    //phone: "18062765851"
    //username: "123123123"






}
