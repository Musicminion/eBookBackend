package com.zzq.ebook.utils.listener;

import com.zzq.ebook.constant.constant;
import com.zzq.ebook.service.OrderService;
import com.zzq.ebook.utils.tool.ToolFunction;
import com.zzq.ebook.utils.websocket.WebSocketServer;
import net.sf.json.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Objects;

import static com.zzq.ebook.utils.tool.ToolFunction.getSHA256StrJava;

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

        JSONObject respData = new JSONObject();
        // 根据购买的来源，把数组交给服务层业务函数
        try {
            int result = -1;
            if(Objects.equals(orderFrom, "ShopCart")) {
                result = orderService.orderMakeFromShopCart(bookIDGroup,bookNumGroup,username,receivename,
                        postcode, phonenumber, receiveaddress,itemNum);

            }
            else if(Objects.equals(orderFrom, "DirectBuy")){
                result = orderService.orderMakeFromDirectBuy(bookIDGroup,bookNumGroup,username,receivename,
                        postcode, phonenumber, receiveaddress,itemNum);
            }
            else {
                respData.put(constant.WEBSOCKET_MSG_CODE,constant.WEBSOCKET_MSG_CODE_Info_Error);
                respData.put(constant.WEBSOCKET_MSG_Info,constant.OrderDeal_MSG_ERROR_POST_PARAMETER);
            }
            respData.put(constant.WEBSOCKET_MSG_CODE,constant.WEBSOCKET_MSG_CODE_Info_Success);
            respData.put(constant.WEBSOCKET_MSG_Info,constant.OrderDeal_MSG_Success);

        }catch (Exception e){
            respData.put(constant.WEBSOCKET_MSG_CODE,constant.WEBSOCKET_MSG_CODE_Info_Error);
            respData.put(constant.WEBSOCKET_MSG_Info,constant.OrderDeal_MSG_ERROR_TRAINSITION);
        }

        kafkaTemplate.send("orderFinished",  getSHA256StrJava(username), respData.toString());
    }

    @KafkaListener(topics = "orderFinished", groupId = "group_topic_order")
    public void orderFinishedListener(ConsumerRecord<String, String> record) throws InterruptedException {
        String key = record.key();
        System.out.println(key);
        webSocketServer.sendMessageToUser(key, record.value());
    }

}
