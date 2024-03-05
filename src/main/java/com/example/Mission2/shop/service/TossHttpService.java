package com.example.Mission2.shop.service;

import com.example.Mission2.shop.dto.PaymentCancelDto;
import com.example.Mission2.shop.dto.PaymentConfirmDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/payments")
public interface TossHttpService {
    @PostExchange("/confirm")
    Object confirmPayment(@RequestBody PaymentConfirmDto dto);

    @GetExchange("/{paymentKey}")
    Object getPayment(
            @PathVariable("paymentKey")
            String paymentKey
    );

    @PostExchange("/{paymentKey}/cancel")
    Object cancelPayment(
            @PathVariable("paymentKey")
            String paymentKey,
            @RequestBody
            PaymentCancelDto dto
    );
}
