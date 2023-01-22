package com.zzq.ebook.dao;
import com.zzq.ebook.entity.Book;
import com.zzq.ebook.entity.BookType;
import com.zzq.ebook.entity.ESBook;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

public interface BookDao {
    Book getOneBookByID(Integer id);

    Book numInfoChange(int bookID, int buyNum) throws Exception;
    List<Book> getAllBooks();
    List<Book> getTopSellBooks(int limit);
    List<Book> findBooksByAuthorLike(String author);
    List<Book> findBooksByPublisherLike(String publisher);
    List<Book> findBooksByDisplaytitleLike(String displaytitle);
    List<Book> findBooksGlobal(String keyword);
    void deleteOneBookByID(Integer id);
    Book saveOneBook(Book newoneBook);
    List<Book> saveAllBooks(List<Book> allBooks);

    SearchHits<ESBook> findESBooksByDescription(String keyword);

    List<Book> findBooksByTagRelation(String tagName);

}
