package com.zzq.ebook.utils.tool;

import com.zzq.ebook.entity.Book;
import com.zzq.ebook.entity.ESBook;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ToolFunction {

    public static List<Book> ESHitsToBook(SearchHits<ESBook> searchHits){
        List<Book> books = new ArrayList<>();
        for (SearchHit<ESBook> hit : searchHits){
            ESBook esBook = hit.getContent();
            Book book = new Book();
            book.setAuthor(esBook.getAuthor());
            book.setBookname(esBook.getBookname());

            book.setDisplaytitle(esBook.getDisplaytitle());
            book.setDeparture(esBook.getDeparture());

            book.setDescription(hit.getHighlightField("description").toString());

            book.setInventory(esBook.getInventory());
            book.setISBN(esBook.getISBN());
            book.setImgtitle(esBook.getImgtitle());

            book.setSellnumber(esBook.getSellnumber());
            book.setPrice(esBook.getPrice());
            book.setID(esBook.getID());
            book.setPublisher(esBook.getPublisher());
            books.add(book);
        }
        return books;
    }



    public static Map<String,String> mapStringToMap(String str){
        str = str.substring(1, str.length()-1);
        String[] strs = str.split(",");
        Map<String,String> map = new HashMap<String,String>();
        for (String string : strs) {
            String key = string.split("=")[0];
            String value = string.split("=")[1];
            // 去掉头部空格
            String key1 = key.trim();
            String value1 = value.trim();
            map.put(key1, value1);
        }
        return map;
    }
    public static String getSHA256StrJava(String str){
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    private static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
}
