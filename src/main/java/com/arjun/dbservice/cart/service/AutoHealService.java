package com.arjun.dbservice.cart.service;

import com.arjun.dbservice.cart.controller.model.SendToMQRequest;
import com.arjun.dbservice.cart.dao.RedisRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class AutoHealService implements Runnable {

    @Autowired
    RedisRepo redisRepo;

    @Autowired
    CartService cartService;

    @Override
    public void run() {
        if(redisRepo.getRecordCount()>0) {
            boolean status = cartService.checkDB();
            if(!status) {
                // circuit breaker needs to be applied
                System.out.println("db is down");
            } else {
                System.out.println("taking from cache, putting it in db");
                Set<String> keysFromCache = redisRepo.getKeysFromRedis();
                Map<String, SendToMQRequest> ordersFromCache = redisRepo.getAllItems();
                ordersFromCache.entrySet().stream()
                        .forEach( order -> {
                            cartService.processMQMessage(order.getValue());
                            redisRepo.deleteItem(order.getKey());
                        });
            }
        }
    }
}
