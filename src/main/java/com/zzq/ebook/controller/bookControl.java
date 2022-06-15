package com.zzq.ebook.controller;

import com.zzq.ebook.constant.constant;
import com.zzq.ebook.entity.Book;
import com.zzq.ebook.service.BookService;
import com.zzq.ebook.utils.message.Msg;
import com.zzq.ebook.utils.message.MsgCode;
import com.zzq.ebook.utils.message.MsgUtil;
import net.sf.json.JSONObject;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.security.SignatureException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.util.Map;


import com.zzq.ebook.privateKey.keyInfo;

import com.aliyun.oss.OSSClient;
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

    @RequestMapping("/addBook/requestSignature")
    public Msg requestSignature(@RequestBody Map<String, String> params){
        String data = params.get(constant.POLICYDATA);
        String key = keyInfo.accessKey;
        String HMAC_SHA1_ALGORITHM = "HmacSHA1";

        try {

            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);

            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes());
            String result = Base64.encodeBase64String(rawHmac);
            System.out.println(result);
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
