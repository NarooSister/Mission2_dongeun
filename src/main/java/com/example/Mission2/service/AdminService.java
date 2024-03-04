package com.example.Mission2.service;

import com.example.Mission2.dto.ShopDto;
import com.example.Mission2.dto.UserDto;
import com.example.Mission2.entity.Shop;
import com.example.Mission2.entity.ShopStatus;
import com.example.Mission2.entity.UserEntity;
import com.example.Mission2.repository.ShopRepository;
import com.example.Mission2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    private final UserRepository userRepository;
    private final ShopService shopService;
    private final ShopRepository shopRepository;

    //사업자 이용자 전환 신청 유저 목록
    public List<UserEntity> readBusinessList() {
        //ROLE_USER 중에 businessNum이 null이 아닌 User만 가져오기
        List<UserEntity> list = userRepository.findByBusinessNumIsNotNullAndRoleContains("ROLE_USER");

        if (list.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "전환 신청 이용자가 없습니다.");

        return list;
    }

    //admin이 목록을 본 뒤 id로 유저 승인 또는 반려
    public void updateBusiness(Long id, Boolean confirm) {
        //유저 정보 가져오기
        Optional<UserEntity> optionalUser = userRepository.findById(id);

        //유저 확인
        if (optionalUser.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 이름을 찾을 수 없습니다.");

        //ROLE 변경 후 저장
        UserEntity target = optionalUser.get();
        if (confirm) {
            log.info("사업자 승인 완료.");
            target.setRole("ROLE_BUSINESS_USER");
            UserDto.fromEntity(userRepository.save(target));
        } else {
            log.info("사업자 승인 거절.");
            UserDto.fromEntity(target);
        }

        //사업자 사용자에게 준비중 상태의 쇼핑몰 추가
        shopService.createShop(id);
    }


    //개설 신청 쇼핑몰 목록 열람
    public List<ShopDto> readShopList() {
        List<ShopDto> dtoList = new ArrayList<>();
        //신청 목록 열람
        List<Shop> shopList = shopRepository.findAllByStatus(ShopStatus.APPLY);
        for (Shop shop : shopList) {
            dtoList.add(ShopDto.fromEntity(shop));
        }
        return dtoList;
    }


    //허가 또는 불허에 따라 쇼핑몰 개설
    public ShopDto updateShop(Long shopId, Boolean confirm, ShopDto dto) {
        //쇼핑몰 가져오기
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //Status 변경 후 저장
        //true = 승인, false = 반려
        if (confirm) {
            shop.setStatus(ShopStatus.OPEN);
        } else {
            shop.setStatus(ShopStatus.REJECT);
            shop.setRejectReason(dto.getRejectReason());
        }
        return ShopDto.fromEntity(shopRepository.save(shop));
    }


    //쇼핑몰 폐쇄 요청 열람
    public List<ShopDto> readCloseShopList() {
        List<ShopDto> dtoList = new ArrayList<>();
        //폐쇄 요청 목록 열람
        List<Shop> shopList = shopRepository.findAllByStatus(ShopStatus.CLOSE_REQUEST);
        for (Shop shop : shopList) {
            dtoList.add(ShopDto.fromEntity(shop));
        }
        return dtoList;
    }

    //쇼핑몰 폐쇄 요청 수락 및 거절
    public ShopDto updateCloseShop(Long shopId) {
        //쇼핑몰 확인
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        //쇼핑몰 삭제하기
        shopRepository.deleteById(shopId);
        return ShopDto.fromEntity(shop);
    }
}
