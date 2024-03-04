package com.example.Mission2.shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Column(nullable = false)
    private String name;            //상품 이름
    @Setter
    @Column(nullable = false)
    private String imageUrl;        //이미지 URL
    @Setter
    @Column(nullable = false)
    private String description;     //상품 설명
    @Setter
    @Column(nullable = false)
    private Integer price;          //상품 가격
    @Setter
    @Column(nullable = false)
    private Integer stock;          //상품 재고

    @ManyToOne
    private Shop shop;
}
