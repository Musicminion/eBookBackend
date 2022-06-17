package com.zzq.ebook.controller;


import com.zzq.ebook.repository.OrderItemRepository;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class statisticControl {


    @Autowired
    private OrderItemRepository orderItemRepository;


    @RequestMapping("/statistic/userConsume")
    public JSONArray userConsumeData() throws ParseException {
//
//        return null;
        String startstr = "2000-06-01";
        String endstr = "2025-06-01";

        String string1 = "2022-06-01 19:23:16";
        String string2 = "2029-03-19 19:23:16";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datastart = sdf1.parse(string1);
        Date dataend = sdf1.parse(string2);



//        return null;
        return orderItemRepository.userConsumeStatistic(datastart,dataend);
    }
}
