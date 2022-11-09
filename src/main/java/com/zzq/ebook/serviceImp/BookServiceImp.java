package com.zzq.ebook.serviceImp;

import com.zzq.ebook.dao.BookDao;
import com.zzq.ebook.dao.OrderItemDao;
import com.zzq.ebook.entity.Book;
import com.zzq.ebook.entity.ESBook;
import com.zzq.ebook.service.BookService;
import com.zzq.ebook.utils.message.Msg;
import com.zzq.ebook.utils.message.MsgCode;
import com.zzq.ebook.utils.message.MsgUtil;

import com.zzq.ebook.utils.tool.ToolFunction;
import net.sf.json.JSONObject;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import com.zzq.ebook.constant.constant;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BookServiceImp implements BookService {

    @Autowired
    private BookDao bookDao;
    @Autowired
    private OrderItemDao orderItemDao;

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

        // 0-全局搜 1-书籍名搜 2-出版社搜 3-作者搜 4-描述搜索
        switch (type){
            case 0:
                return bookDao.findBooksGlobal(keyword);
            case 1:
                return bookDao.findBooksByDisplaytitleLike(keyword);
            case 2:
                return bookDao.findBooksByPublisherLike(keyword);
            case 3:
                return bookDao.findBooksByAuthorLike(keyword);
            case 4:
                return ToolFunction.ESHitsToBook(bookDao.findESBooksByDescription(keyword));
            default:
                break;
        }
        return null;
    }

    public SearchHits<ESBook> getBooksByDescription(String keyword){
        return bookDao.findESBooksByDescription(keyword);
    }


    public Book addOneBook(JSONObject bookInfo){
        Book newBook = new Book();
        newBook.setInventory(Integer.parseInt(bookInfo.getString(constant.INVENTORY)));
        newBook.setSellnumber(0);
        newBook = bookInfoParser(newBook,bookInfo);
        newBook.setImgtitle(bookInfo.getString(constant.IMGTITLE));
        double tmpPrice = Double.parseDouble(bookInfo.getString(constant.PRICE));
        tmpPrice = tmpPrice * 100;
        int price = (int) tmpPrice;
        newBook.setPrice(price);

        return bookDao.saveOneBook(newBook);
    }



    public Book editOneBook(JSONObject bookInfo){
        int bookID = Integer.parseInt(bookInfo.getString(constant.BOOKID));
        Book targetBook = bookDao.getOneBookByID(bookID);

        if(targetBook==null)
            return null;
        else{
            targetBook.setInventory(Integer.parseInt(bookInfo.getString(constant.INVENTORY)));
            targetBook = bookInfoParser(targetBook,bookInfo);

            // 编辑书信息的时候，如果没有修改图片的信息，那么这个值就是nochange，反之，就说明改了，就要把新的URL写入
            if(!Objects.equals(bookInfo.getString(constant.IMGTITLE), "nochange")){
                targetBook.setImgtitle(bookInfo.getString(constant.IMGTITLE));
            }

            double tmpPrice = Double.parseDouble(bookInfo.getString(constant.PRICE));
            tmpPrice = tmpPrice * 100;
            int price = (int) tmpPrice;
            targetBook.setPrice(price);

            return bookDao.saveOneBook(targetBook);
        }
    }

    // 一个解析器，把相同的代码提取出来
    public Book bookInfoParser(Book targetBook,JSONObject bookInfo){
        targetBook.setAuthor(bookInfo.getString(constant.AUTHOR));
        targetBook.setBookname(bookInfo.getString(constant.BOOKNAME));
        targetBook.setDeparture(bookInfo.getString(constant.DEPARTURE));
        targetBook.setDisplaytitle(bookInfo.getString(constant.DISPLAYTITLE));
        targetBook.setPublisher(bookInfo.getString(constant.PUBLISHER));
        targetBook.setDescription(bookInfo.getString(constant.DESCRIPTION));
        targetBook.setISBN(bookInfo.getString(constant.ISBN));
        return targetBook;
    }


    public int deleteOneBook(Integer bookid){
        // 如果这个数有订单的话，不能运行删除 返回-1
        if(orderItemDao.getOrderItemByBookID(bookid) != null)
            return -1;
        bookDao.deleteOneBookByID(bookid);
        return 0;
    }


    public Msg getUploadSignature(String data, String key, String HMAC_SHA1_ALGORITHM){
        try {

            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);

            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            // calculate the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes());
            String result = Base64.encodeBase64String(rawHmac);

            JSONObject respData = new JSONObject();
            respData.put(constant.SIGNATURE,result);
            return MsgUtil.makeMsg(MsgCode.SUCCESS, MsgUtil.SUCCESS_MSG, respData);

        } catch (Exception e) {
            try {
                throw new SignatureException("Failed to generate HMAC:" + e.getMessage());
            } catch (SignatureException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
