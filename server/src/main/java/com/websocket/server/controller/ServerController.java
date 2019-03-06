package com.websocket.server.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * @Description:
 * @Author: 52331
 * @CreateTime: 2019-03-06 19:58
 */
@Controller
public class ServerController {

    @MessageMapping("/queue/receive")
    public void receive(String msg){
        System.out.println("----------------------");
        System.out.println("---------receive--------");
        System.out.println("----------"+msg+"------");
        System.out.println("----------------------");
    }
}
