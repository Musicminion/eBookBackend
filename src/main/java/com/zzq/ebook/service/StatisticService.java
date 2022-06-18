package com.zzq.ebook.service;

import net.sf.json.JSONArray;

import java.util.Date;

public interface StatisticService {

    public JSONArray userConsumeStatistic(Date starttime, Date endtime);


    public JSONArray bookSellnumStatistic(Date starttime, Date endtime);





    JSONArray userSelfStatistic_BookWithBuyNum(Date starttime, Date endtime, String username);

    JSONArray userSelfStatistic_BookAllBuyNum(Date starttime, Date endtime, String username);

    JSONArray userSelfStatistic_BookTotalPay(Date starttime, Date endtime, String username);

}
