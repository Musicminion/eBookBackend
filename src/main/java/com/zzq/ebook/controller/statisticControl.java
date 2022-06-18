package com.zzq.ebook.controller;


import com.zzq.ebook.constant.constant;
import com.zzq.ebook.entity.Order;
import com.zzq.ebook.repository.OrderItemRepository;
import com.zzq.ebook.repository.OrderRepository;
import com.zzq.ebook.service.StatisticService;
import com.zzq.ebook.utils.session.SessionUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class statisticControl {

    @Autowired
    private StatisticService statisticService;


//   测试用的
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @RequestMapping("/statistic/userConsume")
    public JSONArray userConsumeData(@RequestBody Map<String, String> params) throws ParseException {
        if(params.get("startDate")!=null && params.get("endDate")!=null){
            String startstr = params.get("startDate");
            String endstr = params.get("endDate");

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date datastart = sdf1.parse(startstr);
            Date dataend = sdf1.parse(endstr);

            return statisticService.userConsumeStatistic(datastart,dataend);
        }

        else{

            String startstr = "1000-01-01 00:00:00";
            String endstr = "9999-12-31 12:59:59";
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datastart = sdf1.parse(startstr);
            Date dataend = sdf1.parse(endstr);

            return statisticService.userConsumeStatistic(datastart,dataend);
        }
    }


    @RequestMapping("/statistic/bookSellnum")
    public JSONArray bookSellnum(@RequestBody Map<String, String> params) throws ParseException {
        if(params.get("startDate")!=null && params.get("endDate")!=null){
            String startstr = params.get("startDate");
            String endstr = params.get("endDate");

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date datastart = sdf1.parse(startstr);
            Date dataend = sdf1.parse(endstr);

            return statisticService.bookSellnumStatistic(datastart,dataend);
        }

        else{

            String startstr = "1000-01-01 00:00:00";
            String endstr = "9999-12-31 12:59:59";
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datastart = sdf1.parse(startstr);
            Date dataend = sdf1.parse(endstr);

            return statisticService.bookSellnumStatistic(datastart,dataend);
        }

    }

//    JSONArray userSelfStatistic_BookWithBuyNum(Date starttime, Date endtime, String username);
//
//    JSONArray userSelfStatistic_BookAllBuyNum(Date starttime, Date endtime, String username);
//
//    JSONArray userSelfStatistic_BookTotalPay(Date starttime, Date endtime, String username);

    @RequestMapping("/statistic/userStatistic/bookTotalPay")
    public JSONArray userBookTotayPay(@RequestBody Map<String, String> params) throws ParseException {
        JSONObject auth = SessionUtil.getAuth();
        assert auth != null;
        String username = (String) auth.get(constant.USERNAME);

        if(params.get("startDate")!=null && params.get("endDate")!=null){
            String startstr = params.get("startDate");
            String endstr = params.get("endDate");

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date datastart = sdf1.parse(startstr);
            Date dataend = sdf1.parse(endstr);

            return statisticService.userSelfStatistic_BookTotalPay(datastart,dataend,username);
        }

        else{
            String startstr = "1000-01-01 00:00:00";
            String endstr = "9999-12-31 12:59:59";
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datastart = sdf1.parse(startstr);
            Date dataend = sdf1.parse(endstr);

            return statisticService.userSelfStatistic_BookTotalPay(datastart,dataend,username);
        }

    }

    @RequestMapping("/statistic/userStatistic/bookAllBuyNum")
    public JSONArray userBookAllBuyNum(@RequestBody Map<String, String> params) throws ParseException {
        JSONObject auth = SessionUtil.getAuth();
        assert auth != null;
        String username = (String) auth.get(constant.USERNAME);

        if(params.get("startDate")!=null && params.get("endDate")!=null){
            String startstr = params.get("startDate");
            String endstr = params.get("endDate");

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date datastart = sdf1.parse(startstr);
            Date dataend = sdf1.parse(endstr);

            return statisticService.userSelfStatistic_BookAllBuyNum(datastart,dataend,username);
        }

        else{
            String startstr = "1000-01-01 00:00:00";
            String endstr = "9999-12-31 12:59:59";
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datastart = sdf1.parse(startstr);
            Date dataend = sdf1.parse(endstr);

            return statisticService.userSelfStatistic_BookAllBuyNum(datastart,dataend,username);
        }
    }

    @RequestMapping("/statistic/userStatistic/bookWithBuyNum")
    public JSONArray userbookWithBuyNum(@RequestBody Map<String, String> params) throws ParseException {
        JSONObject auth = SessionUtil.getAuth();
        assert auth != null;
        String username = (String) auth.get(constant.USERNAME);

        if(params.get("startDate")!=null && params.get("endDate")!=null){
            String startstr = params.get("startDate");
            String endstr = params.get("endDate");

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date datastart = sdf1.parse(startstr);
            Date dataend = sdf1.parse(endstr);

            return statisticService.userSelfStatistic_BookWithBuyNum(datastart,dataend,username);
        }

        else{
            String startstr = "1000-01-01 00:00:00";
            String endstr = "9999-12-31 12:59:59";
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datastart = sdf1.parse(startstr);
            Date dataend = sdf1.parse(endstr);

            return statisticService.userSelfStatistic_BookWithBuyNum(datastart,dataend,username);
        }
    }



    @RequestMapping("/statistic/test")
    public List<Order> test() throws ParseException {
        String startstr = "1000-01-01 00:00:00";
        String endstr = "9999-12-31 12:59:59";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datastart = sdf1.parse(startstr);
        Date dataend = sdf1.parse(endstr);


        String user = "user2";
//        return null;
        return orderItemRepository.userSelfStatistic_BookTotalPay(datastart,dataend,user);
    }


}
