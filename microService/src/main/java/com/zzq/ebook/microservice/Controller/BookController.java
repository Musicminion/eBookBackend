package com.zzq.ebook.microservice.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zzq.ebook.microservice.DTO.ResultDTO;
import com.zzq.ebook.microservice.Repository.BookRepository;
import com.zzq.ebook.microservice.entity.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
public class BookController {
    @Autowired
    BookRepository bookRepository;
    @GetMapping(value = "/getBookAuthorByName/{bookName}")
    public ResultDTO getBookAuthorByName(@PathVariable("bookName") String bookName){
        bookName = "%"+ bookName +"%";
        List<Book> books = bookRepository.findBooksByDisplaytitleLike(bookName);
        List<String> authors = new ArrayList<>();
        for (Book book : books) {
            authors.add(book.getBookname() + "的作者是"+ book.getAuthor());
        }
        return ResultDTO.success(JSON.toJSONString(authors));
    }
}
