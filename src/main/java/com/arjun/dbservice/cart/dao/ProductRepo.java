package com.arjun.dbservice.cart.dao;

import com.arjun.dbservice.cart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, String> {
}
