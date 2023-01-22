package com.zzq.ebook.service;

import com.zzq.ebook.entity.Book;
import com.zzq.ebook.entity.ESBook;
import com.zzq.ebook.utils.message.Msg;
import net.sf.json.JSONObject;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

public interface BookService {

    public Book getBookByID(Integer bookID);
    public List<Book> getBookAll();
    public List<Book> getRecommendBooks();
    public List<Book> getSearchedBooks(int type, String keyword);
    public Book addOneBook(JSONObject bookinfo);
    public Book editOneBook(JSONObject bookinfo);
    public int deleteOneBook(Integer bookid);

    public SearchHits<ESBook> getBooksByDescription(String keyword);


    public Msg getUploadSignature(String data,String key,String HMAC_SHA1_ALGORITHM);
}
