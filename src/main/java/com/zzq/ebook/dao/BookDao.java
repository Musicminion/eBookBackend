package com.zzq.ebook.dao;
import com.zzq.ebook.entity.Book;

import java.util.List;

public interface BookDao {
    Book getOneBookByID(Integer id);

    void deleteOneBookByID(Integer id);

    List<Book> getAllBooks();

    Book saveOneBook(Book newoneBook);
}
