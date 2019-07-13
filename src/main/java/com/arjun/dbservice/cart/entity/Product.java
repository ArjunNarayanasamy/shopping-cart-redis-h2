package com.arjun.dbservice.cart.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "PRODUCT_TABLE")
public class Product {

    @Id
    @Column(name = "PRODUCT_ID")
    String productId;

    @Column(name = "PRODUCT_NAME")
    String productName;

    @Column(name = "MANUFACTURER_ID")
    String manufacturerId;

    @Column(name = "CREATED")
    Timestamp created;

    @Column(name = "UPDATED")
    Timestamp update;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdate() {
        return update;
    }

    public void setUpdate(Timestamp update) {
        this.update = update;
    }
}
