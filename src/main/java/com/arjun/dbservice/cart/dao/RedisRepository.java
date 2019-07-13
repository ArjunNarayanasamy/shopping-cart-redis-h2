package com.arjun.dbservice.cart.dao;

import com.arjun.dbservice.cart.controller.model.SendToMQRequest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class RedisRepository {

    public static final String KEY = "ITEM";
    private RedisTemplate<String, SendToMQRequest> redisTemplate;
    private HashOperations hashOperations;

    public RedisRepository(RedisTemplate<String, SendToMQRequest> redisTemplate) {
        this.redisTemplate = redisTemplate;
        hashOperations = redisTemplate.opsForHash();
    }

    /*Getting all Items from tSable*/
    public Map<String,SendToMQRequest> getAllItems(){
        return hashOperations.entries(KEY);
    }

    /*Getting a specific item by item id from table*/
    public SendToMQRequest getItem(int itemId){
        return (SendToMQRequest) hashOperations.get(KEY,itemId);
    }

    /*Adding an item into redis database*/
    public void addItem(SendToMQRequest item){
        hashOperations.put(KEY,item.getProductId()+"_"+item.getUserId(),item);
    }

    /*delete an item from database*/
    public void deleteItem(int id){
        hashOperations.delete(KEY,id);
    }

    /*update an item from database*/
    public void updateItem(SendToMQRequest item){
        addItem(item);
    }
}
