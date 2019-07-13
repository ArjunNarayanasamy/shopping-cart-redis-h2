package com.arjun.dbservice.cart.dao;

import com.arjun.dbservice.cart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, String> {
}
