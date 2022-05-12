package com.zzq.ebook.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

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
    private Timestamp createTime;

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
    public Timestamp getCreateTime() {
        return createTime;
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
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
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
}
