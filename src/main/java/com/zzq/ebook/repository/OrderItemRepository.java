package com.zzq.ebook.repository;

import com.zzq.ebook.entity.OrderItem;
import com.zzq.ebook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {

//    @Query(value = "from OrderItem where status =?1 and belonguser = :belonguser and bookID = :bookID")
//    User checkIfAddBefore(@Param("belonguser") String belonguser, @Param("bookID") int bookID);

    // 已知：用户
    @Query(value = "from OrderItem where belonguser = :belonguser and status =0")
    List<OrderItem> findUserShopCartItem(@Param("belonguser") String belonguser);

    @Query(value = "from OrderItem where belonguser = :belonguser and bookID = :bookID and status =0")
    OrderItem findUserShopCartItemOfBook(@Param("belonguser") String belonguser,@Param("bookID") int bookID);

    List<OrderItem> findOrderItemsByBelonguser(String username);


//    List<OrderItem> findOrderItemsByCreate_ItemtimeBetween();
}
