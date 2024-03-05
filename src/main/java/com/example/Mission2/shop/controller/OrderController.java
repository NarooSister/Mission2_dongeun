package com.example.Mission2.shop.controller;

import com.example.Mission2.shop.dto.OrderDto;
import com.example.Mission2.shop.dto.PaymentCancelDto;
import com.example.Mission2.shop.dto.PaymentConfirmDto;
import com.example.Mission2.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    //CREATE
    //구매자가 구매 요청하기
    @PostMapping("/shop/{shopId}/goods/{goodsId}/orders/create")
    public OrderDto create(
            @PathVariable("shopId") Long shopId,
            @PathVariable("goodsId") Long goodsId,
            @RequestBody OrderDto dto
    ){
        return service.createOrder(shopId, goodsId, dto);
    }


    //READ
    //쇼핑몰 주인이 구매 요청 확인 하기
    @GetMapping("/shop/{shopId}/goods/{goodsId}/orders/read")
    public List<OrderDto> readAll(
            @PathVariable("shopId") Long shopId,
            @PathVariable("goodsId") Long goodsId
    ) {
        return service.readOrderAll(shopId, goodsId);
    }


    //UPDATE
    //쇼핑몰 주인이 구매 요청을 수락 또는 거절
    @PostMapping("/shop/{shopId}/goods/{goodsId}/orders/{orderId}")
    public OrderDto acceptOrder(
            @PathVariable("shopId") Long shopId,
            @PathVariable("goodsId") Long goodsId,
            @PathVariable("orderId") Long orderId,
            //true: 수락, false: 거절
            @RequestParam Boolean accept,
            //거절 이유 작성
            @RequestBody(required = false) OrderDto dto
    ) {
        return service.acceptOrder(shopId, goodsId, orderId, accept, dto);
    }

    //DELETE
    //구매자가 구매 요청 취소
    @DeleteMapping("/orders/{orderId}")
    public void delete(
            @PathVariable("orderId") Long orderId
    ) {
        service.cancelOrder(orderId);
    }




//    //DELETE
//    //구매자가 구매 요청 취소
//    @DeleteMapping("{orderId}")
//    public Object delete(
//            @PathVariable("orderId") Long orderId,
//            @RequestBody
//            PaymentCancelDto dto
//    ) {
//        return service.cancelOrder(orderId, dto);
//    }

//    @GetMapping("{orderId}/payment")
//    public Object readTossPayment(
//            @PathVariable("orderId")
//            Long id
//    ) {
//        return service.readTossPayment(id);
//    }
//    @PostMapping("{orderId}/cancel")
//    public Object cancelPayment(
//            @PathVariable("orderId")
//            Long id,
//            @RequestBody
//            PaymentCancelDto dto
//    ) {
//        return service.cancelPayment(id, dto);
//    }

}
