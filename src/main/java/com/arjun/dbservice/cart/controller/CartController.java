package com.arjun.dbservice.cart.controller;

import com.arjun.dbservice.cart.controller.model.SendToMQRequest;
import com.arjun.dbservice.cart.config.RabbitConfig;
import com.arjun.dbservice.cart.dao.RedisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CartController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    RedisRepository redisRepo;

    @PostMapping("/data")
    ResponseEntity sendDataToMQ(@RequestBody SendToMQRequest request) throws JsonProcessingException {
       String dataToSend = mapper.writeValueAsString(request);
       rabbitTemplate.convertAndSend(RabbitConfig.topicExchangeName, "foo.bar.baz", dataToSend);
       return generateResponse("Data send to MQ successfully", HttpStatus.OK);
    }

    @RequestMapping("/getAllItems")
    @ResponseBody
    public ResponseEntity<Map<String, SendToMQRequest>> getAllItems(){
        Map<String,SendToMQRequest> items =  redisRepo.getAllItems();
        return new ResponseEntity<Map<String, SendToMQRequest>>(items, HttpStatus.OK);
    }

    private ResponseEntity generateResponse(String message, HttpStatus status) {
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return new ResponseEntity<>(map, status);
    }
}
