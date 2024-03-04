package com.example.Mission2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer amount;     //구매 수량
    private String reason;      //구매 요청 거절 이유
    private LocalDateTime createdAt;    //주문 시간

    @ManyToOne(fetch = FetchType.LAZY)
    private Goods goods;

    @ManyToOne(fetch = FetchType.LAZY)
    private Shop owner;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity customer;
}
