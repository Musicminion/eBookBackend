package com.zzq.ebook.controller;

import net.sf.json.JSONObject;
import com.zzq.ebook.constant.constant;
import com.zzq.ebook.entity.User;
import com.zzq.ebook.service.UserService;
import com.zzq.ebook.utils.message.Msg;
import com.zzq.ebook.utils.message.MsgCode;
import com.zzq.ebook.utils.message.MsgUtil;
import com.zzq.ebook.utils.session.SessionUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;


// -------------------------------------接-----------口-----------表-----------------------------------------------------
//      接口名称                                接口说明：
//      /checkUserExit                  检查用户是否存在
//      /queryAllUserInfo               请求所有的用户的信息，接收POST请求
//      /queryMeInfo                    读取用户信息（基本信息：用户名/地址/邮箱/电话号）
//      /setUserLoginPermit             设置用户登录许可（允许/禁止某用户登录）
//      /register                       注册一个新的用户
// ---------------------------------------------------------------------------------------------------------------------


@RestController
public class userControl {

    @Autowired
    private UserService userService;
    // 函数用途：用户是否存在的检查，接收POST请求，参数必须包括要检查的用户名username，接口返回查询结果，是否存在.
    //         该接口不需要鉴定权限
    // 使用场景：注册页面，用户输入用户名的时候,利用ajax请求，校验用户名是否合法
    // 权限要求：无
    @RequestMapping("/user/checkUserExit")
    public Msg checkUserExit(@RequestBody Map<String, String> params){
        String username = params.get(constant.USERNAME);
        System.out.println(username);

        if(!userService.checkUserIfExist(username)){
            return MsgUtil.makeMsg(MsgCode.SUCCESS, MsgUtil.SUCCESS_MSG);
        }
        else{
            return MsgUtil.makeMsg(MsgCode.ERROR,MsgUtil.USERNAME_REPETE);
        }
    }

    // 函数用途：用户信息接口，接收POST请求，参数必须包括用户名username，接口返回用户不包括密码的完整数据库表json字段对象，
    //         会检查当前的会话，判断用户信息，接口用于获取用户的个人信息、权限等
    // 使用场景：购物下单时候，读取![当前会话]!用户信息,前台展示
    // 权限要求：登录的用户
    @RequestMapping("/user/queryMeInfo")
    public Msg requestUserInfo(@RequestBody Map<String, String> params){
        String username = params.get(constant.USERNAME);
        JSONObject auth = SessionUtil.getAuth();
        // 拒绝非法的用户获取用户的信息
        assert auth != null;
        if(!Objects.equals((String) auth.get(constant.USERNAME), username)){
            return null;
        }
        User user = userService.getUserByusername(username);
        JSONObject data = JSONObject.fromObject(user);
        data.remove(constant.PASSWORD);

        return MsgUtil.makeMsg(MsgCode.SUCCESS, MsgUtil.SUCCESS_MSG, data);
    }

    // 函数用途：请求所有的用户信息列表，接收POST请求，接口返回用户的信息数据表，当然会移除密码等敏感信息
    // 使用场景：管理员 用户管理页面的表格读取数据使用的
    // 权限要求：管理员（权限号-0）
    @RequestMapping("/user/queryAllUserInfo")
    public List<User> queryAllUserInfo(){
        JSONObject auth = SessionUtil.getAuth();
        // 检查是全局管理员
        if(auth != null && Objects.equals(auth.get(constant.PRIVILEGE),0)){
            List<User> resultData = userService.getAllUser();

            // 移除密码等敏感信息
            for (User resultDatum : resultData) {
                resultDatum.setPassword("");
            }

            return userService.getAllUser();
        }
        else
            return null;
    }


    // 函数用途：响应用户的注册请求
    // 使用场景：登录页面的注册按钮，注册完成后，信息发送到这个端点
    // 权限要求：无限制
    @RequestMapping("/user/register")
    public Msg register(@RequestBody Map<String, String> params){
        String email = params.get(constant.EMAIL);
        String location = params.get(constant.LOCATION);
        String password = params.get(constant.PASSWORD);
        String phone = params.get(constant.PHONE);
        String username = params.get(constant.USERNAME);
        String confirm = params.get(constant.CONFIRM);
        String agreement = params.get(constant.AGREEMENT);

        int result = userService.registerUser(email,location,password,phone,username,confirm,agreement);

        // 0-正常注册 1-用户名已经存在 2-不一致的两次密码 3-其他原因失败
        switch (result) {
            case 0:
                return MsgUtil.makeMsg(MsgCode.SUCCESS,MsgUtil.SUCCESS_MSG);
            case -1:
                return MsgUtil.makeMsg(MsgCode.ERROR,MsgUtil.REGISTER_FAIL_USERNAME_EXIT);
            case -2:
                return MsgUtil.makeMsg(MsgCode.ERROR,MsgUtil.REGISTER_FAIL_NOT_CONSISTENT_PWD);
            case -3:
                return MsgUtil.makeMsg(MsgCode.ERROR,MsgUtil.REGISTER_FAIL_OTHER);

            default:return null;
        }
    }

    // 函数用途：修改用户的登录许可，接收POST请求，接口返回修改的结果是否成功
    // 使用场景：管理员 用户管理页面的表格，管理员设置用户是否允许登录
    // 权限要求：管理员（权限号-0）
    // 请求格式：obj = {
    //              setUsername : setUser,
    //              loginPermitState:loginPermitState
    //          };
    @RequestMapping("/user/setUserLoginPermit")
    public Msg setUserLoginPermit(@RequestBody Map<String, String> params){
        JSONObject auth = SessionUtil.getAuth();
        // 检查是全局管理员，才允许设置
        if(auth != null && Objects.equals(auth.get(constant.PRIVILEGE),0)){
            String setObjUsername = params.get(constant.SET_OBJ_USERNAME);
            String setObjPermitState = params.get(constant.SET_OBJ_PERMITSTATE);
            int setObjPermitStateVal = Integer.parseInt(setObjPermitState);
            userService.setUserLoginPermit(setObjUsername,setObjPermitStateVal);

            // 自己修改自己的登录状态，会被拒绝 这在逻辑上也是不好的一个操作
            if(Objects.equals(auth.get(constant.USERNAME),setObjUsername))
                return MsgUtil.makeMsg(MsgCode.ERROR,MsgUtil.SET_LOGIN_PERMISSION_FAIL);

            return MsgUtil.makeMsg(MsgCode.SUCCESS,MsgUtil.SET_LOGIN_PERMISSION_SUCCESS);
        }
        else
            return MsgUtil.makeMsg(MsgCode.ERROR,MsgUtil.SET_LOGIN_PERMISSION_FAIL);
    }
}
