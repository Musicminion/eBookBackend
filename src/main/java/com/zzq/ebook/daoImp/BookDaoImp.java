package com.zzq.ebook.daoImp;

import com.zzq.ebook.dao.BookDao;

import com.zzq.ebook.entity.Book;
import com.zzq.ebook.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookDaoImp implements BookDao {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book getOneBookByID(Integer id){
        return bookRepository.getOne(id);
    }
}
