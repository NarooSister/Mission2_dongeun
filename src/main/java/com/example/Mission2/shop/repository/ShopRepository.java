package com.example.Mission2.shop.repository;

import com.example.Mission2.shop.entity.Shop;
import com.example.Mission2.shop.entity.ShopCategory;
import com.example.Mission2.shop.entity.ShopStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    //가장 최근에 거래가 있었던 쇼핑몰 순서로 조회
    @Query("SELECT DISTINCT s FROM Shop s LEFT JOIN FETCH s.orders o ORDER BY o.createdAt DESC")
    List<Shop> findAllByRecentOrder();

    //이름으로 검색
    List<Shop> findAllByName(String name);

    //분류로 검색
    List<Shop> findAllByCategory(ShopCategory category);

    //주인 아이디로 찾기
    Optional<Shop> findByOwnerId(Long ownerId);

    //admin - status로 쇼핑몰 찾기
    List<Shop> findAllByStatus(ShopStatus status);



}
