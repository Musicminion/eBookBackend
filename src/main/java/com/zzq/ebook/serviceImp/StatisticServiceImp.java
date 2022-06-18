package com.zzq.ebook.serviceImp;

import com.zzq.ebook.dao.BookDao;
import com.zzq.ebook.dao.OrderItemDao;
import com.zzq.ebook.entity.Book;
import com.zzq.ebook.service.StatisticService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StatisticServiceImp implements StatisticService {

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private BookDao bookDao;

    public JSONArray userConsumeStatistic(Date starttime, Date endtime){
        return orderItemDao.userConsumeStatistic(starttime,endtime);
    }


    public JSONArray bookSellnumStatistic(Date starttime, Date endtime){
        JSONArray SellData = orderItemDao.bookSellnumStatistic(starttime,endtime);
        System.out.println("SellData");
        System.out.println(SellData);

        for(int i=0;i<SellData.size();i++){
            JSONArray RowData = SellData.getJSONArray(i);
            System.out.println("RowData");
            System.out.println(RowData);

            String bookID = RowData.getString(0);

            Book tmpBook = bookDao.getOneBookByID(Integer.parseInt(bookID));

            if(tmpBook!=null){
                RowData.add(2,tmpBook.getBookname());
                RowData.add(3,tmpBook.getImgtitle());
            }
            SellData.set(i,RowData);
        }

        return SellData;
    }



    public JSONArray userSelfStatistic_BookWithBuyNum(Date starttime, Date endtime, String username){
        JSONArray BookWithBuyNumData = orderItemDao.userSelfStatistic_BookWithBuyNum(starttime, endtime,username);

        for(int i=0;i<BookWithBuyNumData.size();i++){
            JSONArray RowData = BookWithBuyNumData.getJSONArray(i);

            String bookID = RowData.getString(0);
            Book tmpBook = bookDao.getOneBookByID(Integer.parseInt(bookID));

            if(tmpBook!=null){
                RowData.add(2,tmpBook.getBookname());
                RowData.add(3,tmpBook.getImgtitle());
            }
            BookWithBuyNumData.set(i,RowData);
        }

        return BookWithBuyNumData;
    };

    public JSONArray userSelfStatistic_BookAllBuyNum(Date starttime, Date endtime, String username){
        return orderItemDao.userSelfStatistic_BookAllBuyNum(starttime, endtime,username);
    };

    public JSONArray userSelfStatistic_BookTotalPay(Date starttime, Date endtime, String username){
        return orderItemDao.userSelfStatistic_BookTotalPay(starttime, endtime,username);
    };

}
