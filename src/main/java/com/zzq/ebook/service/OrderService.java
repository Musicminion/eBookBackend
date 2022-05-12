package com.zzq.ebook.service;

import com.zzq.ebook.entity.OrderItem;

public interface OrderService {
    public OrderItem addOneOrderItemToChart(String username, int bookID,int buynum);
}
