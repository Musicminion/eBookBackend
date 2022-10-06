package com.zzq.ebook.dao;

import com.zzq.ebook.entity.OrderItem;
import net.sf.json.JSONArray;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface OrderItemDao {
    OrderItem addOneOrderItem(OrderItem newOrder);

    OrderItem createOrderItem(int status, String belongUser, int orderID,
                              int bookID, int BuyNum, int payPrice, Timestamp createTime);


    List<OrderItem> queryOneUserShopCart(String username);

    OrderItem checkUserOrderItemByID(String username, int bookID);
    OrderItem setOrderItemStatusByUsernameAndBookID(String username, int bookID, int status, int OrderID);

    OrderItem saveOneOrderItem(OrderItem saveObj);

    List<OrderItem> saveAllOrderItems(List<OrderItem> orderItems);

    List<OrderItem> getAllOrderItem();

    List<OrderItem> getUserOrderItem(String username);

    OrderItem getOrderItemByBookID(int id);

    JSONArray userConsumeStatistic(Date starttime, Date endtime);


    JSONArray bookSellnumStatistic(Date starttime, Date endtime);


    JSONArray userSelfStatistic_BookWithBuyNum(Date starttime, Date endtime, String username);

    JSONArray userSelfStatistic_BookAllBuyNum(Date starttime, Date endtime, String username);

    JSONArray userSelfStatistic_BookTotalPay(Date starttime, Date endtime, String username);


}
