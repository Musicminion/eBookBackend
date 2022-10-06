package com.zzq.ebook.utils.listener;

import com.zzq.ebook.constant.constant;
import com.zzq.ebook.service.OrderService;
import com.zzq.ebook.utils.tool.ToolFunction;
import com.zzq.ebook.utils.websocket.WebSocketServer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Objects;

@Component
public class OrderListener {
    @Autowired
    private OrderService orderService;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private WebSocketServer webSocketServer;

    @KafkaListener(topics = "orderQueue", groupId = "group_topic_order")
    public void orderQueueListener(ConsumerRecord<String, String> record) throws Exception {
        System.out.println(record.value());
        Map<String,String> params = ToolFunction.mapStringToMap(record.value());
        int itemNum = (params.size() - 6) / 2 ;
        String orderFrom = params.get("orderFrom");
        String username = params.get(constant.USERNAME);
        String receivename = params.get("receivename");
        String postcode = params.get("postcode");
        String phonenumber = params.get("phonenumber");
        String receiveaddress = params.get("receiveaddress");
        int[] bookIDGroup = new int[itemNum];
        int[] bookNumGroup = new int[itemNum];

        for(int i=1; i<=itemNum; i++){
            bookIDGroup[i-1] = Integer.parseInt(params.get("bookIDGroup" + i));
            bookNumGroup[i-1] = Integer.parseInt(params.get("bookNumGroup" + i));
        }


        // 根据购买的来源，把数组交给服务层业务函数
        int result = -1;
        if(Objects.equals(orderFrom, "ShopCart")) {
            result = orderService.orderMakeFromShopCart(bookIDGroup,bookNumGroup,username,receivename,
                    postcode, phonenumber, receiveaddress,itemNum);
        }
        else if(Objects.equals(orderFrom, "DirectBuy")){
            result = orderService.orderMakeFromDirectBuy(bookIDGroup,bookNumGroup,username,receivename,
                    postcode, phonenumber, receiveaddress,itemNum);
        }

        kafkaTemplate.send("orderFinished",  record.key(), "Done Order");
    }

    @KafkaListener(topics = "orderFinished", groupId = "group_topic_order")
    public void orderFinishedListener(ConsumerRecord<String, String> record) throws InterruptedException {
        String value = record.key();
        System.out.println("orderFinishedListener 输出" + value);
        webSocketServer.sendMessageToUser(value, "Done");
    }

}
