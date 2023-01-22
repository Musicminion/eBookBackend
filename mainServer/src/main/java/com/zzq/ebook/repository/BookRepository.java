package com.zzq.ebook.repository;

import com.zzq.ebook.entity.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Integer> {
    List<Book> findAllByOrderBySellnumberDesc(Pageable pageable);
    List<Book> findBooksByAuthorLike(String author);
    List<Book> findBooksByPublisherLike(String publisher);
    List<Book> findBooksByDisplaytitleIsLike(String displaytitle);

    List<Book> findBooksByAuthorLikeOrPublisherLikeOrDisplaytitleLike(
            String author,String publisher,String displaytitle
    );

}
