package com.example.Mission2.shop.controller;

import com.example.Mission2.shop.dto.ShopDto;
import com.example.Mission2.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
public class ShopController {
    private final ShopService service;

    //CREATE
    //쇼핑몰 생성
    //일반 사용자-> 사업자 사용자 변경시 자동 등록


    //READ ALL
    //사용자가 쇼핑몰 조회
    @GetMapping
    public List<ShopDto> readAll(
            // 0:기본 조회, 1:이름순 조회, 2:분류별 조회
            @RequestParam("condition") Integer condition,
            // 이름순, 분류별로 조회할 때 이름, 분류 입력
            // 기본 조회 시 null 값 가능
            @RequestParam(value = "search", required = false) String search
    ){
        return service.readShopAll(condition, search);
    }

    //UPDATE
    //쇼핑몰 정보 수정
    @PutMapping("{id}")
    public ShopDto update(
            @PathVariable Long id,
            @RequestBody ShopDto dto
    ){
        return service.updateShop(id, dto);
    }

    //DELETE
    //쇼핑몰 폐쇄 요청
    @DeleteMapping("{id}")
    public String delete(
            @PathVariable Long id,
            @RequestBody ShopDto dto
    ){
        service.deleteShop(id, dto);
        return "폐쇄 요청이 완료되었습니다.";
    }


}
