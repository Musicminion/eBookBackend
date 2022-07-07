package com.zzq.ebook.controller;

import com.zzq.ebook.constant.constant;
import com.zzq.ebook.entity.Book;
import com.zzq.ebook.privateKey.keyInfo;
import com.zzq.ebook.service.BookService;
import com.zzq.ebook.utils.message.Msg;
import com.zzq.ebook.utils.message.MsgCode;
import com.zzq.ebook.utils.message.MsgUtil;
import com.zzq.ebook.utils.session.SessionUtil;
import net.sf.json.JSONObject;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
// 接口说明：


@RestController
public class bookControl {
    @Autowired
    private BookService bookService;

    @GetMapping(value = "/queryBookdetail/{bookid}")
    public Book getBook(@PathVariable("bookid") Integer id){
        if(id > 0){
            return bookService.getBookByID(id);
        }
        return null;
    }

    @GetMapping(value = "/queryMainPageBooks")
    public List<Book> getMainPageBook(){
        return bookService.getRecommendBooks();
    }

    @GetMapping(value = "/queryBooksSearch/{searchType}/{keyword}")
    public List<Book> searchBooks(@PathVariable("searchType") int type,
                                  @PathVariable("keyword") String keyword){

        return bookService.getSearchedBooks(type,keyword);
    }


    @RequestMapping("/queryBook/All")
    public List<Book> getAllBook(@RequestBody Map<String, String> params){
        return bookService.getBookAll();
    }

    @RequestMapping("/addBook/requestSignature")
    public Msg requestSignature(@RequestBody Map<String, String> params){
        JSONObject auth = SessionUtil.getAuth();
        // 检查是全局管理员，才允许获得签名
        if(auth == null || !Objects.equals(auth.get(constant.PRIVILEGE),0))
            return null;

        String data = params.get(constant.POLICYDATA);
        String key = keyInfo.accessKey;
        String HMAC_SHA1_ALGORITHM = "HmacSHA1";

        return bookService.getUploadSignature(data,key,HMAC_SHA1_ALGORITHM);
    }


    @RequestMapping("/addBook/addone")
    public Msg addBook(@RequestBody Map<String, String> params){

        JSONObject auth = SessionUtil.getAuth();
        // 检查是全局管理员，才允许加书
        if(auth == null || !Objects.equals(auth.get(constant.PRIVILEGE),0))
            return null;

        // 解析参数 转JSON对象交服务层
        JSONObject bookinfo = new JSONObject();
        bookinfo.putAll(params);
        Book result = bookService.addOneBook(bookinfo);

        if(result!=null){
            int resultID = result.getID();
            JSONObject respData = new JSONObject();
            respData.put(constant.BOOKID,resultID);
            return MsgUtil.makeMsg(MsgCode.SUCCESS, MsgUtil.SUCCESS_MSG, respData);
        }
        else
            return MsgUtil.makeMsg(MsgCode.ERROR);
    }

    @RequestMapping("/editBook")
    public Msg editBook(@RequestBody Map<String, String> params){

        JSONObject auth = SessionUtil.getAuth();
        // 检查是全局管理员，才允许获得签名
        if(auth == null || !Objects.equals(auth.get(constant.PRIVILEGE),0))
            return null;

        // 解析参数 转JSON对象交服务层
        JSONObject bookinfo = new JSONObject();
        bookinfo.putAll(params);
        Book result = bookService.editOneBook(bookinfo);

        if(result!=null){
            int resultID = result.getID();
            JSONObject respData = new JSONObject();
            respData.put(constant.BOOKID,resultID);
            return MsgUtil.makeMsg(MsgCode.SUCCESS, MsgUtil.SUCCESS_MSG, respData);
        }
        else
            return MsgUtil.makeMsg(MsgCode.ERROR);
    }

    @RequestMapping("/deleteBook")
    public Msg deleteBook(@RequestBody Map<String, String> params){
        JSONObject auth = SessionUtil.getAuth();
        if(auth == null || !Objects.equals(auth.get(constant.PRIVILEGE),0))
            return MsgUtil.makeMsg(MsgCode.ERROR,MsgUtil.ERROR_MSG);

        String BookIDStr = params.get(constant.BOOKID);
        Integer BookID = Integer.parseInt(BookIDStr);
        bookService.deleteOneBook(BookID);
        return MsgUtil.makeMsg(MsgCode.SUCCESS);
    }

}




//
//
//        bookinfo.put(constant.ISBN, params.get(constant.ISBN));
//        bookinfo.put(constant.BOOKNAME, params.get(constant.BOOKNAME));
//        bookinfo.put(constant.DISPLAYTITLE,params.get(constant.DISPLAYTITLE));
//        bookinfo.put(constant.INVENTORY,params.get(constant.INVENTORY));
//        bookinfo.put(constant.DEPARTURE,params.get(constant.DEPARTURE));
//        bookinfo.put(constant.AUTHOR,params.get(constant.AUTHOR));
//        bookinfo.put(constant.PRICE,params.get(constant.PRICE));
//        bookinfo.put(constant.IMGTITLE,params.get(constant.IMGTITLE));
//        bookinfo.put(constant.PUBLISHER,params.get(constant.PUBLISHER));
//        bookinfo.put(constant.DESCRIPTION,params.get(constant.DESCRIPTION));
//
//        System.out.println(bookinfo);