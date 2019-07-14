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

        Order order = null;
        try {
            User user = userRepo.getOne(mqRequest.getUserId());
            Product product = productRepo.getOne(mqRequest.getProductId());
            order = new Order(product.getProductId(), user.getUserId(), mqRequest.getQuantity());
            order = orderRepo.save(order);
            logger.info("Order Saved: " + order.getOrderId());
        } catch (Exception e) {
            logger.error("Couldn't save order: {} , Saving the order to cache.");
            redisRepo.addItem(mqRequest);
        }

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
