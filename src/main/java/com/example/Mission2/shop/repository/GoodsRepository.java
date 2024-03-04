package com.example.Mission2.shop.repository;

import com.example.Mission2.shop.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public interface GoodsRepository extends JpaRepository<Goods, Long> {

    //이름으로 검색
    List<Goods> findAllByName(String name);

    //가격 범위를 기준으로 상품을 검색
    List<Goods> findAllByPriceBetween(Integer minPrice, Integer maxPrice);

    // shopId와 goodsId로 검색
    Optional<Goods> findByIdAndShopId(Long goodsId, Long shopId);

    //shopId로 검색
    List<Goods> findByShopId(Long shopId);
}
