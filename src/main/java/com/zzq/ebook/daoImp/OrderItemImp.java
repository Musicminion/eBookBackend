package com.zzq.ebook.daoImp;

import com.zzq.ebook.constant.constant;
import com.zzq.ebook.dao.OrderItemDao;
import com.zzq.ebook.entity.OrderItem;
import com.zzq.ebook.entity.User;
import com.zzq.ebook.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Repository
public class OrderItemImp implements OrderItemDao {

    @Autowired
    private OrderItemRepository orderItemRepository;

    public OrderItem addOneOrderItem(OrderItem newOrder){

        return orderItemRepository.save(newOrder);
    }

    public List<OrderItem> queryOneUserShopCart(String username){
        return orderItemRepository.findUserShopCartItem(username);
    }

    // 根据用户名 书籍的ID号，来获取一个OrderItem实例
    public OrderItem checkUserOrderItemByID(String username, int bookID){
//        return null;
        return orderItemRepository.findUserShopCartItemOfBook(username,bookID);
    }

    public OrderItem saveOneOrderItem(OrderItem saveObj){
        return orderItemRepository.save(saveObj);
    }

    @Override
    public List<OrderItem> getAllOrderItem(){
        return orderItemRepository.findAll();
    }
}
