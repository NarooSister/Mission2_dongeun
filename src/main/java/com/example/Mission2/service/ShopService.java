package com.example.Mission2.service;

import com.example.Mission2.AuthenticationFacade;
import com.example.Mission2.dto.ShopDto;
import com.example.Mission2.entity.Shop;
import com.example.Mission2.entity.ShopCategory;
import com.example.Mission2.entity.ShopStatus;
import com.example.Mission2.entity.UserEntity;
import com.example.Mission2.repository.ShopRepository;
import com.example.Mission2.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade facade;


    //CREATE
    //일반 사용자 -> 사용자 사용자 변경시 준비중 상태의 쇼핑몰 추가
    // AdminService 에서 사용
    public void createShop(Long userId) {
        // UserId로 쇼핑몰 주인 정보 가져오기
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 쇼핑몰 정보 저장
        shopRepository.save(Shop.builder()
                .status(ShopStatus.READY)
                .owner(user)
                .build());
    }

    //READ ALL
    //쇼핑몰 조회
    // 조건 없이 조회할 경우 가장 최근에 거래가 있었던 쇼핑몰 순서
    // 이름, 분류를 조건으로 검색 가능
    public List<ShopDto> readShopAll(
            Integer condition, String search
    ) {

        List<Shop> shopList = new ArrayList<>();

        // 0:기본 조회, 1:이름순 조회, 2:분류별 조회
        //조건에 따라 검색
        if (condition == 0) {
            shopList = shopRepository.findAllByRecentOrder();
        }
        if (condition == 1) {
            shopList = shopRepository.findAllByName(search);
        }
        if (condition == 2) {
            shopList = shopRepository.findAllByCategory(ShopCategory.valueOf(search));
        }

        List<ShopDto> shopDtoList = new ArrayList<>();
        for (Shop shop : shopList) {
            ShopDto dto = ShopDto.fromEntity(shop);
            shopDtoList.add(dto);
        }

        return shopDtoList;

    }

    //UPDATE
    //쇼핑몰 정보 수정
    public ShopDto updateShop(Long id, ShopDto dto){

        //유저 정보 가져오기
        String username = facade.getUserEntity().getUsername();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //shop 정보 가져오기
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //쇼핑몰 주인 확인
        if(!user.getId().equals(shop.getOwner().getId()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        //수정
        shop.setName(dto.getName());
        shop.setDescription(dto.getDescription());
        shop.setCategory(dto.getCategory());
        shop.setStatus(ShopStatus.APPLY);

        return ShopDto.fromEntity(shopRepository.save(shop));
    }

    //DELETE
    //쇼핑몰 폐쇄 신청
    public void deleteShop(Long id, ShopDto dto){
        //유저 정보 가져오기
        String username = facade.getUserEntity().getUsername();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //shop 정보 가져오기
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //쇼핑몰 주인 확인
        if(!user.getId().equals(shop.getOwner().getId()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        //폐쇄 요청
        shop.setCloseReason(dto.getCloseReason());
        shop.setStatus(ShopStatus.CLOSE_REQUEST);
        shopRepository.save(shop);

    }

}
