package com.zzq.ebook.repository;

import com.zzq.ebook.entity.Order;
import com.zzq.ebook.entity.User;
import net.sf.json.JSONArray;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {

    List<Order> findOrdersByBelonguser(String username);


//    List<Order> findOrdersByCreate_time(Date starttime);
}
