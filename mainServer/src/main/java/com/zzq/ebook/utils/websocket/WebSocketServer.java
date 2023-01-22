package com.zzq.ebook.utils.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@ServerEndpoint("/websocket/transfer/{userId}")
@Component
public class WebSocketServer {

    public WebSocketServer() {
        //每当有一个连接，都会执行一次构造方法
        System.out.println("新的连接已经开启");
    }

    private static final AtomicInteger COUNT = new AtomicInteger();

    private static final ConcurrentHashMap<String, Session> SESSIONS = new ConcurrentHashMap<>();

    public int sendMessage(Session toSession, String message) {
        if (toSession != null) {
            try {
                toSession.getBasicRemote().sendText(message);
                return 0;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("对方不在线");
            return 1;
        }
        return 1;
    }

    public void sendMessageToUser(String user, String message) throws InterruptedException {
        System.out.println( "sendMessageToUser output  " + user);
        for (int i = 0; i < 10; i++){
            Session toSession = SESSIONS.get(user);
            if(sendMessage(toSession, message) == 0)
                return;
            Thread.sleep(1000);
        }
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("服务器收到消息：" + message);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        if (SESSIONS.get(userId) != null) {
            return;
        }
        SESSIONS.put(userId, session);
        COUNT.incrementAndGet();
        System.out.println(userId + "加入Websocket连接，当前在线人数：" + COUNT);

    }

    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        SESSIONS.remove(userId);
        COUNT.decrementAndGet();
        System.out.println(userId + "推出Websocket连接，当前在线人数：" + COUNT);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("发生错误");
        throwable.printStackTrace();
    }
}



