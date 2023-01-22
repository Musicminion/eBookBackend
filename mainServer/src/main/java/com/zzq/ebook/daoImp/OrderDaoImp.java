package com.zzq.ebook.daoImp;


import com.zzq.ebook.dao.OrderDao;
import com.zzq.ebook.entity.Order;
import com.zzq.ebook.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class OrderDaoImp implements OrderDao {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
    public Order createOneOrderWithMultipleOrderItems(String belonguser, Timestamp create_time, String destination, String postalcode,
                                                      String contactphone, String receivername){
        Order newOrder = new Order(belonguser,create_time,destination,postalcode,
                contactphone,receivername);
        return orderRepository.save(newOrder);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
    public Order createOneOrderWithOneOrderItem(String belonguser, Timestamp create_time, String destination, String postalcode,
                                                String contactphone, String receivername, int payprice){
        Order newOrder =  new Order(belonguser,create_time,destination,postalcode,
                contactphone,receivername,payprice);
        return orderRepository.save(newOrder);
    }


    @Override
    public Order saveOneOrder(Order oneOrder){
        return orderRepository.save(oneOrder);
    }

    @Override
    public Order getOrderByID(int ID){
        return orderRepository.getOne(ID);
    }

    @Override
    public List<Order> getAllOrder(){
        return orderRepository.findAll();
    }


    @Override
    public List<Order> getUserOrder(String username){
        return orderRepository.findOrdersByBelonguser(username);
    }



    @Override
    public Order setOrderPayPrice(int OrderID, int PayPrice){
        Order order = orderRepository.getOne(OrderID);
        order.setTotalprice(PayPrice);
        return orderRepository.save(order);
    }
}
