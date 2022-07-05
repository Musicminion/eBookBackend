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

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SignatureException;
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
            System.out.println(id);
            return bookService.getBookByID(id);
        }
        return null;
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

    //ISBN: "123123123123"
    //author: "作者"
    //bookname: "名称"
    //departure: "上海"
    //description: "介绍"
    //displaytitle: "标题"
    //imgtitle: "https://ebookpicture.oss-cn-hangzhou.aliyuncs.com/image/1655272287575.jpg"
    //inventory: 1333
    //price: 12
    //publisher: "1111"
    @RequestMapping("/addBook/addone")
    public Msg addBook(@RequestBody Map<String, String> params){

        JSONObject auth = SessionUtil.getAuth();
        // 检查是全局管理员，才允许获得签名
        if(auth == null || !Objects.equals(auth.get(constant.PRIVILEGE),0))
            return null;
        JSONObject bookinfo = new JSONObject();
        bookinfo.put(constant.ISBN, params.get(constant.ISBN));
        bookinfo.put(constant.BOOKNAME, params.get(constant.BOOKNAME));
        bookinfo.put(constant.DISPLAYTITLE,params.get(constant.DISPLAYTITLE));
        bookinfo.put(constant.INVENTORY,params.get(constant.INVENTORY));
        bookinfo.put(constant.DEPARTURE,params.get(constant.DEPARTURE));
        bookinfo.put(constant.AUTHOR,params.get(constant.AUTHOR));
        bookinfo.put(constant.PRICE,params.get(constant.PRICE));
        bookinfo.put(constant.IMGTITLE,params.get(constant.IMGTITLE));
        bookinfo.put(constant.PUBLISHER,params.get(constant.PUBLISHER));
        bookinfo.put(constant.DESCRIPTION,params.get(constant.DESCRIPTION));

        System.out.println(bookinfo);

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
        JSONObject bookinfo = new JSONObject();
        bookinfo.put(constant.BOOKID,params.get(constant.BOOKID));
        bookinfo.put(constant.ISBN, params.get(constant.ISBN));
        bookinfo.put(constant.BOOKNAME, params.get(constant.BOOKNAME));
        bookinfo.put(constant.DISPLAYTITLE,params.get(constant.DISPLAYTITLE));
        bookinfo.put(constant.INVENTORY,params.get(constant.INVENTORY));
        bookinfo.put(constant.DEPARTURE,params.get(constant.DEPARTURE));
        bookinfo.put(constant.AUTHOR,params.get(constant.AUTHOR));
        bookinfo.put(constant.PRICE,params.get(constant.PRICE));
        bookinfo.put(constant.IMGTITLE,params.get(constant.IMGTITLE));
        bookinfo.put(constant.PUBLISHER,params.get(constant.PUBLISHER));
        bookinfo.put(constant.DESCRIPTION,params.get(constant.DESCRIPTION));

        System.out.println(bookinfo);
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
