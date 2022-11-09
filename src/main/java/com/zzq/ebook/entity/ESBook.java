package com.zzq.ebook.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

// 整个实体类参考Book，但是为了避免混淆、适应ES的搜索引擎，所以我单独建立了一个实体
// 考虑到整个搜索中分词器的特性，我们需要在建立索引的时候使用ik_max_word分词器，
// 这个分词器的特性会把：例如会将文本做最细粒度的拆分，比如会将“中华人民共和国人民大会堂”
// 拆分为“中华人民共和国、中华人民、中华、华人、人民共和国、人民、共和国、大会堂、大会、会堂等词语。
// 索引时用ik_max_word，这样会把词组的划分最大化，
// 但是在搜索时用ik_smart，尽可能的匹配最大化。这也是对应我的Description的内容。

@Data
@Document(indexName = "book")
public class ESBook {
    @Id
    @Field(type = FieldType.Integer)
    private int ID;

    @Field(type = FieldType.Text)
    private String ISBN;

    @Field(type = FieldType.Text,index = false)
    private String bookname;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String displaytitle;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Field(type = FieldType.Integer,index = false)
    private int inventory;

    @Field(analyzer = "ik_max_word",type = FieldType.Text)
    private String departure;

    @Field(analyzer = "ik_max_word",type = FieldType.Text)
    private String author;

    @Field(type = FieldType.Integer,index = false)
    private int price;

    @Field(type = FieldType.Integer,index = false)
    private int sellnumber;

    @Field(type = FieldType.Text,index = false)
    private String imgtitle;

    @Field(analyzer = "ik_max_word",type = FieldType.Text)
    private String publisher;

    @Field(analyzer = "ik_smart", searchAnalyzer = "ik_max_word",type = FieldType.Text)
    private String description;

    public ESBook(){}
    public ESBook(Book book){
        this.ID = book.getID();
        this.ISBN = book.getISBN();
        this.bookname = book.getBookname();
        this.displaytitle = book.getDisplaytitle();
        this.inventory = book.getInventory();
        this.departure = book.getDeparture();
        this.author = book.getAuthor();
        this.price = book.getPrice();
        this.sellnumber = book.getSellnumber();
        this.imgtitle = book.getImgtitle();
        this.publisher = book.getPublisher();
        this.description = book.getDescription();
    }
}
