package com.zzq.ebook.daoImp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zzq.ebook.config.RedisConfig;
import com.zzq.ebook.dao.BookDao;

import com.zzq.ebook.entity.Book;
import com.zzq.ebook.entity.ESBook;
import com.zzq.ebook.repository.BookRepository;
//import com.zzq.ebook.utils.redis.RedisUtil;


import com.zzq.ebook.repository.ESBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class BookDaoImp implements BookDao {
    @Autowired
    private BookRepository bookRepository;

//    @Autowired
//    RedisUtil redisUtil;

    @Autowired
    private ESBookRepository esBookRepository;

    @Override
    public SearchHits<ESBook> findESBooksByDescription(String keyword){
        return esBookRepository.findByDescription(keyword);
    }


    // Redis:通过ID获取书的信息的时候，book + [ID] 作为Redis存储的Key，
    // value为book转化为JSON字符串的内容
    // 操作逻辑：先在缓存里面找一下，找到了就直接返回，没找到就去数据库，并把找到的写入缓存
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor=Exception.class)
    public Book getOneBookByID(Integer id){
//        Book book;
//        Object p = redisUtil.get("book"+id);
//        if(p == null){
//            book = bookRepository.getOne(id);
//            redisUtil.set("book" + id, JSONArray.toJSON(book));
//            System.out.println("通过数据库拿到的一本书");
//        }
//        else {
//            book = JSONArray.parseObject(p.toString(),Book.class);
//            System.out.println("通过Redis拿到的一本书");
//        }
//        return book;
        return bookRepository.getOne(id);
    }

    // Redis:保存一本书的时候，去Redis里面查找一下，如果不存在，就直接写入数据库
    // 如果存在，就要更新Redis的内容，然后再写入数据库
    @Override
    @Transactional
    public Book saveOneBook(Book newOneBook){
//        Object p = redisUtil.get("book"+newOneBook.getID());
//        if(p != null){
//            redisUtil.set("book" + newOneBook.getID(), JSONArray.toJSON(newOneBook));
//        }
        return bookRepository.save(newOneBook);
    }

    // 获取所有的书，类比前面的，这个功能是给管理员用的，获取所有的书籍信息
    @Override
    public List<Book> getAllBooks(){
//        List<Book> bookList = null;
//        Object p = redisUtil.get("bookAll");
//        if(p == null){
//            bookList = bookRepository.findAll();
//            redisUtil.set("bookAll", JSONArray.toJSON(bookList));
//            System.out.println("通过数据库拿到的书[bookAll]");
//        }
//        else {
//            bookList = (List<Book>) JSONObject.parseArray(p.toString(),Book.class);
//            System.out.println("通过Redis拿到的书[bookAll]");
//        }
//        return bookList;
        return bookRepository.findAll();
    }

    // 功能：更新书的库存信息
    // Redis 操作相关：
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
    public Book numInfoChange(int bookID, int buyNum) throws Exception {
        Book book = bookRepository.getOne(bookID);
        if(book.getInventory() < buyNum)
            throw new Exception("购买数量超过库存！");
        // 扣除库存，增加销量
        book.setInventory(book.getInventory() - buyNum);
        book.setSellnumber(book.getSellnumber() + buyNum);

//        // Redis 操作
//        Object p = redisUtil.get("book"+ bookID);
//        if(p != null){
//            redisUtil.set("bookAll", JSONArray.toJSON(book));
//        }
        return bookRepository.save(book);
    }

    // 删除书的操作，先删除缓存
    @Override
    public void deleteOneBookByID(Integer id){
//        redisUtil.del("book"+id);
        bookRepository.deleteById(id);
    }

    // 函数用途：接收整数 limit 参数，获取销量前 limit 的书籍 用途
    // 使用场景：主页的获取推荐书籍
    @Override
    public List<Book> getTopSellBooks(int limit){
        return bookRepository.findAllByOrderBySellnumberDesc(PageRequest.of(0, limit));
    };

    // 函数用途：接收字符串参数，然后到数据库里面去查找书籍
    // 使用场景：搜素书籍，搜索方式是根据作者名字
    @Override
    public List<Book> findBooksByAuthorLike(String author){
        return bookRepository.findBooksByAuthorLike(author);
    }

    // 函数用途：接收字符串参数，然后到数据库里面去查找书籍
    // 使用场景：搜素书籍，搜索方式是根据出版社名字
    @Override
    public List<Book> findBooksByPublisherLike(String publisher){
        return bookRepository.findBooksByPublisherLike(publisher);
    }

    // 函数用途：接收字符串参数，然后到数据库里面去查找书籍
    // 使用场景：搜素书籍，搜索方式是根据展示的标题名字
    @Override
    public List<Book> findBooksByDisplaytitleLike(String displaytitle){
        return bookRepository.findBooksByDisplaytitleIsLike(displaytitle);
    }

    // 函数用途：接收字符串参数，然后到数据库里面去查找书籍
    // 使用场景：搜素书籍，搜索方式是以上三种的综合
    @Override
    public List<Book> findBooksGlobal(String keyword){
        return bookRepository.findBooksByAuthorLikeOrPublisherLikeOrDisplaytitleLike(
            keyword,keyword,keyword
        );
    }
    @Override
    public List<Book> saveAllBooks(List<Book> allBooks){
        return bookRepository.saveAll(allBooks);
    }
}
