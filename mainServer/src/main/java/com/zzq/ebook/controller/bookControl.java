package com.zzq.ebook.controller;

import com.zzq.ebook.constant.constant;
import com.zzq.ebook.entity.Book;
import com.zzq.ebook.entity.ESBook;
import com.zzq.ebook.privateKey.keyInfo;
import com.zzq.ebook.service.BookService;
import com.zzq.ebook.utils.message.Msg;
import com.zzq.ebook.utils.message.MsgCode;
import com.zzq.ebook.utils.message.MsgUtil;
import com.zzq.ebook.utils.session.SessionUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
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
//      /queryBooksSearch/{searchType}/{keyword}    按照条件查询书籍，具体种类参考函数内容，GET
//      /requestUploadSignature                     添加书的时候上传图片，然后获取签名，POST
//      /addBook/addone                             添加一本书，POST
//      /editBook                                   编辑一本书，POST
//      /deleteBook                                 删除一本书，POST
// ---------------------------------------------------------------------------------------------------------------------

@RestController
public class bookControl {
    @Autowired
    private BookService bookService;

    // 函数用途：按照ID请求一个书籍的信息，GET方法
    // 使用场景：书籍详情页面，前端通过Ajax请求发送过来，获得书籍详情的JSON数据
    // 权限要求：无限制
    @GetMapping(value = "/queryBookdetail/{bookid}")
    public Book getBook(@PathVariable("bookid") Integer id){
        if(id > 0){
            return bookService.getBookByID(id);
        }
        return null;
    }

    // 函数用途：获取主页的书籍推荐的信息，GET方法
    // 使用场景：主页的推荐书籍组件，通过GET请求，获取销量排名前15的书籍数组JSON信息
    // 权限要求：无限制
    @GetMapping(value = "/queryMainPageBooks")
    public List<Book> getMainPageBook(){
        return bookService.getRecommendBooks();
    }

    // 函数用途：按照条件查询书籍，具体种类参考函数内容，GET请求
    // 使用场景：搜索书籍，其中第一个参数是搜索的方法，第二个是关键字
    // 权限要求：无限制
    @GetMapping(value = "/queryBooksSearch/{searchType}/{keyword}")
    public List<Book> searchBooks(@PathVariable("searchType") int type,
                                  @PathVariable("keyword") String keyword){
        return bookService.getSearchedBooks(type,keyword);
    }

    @RequestMapping("/queryBook/All")
    public List<Book> getAllBook(@RequestBody Map<String, String> params){
        return bookService.getBookAll();
    }

    // 函数用途：获取上传文件的签名
    // 使用场景：上传图片，根据前端实时生成的Policy数据进行加密
    // 权限要求：管理员
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

    // 函数用途：添加书籍，POST
    // 使用场景：增加一本书
    // 权限要求：管理员
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

    // 函数用途：修改书籍，POST
    // 使用场景：编辑一本书的信息
    // 权限要求：管理员
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

    // 函数用途：删除书籍，POST,删除前由于要考虑外键关联
    // 使用场景：删除一本书的信息
    // 权限要求：管理员
    @RequestMapping("/deleteBook")
    public Msg deleteBook(@RequestBody Map<String, String> params){
        JSONObject auth = SessionUtil.getAuth();
        if(auth == null || !Objects.equals(auth.get(constant.PRIVILEGE),0))
            return MsgUtil.makeMsg(MsgCode.ERROR,MsgUtil.ERROR_MSG);

        String BookIDStr = params.get(constant.BOOKID);
        Integer BookID = Integer.parseInt(BookIDStr);
        if(bookService.deleteOneBook(BookID)==0)
            return MsgUtil.makeMsg(MsgCode.SUCCESS);
        else
            return MsgUtil.makeMsg(MsgCode.ERROR);
    }

}



