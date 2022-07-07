package com.zzq.ebook.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orderitem")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class OrderItem {
    @Id
    private int itemID;

    private int status;
    private String belonguser;
    private int orderID;
    private int bookID;
    private int buynum;
    private int payprice;
    private Timestamp create_Itemtime;
    private String comment;

    public int getItemID() {return itemID;}
    public int getBookID() {return bookID;}
    public int getOrderID() {return orderID;}
    public int getBuynum() {
        return buynum;
    }
    public int getStatus() {
        return status;
    }
    public String getBelonguser() {
        return belonguser;
    }
    public int getPayprice() {
        return payprice;
    }
    public Timestamp getCreate_Itemtime() {
        return create_Itemtime;
    }

    public String getComment() {
        return comment;
    }

    public void setPayprice(int payprice) {
        this.payprice = payprice;
    }
    public void setBelonguser(String belonguser) {
        this.belonguser = belonguser;
    }
    public void setBookID(int bookID) {
        this.bookID = bookID;
    }
    public void setBuynum(int buynum) {
        this.buynum = buynum;
    }

    public void setCreate_Itemtime(Timestamp createItemtime) {
        this.create_Itemtime = createItemtime;
    }
    public void setItemID(int itemID) {
        this.itemID = itemID;
    }
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bookID",insertable=false,updatable=false)
    private Book bookinfo;

    public Book getBookinfo() {
        return this.bookinfo;
    }

    public void setBookinfo(Book bookinfo) {
        this.bookinfo = bookinfo;
    }

}
