package com.arjun.dbservice.cart.service;

import com.arjun.dbservice.cart.dao.RedisRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

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
                System.out.println("db is down");
            } else {
                System.out.println("taking from cache, putting it in db");
            }
        }
    }

}
