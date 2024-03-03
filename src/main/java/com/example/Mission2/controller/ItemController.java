package com.example.Mission2.controller;

import com.example.Mission2.dto.ItemDto;
import com.example.Mission2.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    //CREATE
    //물품 정보 등록
    @PostMapping
    public ItemDto create(
            @RequestBody
            ItemDto dto
    ){
        return itemService.createItem(dto);

    }

    //READ ALL
    //물품 목록 열람
    @GetMapping
    public List<ItemDto> readAll(){
        return itemService.readItemAll();
    }

    //READ ONE
    //물품 상세 정보 열람
    @GetMapping("/{id}")
    public ItemDto readOne(
            @PathVariable("id") Long id
    ){
        return itemService.readItem(id);
    }


    //UPDATE
    //물품 정보 수정
    @PutMapping("/{id}")
    public ItemDto update(
            @PathVariable("id") Long id,
            @RequestBody ItemDto dto
    ){
        return itemService.updateItem(id, dto);
    }


    //DELETE
    //등록된 물품 삭제
    @DeleteMapping("{id}")
    public void delete(
            @PathVariable("id") Long id
    ){
        itemService.deleteItem(id);
    }
}
