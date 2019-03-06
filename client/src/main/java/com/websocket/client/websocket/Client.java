package com.websocket.client.websocket;


import javax.websocket.*;

/**
 * @Description:
 * @Author: 52331
 * @CreateTime: 2019-03-06 20:11
 */
@ClientEndpoint
public class Client {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to endpoint: " + session.getBasicRemote());
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println(message);
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @OnClose
    public void onClose(){
        System.out.println("WebSocket Close");
    }

}
