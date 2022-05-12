package com.zzq.ebook.controller;


import com.zzq.ebook.constant.constant;
import com.zzq.ebook.entity.User;
import com.zzq.ebook.service.UserService;
import com.zzq.ebook.utils.message.Msg;
import com.zzq.ebook.utils.message.MsgCode;
import com.zzq.ebook.utils.message.MsgUtil;
import com.zzq.ebook.utils.session.SessionUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
public class userControl {

    @Autowired
    private UserService userService;

    // 请求用户的信息，需要附上参数用户名
    @RequestMapping("/user/info")
    public Msg requestUserInfo(@RequestBody Map<String, String> params){
        String username = params.get(constant.USERNAME);
        JSONObject auth = SessionUtil.getAuth();
        // 拒绝非法的用户获取用户的信息
        if(!Objects.equals((String) auth.get(constant.USERNAME), username)){

            System.out.println(username);
            System.out.println((String) auth.get(constant.USERNAME));
            return null;
        }
        User user = userService.getUserByusername(username);
        JSONObject data = JSONObject.fromObject(user);
        data.remove(constant.PASSWORD);

        return MsgUtil.makeMsg(MsgCode.SUCCESS, MsgUtil.SUCCESS_MSG, data);
    }
}
