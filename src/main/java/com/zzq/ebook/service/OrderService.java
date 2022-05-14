package com.zzq.ebook.service;

import com.zzq.ebook.entity.OrderItem;

import java.util.List;

public interface OrderService {
    public OrderItem addOneOrderItemToChart(String username, int bookID,int buynum);

    public List<OrderItem> findAllOrderItemInCart(String username);



}
