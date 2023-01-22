package com.zzq.ebook.microservice.Repository;


import com.zzq.ebook.microservice.entity.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {

    List<Book> findBooksByDisplaytitleLike(String displaytitle);

    Book findBookByAuthor(String str);

}
