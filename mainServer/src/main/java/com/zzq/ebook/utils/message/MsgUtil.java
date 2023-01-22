package com.zzq.ebook.utils.message;

import net.sf.json.JSONObject;

/**
 * @ClassName Msg
 * @Description TODO
 * @Author thunderBoy
 * @Date 2019/11/7 14:32
 */
public class MsgUtil {

    public static final int SUCCESS = 0;
    public static final int ERROR = -1;
    public static final int LOGIN_USER_ERROR = -100;
    public static final int NOT_LOGGED_IN_ERROR = -101;
    public static final String SUCCESS_MSG = "成功！";
    public static final String ERROR_MSG = "错误！";
    public static final String LOGIN_SUCCESS_MSG = "登录成功！";
    public static final String LOGIN_FAIL_ACCOUNT_FORBIDDEN = "账户被禁用，请联系管理员";
    public static final String LOGOUT_SUCCESS_MSG = "退出登录成功！";
    public static final String LOGOUT_ERR_MSG = "退出登录异常！";
    public static final String LOGIN_USER_ERROR_MSG = "用户名或密码错误，请重新输入！";
    public static final String NOT_LOGGED_IN_ERROR_MSG = "登录失效，请重新登录！";

    public static final String ADD_TO_SHOPCART_SUCCESS = "添加到购物车成功！";
    public static final String ADD_TOO_MUCH_TO_SHOPCART = "请求加入的购物车数量过多，超出容量";
    public static final String EDIT_SHOPCART_FAIL = "编辑购物车信息失败";
    public static final String EDIT_SHOPCART_SUCCESS = "编辑购物车信息成功";
    public static final String QUERY_BookID_NOT_EXIST = "本书不存在，请选择其他图书";
    public static final String USERNAME_REPETE = "用户名重复，请重新输入用户名";
    public static final String SET_LOGIN_PERMISSION_SUCCESS = "设置用户登录许可成功！";
    public static final String SET_LOGIN_PERMISSION_FAIL = "设置用户登录许可失败！";

    public static final String REGISTER_FAIL_NOT_CONSISTENT_PWD = "密码两次输入的不一致";
    public static final String REGISTER_FAIL_USERNAME_EXIT = "用户名已经存在";
    public static final String REGISTER_FAIL_OTHER = "其他某种原因注册出错";

    public static Msg makeMsg(MsgCode code, JSONObject data){
        return new Msg(code, data);
    }

    public static Msg makeMsg(MsgCode code, String msg, JSONObject data){
        return new Msg(code, msg, data);
    }

    public static Msg makeMsg(MsgCode code){
        return new Msg(code);
    }

    public static Msg makeMsg(MsgCode code, String msg){
        return new Msg(code, msg);
    }

    public static Msg makeMsg(int status, String msg, JSONObject data){
        return new Msg(status, msg, data);
    }

    public static Msg makeMsg(int status, String msg){
        return new Msg(status, msg);
    }
}
