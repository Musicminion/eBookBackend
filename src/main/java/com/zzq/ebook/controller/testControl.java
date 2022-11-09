package com.zzq.ebook.controller;

import com.zzq.ebook.entity.Book;
import com.zzq.ebook.entity.ESBook;
import com.zzq.ebook.repository.BookRepository;
import com.zzq.ebook.repository.ESBookRepository;
import com.zzq.ebook.service.BookService;
import com.zzq.ebook.utils.tool.ToolFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class testControl {

    @Autowired
    BookService bookService;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    ESBookRepository esBookRepository;

    //  测试的接口函数
    // 将书籍迁移到es中
    @RequestMapping("/init")
    public List<ESBook> initFunction(){
        esBookRepository.deleteAll();
        List<Book> allBooks = bookRepository.findAll();
        for (Book oneBook : allBooks) {
            ESBook tmpES = new ESBook(oneBook);
            esBookRepository.save(tmpES);
        }
        return esBookRepository.findAll();
    }

    @RequestMapping("/findtest")
    public SearchHits<ESBook> testFunction(){
        esBookRepository.deleteAll();
        List<Book> allBooks = bookRepository.findAll();
        for (Book allBook : allBooks) {
            ESBook tmpES = new ESBook(allBook);
            esBookRepository.save(tmpES);
        }
        return esBookRepository.findByDescription("地位");
    }

}
