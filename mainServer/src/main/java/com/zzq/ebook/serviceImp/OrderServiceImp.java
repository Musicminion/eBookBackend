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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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
    // 订单业务逻辑，作为一组事务，提交，出现异常可以回滚，购物车里面可能有多个购买项目，需要逐一处理
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class, isolation = Isolation.READ_COMMITTED)
    public int orderMakeFromShopCart(int [] bookIDGroup, int [] bookNumGroup, String username,
            String receivename, String postcode, String phonenumber, String receiveaddress, int size) throws Exception {
        // Step 0: 准备工作,初始化订单的总金额为0
        int totalMoney = 0;

        // Step 1: [增加] 操作orderDao, 根据用户订单信息创建新订单，并且保存
        // 说明：由于购物车中订单是用OrderItem表示的（用状态号区分是购物车的还是订单的），正式成为订单之前，
        //      需要创建一个新Order并获取此Order的ID号码 这个Order对象不包括他的ID，因为save之后会自动自增生成，
        //      然后我们通过获取实体，得到ID号码
        int orderID = orderDao.createOneOrderWithMultipleOrderItems(username,new Timestamp(System.currentTimeMillis()),
                receiveaddress,postcode,phonenumber,receivename).getOrderID();
        
        // Step 2 处理订单中的每一个项目：for循环处理
        for (int i = 0; i < size; i++){
            // Step 2-1: 操作orderItemDao，把购物车状态修改为已购买状态:2 ，返回实体获取价格
            OrderItem oneItem = orderItemDao.setOrderItemStatusByUsernameAndBookID(username,bookIDGroup[i],2,orderID);
            if(oneItem != null){
                totalMoney += oneItem.getPayprice();
                // Step 2-2: 操作bookDao，修改相关的数量，扣除购买的库存，增加销量，返回的数据要包括价格，然后把总价加起来
                bookDao.numInfoChange(bookIDGroup[i], bookNumGroup[i]);
            }
            else{
                throw new Exception("用户和订单不匹配");
            }
        }
        
        // Step 3: 根据刚刚的orderID，修改订单的支付价格
        orderDao.setOrderPayPrice(orderID,totalMoney);
        return 0;
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class, isolation = Isolation.READ_COMMITTED)
    public int orderMakeFromDirectBuy(int [] bookIDGroup, int [] bookNumGroup, String username,
        String receiveName, String postcode, String phoneNumber, String receiveAddress, int size) throws Exception {

        // 就一本书的话，就获取实体，检查库存
        Book targetBook = bookDao.getOneBookByID(bookIDGroup[0]);

        // Step 1 创建订单 Order
        int OrderID = orderDao.createOneOrderWithOneOrderItem(username,new Timestamp(System.currentTimeMillis()),
                receiveAddress,postcode,phoneNumber,receiveName,
                targetBook.getPrice() * bookNumGroup[0]).getOrderID();

        // Step 2 创建订单项目 OrderItem
        orderItemDao.createOrderItem(2,username,OrderID,bookIDGroup[0],bookNumGroup[0],
                targetBook.getPrice() * bookNumGroup[0],
                new Timestamp(System.currentTimeMillis()));

        // Step 3 操作库存信息
        bookDao.numInfoChange(bookIDGroup[0], bookNumGroup[0]);

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

        System.out.println("################");

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
