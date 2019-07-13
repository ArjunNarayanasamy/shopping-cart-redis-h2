package com.arjun.dbservice.cart.service.mq;

import com.arjun.dbservice.cart.controller.model.SendToMQRequest;
import com.arjun.dbservice.cart.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Receiver {

    @Autowired
    CartService cartService;

    @Autowired
    ObjectMapper mapper;

    public void receiveMessage(String message) throws IOException {
        System.out.println("Received <" + message + ">");
        SendToMQRequest mqRequest = mapper.readValue(message, SendToMQRequest.class);
        cartService.processMQMessage(mqRequest);
    }
}
