package com.zzq.ebook.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "book")
public class ESBook {
    @Id
    @Field(type = FieldType.Integer)
    private int ID;

    @Field(analyzer = "ik_max_word")
    private String ISBN;

    @Field(type = FieldType.Text,index = false)
    private String bookname;

    @Field(analyzer = "ik_max_word")
    private String displaytitle;

    @Field(type = FieldType.Integer,index = false)
    private int inventory;

    @Field(analyzer = "ik_max_word")
    private String departure;

    @Field(analyzer = "ik_max_word")
    private String author;

    @Field(type = FieldType.Integer,index = false)
    private int price;

    @Field(type = FieldType.Integer,index = false)
    private int sellnumber;

    @Field(type = FieldType.Text,index = false)
    private String imgtitle;

    @Field(analyzer = "ik_max_word")
    private String publisher;

    @Field(analyzer = "ik_smart", searchAnalyzer = "ik_max_word")
    private String description;
}
