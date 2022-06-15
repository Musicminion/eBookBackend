package com.zzq.ebook.daoImp;


import com.zzq.ebook.dao.OrderDao;
import com.zzq.ebook.entity.Order;
import com.zzq.ebook.repository.BookRepository;
import com.zzq.ebook.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDaoImp implements OrderDao {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order saveOneOrder(Order oneOrder){
        return orderRepository.save(oneOrder);
    }


    @Override
    public Order getOrderByID(int ID){
        return orderRepository.getOne(ID);
    }
}
