package com.example.Mission2.shop.entity;

public enum OrderStatus {
    WAITING,    //구매 요청 대기중
    ACCEPT,     //구매 요청 수락
    REJECT,     //구매 요청 거절
    CANCEL,     //구매 요청 취소
    COMPLETE    //구매 완료
}
