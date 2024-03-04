package com.example.Mission2.shop.entity;

import com.example.Mission2.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

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
    @Setter
    private String name;            //이름
    @Setter
    private String description;     //소개
    @Setter
    private String rejectReason;    //개설 불허 이유
    @Setter
    private String closeReason;     //폐쇄 요청 사유
    @Setter
    @Enumerated(EnumType.STRING)
    private ShopCategory category;        //분류
    @Setter
    @Enumerated(EnumType.STRING)
    private ShopStatus status;        //상태

    @Setter
    @OneToOne
    private UserEntity owner;

//    @OneToMany(mappedBy = "shop")
//    private List<Goods> goods = new ArrayList<>();

    @OneToMany
    private List<Order> orders = new ArrayList<>();
}
