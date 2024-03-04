package com.example.Mission2.controller;

import com.example.Mission2.dto.GoodsDto;
import com.example.Mission2.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {
    private final GoodsService service;

    //CREATE
    //쇼핑몰 주인이 상품을 등록
    @PostMapping
    public GoodsDto create(
            @RequestBody GoodsDto dto
    ){
        return service.createGoods(dto);
    }

    //READ ALL
    //쇼핑몰 상품을 검색할 수 있다.
    //이름, 가격 범위를 기준으로 상품을 검색한다.
    @GetMapping
    public List<GoodsDto> readAll(
            // 1: 이름순 조회, 2: 가격 범위 기준
            @RequestParam("condition") Integer condition,
            // 이름순 조회의 경우 name 입력
            @RequestParam(value = "name", required = false) String name,
            // 가격 범위 기준 조회 : min - 최소 가격, max - 최대 가격
            @RequestParam(value = "min", required = false) Integer min,
            @RequestParam(value = "max", required = false) Integer max
    ){
        return service.readGoodsAll(condition, name, min, max);
    }

    //UPDATE
    //쇼핑몰 주인이 등록한 상품을 수정
    @PutMapping("{id}")
    public GoodsDto update(
            @PathVariable("id") Long id,
            @RequestBody GoodsDto dto
    ){
        return service.updateGoods(id, dto);
    }

    //DELETE
    //쇼핑몰 주인이 등록한 상품을 삭제
    @DeleteMapping("{id}")
    public void delete(
            @PathVariable("id") Long id
    ){
        service.deleteGoods(id);
    }

}
