package com.zzq.ebook.constant;

// 所有的通用字段在这里统一写明
public class constant {

    // 用户相关 和数据库的字段名字统一 这里对应users表格
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String PRIVILEGE = "privilege";
    public static final String EMAIL = "email";
    public static final String USERADDRESS = "useraddress";
    public static final String FORBIDLOGIN = "forbidlogin";

    // 状态 0-在购物车中 1-归属于订单，但是未支付 2-归属于订单，且已经支付，3-归属于订单，且已经完成的，-1表示这个字段无效（已经撤销的订单）
    public static final int IN_SHOPPING_CHART = 0;
    public static final int BELONG_NOT_PAYID_ORDER = 1;
    public static final int BELONG_PAYID_ORDER = 2;
    public static final int BELONG_FINISHED_ORDER = 3;
    public static final int INVALID_ITEM = -1;


    // 下面的项目都是item表格里面的字段
    public static final String BOOKID = "bookID";
    public static final String SINGLE_ITEM_BUYNUM = "buynum";


    public static final String REFRESHED_BUY_NUM = "refreshedbuynum";
    public static final String ORDER_ITEMID = "itemID";

    // 用户操作的相关
    public static final String SET_OBJ_USERNAME = "setUsername";
    public static final String SET_OBJ_PERMITSTATE = "loginPermitState";
    //    let obj = {
//      setUsername : setUser,
//      loginPermitState:loginPermitState
//    };
}
