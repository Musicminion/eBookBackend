package com.zzq.ebook.dao;

import com.zzq.ebook.entity.OrderItem;

public interface OrderItemDao {
    OrderItem addOneOrderItem(OrderItem newOrder);
}
