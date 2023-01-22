package com.zzq.ebook.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "books")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    private String ISBN;
    private String bookname;
    private String displaytitle;
    private int inventory;
    private String departure;
    private String author;
    private int price;
    private int sellnumber;
    private String imgtitle;
    private String publisher;
    private String description;

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {return ID;}
    public String getISBN() {return ISBN;}
    public String getBookname() {return bookname;}
    public int getInventory() {
        return inventory;
    }
    public int getSellnumber() {
        return sellnumber;
    }
    public String getAuthor() {
        return author;
    }
    public String getDeparture() {
        return departure;
    }
    public int getPrice() {
        return price;
    }
    public String getDescription() {
        return description;
    }
    public String getDisplaytitle() {
        return displaytitle;
    }
    public String getImgtitle() {
        return imgtitle;
    }
    public String getPublisher() {
        return publisher;
    }
    // -----------------------  set  -----------------------
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setBookname(String bookname) {
        this.bookname = bookname;
    }
    public void setDeparture(String departure) {
        this.departure = departure;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDisplaytitle(String displaytitle) {
        this.displaytitle = displaytitle;
    }
    public void setImgtitle(String imgtitle) {
        this.imgtitle = imgtitle;
    }
    public void setInventory(int inventory) {
        this.inventory = inventory;
    }
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public void setSellnumber(int sellnumber) {
        this.sellnumber = sellnumber;
    }

    //    public void setID(int ID) { this.ID = ID; }

    public Book(){}
    public Book(ESBook esBook){
        this.ID = esBook.getID();
        this.ISBN = esBook.getISBN();
        this.bookname = esBook.getBookname();
        this.displaytitle = esBook.getDisplaytitle();
        this.inventory = esBook.getInventory();
        this.departure = esBook.getDeparture();
        this.author = esBook.getAuthor();
        this.price = esBook.getPrice();
        this.sellnumber = esBook.getSellnumber();
        this.imgtitle = esBook.getImgtitle();
        this.publisher = esBook.getPublisher();
        this.description = esBook.getDescription();
    }
}
