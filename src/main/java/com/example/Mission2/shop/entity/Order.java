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
    private Integer amount;     //주문 수량
    @Setter
    private String rejectReason;      //구매 요청 거절 이유
    @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus status;     //주문 상태
    @Setter
    private LocalDateTime createdAt;    //주문 시간
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Goods goods;                //주문 상품
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Shop shop;                 //판매자
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity customer;        //구매자

    private String tossPaymentKey;
    private String tossOrderId;
}
