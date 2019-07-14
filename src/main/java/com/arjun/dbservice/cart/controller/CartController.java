package com.arjun.dbservice.cart.controller;

import com.arjun.dbservice.cart.controller.model.SendToMQRequest;
import com.arjun.dbservice.cart.config.RabbitConfig;
import com.arjun.dbservice.cart.dao.RedisRepo;
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
    RedisRepo redisRepo;

    /* sends message to MQ */
    @PostMapping("/data")
    ResponseEntity sendDataToMQ(@RequestBody SendToMQRequest request) throws JsonProcessingException {
       String dataToSend = mapper.writeValueAsString(request);
       rabbitTemplate.convertAndSend(RabbitConfig.topicExchangeName, "dbs.cart.baz", dataToSend);
       return generateResponse("Data send to MQ successfully", HttpStatus.OK);
    }

    /* To get all the order items in the redis cache */
    @RequestMapping("/cache/getAllItems")
    @ResponseBody
    public ResponseEntity<Map<String, SendToMQRequest>> getAllItems(){
        Map<String, SendToMQRequest> items =  redisRepo.getAllItems();
        return new ResponseEntity<Map<String, SendToMQRequest>>(items, HttpStatus.OK);
    }

    @RequestMapping("/cache/getNoOfItems")
    @ResponseBody
    public ResponseEntity getTotalRecordsInCache() {
        Long total = redisRepo.getRecordCount();
        Map<String, Long> response = new HashMap<>();
        response.put("TotalRecords", total);
        return new ResponseEntity<Map<String, Long>>(response, HttpStatus.OK);
    }

    private ResponseEntity generateResponse(String message, HttpStatus status) {
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return new ResponseEntity<>(map, status);
    }
}
