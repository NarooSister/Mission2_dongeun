package com.example.Mission2.entity;

import jakarta.persistence.*;

@Entity
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;            //상품 이름
    private String imageUrl;        //이미지 URL
    private String description;     //상품 설명
    private Integer price;          //상품 가격
    private Integer stock;          //상품 재고

    @ManyToOne
    private Shop shop;
}
