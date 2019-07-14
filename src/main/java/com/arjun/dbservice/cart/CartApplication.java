package com.arjun.dbservice.cart;

import com.arjun.dbservice.cart.controller.model.SendToMQRequest;
import com.arjun.dbservice.cart.service.AutoHealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.concurrent.*;

/**
 * Entry point of Application
 */
@SpringBootApplication
@EnableCaching
public class CartApplication {

	@Autowired
	AutoHealService autoHealService;

	ScheduledExecutorService scheduler
			= Executors.newSingleThreadScheduledExecutor();

	public static void main(String[] args) {
		SpringApplication.run(CartApplication.class, args);
	}

	/* For simplicity defined the redis beans here in main class itself */
	@Bean
	JedisConnectionFactory jedisConnectionFactory(){
		return new JedisConnectionFactory();
	}

	@Bean
	RedisTemplate<String, SendToMQRequest> redisTemplate() {
		RedisTemplate<String,SendToMQRequest> redisTemplate = new RedisTemplate<String, SendToMQRequest>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}

	/* Calling self-healing service at scheduled interval
	* to keep an eye on DB connection and Cache entries */
	@PostConstruct
	public void startDBCheck() {
		scheduler.scheduleWithFixedDelay(autoHealService, 5, 5, TimeUnit.SECONDS);
	}

}
