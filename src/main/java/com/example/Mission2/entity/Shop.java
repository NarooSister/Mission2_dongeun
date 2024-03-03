package com.example.Mission2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;            //이름
    private String description;     //소개
    private String reason;          //개설 불허 이유

    @Enumerated(EnumType.STRING)
    private ShopCategory category;        //분류

    @Enumerated(EnumType.STRING)
    private ShopStatus status;        //상태

    @OneToOne
    private UserEntity owner;

    @OneToMany(mappedBy = "shop")
    private List<Goods> goods = new ArrayList<>();

    @OneToMany(mappedBy = "shop")
    private List<Order> orders = new ArrayList<>();
}
