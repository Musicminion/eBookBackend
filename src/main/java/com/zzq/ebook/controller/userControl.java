package com.zzq.ebook.controller;

import com.zzq.ebook.constant.constant;
import com.zzq.ebook.entity.User;
import com.zzq.ebook.service.UserService;
import com.zzq.ebook.utils.message.Msg;
import com.zzq.ebook.utils.message.MsgCode;
import com.zzq.ebook.utils.message.MsgUtil;
import com.zzq.ebook.utils.session.SessionUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.aspectj.apache.bcel.classfile.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

//   ___  ___   ________   _______    ________          ________   ________   ___
//  |\  \|\  \ |\   ____\ |\  ___ \  |\   __  \        |\   __  \ |\   __  \ |\  \
//  \ \  \\\  \\ \  \___|_\ \   __/| \ \  \|\  \       \ \  \|\  \\ \  \|\  \\ \  \
//   \ \  \\\  \\ \_____  \\ \  \_|/__\ \   _  _\       \ \   __  \\ \   ____\\ \  \
//    \ \  \\\  \\|____|\  \\ \  \_|\ \\ \  \\  \|       \ \  \ \  \\ \  \___| \ \  \
//     \ \_______\ ____\_\  \\ \_______\\ \__\\ _\        \ \__\ \__\\ \__\     \ \__\
//      \|_______||\_________\\|_______| \|__|\|__|        \|__|\|__| \|__|      \|__|
//                \|_________|
//

// -------------------------------------接-----------口-----------表-----------------------------------------------------
//      接口名称                                接口说明：
//      /userinfo        用户信息接口，接收POST请求，参数必须包括用户名username，接口返回用户不包括密码的完整数据库表json字段对象，
//                       会检查当前的回话，判断用户信息，接口用于获取用户的个人信息、权限等
//      /checkUserExit   用户是否存在的检查，接收POST请求，参数必须包括要检查的用户名username，接口返回查询结果，是否存在.
//                       该接口不需要鉴定权限
//      /queryAllUserInfo请求所有的用户的信息，接收POST请求
// ---------------------------------------------------------------------------------------------------------------------



@RestController
public class userControl {

    @Autowired
    private UserService userService;

    // 函数用途：用户信息接口，接收POST请求，参数必须包括用户名username，接口返回用户不包括密码的完整数据库表json字段对象，
    //         会检查当前的回话，判断用户信息，接口用于获取用户的个人信息、权限等
    // 使用场景：购物下单时候，读取![当前]!用户信息,前台展示
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


    // 函数用途：用户是否存在的检查，接收POST请求，参数必须包括要检查的用户名username，接口返回查询结果，是否存在.
    //         该接口不需要鉴定权限
    // 使用场景：注册页面，用户输入用户名的时候
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

    //请求所有的用户信息列表 此功能仅限管理员使用
    @RequestMapping("/user/queryAllUserInfo")
    public List<User> queryAllUserInfo(){
        JSONObject auth = SessionUtil.getAuth();
        // 检查是全局管理员
        if(auth != null && Objects.equals(auth.get(constant.PRIVILEGE),0)){
            List<User> resultData = userService.getAllUser();

            // 移除密码等敏感信息
            for(int i=0;i<resultData.size(); i++){
                resultData.get(i).setPassword("");
            }

            return userService.getAllUser();
        }
        else
            return null;
    }

    //设置用户是否可以登录，此功能仅限管理员使用
    //    /user/setUserLoginPermit
    @RequestMapping("/user/setUserLoginPermit")
    public Msg setUserLoginPermit(@RequestBody Map<String, String> params){
    //    前端来的数据格式，解析参考
    //    let obj = {
    //      setUsername : setUser,
    //      loginPermitState:loginPermitState
    //    };
        JSONObject auth = SessionUtil.getAuth();
        // 检查是全局管理员，才允许设置
        if(auth != null && Objects.equals(auth.get(constant.PRIVILEGE),0)){

            String setObjUsername = params.get(constant.SET_OBJ_USERNAME);
            String setObjPermitState = params.get(constant.SET_OBJ_PERMITSTATE);
            int setObjPermitStateVal = Integer.parseInt(setObjPermitState);
            System.out.println(setObjUsername);
            System.out.println(setObjPermitState);
            System.out.println(setObjPermitStateVal);
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