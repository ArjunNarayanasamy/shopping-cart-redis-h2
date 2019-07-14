package com.arjun.dbservice.cart.controller.model;

import java.io.Serializable;

public class SendToMQRequest implements Serializable {
    String productId;
    String userId;
    Integer quantity;

    public SendToMQRequest() {

    }

    public SendToMQRequest(String productId, String userId, Integer quantity) {
        this.productId = productId;
        this.userId = userId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
