package com.example.Mission2.shop.repository;

import com.example.Mission2.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
