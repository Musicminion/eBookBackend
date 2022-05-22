package com.zzq.ebook.controller;

import com.zzq.ebook.constant.constant;
import com.zzq.ebook.entity.OrderItem;
import com.zzq.ebook.service.OrderService;
import com.zzq.ebook.utils.message.Msg;
import com.zzq.ebook.utils.message.MsgCode;
import com.zzq.ebook.utils.message.MsgUtil;
import com.zzq.ebook.utils.session.SessionUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class orderControl {

    @Autowired
    private OrderService orderService;
    @RequestMapping("/order/addToChart")
    public Msg addToChart(@RequestBody Map<String, String> params){
        String username = params.get(constant.USERNAME);

        // 拒绝非法的用户添加购物车到他人用户
        JSONObject auth = SessionUtil.getAuth();
        if(!Objects.equals((String) auth.get(constant.USERNAME), username)){
            return MsgUtil.makeMsg(MsgCode.ERROR, MsgUtil.NOT_LOGGED_IN_ERROR_MSG);
        }

        String bookID = params.get(constant.BOOKID);
        String buynumStr = params.get(constant.SINGLE_ITEM_BUYNUM);

        int IDnum = Integer.parseInt(bookID);
        int buynum = Integer.parseInt(buynumStr);

        OrderItem resultItem = orderService.addOneOrderItemToChart(username,IDnum,buynum);

        if(resultItem == null)
            return MsgUtil.makeMsg(MsgCode.ERROR, MsgUtil.ADD_TOO_MUCH_TO_SHOPCART);

        return MsgUtil.makeMsg(MsgCode.SUCCESS,MsgUtil.ADD_TO_SHOPCART_SUCCESS);
    }

    //        接收参数表
    //        username: user,
    //        itemID: orderID,
    //        refreshedbuynum: newbuynum,
    @RequestMapping("/order/refreshShopCartItem")
    public Msg refreshShopCartItem(@RequestBody Map<String, String> params){
        String username = params.get(constant.USERNAME);

        // 拒绝非法的用户添加购物车到他人用户
        JSONObject auth = SessionUtil.getAuth();
        if(!Objects.equals((String) auth.get(constant.USERNAME), username)){
            return MsgUtil.makeMsg(MsgCode.ERROR, MsgUtil.NOT_LOGGED_IN_ERROR_MSG);
        }

        String bookID = params.get(constant.BOOKID);
        String buynumStr = params.get(constant.REFRESHED_BUY_NUM);
        int IDnum = Integer.parseInt(bookID);
        int buynum = Integer.parseInt(buynumStr);


        int result = orderService.editOneOrderItemBUYNUMInChart(username,IDnum,buynum);

        if(result == 0)
            return MsgUtil.makeMsg(MsgCode.SUCCESS,MsgUtil.EDIT_SHOPCART_SUCCESS);

        if(result <= -1)
            return MsgUtil.makeMsg(MsgCode.ERROR,MsgUtil.EDIT_SHOPCART_FAIL);
        return null;
    }


    @RequestMapping("/order/queryMyChart")
    public List<OrderItem> queryChart(@RequestBody Map<String, String> params){
        String queryUser = params.get(constant.USERNAME);
        JSONObject auth = SessionUtil.getAuth();
        // 拒绝非法的用户获取用户的购物车信息
        if(auth == null || (!Objects.equals((String) auth.get(constant.USERNAME), queryUser))){
            return null;
        }

        return orderService.findAllOrderItemInCart(queryUser);
    }

//        orderFrom : "ShopCart",
//        username: user,
//        receivename: orderInfo.receivename,
//        postcode:orderInfo.postcode,
//        phonenumber:orderInfo.phonenumber,
//        receiveaddress:orderInfo.receiveaddress,
    @RequestMapping("/order/makeorder/shopcart")
    public Msg orderMakeFromShopCart(@RequestBody Map<String, String> params){

        int itemNum = (params.size() - 6) / 2 ;

        if(itemNum <= 0)
            return null;

        String orderFrom = params.get("orderFrom");
        String username = params.get(constant.USERNAME);
        String receivename = params.get("receivename");
        String postcode = params.get("postcode");
        String phonenumber = params.get("phonenumber");
        String receiveaddress = params.get("receiveaddress");

        int[] bookIDGroup = new int[itemNum];
        int[] bookNumGroup = new int[itemNum];

        for(int i=1; i<=itemNum; i++){
            String bookIDGroupNum = "bookIDGroup" + i;
            String bookNumGroupNum = "bookNumGroup" + i;
            String val1str = params.get(bookIDGroupNum);
            String val2str = params.get(bookNumGroupNum);

            int val1 = Integer.parseInt(val1str);
            int val2 = Integer.parseInt(val2str);

            bookIDGroup[i-1] = val1;
            bookNumGroup[i-1] = val2;
        }

        int result = orderService.orderMakeFromShopCart(bookIDGroup,bookNumGroup,username,receivename,
                postcode, phonenumber, receiveaddress,itemNum);

        return MsgUtil.makeMsg(MsgCode.SUCCESS,MsgUtil.EDIT_SHOPCART_SUCCESS);
    }


    @RequestMapping("/order/buyOneImmediately")
    public Msg buyOneImmediately(@RequestBody Map<String, String> params){
        // 首先，获取必要参数，用户名、收件人、联系电话、邮政编码、收件地址、购买书本的ID、购买书籍的数量、购买时的价格

        return null;
    }

}
