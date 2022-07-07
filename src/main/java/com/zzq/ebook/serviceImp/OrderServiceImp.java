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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
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
        int bookPrice = book.getPrice();
        int itemAllPrice = bookPrice * buynum;
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
        newItem.setOrderID(2);
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

        if(refreshedBuynum ==0){
            tmpitem.setStatus(-1);
            tmpitem.setOrderID(1);
        }
        orderItemDao.saveOneOrderItem(tmpitem);
        return 0;
    }

    // 下订单 来自购物车的订单
    // 订单业务逻辑，作为一组事务，提交，出现异常可以回滚
    @Transactional(rollbackFor=Exception.class)
    public int orderMakeFromShopCart(int [] bookIDGroup, int [] bookNumGroup, String username,
            String receivename, String postcode, String phonenumber, String receiveaddress, int size) throws Exception {
        // 由于购物车中的是归属于OrderItem，正式成为订单之前，需要创建一个新的 Order
        Order newOrder = new Order();
        newOrder.setBelonguser(username);
        newOrder.setContactphone(phonenumber);
        newOrder.setDestination(receiveaddress);
        newOrder.setReceivername(receivename);
        newOrder.setPostalcode(postcode);
        Timestamp timenow = new Timestamp(System.currentTimeMillis());
        newOrder.setCreate_time(timenow);

        // 获取Order的ID号码
        int orderid = orderDao.saveOneOrder(newOrder).getOrderID();
        int totalMoney = 0;

        List<Book> BufferBooks = new ArrayList<Book>();
        List<OrderItem> BufferOrderItems = new ArrayList<OrderItem>();

        // 金额在后端计算，不依赖前端计算，确保安全
        for(int i=0; i<size; i++) {
            OrderItem oneitem = orderItemDao.checkUserOrderItemByID(username,bookIDGroup[i]);

            // 取出 OrderItem 设置为已经支付的情况 2代表已经支付
            if(oneitem != null){
                oneitem.setStatus(2);
                oneitem.setOrderID(orderid);
                totalMoney =totalMoney + oneitem.getPayprice();

                BufferOrderItems.add(oneitem);
            }

            // 通过前端的传来的购买书籍的ID号码，转化为一个书
            Book book = bookDao.getOneBookByID(bookIDGroup[i]);
            // 购买数量后端再校验一次，保证不会出现库存减法之后变成了负数的情况
            int reaminNum = book.getInventory() - bookNumGroup[i];
            if(reaminNum < 0){
                throw new Exception("库存不够");
            }

            // 设置新的库存结果
            book.setInventory(reaminNum);

            // 销量的更新，根据用户买的数量更新
            int newSellnum = book.getSellnumber() + bookNumGroup[i];
            book.setSellnumber(newSellnum);

            // 放入缓冲区域
            BufferBooks.add(book);
        }

        // 所有的没有异常，开始写入数据！
        if(orderid >=0 ){
            Order justNowOrder = orderDao.getOrderByID(orderid);
            justNowOrder.setTotalprice(totalMoney);
            orderDao.saveOneOrder(justNowOrder);
            bookDao.saveAllBooks(BufferBooks);
            orderItemDao.saveAllOrderItems(BufferOrderItems);
        }
        return 0;
    }


    @Transactional(rollbackFor=Exception.class)
    public int orderMakeFromDirectBuy(int [] bookIDGroup, int [] bookNumGroup, String username,
        String receivename, String postcode, String phonenumber, String receiveaddress, int size) throws Exception {

        // 书籍相关的信息 库存检查 超过库存直接返回错误
        Book targetBook = bookDao.getOneBookByID(bookIDGroup[0]);
        if(targetBook.getInventory() - bookNumGroup[0] <0)
            throw new Exception("库存不够");

        // 和上面是一样的道理 创建一个Order
        Order newOrder = new Order();
        newOrder.setBelonguser(username);
        newOrder.setContactphone(phonenumber);
        newOrder.setDestination(receiveaddress);
        newOrder.setReceivername(receivename);
        newOrder.setPostalcode(postcode);
        Timestamp timenow = new Timestamp(System.currentTimeMillis());
        newOrder.setCreate_time(timenow);

        // 然后获取Order的ID号码
        int orderid = orderDao.saveOneOrder(newOrder).getOrderID();

        // 创建一个全新的 OrderItem 并查询书籍当前价格，写入订单
        OrderItem oneitem = new OrderItem();
        oneitem.setStatus(2);
        oneitem.setBelonguser(username);
        oneitem.setOrderID(orderid);
        oneitem.setBookID(bookIDGroup[0]);
        oneitem.setBuynum(bookNumGroup[0]);
        oneitem.setPayprice(bookDao.getOneBookByID(bookIDGroup[0]).getPrice() * bookNumGroup[0]);
        oneitem.setCreate_Itemtime(new Timestamp(System.currentTimeMillis()));

        // 销量 和 库存 修改
        targetBook.setSellnumber(targetBook.getSellnumber() + bookNumGroup[0]);
        targetBook.setInventory(targetBook.getInventory() - bookNumGroup[0]);

        return 0;
    }



    public List<OrderItem> findAllOrderItemInCart(String username){
        return orderItemDao.queryOneUserShopCart(username);
    }


    public Order getOneOrder(int ID){
        return orderDao.getOrderByID(ID);
    }

    public JSONArray getAllOrder(){
        JSONArray respData = new JSONArray();

        List<Order> allOrder = orderDao.getAllOrder();

        for(Order order : allOrder){
            JSONObject obj = JSONObject.fromObject(order);
            List<OrderItem> childItemGroup = order.getChileItem();

            JSONArray childJSON = new JSONArray().fromObject(childItemGroup);

            for(int i=0;i<childItemGroup.size();i++){
                int bookID = childItemGroup.get(i).getBookID();
                Book tmpbook = bookDao.getOneBookByID(bookID);

                JSONObject oneItemInchildJSON = childJSON.getJSONObject(i);

                oneItemInchildJSON.put("booktitle", tmpbook.getDisplaytitle());
                oneItemInchildJSON.put("bookurl", tmpbook.getImgtitle());

                childJSON.set(i,oneItemInchildJSON);
            }

            obj.put("chileItem",childJSON);
            respData.add(obj);
        }

        return respData;
    }

    public List<OrderItem> getAllOrderItem(){

        return orderItemDao.getAllOrderItem();
    }

    public JSONArray getAllOrderItemWithBook(){
        JSONArray respData = new JSONArray();
        List<OrderItem> allData = orderItemDao.getAllOrderItem();
        for (OrderItem allDatum : allData) {
            JSONObject obj = JSONObject.fromObject(allDatum);
            int bookID = allDatum.getBookID();
            Book tmpbook = bookDao.getOneBookByID(bookID);
            obj.put("displaytitle", tmpbook.getDisplaytitle());
            obj.put("imgtitle", tmpbook.getImgtitle());
            respData.add(obj);
        }
        return respData;
    }


    public List<OrderItem> getUserOrderItem(String username){
        return orderItemDao.getUserOrderItem(username);
    }


    public JSONArray getUserOrder(String username){

        JSONArray respData = new JSONArray();

        List<Order> allOrder = orderDao.getUserOrder(username);

        for(Order order : allOrder){
            JSONObject obj = JSONObject.fromObject(order);
            List<OrderItem> childItemGroup = order.getChileItem();

            JSONArray childJSON = new JSONArray().fromObject(childItemGroup);

            for(int i=0;i<childItemGroup.size();i++){
                int bookID = childItemGroup.get(i).getBookID();
                Book tmpbook = bookDao.getOneBookByID(bookID);
                JSONObject oneItemInchildJSON = childJSON.getJSONObject(i);
                oneItemInchildJSON.put("booktitle", tmpbook.getDisplaytitle());
                oneItemInchildJSON.put("bookurl", tmpbook.getImgtitle());
                childJSON.set(i,oneItemInchildJSON);
            }

            obj.put("chileItem",childJSON);
            respData.add(obj);
        }

        return respData;
    }

    public JSONArray getUserOrderItemWithBook(String username){
        JSONArray respData = new JSONArray();
        List<OrderItem> allData = orderItemDao.getUserOrderItem(username);

        for (OrderItem allDatum : allData) {
            JSONObject obj = JSONObject.fromObject(allDatum);
            int bookID = allDatum.getBookID();
            Book tmpbook = bookDao.getOneBookByID(bookID);
            obj.put("displaytitle", tmpbook.getDisplaytitle());
            obj.put("imgtitle", tmpbook.getImgtitle());
            respData.add(obj);
        }

        return respData;
    }


}
