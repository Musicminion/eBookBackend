package com.zzq.ebook.controller;


import com.zzq.ebook.constant.constant;
import com.zzq.ebook.entity.Order;
import com.zzq.ebook.repository.OrderItemRepository;
import com.zzq.ebook.repository.OrderRepository;
import com.zzq.ebook.service.StatisticService;
import com.zzq.ebook.utils.message.MsgCode;
import com.zzq.ebook.utils.message.MsgUtil;
import com.zzq.ebook.utils.session.SessionUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


// 说明：statistic controller负责统计相关的请求
// -------------------------------------接-----------口-----------表-----------------------------------------------------
//          接口名称                                            接口说明：
//                                                  [系 统 全 局 数 据 统 计]
//      /statistic/userConsume                      全局数据统计：统计指定时间段 统计所有用户的消费情况
//      /statistic/bookSellnum                      全局数据统计：统计指定时间段 统计所有书籍的销量
//                                                  [用 户 个 人 数 据 统 计]
//      /statistic/userStatistic/bookTotalPay       用户个人数据统计：统计指定时间段 消费的金额钱数
//      /statistic/userStatistic/bookAllBuyNum      用户个人数据统计：统计指定时间段 书买了多少本（所有订单一共买了多少本书）
//      /statistic/userStatistic/bookWithBuyNum     用户个人数据统计：统计指定时间段 书籍以及购买数量的信息（数组）
//  注释：如果没有传入参数，默认统计所有的时间 用户个人数据统计的用户通过session读取

@RestController
public class statisticControl {

    @Autowired
    private StatisticService statisticService;

    // 函数用途：统计![所有]!用户的消费数据，如果请求的时候说明了开始和终止日期，就按照设定，否则就是所有时间段
    //         返回的格式是数组，以 [用户：消费金额] 的形式呈现
    // 使用场景：管理员 统计用户的消费数据
    // 权限要求：管理员（权限号-0）
    @RequestMapping("/statistic/userConsume")
    public JSONArray userConsumeData(@RequestBody Map<String, String> params) throws ParseException {
        JSONObject auth = SessionUtil.getAuth();
        if(auth == null || !Objects.equals(auth.get(constant.PRIVILEGE),0))
            return null;

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
            String endstr = "9999-12-31 23:59:59";
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datastart = sdf1.parse(startstr);
            Date dataend = sdf1.parse(endstr);
            return statisticService.userConsumeStatistic(datastart,dataend);
        }
    }

    // 函数用途：统计![所有]!用户的购买书籍的数量，如果请求的时候说明了开始和终止日期，就按照设定，否则就是所有时间段
    //         返回的格式是数组，以 [用户：买的书籍本数] 的形式呈现
    // 使用场景：管理员 统计用户的购买书籍的数量
    // 权限要求：管理员（权限号-0）
    @RequestMapping("/statistic/bookSellnum")
    public JSONArray bookSellnum(@RequestBody Map<String, String> params) throws ParseException {
        JSONObject auth = SessionUtil.getAuth();
        if(auth == null || !Objects.equals(auth.get(constant.PRIVILEGE),0))
            return null;

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
            String endstr = "9999-12-31 23:59:59";
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datastart = sdf1.parse(startstr);
            Date dataend = sdf1.parse(endstr);
            return statisticService.bookSellnumStatistic(datastart,dataend);
        }
    }

    // 函数用途：统计当前用户在指定的时间段消费的金额数量，返回的格式是 [花费金额数]
    // 使用场景：用户自己统计自己花费的金额数量
    // 权限要求：登录的用户（权限号-2）
    @RequestMapping("/statistic/userStatistic/bookTotalPay")
    public JSONArray userBookTotalPay(@RequestBody Map<String, String> params) throws ParseException {
        JSONObject auth = SessionUtil.getAuth();
        if(auth == null)
            return null;
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
            String endstr = "9999-12-31 23:59:59";
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datastart = sdf1.parse(startstr);
            Date dataend = sdf1.parse(endstr);

            return statisticService.userSelfStatistic_BookTotalPay(datastart,dataend,username);
        }

    }

    // 函数用途：统计当前用户在指定的时间段买了的书数量，返回的格式是 [买了的书数量]
    // 使用场景：用户自己统计自己买了多少本书
    // 权限要求：登录的用户（权限号-2）
    @RequestMapping("/statistic/userStatistic/bookAllBuyNum")
    public JSONArray userBookAllBuyNum(@RequestBody Map<String, String> params) throws ParseException {
        JSONObject auth = SessionUtil.getAuth();
        if(auth == null)
            return null;
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
            String endstr = "9999-12-31 23:59:59";
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datastart = sdf1.parse(startstr);
            Date dataend = sdf1.parse(endstr);

            return statisticService.userSelfStatistic_BookAllBuyNum(datastart,dataend,username);
        }
    }


    // 函数用途：统计当前用户在指定的时间段买书情况，返回的格式是，[书名：本数]
    // 使用场景：用户自己统计自己买书情况：什么书买了多少本
    // 权限要求：登录的用户（权限号-2）
    @RequestMapping("/statistic/userStatistic/bookWithBuyNum")
    public JSONArray userbookWithBuyNum(@RequestBody Map<String, String> params) throws ParseException {
        JSONObject auth = SessionUtil.getAuth();
        if(auth == null)
            return null;
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
            String endstr = "9999-12-31 23:59:59";
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datastart = sdf1.parse(startstr);
            Date dataend = sdf1.parse(endstr);

            return statisticService.userSelfStatistic_BookWithBuyNum(datastart,dataend,username);
        }
    }
}
