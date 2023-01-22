package com.zzq.ebook.controller;

import com.zzq.ebook.dao.BookDao;
import com.zzq.ebook.dao.UserDao;
import com.zzq.ebook.entity.Book;
import com.zzq.ebook.entity.BookType;
import com.zzq.ebook.entity.ESBook;
import com.zzq.ebook.repository.BookRepository;
import com.zzq.ebook.repository.BookTypeRepository;
import com.zzq.ebook.repository.ESBookRepository;
import com.zzq.ebook.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@RestController
public class testControl {

    @Autowired
    BookService bookService;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    ESBookRepository esBookRepository;

    @Autowired
    BookTypeRepository bookTypeRepository;

    @Autowired
    BookDao bookDao;


    @Autowired
    UserDao userDao;

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

    @RequestMapping("/testMongo")
    public String testMongo(){
        System.out.println("搜了个啥？");
        return userDao.getUserIconByUsername("user7").getIconBase64();
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

    @RequestMapping("/neo4j")
    public List<Book> testNeo4j(){
        // 先删除所有的内容
        bookTypeRepository.deleteAll();
        // 添加书籍类型
        BookType bookType1 = new BookType("高中教辅");
        BookType bookType2 = new BookType("科普");
        BookType bookType3 = new BookType("大学教材");
        BookType bookType4 = new BookType("名著");
        BookType bookType5 = new BookType("杂志");
        BookType bookType6 = new BookType("游戏");
        BookType bookType7 = new BookType("文学");

        // 数据准备
        bookType1.addBookID(1);
        bookType1.addBookID(21);
        bookType1.addBookID(22);

        bookType2.addBookID(2);
        bookType2.addBookID(18);
        bookType2.addBookID(20);

        bookType3.addBookID(3);
        bookType3.addBookID(5);
        bookType3.addBookID(6);
        bookType3.addBookID(7);
        bookType3.addBookID(8);
        bookType3.addBookID(9);
        bookType3.addBookID(10);

        bookType4.addBookID(11);
        bookType5.addBookID(12);
        bookType6.addBookID(13);
        bookType6.addBookID(17);
        bookType7.addBookID(14);
        bookType7.addBookID(23);

        bookType1.addRelateBookType(bookType2);
        bookType1.addRelateBookType(bookType3);
        bookType1.addRelateBookType(bookType4);

        bookType2.addRelateBookType(bookType5);
        bookType2.addRelateBookType(bookType6);
        bookType3.addRelateBookType(bookType5);
        bookType3.addRelateBookType(bookType6);
        bookType4.addRelateBookType(bookType5);
        bookType4.addRelateBookType(bookType6);

        bookType5.addRelateBookType(bookType7);
        bookType6.addRelateBookType(bookType7);
        bookType7.addRelateBookType(bookType1);



        bookTypeRepository.save(bookType1);
        bookTypeRepository.save(bookType2);
        bookTypeRepository.save(bookType3);
        bookTypeRepository.save(bookType4);
        bookTypeRepository.save(bookType5);
        bookTypeRepository.save(bookType6);
        bookTypeRepository.save(bookType7);

        return bookDao.findBooksByTagRelation("高中教辅");
    }



}
