package com.zzq.ebook.dao;

import com.zzq.ebook.entity.Order;

import java.sql.Timestamp;
import java.util.List;

public interface OrderDao {
    Order createOneOrderWithMultipleOrderItems(String belonguser, Timestamp create_time, String destination, String postalcode,
                                               String contactphone, String receivername);


    Order saveOneOrder(Order oneOrder);

    public Order setOrderPayPrice(int OrderID, int PayPrice);

    Order getOrderByID(int ID);

    List<Order> getAllOrder();

    List<Order> getUserOrder(String username);

    Order createOneOrderWithOneOrderItem(String username, Timestamp timestamp, String receiveAddress, String postcode,
                                         String phoneNumber, String receiveName, int PayPrice);
}
