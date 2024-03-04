package com.example.Mission2.shop.entity;

import com.example.Mission2.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_table")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private Integer amount;     //구매 수량
    @Setter
    private String reason;      //구매 요청 거절 이유
    @Setter
    private LocalDateTime createdAt;    //주문 시간

    @ManyToOne(fetch = FetchType.LAZY)
    private Goods goods;

    @ManyToOne(fetch = FetchType.LAZY)
    private Shop owner;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity customer;
}
