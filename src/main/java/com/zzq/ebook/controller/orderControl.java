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


    // 函数用途：用户添加商品到购物车，接收参数：用户名,书的ID，买的数量
    // 权限要求：登录用户
    @RequestMapping("/order/addToChart")
    public Msg addToChart(@RequestBody Map<String, String> params){
        String username = params.get(constant.USERNAME);

        // 拒绝非法的用户添加购物车到他人用户
        JSONObject auth = SessionUtil.getAuth();
        assert auth != null;
        if(!Objects.equals((String) auth.get(constant.USERNAME), username)){
            return MsgUtil.makeMsg(MsgCode.ERROR, MsgUtil.NOT_LOGGED_IN_ERROR_MSG);
        }

        // 解析参数
        String bookID = params.get(constant.BOOKID);
        String buynumStr = params.get(constant.SINGLE_ITEM_BUYNUM);
        int IDnum = Integer.parseInt(bookID);
        int buynum = Integer.parseInt(buynumStr);

        // 交给服务层
        OrderItem resultItem = orderService.addOneOrderItemToChart(username,IDnum,buynum);

        // 检查服务层的结果情况如何，正常就返回处理正常的结果，反之报错
        if(resultItem == null)
            return MsgUtil.makeMsg(MsgCode.ERROR, MsgUtil.ADD_TOO_MUCH_TO_SHOPCART);

        return MsgUtil.makeMsg(MsgCode.SUCCESS,MsgUtil.ADD_TO_SHOPCART_SUCCESS);
    }


    // 函数用途：用户修改购物车商品的数量，接收参数：用户名,书的ID，买的数量
    // 权限要求：登录用户
    // 补充注释：如果用户删除购物车数据，那么我这里的操作是吧orderItem的状态保留为-1，原因是哪怕删除的数据，对于用户的
    //         喜好分析是有一定的意义的，所以暂且保留了，此外，直接删除会导致订单项目的流水号不连续，这种“重要的数据”我认为需要保留
    //         当然，能不能直接删除，当然是可以的，比较没有指向这个orderItem的引用，所以不会带来删除异常
    @RequestMapping("/order/refreshShopCartItem")
    public Msg refreshShopCartItem(@RequestBody Map<String, String> params){
        String username = params.get(constant.USERNAME);
        // 拒绝非法的用户添加购物车到他人用户
        JSONObject auth = SessionUtil.getAuth();
        assert auth != null;
        if(!Objects.equals((String) auth.get(constant.USERNAME), username)){
            return MsgUtil.makeMsg(MsgCode.ERROR, MsgUtil.NOT_LOGGED_IN_ERROR_MSG);
        }
        // 解析参数
        String bookID = params.get(constant.BOOKID);
        String buynumStr = params.get(constant.REFRESHED_BUY_NUM);
        int IDnum = Integer.parseInt(bookID);
        int buynum = Integer.parseInt(buynumStr);

        // 检查服务层的处理结果
        int result = orderService.editOneOrderItemBUYNUMInChart(username,IDnum,buynum);
        if(result == 0)
            return MsgUtil.makeMsg(MsgCode.SUCCESS,MsgUtil.EDIT_SHOPCART_SUCCESS);

        if(result <= -1)
            return MsgUtil.makeMsg(MsgCode.ERROR,MsgUtil.EDIT_SHOPCART_FAIL);
        return null;
    }

    // 函数功能：请求用户自己的购物车信息
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

    // 函数功能：下单！
    @RequestMapping("/order/makeorder")
    public Msg orderMakeFromShopCart(@RequestBody Map<String, String> params) throws Exception {
        int itemNum = (params.size() - 6) / 2 ;
        if(itemNum <= 0)
            return null;
        // 解析前端的请求数据，6个基本信息+后面的订单信息
        String orderFrom = params.get("orderFrom");
        String username = params.get(constant.USERNAME);
        String receivename = params.get("receivename");
        String postcode = params.get("postcode");
        String phonenumber = params.get("phonenumber");
        String receiveaddress = params.get("receiveaddress");
        int[] bookIDGroup = new int[itemNum];
        int[] bookNumGroup = new int[itemNum];

        for(int i=1; i<=itemNum; i++){
            bookIDGroup[i-1] = Integer.parseInt(params.get("bookIDGroup" + i));
            bookNumGroup[i-1] = Integer.parseInt(params.get("bookNumGroup" + i));
        }

        // 根据购买的来源，把数组交给服务层业务函数
        int result = -1;
        if(Objects.equals(orderFrom, "ShopCart")) {
            result = orderService.orderMakeFromShopCart(bookIDGroup,bookNumGroup,username,receivename,
                    postcode, phonenumber, receiveaddress,itemNum);
        }
        else if(Objects.equals(orderFrom, "DirectBuy")){
            result = orderService.orderMakeFromDirectBuy(bookIDGroup,bookNumGroup,username,receivename,
                    postcode, phonenumber, receiveaddress,itemNum);
        }

        // 根据结果返回
        if(result == 0)
            return MsgUtil.makeMsg(MsgCode.SUCCESS,MsgUtil.SUCCESS_MSG);
        else
            return MsgUtil.makeMsg(MsgCode.ERROR,MsgUtil.ERROR_MSG);
    }

    // 函数用途：管理员获取所有的订单项目的数据
    // 权限要求：管理员
    @RequestMapping("/order/getAllOrderItem")
    public JSONArray getAllOrderItem(){
        JSONObject auth = SessionUtil.getAuth();
        if(auth == null || !Objects.equals(auth.get(constant.PRIVILEGE),0))
            return null;

        return orderService.getAllOrderItemWithBook();
    }

    // 函数用途：管理员获取所有的订单数据
    // 权限要求：管理员
    @RequestMapping("/order/getAllOrder")
    public JSONArray getAllOrder(){
        JSONObject auth = SessionUtil.getAuth();
        if(auth == null || !Objects.equals(auth.get(constant.PRIVILEGE),0))
            return null;
        return orderService.getAllOrder();
    }

    // 函数用途：用户个人的订单，POST
    // 权限要求：登录用户
    @RequestMapping("/order/getUserOrderItem")
    public JSONArray getUserOrderItem(){
        JSONObject auth = SessionUtil.getAuth();
        assert auth != null;
        String username = (String) auth.get(constant.USERNAME);

        return orderService.getUserOrderItemWithBook(username);
    }

    // 函数用途：用户获取个人的订单项目，POST
    @RequestMapping("/order/getUserOrder")
    public JSONArray getUserOrder(){
        JSONObject auth = SessionUtil.getAuth();
        assert auth != null;
        String username = (String) auth.get(constant.USERNAME);

        return orderService.getUserOrder(username);
    }

    //  测试的接口函数
    @RequestMapping("/test")
    public JSONArray testFunction(){
        System.out.println("测试开始！！！！！！！！！！");

        return orderService.getAllOrder();
    }
}
