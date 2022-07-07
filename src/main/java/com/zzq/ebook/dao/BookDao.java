package com.zzq.ebook.dao;
import com.zzq.ebook.entity.Book;

import java.util.List;

public interface BookDao {
    Book getOneBookByID(Integer id);
    List<Book> getAllBooks();
    List<Book> getTopSellBooks(int limit);
    List<Book> findBooksByAuthorLike(String author);
    List<Book> findBooksByPublisherLike(String publisher);
    List<Book> findBooksByDisplaytitleLike(String displaytitle);
    List<Book> findBooksGlobal(String keyword);
    void deleteOneBookByID(Integer id);
    Book saveOneBook(Book newoneBook);
    List<Book> saveAllBooks(List<Book> allBooks);

}
