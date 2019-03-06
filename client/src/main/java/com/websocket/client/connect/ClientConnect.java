package com.websocket.client.connect;

import com.websocket.client.websocket.Client;
import org.apache.tomcat.websocket.WsContainerProvider;
import org.apache.tomcat.websocket.WsSession;
import org.apache.tomcat.websocket.WsWebSocketContainer;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.client.standard.WebSocketContainerFactoryBean;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.support.SockJsHttpRequestHandler;
import org.springframework.web.socket.sockjs.transport.SockJsSession;
import org.springframework.web.socket.sockjs.transport.handler.SockJsWebSocketHandler;

import javax.websocket.ContainerProvider;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

/**
 * @Description:
 * @Author: 52331
 * @CreateTime: 2019-03-06 20:44
 */
public class ClientConnect {

    public static void main(String[] args) {
        //stomp();
        container();
    }

    private static void container() {
        try {
            WebSocketContainer webSocketContainer = WsContainerProvider.getWebSocketContainer();
            String uri = "ws://192.168.0.107:8081/websocket";
            //String uri = "ws://192.168.0.107:8081/websocket/queue/receive";
            Session session = webSocketContainer.connectToServer(Client.class, new URI(uri));// 连接会话
            // 发送文本消息
            RemoteEndpoint.Async remote = session.getAsyncRemote();
            StompHeaders headers = new StompHeaders();
            headers.setDestination("/queue/receive");
            headers.add("Content-Type","application/json");
            headers.add("Content-Length","0");
            String message = headers.toString();
            remote.sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stomp(){
        ArrayList<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));

        SockJsClient sockjsClient = new SockJsClient(transports);

        WebSocketStompClient stompClient = new WebSocketStompClient(sockjsClient);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

       // stompClient.setTaskScheduler(new ConcurrentTaskScheduler());

        StompSessionHandler sessionHandler = new SessionHandler();
        ListenableFuture<StompSession> connect = stompClient.connect("ws://192.168.0.107:8081/websocket", sessionHandler);
        CompletableFuture<StompSession> completable = connect.completable();
        StompSession join = completable.join();
        join.send("/queue/receive","123");

    }

    private static class SessionHandler implements StompSessionHandler {
        @Override
        public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
            System.out.println("---------------------------");
            System.out.println("----------connected!---------");
            System.out.println("---------------------------");
        }

        @Override
        public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
            System.out.println("------------");
            System.out.println("handleException");
            System.out.println("------------");
        }

        @Override
        public void handleTransportError(StompSession stompSession, Throwable throwable) {
            System.out.println("-----------------");
            System.out.println("handleTransportError");
            System.out.println("-----------------");
        }

        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return String.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            System.out.println("----------");
            System.out.println("handleFrame");
            System.out.println("----------");
        }
    }
}
