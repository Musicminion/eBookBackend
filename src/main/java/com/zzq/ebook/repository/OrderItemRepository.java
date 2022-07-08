package com.zzq.ebook.repository;

import com.zzq.ebook.entity.OrderItem;
import com.zzq.ebook.entity.User;
import net.sf.json.JSONArray;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
    OrderItem findOrderItemByBookID(int id);

    // 已知：用户
    @Query(value = "from OrderItem where belonguser = :belonguser and status =0")
    List<OrderItem> findUserShopCartItem(@Param("belonguser") String belonguser);

    @Query(value = "from OrderItem where belonguser = :belonguser and bookID = :bookID and status =0")
    OrderItem findUserShopCartItemOfBook(@Param("belonguser") String belonguser,@Param("bookID") int bookID);

    List<OrderItem> findOrderItemsByBelonguser(String username);

//
    @Query(value = "select belonguser,sum(payprice) from OrderItem " +
            "where status =2 and create_Itemtime >= :starttime and create_Itemtime <= :endtime " +
            "group by belonguser")
    JSONArray userConsumeStatistic(@Param(value = "starttime") Date starttime, @Param(value = "endtime") Date endtime);

    @Query(value = "select bookID,sum(buynum) from OrderItem " +
            "where status =2 and create_Itemtime >= :starttime and create_Itemtime <= :endtime " +
            "group by bookID")
    JSONArray bookSellnumStatistic(@Param(value = "starttime") Date starttime, @Param(value = "endtime") Date endtime);


    @Query(value = "select bookID,sum(buynum) from OrderItem " +
            "where belonguser = :username and status =2 and create_Itemtime >= :starttime and create_Itemtime <= :endtime " +
            "group by bookID")
    JSONArray userSelfStatistic_BookWithBuyNum
            (@Param(value = "starttime") Date starttime,
             @Param(value = "endtime") Date endtime,
             @Param("username") String username);

    @Query(value = "select sum(buynum) from OrderItem " +
            "where belonguser = :username and status =2 and create_Itemtime >= :starttime and create_Itemtime <= :endtime " +
            "group by belonguser")
    JSONArray userSelfStatistic_BookAllBuyNum
            (@Param(value = "starttime") Date starttime,
             @Param(value = "endtime") Date endtime,
             @Param("username") String username);


    @Query(value = "select sum(payprice) from OrderItem " +
            "where belonguser = :username and status =2 and create_Itemtime >= :starttime and create_Itemtime <= :endtime " +
            "group by belonguser")
    JSONArray userSelfStatistic_BookTotalPay
            (@Param(value = "starttime") Date starttime,
             @Param(value = "endtime") Date endtime,
             @Param("username") String username);


//    @Query(value = "select * from OrderItem where create_Itemtime &gt;= ?1",nativeQuery = true)
//    JSONArray tset(String test);
//    List<OrderItem> findOrderItemsByCreate_ItemtimeBetween();
}
