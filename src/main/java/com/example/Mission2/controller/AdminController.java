package com.example.Mission2.controller;

import com.example.Mission2.dto.ShopDto;
import com.example.Mission2.entity.UserEntity;
import com.example.Mission2.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    //사업자 사용자 전환 신청 목록 열람
    //READ ALL
    @GetMapping("/business-request-list")
    public List<UserEntity> readBusinessList() {
        return adminService.readBusinessList();
    }

    //허가 또는 불허에 따라 User ROLE 변경
    //UPDATE
    @PostMapping("/business-update/{id}")
    public String updateBusiness(
            @PathVariable("id") Long id,
            //true = 승인, false = 반려
            @RequestParam("confirm") Boolean confirm
    ) {
        adminService.updateBusiness(id, confirm);
        return "완료되었습니다.";
    }

    //개설 신청 쇼핑몰 목록 열람
    @GetMapping("/shop-open-list")
    public List<ShopDto> readShopList() {
        return adminService.readShopList();
    }

    //허가 또는 불허에 따라 쇼핑몰 개설
    @PostMapping("/shop-open/{shopId}")
    public ShopDto updateShop(
            @PathVariable("shopId") Long shopId,
            //true = 승인, false = 반려
            @RequestParam("confirm") Boolean confirm,
            //반려 이유 작성
            @RequestBody(required = false) ShopDto dto
    ) {
        return adminService.updateShop(shopId, confirm, dto);
    }

    //쇼핑몰 폐쇄 요청 열람
    @GetMapping("/shop-close-list")
    public List<ShopDto> readCloseShopList() {
          return adminService.readCloseShopList();
    }

    //쇼핑몰 폐쇄 요청 수락하고 삭제
    @PostMapping("/shop-close/{shopId}")
    public ShopDto updateCloseShop(
            @PathVariable("shopId") Long shopId
    ) {
           return adminService.updateCloseShop(shopId);
    }

}
