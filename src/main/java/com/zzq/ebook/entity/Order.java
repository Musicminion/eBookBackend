package com.zzq.ebook.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "orders")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class Order {

    @Id
    private int OrderID;

    private String belonguser;
    private Timestamp create_time;

    private String destination;
    private String postalcode;
    private String contactphone;
    private String receivername;

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
}
