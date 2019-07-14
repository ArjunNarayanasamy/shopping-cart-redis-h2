package com.arjun.dbservice.cart.service;

import com.arjun.dbservice.cart.dao.OrderRepo;
import com.arjun.dbservice.cart.dao.RedisRepo;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import org.mockito.InjectMocks;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class CircuitBreakerTest {

    @InjectMocks
    CircuitBreaker circuitBreaker;
    @Mock
    RedisRepo redisRepo;
    @Mock
    CartService cartService;
    @Mock
    OrderRepo orderRepo;

    Logger logger = LoggerFactory.getLogger(CircuitBreakerTest.class);

    public static AtomicBoolean circuitOpen = new AtomicBoolean(false);

    @BeforeTest(groups = "circuit-breaker-tests")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(groups = {"circuit-breaker-tests"})
    public void checkCircuitStateTestClosed() {
        CircuitBreaker.circuitOpen.set(false);
        circuitBreaker.checkCircuitState();
        logger.info("Testing if getRecordCounts execution when Circuit is closed (i.e DB is UP)");
        verify (redisRepo,times(1)).getRecordCount();
    }

    @Test(groups = {"circuit-breaker-tests"})
    public void checkCircuitStateTestOpen() {
        CircuitBreaker.circuitOpen.set(true);
        System.out.println(CircuitBreaker.circuitOpen.get());
        circuitBreaker.checkCircuitState();
        logger.info("Testing if checkDBStatus execution when Circuit is OPEN (i.e DB is down)");
        verify (orderRepo, times(1)).checkDbStatus();
    }

}
