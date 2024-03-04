package com.example.Mission2.shop.repository;

import com.example.Mission2.shop.dto.OrderDto;
import com.example.Mission2.shop.entity.Order;
import com.example.Mission2.shop.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByShopIdAndGoodsIdAndStatus(Long shopId, Long goodsId, OrderStatus status);
}
