package com.zzq.ebook.dao;

import com.zzq.ebook.entity.Order;

import java.util.List;

public interface OrderDao {

    Order saveOneOrder(Order oneOrder);

    Order getOrderByID(int ID);

    List<Order> getAllOrder();


    List<Order> getUserOrder(String username);
}
