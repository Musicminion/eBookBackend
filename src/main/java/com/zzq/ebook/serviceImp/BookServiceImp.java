package com.zzq.ebook.serviceImp;

import com.zzq.ebook.dao.BookDao;
import com.zzq.ebook.entity.Book;
import com.zzq.ebook.service.BookService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zzq.ebook.constant.constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BookServiceImp implements BookService {

    @Autowired
    private BookDao bookDao;
    public Book getBookByID(Integer bookID){
        return bookDao.getOneBookByID(bookID);
    }

    public List<Book> getBookAll(){
        return bookDao.getAllBooks();
    }

    public List<Book> getRecommendBooks(){
        return bookDao.getTopSellBooks(15);
    }
    public List<Book> getSearchedBooks(int type, String keyword){
        keyword = "%"+ keyword +"%";
        System.out.println(bookDao.findBooksGlobal(keyword));

        // 0-全局搜 1-书籍名搜 2-出版社搜 3-作者搜
        switch (type){
            case 0:
                return bookDao.findBooksGlobal(keyword);
            case 1:
                return bookDao.findBooksByDisplaytitleLike(keyword);
            case 2:
                return bookDao.findBooksByAuthorLike(keyword);

            case 3:
                return bookDao.findBooksByPublisherLike(keyword);
            default:
                break;
        }
        return null;

    }


    public Book addOneBook(JSONObject bookinfo){
        Book newbook = new Book();
        newbook.setInventory(Integer.parseInt(bookinfo.getString(constant.INVENTORY)));
        newbook.setSellnumber(0);
        newbook.setAuthor(bookinfo.getString(constant.AUTHOR));
        newbook.setBookname(bookinfo.getString(constant.BOOKNAME));
        newbook.setDeparture(bookinfo.getString(constant.DEPARTURE));
        newbook.setDisplaytitle(bookinfo.getString(constant.DISPLAYTITLE));
        newbook.setPublisher(bookinfo.getString(constant.PUBLISHER));
        newbook.setDescription(bookinfo.getString(constant.DESCRIPTION));
        newbook.setISBN(bookinfo.getString(constant.ISBN));
        newbook.setImgtitle(bookinfo.getString(constant.IMGTITLE));
        double tmpPrice = Double.parseDouble(bookinfo.getString(constant.PRICE));
        tmpPrice = tmpPrice * 100;
        int price = (int) tmpPrice;
        newbook.setPrice(price);

        return bookDao.saveOneBook(newbook);
    }


    public Book editOneBook(JSONObject bookinfo){
        int bookID = Integer.parseInt(bookinfo.getString(constant.BOOKID));
        Book targetBook = bookDao.getOneBookByID(bookID);
        if(targetBook==null)
            return null;
        else{
            targetBook.setInventory(Integer.parseInt(bookinfo.getString(constant.INVENTORY)));
            targetBook.setAuthor(bookinfo.getString(constant.AUTHOR));
            targetBook.setBookname(bookinfo.getString(constant.BOOKNAME));
            targetBook.setDeparture(bookinfo.getString(constant.DEPARTURE));
            targetBook.setDisplaytitle(bookinfo.getString(constant.DISPLAYTITLE));
            targetBook.setPublisher(bookinfo.getString(constant.PUBLISHER));
            targetBook.setDescription(bookinfo.getString(constant.DESCRIPTION));
            targetBook.setISBN(bookinfo.getString(constant.ISBN));
            // 编辑书信息的时候，如果没有修改图片的信息，那么这个值就是nochange，反之，就说明改了，就要把新的URL写入
            if(!Objects.equals(bookinfo.getString(constant.IMGTITLE), "nochange")){
                targetBook.setImgtitle(bookinfo.getString(constant.IMGTITLE));
            }

            double tmpPrice = Double.parseDouble(bookinfo.getString(constant.PRICE));
            tmpPrice = tmpPrice * 100;
            int price = (int) tmpPrice;
            targetBook.setPrice(price);

            return bookDao.saveOneBook(targetBook);
        }
    }

    public void deleteOneBook(Integer bookid){
        bookDao.deleteOneBookByID(bookid);
    }

}
