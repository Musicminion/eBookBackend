package com.zzq.ebook.daoImp;

import com.zzq.ebook.dao.BookDao;

import com.zzq.ebook.entity.Book;
import com.zzq.ebook.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class BookDaoImp implements BookDao {
    @Autowired
    private BookRepository bookRepository;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor=Exception.class)
    public Book getOneBookByID(Integer id){
        return bookRepository.getOne(id);
    }

    @Override
    public Book saveOneBook(Book newoneBook){
        return bookRepository.save(newoneBook);
    }

    @Override
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
    public Book numInfoChange(int bookID, int buyNum) throws Exception {
        Book book = bookRepository.getOne(bookID);
        if(book.getInventory() < buyNum)
            throw new Exception("购买数量超过库存！");
        // 扣除库存，增加销量
        book.setInventory(book.getInventory() - buyNum);
        book.setSellnumber(book.getSellnumber() + buyNum);
        return bookRepository.save(book);
    }

    @Override
    public void deleteOneBookByID(Integer id){
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
