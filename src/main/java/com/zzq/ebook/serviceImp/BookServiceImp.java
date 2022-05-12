package com.zzq.ebook.serviceImp;

import com.zzq.ebook.dao.BookDao;
import com.zzq.ebook.entity.Book;
import com.zzq.ebook.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImp implements BookService {

    @Autowired
    private BookDao bookDao;
    public Book getBookByID(Integer bookID){
        return bookDao.getOneBookByID(bookID);
    }
}
