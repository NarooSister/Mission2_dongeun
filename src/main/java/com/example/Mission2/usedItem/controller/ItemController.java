package com.example.Mission2.usedItem.controller;

import com.example.Mission2.usedItem.dto.ItemDto;
import com.example.Mission2.usedItem.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @ModelAttribute
            ItemDto dto,
            MultipartFile itemImage
    ){
        return itemService.createItem(dto, itemImage);

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
            @ModelAttribute ItemDto dto,
            MultipartFile itemImage
    ){
        return itemService.updateItem(id, dto, itemImage);
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
