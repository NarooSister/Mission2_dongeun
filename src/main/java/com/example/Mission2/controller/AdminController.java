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
    public List<UserEntity> readBusinessList(){
        return adminService.readBusinessList();
    }

    //허가 또는 불허에 따라 User ROLE 변경
    //UPDATE
    @PostMapping("/business-update/{id}")
    public String updateBusiness(
            @PathVariable("id") Long id,
            //true = 승인, false = 반려
            @RequestParam("confirm") Boolean confirm
    ){
        adminService.updateBusiness(id, confirm);
        return "완료되었습니다.";
    }

    //개설 신청 쇼핑몰 목록 열람
    @GetMapping("/shop-list")
    public void readShopList(){
   //     return adminService.readShopList();
    }

    //허가 또는 불허에 따라 쇼핑몰 개설
    @PostMapping("/shop-update")
    public void updateShop(){
//        adminService.updateShop();
    }

    //쇼핑몰 폐쇄 요청 열람
    @GetMapping("/shop-close-list")
    public void readCloseShopList(){
 //       return adminService.readCloseShopList();
    }

    //쇼핑몰 폐쇄 요청 수락 및 거절
    @PostMapping("/shop-close")
    public void updateCloseShop(){
//        return adminService.updateCloseShop();
    }

}
