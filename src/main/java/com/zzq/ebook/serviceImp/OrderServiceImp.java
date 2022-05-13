package com.zzq.ebook.serviceImp;

import com.zzq.ebook.constant.constant;
import com.zzq.ebook.dao.OrderItemDao;
import com.zzq.ebook.entity.OrderItem;
import com.zzq.ebook.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderItemDao orderItemDao;

    public OrderItem addOneOrderItemToChart(String username, int bookID, int buynum){
        // 实例化操作
        OrderItem newItem = new OrderItem();
        newItem.setStatus(constant.IN_SHOPPING_CHART);
        newItem.setBelonguser(username);
        newItem.setBookID(bookID);
        newItem.setBuynum(buynum);
        // 获取时间戳操作！
        Timestamp timenow = new Timestamp(System.currentTimeMillis());
        newItem.setCreate_Itemtime(timenow);
        // 数据库操作！
        orderItemDao.addOneOrderItem(newItem);
        return newItem;
    }

    public List<OrderItem> findAllOrderItemInChart(String username){
        return orderItemDao.queryOneUserShopCart(username);
    }
}
