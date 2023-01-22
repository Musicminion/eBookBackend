package com.zzq.ebook.controller;

import com.zzq.ebook.entity.User;
import com.zzq.ebook.service.BookService;
import com.zzq.ebook.service.ClockService;
import com.zzq.ebook.service.OrderService;
import com.zzq.ebook.service.UserService;
import com.zzq.ebook.utils.session.SessionUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zzq.ebook.utils.message.MsgCode;
import com.zzq.ebook.utils.message.MsgUtil;
import com.zzq.ebook.utils.message.Msg;
import com.zzq.ebook.constant.constant;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;


// -------------------------------------接-----------口-----------表-----------------------------------------------------
//      接口名称                                接口说明：
//      /login           登录接口，接收POST请求，参数必须要包括username password，返回用户的完整信息。
//      /logout          登出接口，接收POST请求，参数无，返回是否登出成功，
//      /checkSession    检查当前会话的Session，接收POST请求，返回当前是否处于登录的窗台
//      /register        注册接口，接收POST请求，接收的参数如下：是否同意用户协议，用户名，密码，邮件，用户住址信息
// ---------------------------------------------------------------------------------------------------------------------


@RestController
@Scope(value = "session")
public class loginControl {

    @Autowired
    private UserService userService;
    @Autowired
    private ClockService clockService;


    @RequestMapping("/login")
    public Msg login(@RequestBody Map<String, String> params){
        // 解析参数
        String username = params.get(constant.USERNAME);
        String password = params.get(constant.PASSWORD);
        Msg tmpMsg = userService.loginUserCheck(username,password);
        // 登录成功 发起计时器
        if(tmpMsg!=null && tmpMsg.getStatus() >= 0) {
            clockService.launchClock();
            return tmpMsg;
        }
        return null;
    }

    @RequestMapping("/logout")
    public Msg logout(){
        Boolean status = SessionUtil.removeSession();
        if(status){
            // 成功移除回话，终止闹钟
            String timeIntervalInfo = clockService.endClock();
            JSONObject data = new JSONObject();
            data.put("timeInfo", timeIntervalInfo);
            return MsgUtil.makeMsg(MsgCode.SUCCESS, MsgUtil.LOGOUT_SUCCESS_MSG, data);
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
