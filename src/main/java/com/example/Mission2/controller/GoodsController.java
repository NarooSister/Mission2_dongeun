package com.example.Mission2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shop/{shopId}/goods")
@RequiredArgsConstructor
public class GoodsController {
    //CREATE
    //쇼핑몰 주인이 상품을 등록


    //READ
    //쇼핑몰 상품을 검색할 수 있다.
    //이름, 가격 범위를 기준으로 상품을 검색한다.


    //UPDATE
    //쇼핑몰 주인이 등록한 상품을 수정


    //DELETE
    //쇼핑몰 주인이 등록한 상품을 삭제


}
