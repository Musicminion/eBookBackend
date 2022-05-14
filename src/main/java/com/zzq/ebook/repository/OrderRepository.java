package com.zzq.ebook.repository;

import com.zzq.ebook.entity.Order;
import com.zzq.ebook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,String> {


}
