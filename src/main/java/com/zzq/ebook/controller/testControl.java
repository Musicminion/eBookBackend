package com.zzq.ebook.controller;

import com.zzq.ebook.entity.Book;
import com.zzq.ebook.entity.ESBook;
import com.zzq.ebook.repository.BookRepository;
import com.zzq.ebook.repository.ESBookRepository;
import com.zzq.ebook.service.BookService;
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
        for (Book allBook : allBooks) {
            ESBook tmpES = changeToESBook(allBook);
            esBookRepository.save(tmpES);
        }
        return esBookRepository.findAll();
    }

    @RequestMapping("/findtest")
    public SearchHits<ESBook> testFunction(){
        esBookRepository.deleteAll();
        List<Book> allBooks = bookRepository.findAll();
        for (Book allBook : allBooks) {
            ESBook tmpES = changeToESBook(allBook);
            esBookRepository.save(tmpES);
        }
        return esBookRepository.findByDescription("地位");
    }


    // 把Book转化为ESBook
    private ESBook changeToESBook(Book book){
        ESBook newESBook = new ESBook();
        newESBook.setID(book.getID());
        newESBook.setBookname(book.getBookname());
        newESBook.setDisplaytitle(book.getDisplaytitle());
        newESBook.setDescription(book.getDescription());
        newESBook.setAuthor(book.getAuthor());
        newESBook.setDeparture(book.getDeparture());
        newESBook.setImgtitle(book.getImgtitle());
        newESBook.setISBN(book.getISBN());
        newESBook.setPrice(book.getPrice());
        newESBook.setPublisher(book.getPublisher());
        newESBook.setSellnumber(book.getSellnumber());
        newESBook.setInventory(book.getInventory());
        return newESBook;
    }
}
