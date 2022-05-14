package com.zzq.ebook.serviceImp;

import com.zzq.ebook.constant.constant;
import com.zzq.ebook.dao.BookDao;
import com.zzq.ebook.dao.OrderDao;
import com.zzq.ebook.dao.OrderItemDao;
import com.zzq.ebook.entity.Book;
import com.zzq.ebook.entity.Order;
import com.zzq.ebook.entity.OrderItem;
import com.zzq.ebook.repository.BookRepository;
import com.zzq.ebook.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private BookRepository bookRepository;

    public OrderItem addOneOrderItemToChart(String username, int bookID, int buynum){

        // 获取书的信息
        Book book = bookDao.getOneBookByID(bookID);
        double bookPrice = book.getPrice();
        double itemAllPrice = bookPrice * buynum;
        if(buynum > book.getInventory())
            return null;

        OrderItem tmp = orderItemDao.checkUserOrderItemByID(username,bookID);
        if(tmp !=null){
            int originNum = tmp.getBuynum();

            // 如果用户加入到购物车的数量过多，甚至超过了库存，会直接拒绝
            if(originNum + buynum > book.getInventory())
                return null;
            tmp.setBuynum(originNum + buynum);
            orderItemDao.saveOneOrderItem(tmp);
            return tmp;
        }

        // 实例化操作
        OrderItem newItem = new OrderItem();
        newItem.setStatus(constant.IN_SHOPPING_CHART);
        newItem.setBelonguser(username);
        newItem.setOrderID(0);
        newItem.setBookID(bookID);
        newItem.setBuynum(buynum);
        newItem.setPayprice(itemAllPrice);

        // 获取时间戳操作！
        Timestamp timenow = new Timestamp(System.currentTimeMillis());
        newItem.setCreate_Itemtime(timenow);

        // 数据库操作！保存写入
        orderItemDao.addOneOrderItem(newItem);
        return newItem;
    }

    // 返回-1 实例不存在 返回-2 超过容量限制，返回0 操作正常
    public int editOneOrderItemBUYNUMInChart(String username, int bookID, int refreshedBuynum){

        OrderItem tmpitem = orderItemDao.checkUserOrderItemByID(username,bookID);
        if(tmpitem ==null)
            return -1;

        Book book = bookDao.getOneBookByID(bookID);
        if(refreshedBuynum > book.getInventory())
            return -2;

        tmpitem.setBuynum(refreshedBuynum);
        tmpitem.setPayprice(book.getPrice()*refreshedBuynum);

        orderItemDao.saveOneOrderItem(tmpitem);
        return 0;
    }


    public int orderMakeFromShopCart(int [] bookIDGroup, int [] bookNumGroup, String username,
            String receivename, String postcode, String phonenumber, String receiveaddress, int size){
        Order newOrder = new Order();
        newOrder.setBelonguser(username);
        newOrder.setContactphone(phonenumber);
        newOrder.setDestination(receiveaddress);
        newOrder.setReceivername(receivename);
        newOrder.setPostalcode(postcode);

        Timestamp timenow = new Timestamp(System.currentTimeMillis());
        newOrder.setCreate_time(timenow);

        int orderid = orderDao.saveOneOrder(newOrder).getOrderID();

        System.out.println(orderid);

        for(int i=0; i<size; i++){
            OrderItem oneitem = orderItemDao.checkUserOrderItemByID(username,bookIDGroup[i]);
            if(oneitem != null){
                oneitem.setStatus(2);
                oneitem.setOrderID(orderid);
                orderItemDao.saveOneOrderItem(oneitem);
            }

            Book book = bookDao.getOneBookByID(bookIDGroup[i]);
            int reaminNum = book.getInventory() - bookNumGroup[i];
            book.setInventory(reaminNum);
            int newSellnum = book.getSellnumber() + bookNumGroup[i];
            book.setSellnumber(newSellnum);
            bookDao.savaOneBook(book);
        }
        return 1;
    }

    public List<OrderItem> findAllOrderItemInCart(String username){
        return orderItemDao.queryOneUserShopCart(username);
    }
}
