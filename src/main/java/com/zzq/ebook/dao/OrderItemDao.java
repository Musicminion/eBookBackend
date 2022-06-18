package com.zzq.ebook.dao;

import com.zzq.ebook.entity.OrderItem;
import net.sf.json.JSONArray;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OrderItemDao {
    OrderItem addOneOrderItem(OrderItem newOrder);

    List<OrderItem> queryOneUserShopCart(String username);

    OrderItem checkUserOrderItemByID(String username, int bookID);

    OrderItem saveOneOrderItem(OrderItem saveObj);

    List<OrderItem> getAllOrderItem();

    List<OrderItem> getUserOrderItem(String username);

    JSONArray userConsumeStatistic(Date starttime, Date endtime);


    JSONArray bookSellnumStatistic(Date starttime, Date endtime);


    JSONArray userSelfStatistic_BookWithBuyNum(Date starttime, Date endtime, String username);

    JSONArray userSelfStatistic_BookAllBuyNum(Date starttime, Date endtime, String username);

    JSONArray userSelfStatistic_BookTotalPay(Date starttime, Date endtime, String username);


}
