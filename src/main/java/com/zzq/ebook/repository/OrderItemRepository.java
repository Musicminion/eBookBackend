package com.zzq.ebook.repository;

import com.zzq.ebook.entity.OrderItem;
import com.zzq.ebook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {

    @Query(value = "from OrderItem where status =?1 and belonguser = :belonguser and bookID = :bookID")
    User checkIfAddBefore(@Param("belonguser") String belonguser, @Param("bookID") int bookID);

}
