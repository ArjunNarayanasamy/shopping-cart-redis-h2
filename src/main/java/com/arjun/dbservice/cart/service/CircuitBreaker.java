package com.arjun.dbservice.cart.service;

import com.arjun.dbservice.cart.controller.model.SendToMQRequest;
import com.arjun.dbservice.cart.dao.OrderRepo;
import com.arjun.dbservice.cart.dao.RedisRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@EnableScheduling
public class CircuitBreaker {

    public static AtomicBoolean circuitOpen = new AtomicBoolean(false);
    public static AtomicBoolean writePending = new AtomicBoolean(false);

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    RedisRepo redisRepo;

    @Autowired
    CartService cartService;

    Logger logger = LoggerFactory.getLogger(CircuitBreaker.class);

    @Scheduled(fixedDelay = 60000L)
    void checkCircuitState() {
        logger.info("Checking Circuit");
        if (circuitOpen.get()) {
            try {
                logger.info("Checking DB connection Status...");
                orderRepo.checkDbStatus();
                logger.info("DB Connection is back..");
                circuitOpen.set(false);
                logger.info("Circuit is now closed.");
            } catch (Exception e) {
                logger.info("Circuit Stll Not Open");
            }
        }
        if(!circuitOpen.get()){
            writeFromRedisToDB();
        }
    }

    void writeFromRedisToDB() {
        logger.info("Checking Cache for pending orders to write in DB");

        if (redisRepo.getRecordCount() > 0) {
            logger.info("Taking orders from Cache and Inserting into DB");
            Map<String, SendToMQRequest> ordersFromCache = redisRepo.getAllItems();
            ordersFromCache.entrySet().stream()
                    .forEach(order -> {
                        cartService.processMQMessage(order.getValue());
                        redisRepo.deleteItem(order.getKey());
                    });
        }
    }
}
