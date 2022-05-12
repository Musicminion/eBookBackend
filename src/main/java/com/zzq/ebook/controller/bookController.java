package com.zzq.ebook.controller;

import com.zzq.ebook.entity.Book;
import com.zzq.ebook.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


// 接口说明：




@RestController
public class bookController {
    @Autowired
    private BookService bookService;

    @GetMapping(value = "/queryBookdetail/{bookid}")
    public Book getBook(@PathVariable("bookid") Integer id){
        if(id > 0){
            System.out.println(id);
            return bookService.getBookByID(id);
        }

        return null;
    }
}