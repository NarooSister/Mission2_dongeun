package com.example.Mission2.shop;

import com.example.Mission2.auth.AuthenticationFacade;
import com.example.Mission2.shop.entity.Shop;
import com.example.Mission2.shop.entity.ShopStatus;
import com.example.Mission2.auth.entity.UserEntity;
import com.example.Mission2.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class ShopAuthenticationFacade {
    private final AuthenticationFacade authFacade;
    private final ShopRepository shopRepository;

    //TODO 중복 코드 리팩토링하기 - 에러나서 보류
    public void getShopAuth(){
        //쇼핑몰 주인 유저 정보 가져오기
        UserEntity user = authFacade.getUserEntity();

        //shop 정보 가져오기
        Shop shop = shopRepository.findByOwnerId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //shop 상태 확인하기 (오픈 상태일 때)
        if(!shop.getStatus().equals(ShopStatus.OPEN))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

    }
}
