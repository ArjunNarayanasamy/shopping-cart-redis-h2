package com.arjun.dbservice.cart.service;

import com.arjun.dbservice.cart.controller.model.SendToMQRequest;
import com.arjun.dbservice.cart.dao.OrderRepo;
import com.arjun.dbservice.cart.dao.ProductRepo;
import com.arjun.dbservice.cart.dao.UserRepo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CartServiceTest {

    @Mock
    UserRepo userRepo;

    @Mock
    OrderRepo orderRepo;

    @Mock
    ProductRepo productRepo;

    @Mock
    CartService cartService;

    SendToMQRequest mq;

    @BeforeTest(groups = "cart-service-tests")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mq = new SendToMQRequest("PR001", "US001", 12);
    }

    @Test(groups = "cart-service-tests")
    public void processMQMessageTest() {
        CircuitBreaker.circuitOpen.set(true);
        System.out.println(CircuitBreaker.circuitOpen.get());
        cartService.processMQMessage(mq);
        verify (cartService,times(1)).processMQMessage(mq);
        CircuitBreaker.circuitOpen.set(false);
        System.out.println(CircuitBreaker.circuitOpen.get());
    }
}
