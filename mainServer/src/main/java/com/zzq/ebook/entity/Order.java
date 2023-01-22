package com.zzq.ebook.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "orders")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int OrderID;

    private String belonguser;
    private Timestamp create_time;

    private String destination;
    private String postalcode;
    private String contactphone;
    private String receivername;

    private int totalprice;


    public Order(){}

    public Order(String belonguser,Timestamp create_time,String destination, String postalcode,
                 String contactphone, String receivername){
        this.belonguser = belonguser;
        this.create_time = create_time;
        this.destination = destination;
        this.postalcode = postalcode;
        this.contactphone = contactphone;
        this.receivername = receivername;
    }


    public Order(String belonguser,Timestamp create_time,String destination, String postalcode,
                 String contactphone, String receivername, int payPrice){
        this.belonguser = belonguser;
        this.create_time = create_time;
        this.destination = destination;
        this.postalcode = postalcode;
        this.contactphone = contactphone;
        this.receivername = receivername;
        this.totalprice = payPrice;
    }


    public void setTotalprice(int price){
        this.totalprice = price;
    }

    public int getTotalprice(){
        return this.totalprice;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }
    public void setBelonguser(String belonguser) {
        this.belonguser = belonguser;
    }
    public int getOrderID() {
        return OrderID;
    }
    public String getBelonguser() {
        return belonguser;
    }
    public String getContactphone() {
        return contactphone;
    }
    public String getDestination() {return destination;}
    public String getPostalcode() {return postalcode;}
    public String getReceivername() {return receivername;}
    public Timestamp getCreate_time() {return create_time;}
    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }
    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }
    public void setDestination(String destination) {this.destination = destination;}
    public void setPostalcode(String postalcode) {this.postalcode = postalcode;}
    public void setReceivername(String receivername) {this.receivername = receivername;}


    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "OrderID")
    private List<OrderItem> chileItem;

    public void setChileItem(List<OrderItem> chileItem) {
        this.chileItem = chileItem;
    }

    public List<OrderItem> getChileItem() {
        return chileItem;
    }
}
