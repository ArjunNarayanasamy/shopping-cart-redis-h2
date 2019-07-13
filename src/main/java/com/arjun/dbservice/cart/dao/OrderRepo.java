package com.arjun.dbservice.cart.dao;

import com.arjun.dbservice.cart.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, String> {
}
