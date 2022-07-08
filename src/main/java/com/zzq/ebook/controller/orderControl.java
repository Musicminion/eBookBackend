package com.zzq.ebook.controller;

import com.zzq.ebook.constant.constant;
import com.zzq.ebook.entity.Order;
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

    //   /order/addToChart
    //   接收参数表
    //        username: 用户名,
    //        bookID: 添加到购物车中，书的ID
    //        buynum: 买的数量
    @RequestMapping("/order/addToChart")
    public Msg addToChart(@RequestBody Map<String, String> params){
        String username = params.get(constant.USERNAME);

        // 拒绝非法的用户添加购物车到他人用户
        JSONObject auth = SessionUtil.getAuth();
        assert auth != null;
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

    // 功能：请求用户自己的购物车信息
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


    @RequestMapping("/order/makeorder")
    public Msg orderMakeFromShopCart(@RequestBody Map<String, String> params) throws Exception {
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

        int result = -1;
        if(Objects.equals(orderFrom, "ShopCart")) {
            result = orderService.orderMakeFromShopCart(bookIDGroup,bookNumGroup,username,receivename,
                    postcode, phonenumber, receiveaddress,itemNum);

        }
        else if(Objects.equals(orderFrom, "DirectBuy")){
            result = orderService.orderMakeFromDirectBuy(bookIDGroup,bookNumGroup,username,receivename,
                    postcode, phonenumber, receiveaddress,itemNum);
        }

        if(result == 0)
            return MsgUtil.makeMsg(MsgCode.SUCCESS,MsgUtil.SUCCESS_MSG);
        else
            return MsgUtil.makeMsg(MsgCode.ERROR,MsgUtil.ERROR_MSG);
    }


    @RequestMapping("/order/getAllOrderItem")
    public JSONArray getAllOrderItem(){

        return orderService.getAllOrderItemWithBook();
    }

    @RequestMapping("/order/getAllOrder")
    public JSONArray getAllOrder(){

        return orderService.getAllOrder();
    }

    //用户个人的订单
    @RequestMapping("/order/getUserOrderItem")
    public JSONArray getUserOrderItem(){
        JSONObject auth = SessionUtil.getAuth();
        assert auth != null;
        String username = (String) auth.get(constant.USERNAME);

        return orderService.getUserOrderItemWithBook(username);
    }

    //用户个人的订单项目
    @RequestMapping("/order/getUserOrder")
    public JSONArray getUserOrder(){
        JSONObject auth = SessionUtil.getAuth();
        assert auth != null;
        String username = (String) auth.get(constant.USERNAME);

        //        System.out.println("请求获得所有的订单");
//        return orderService
        return orderService.getUserOrder(username);
    }




//  测试的接口
    @RequestMapping("/test")
    public JSONArray testFunction(){
        System.out.println("测试开始！！！！！！！！！！");

        return orderService.getAllOrder();
    }
}
