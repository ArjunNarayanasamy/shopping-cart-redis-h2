package com.arjun.dbservice.cart.service;

import com.arjun.dbservice.cart.controller.model.SendToMQRequest;
import com.arjun.dbservice.cart.dao.OrderRepo;
import com.arjun.dbservice.cart.dao.ProductRepo;
import com.arjun.dbservice.cart.dao.RedisRepo;
import com.arjun.dbservice.cart.dao.UserRepo;
import com.arjun.dbservice.cart.entity.Order;
import com.arjun.dbservice.cart.entity.Product;
import com.arjun.dbservice.cart.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 *  Service which saves the order to DB
 *  In case of any DB issues, saves the message to Redis cache
 *  So we can reply the orders once the DB is up
 */
@Service
public class CartService {

    @Lazy
    @Autowired
    UserRepo userRepo;

    @Lazy
    @Autowired
    OrderRepo orderRepo;

    @Lazy
    @Autowired
    ProductRepo productRepo;

    @Lazy
    @Autowired
    RedisRepo redisRepo;

    Logger logger = LoggerFactory.getLogger(CartService.class);

    /**
     * @param mqRequest
     */
    public void processMQMessage(SendToMQRequest mqRequest) {

        try {
            if(CircuitBreaker.circuitOpen.get()) {
                this.addToRedis(mqRequest);
            } else {
                logger.info("Circuit is closed. Saving order to DB");
                User user = userRepo.getOne(mqRequest.getUserId());
                Product product = productRepo.getOne(mqRequest.getProductId());
                Order order = new Order(product.getProductId(), user.getUserId(), mqRequest.getQuantity());
                order = orderRepo.save(order);
                logger.info("Order Saved: " + order.getOrderId());
            }
        } catch (Exception e) {
            logger.error("Connection failed to DB, Updating CircuitBreaker status to OPEN.");
            this.addToRedis(mqRequest);
            CircuitBreaker.circuitOpen.set(true);
        }
    }

    public void addToRedis(SendToMQRequest request) {
        logger.error("Circuit is open, Saving the order to Redis cache.");
        redisRepo.addItem(request);
    }

    public boolean checkDB() {
        boolean status = false;
        try {
            User user = userRepo.getOne("US001");
            if(!user.equals(null)) {
                status = true;
            }
        } catch (Exception e) {
            status = false;
        }
        return status;
    }

}
