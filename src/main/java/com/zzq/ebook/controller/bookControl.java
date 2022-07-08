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
// -------------------------------------接-----------口-----------表-----------------------------------------------------
//      接口名称                                                接口说明：
//      /login                                      登录接口，接收POST请求，参数必须要包括username password，返回用户的完整信息。
//      /queryBookdetail/{bookid}                   获取书籍信息的接口，接收GET请求，返回该ID的书籍信息
//      /queryMainPageBooks                         获取主页的书籍推荐的信息，GET
//      /queryBooksSearch/{searchType}/{keyword}    按照条件查询书籍，具体种类参考函数内容，POST
//      /requestUploadSignature                     添加书的时候上传图片，然后获取签名，POST
//      /addBook/addone                             添加一本书，POST
//      /editBook                                   编辑一本书，POST
//      /deleteBook                                 删除一本书，POST
// ---------------------------------------------------------------------------------------------------------------------

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

    @RequestMapping("/requestUploadSignature")
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



