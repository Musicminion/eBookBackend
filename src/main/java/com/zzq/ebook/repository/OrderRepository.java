package com.zzq.ebook.repository;

import com.zzq.ebook.entity.Order;
import com.zzq.ebook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {

    List<Order> findOrdersByBelonguser(String username);

}
