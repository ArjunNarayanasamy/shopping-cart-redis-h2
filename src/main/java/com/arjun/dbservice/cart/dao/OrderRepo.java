package com.arjun.dbservice.cart.dao;

import com.arjun.dbservice.cart.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepo extends JpaRepository<Order, String> {

    @Query(value = "SELECT 1", nativeQuery = true)
    Integer checkDbStatus();

}
