package com.zzq.ebook.repository;

import com.zzq.ebook.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Integer> {

}
