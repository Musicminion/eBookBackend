package com.zzq.ebook.service;

import com.zzq.ebook.entity.Order;
import com.zzq.ebook.entity.OrderItem;
import net.sf.json.JSONArray;

import java.util.List;

public interface OrderService {
    public OrderItem addOneOrderItemToChart(String username, int bookID,int buynum);

    public List<OrderItem> findAllOrderItemInCart(String username);

    public int editOneOrderItemBUYNUMInChart(String username, int bookID, int buynum);

    public int orderMakeFromShopCart(int [] bookIDGroup, int [] bookNumGroup, String username,
            String receivename, String postcode, String phonenumber, String receiveaddress, int size) throws Exception;

    public int orderMakeFromDirectBuy(int [] bookIDGroup, int [] bookNumGroup, String username,
            String receivename, String postcode, String phonenumber, String receiveaddress, int size) throws Exception;

    public Order getOneOrder(int ID);

    public JSONArray getAllOrder();


    public List<OrderItem> getAllOrderItem();

    public JSONArray getAllOrderItemWithBook();

    public List<OrderItem> getUserOrderItem(String username);

    public JSONArray getUserOrderItemWithBook(String username);


    public JSONArray getUserOrder(String username);
}
