package com.example.Mission2.repository;

import com.example.Mission2.entity.Shop;
import com.example.Mission2.entity.ShopCategory;
import com.example.Mission2.entity.ShopStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    //가장 최근에 거래가 있었던 쇼핑몰 순서로 조회
    @Query("SELECT s FROM Shop s LEFT JOIN FETCH s.orders o ORDER BY o.createdAt DESC")
    List<Shop> findAllByRecentOrder();

    //이름으로 검색
    List<Shop> findAllByName(String name);

    //분류로 검색
    List<Shop> findAllByCategory(ShopCategory category);

    //개설, 폐쇄 신청 쇼핑몰 보기
    List<Shop> findAllByStatus(ShopStatus status);

    //개설, 폐쇄 신청 쇼핑몰 한개 보기
    Shop findByIdAndStatus(Long shopId, ShopStatus status);

}
