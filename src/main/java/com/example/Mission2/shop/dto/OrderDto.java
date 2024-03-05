package com.example.Mission2.shop.dto;

import com.example.Mission2.shop.entity.Order;
import com.example.Mission2.shop.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private Integer amount;     //주문 수량
    private String rejectReason;      //구매 요청 거절 이유
    private OrderStatus status;     //주문 상태
    private LocalDateTime createdAt;    //주문시간

    private String tossPaymentKey;
    private String tossOrderId;


    public static OrderDto fromEntity(Order order){
        return OrderDto.builder()
                .id(order.getId())
                .amount(order.getAmount())
                .rejectReason(order.getRejectReason())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .tossPaymentKey(order.getTossPaymentKey())
                .tossOrderId(order.getTossOrderId())
                .build();
    }
}
