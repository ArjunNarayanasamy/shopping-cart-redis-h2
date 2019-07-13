package com.arjun.dbservice.cart.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ORDER_TABLE")
public class Order {

    public Order() {
    }

    public Order(String productId, String userId, Integer quantity) {
        this.productId = productId;
        this.userId = userId;
        this.quantity = quantity;
        this.created = new Timestamp(System.currentTimeMillis());
        this.updated = new Timestamp(System.currentTimeMillis());
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "ORDER_ID")
    String orderId;

    @Column(name = "PRODUCT_ID")
    String productId;

    @Column(name = "USER_ID")
    String userId;

    @Column(name = "ORDER_QTY")
    Integer quantity;

    @Column(name = "CREATED")
    Timestamp created;

    @Column(name = "UPDATED")
    Timestamp updated;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }
}
