package com.zzq.ebook.controller;

import com.zzq.ebook.entity.Book;
import com.zzq.ebook.service.BookService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class testControl {

    @Autowired
    BookService bookService;

    //  测试的接口函数
    @RequestMapping("/test")
    public Book testFunction(){
        return bookService.getBookByID(2);
    }

    @RequestMapping("/testAll")
    public List<Book> testAll(){
        return bookService.getBookAll();
    }

//    @RequestMapping("/test1")
//    public String test1() {
//        return clockService.launchClock();
//    }
//
//    @RequestMapping("/test2")
//    public String test2() {
//        return clockService.endClock();
//    }
//


}
