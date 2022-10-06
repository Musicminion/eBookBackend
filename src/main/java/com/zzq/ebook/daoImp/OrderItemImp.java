package com.zzq.ebook.daoImp;

import com.zzq.ebook.constant.constant;
import com.zzq.ebook.dao.OrderItemDao;
import com.zzq.ebook.entity.OrderItem;
import com.zzq.ebook.entity.User;
import com.zzq.ebook.repository.OrderItemRepository;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Repository
public class OrderItemImp implements OrderItemDao {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
    public OrderItem addOneOrderItem(OrderItem newOrder){
        return orderItemRepository.save(newOrder);
    }

    @Override
    public OrderItem createOrderItem(int status, String belongUser, int orderID,
                              int bookID, int buyNum, int payPrice, Timestamp createTime){

        OrderItem orderItem = new OrderItem();
        orderItem.setStatus(status);
        orderItem.setBelonguser(belongUser);
        orderItem.setOrderID(orderID);
        orderItem.setBookID(bookID);
        orderItem.setBuynum(buyNum);
        orderItem.setPayprice(payPrice);
        orderItem.setCreate_Itemtime(createTime);

        return orderItemRepository.save(orderItem);
    }



    @Override
    public List<OrderItem> queryOneUserShopCart(String username){
        return orderItemRepository.findUserShopCartItem(username);
    }

    // 根据用户名 书籍的ID号，来获取一个OrderItem实例
    @Override
    public OrderItem checkUserOrderItemByID(String username, int bookID){
//        return null;
        return orderItemRepository.findUserShopCartItemOfBook(username,bookID);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
    public OrderItem setOrderItemStatusByUsernameAndBookID(String username, int bookID, int status, int OrderID){
        OrderItem orderItem = orderItemRepository.findUserShopCartItemOfBook(username,bookID);
        orderItem.setStatus(status);
        orderItem.setOrderID(OrderID);
        return orderItemRepository.save(orderItem);
    }


    @Override
    public OrderItem saveOneOrderItem(OrderItem saveObj){
        return orderItemRepository.save(saveObj);
    }

    @Override
    public List<OrderItem> saveAllOrderItems(List<OrderItem> orderItems){
        return orderItemRepository.saveAll(orderItems);
    }


    @Override
    public List<OrderItem> getAllOrderItem(){
        return orderItemRepository.findAll();
    }

    @Override
    public List<OrderItem> getUserOrderItem(String username){
        return orderItemRepository.findOrderItemsByBelonguser(username);
    }
    @Override
    public OrderItem getOrderItemByBookID(int id){
        return orderItemRepository.findOrderItemByBookID(id);
    }


    @Override
    public JSONArray userConsumeStatistic(Date starttime, Date endtime){
        return orderItemRepository.userConsumeStatistic(starttime,endtime);
    }

    @Override
    public JSONArray bookSellnumStatistic(Date starttime, Date endtime){
        return orderItemRepository.bookSellnumStatistic(starttime,endtime);
    }

    @Override
    public JSONArray userSelfStatistic_BookWithBuyNum(Date starttime, Date endtime, String username){
        return orderItemRepository.userSelfStatistic_BookWithBuyNum(starttime,endtime,username);
    };
    @Override
    public JSONArray userSelfStatistic_BookAllBuyNum(Date starttime, Date endtime, String username){
        return orderItemRepository.userSelfStatistic_BookAllBuyNum(starttime,endtime,username);
    };

    @Override
    public JSONArray userSelfStatistic_BookTotalPay(Date starttime, Date endtime, String username){
        return orderItemRepository.userSelfStatistic_BookTotalPay(starttime,endtime,username);
    };

}
