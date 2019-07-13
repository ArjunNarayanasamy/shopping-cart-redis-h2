package com.arjun.dbservice.cart.service;

import com.arjun.dbservice.cart.controller.model.SendToMQRequest;
import com.arjun.dbservice.cart.dao.OrderRepo;
import com.arjun.dbservice.cart.dao.ProductRepo;
import com.arjun.dbservice.cart.dao.RedisRepository;
import com.arjun.dbservice.cart.dao.UserRepo;
import com.arjun.dbservice.cart.entity.Order;
import com.arjun.dbservice.cart.entity.Product;
import com.arjun.dbservice.cart.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    RedisRepository redisRepo;

    public void processMQMessage(SendToMQRequest mqRequest) {

        User user = userRepo.getOne(mqRequest.getUserId());
        Product product = productRepo.getOne(mqRequest.getProductId());
        Order order = null;
        try {
            order = new Order(product.getProductId(), user.getUserId(), mqRequest.getQuantity());
            order = orderRepo.save(order);
            System.out.println("Order Saved: " + order.getOrderId());
        } catch (Exception e) {
            System.out.println("Couldn't save order: "+ order.getOrderId());
            redisRepo.addItem(mqRequest);
        }

    }

}
