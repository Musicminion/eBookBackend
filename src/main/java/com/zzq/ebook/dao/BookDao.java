package com.zzq.ebook.dao;
import com.zzq.ebook.entity.Book;
public interface BookDao {
    Book getOneBookByID(Integer id);
}
