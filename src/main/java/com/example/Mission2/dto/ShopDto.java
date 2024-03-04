package com.example.Mission2.dto;

import com.example.Mission2.entity.Shop;
import com.example.Mission2.entity.ShopCategory;
import com.example.Mission2.entity.ShopStatus;
import com.example.Mission2.entity.UserEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ShopDto {
    private Long id;
    private String name;            //이름
    private String description;     //소개
    private String rejectReason;    //개설 불허 이유
    private String closeReason;     //폐쇄 요청 사유
    private ShopCategory category;  //분류
    private ShopStatus status;      //상태
    private UserEntity owner;

    public static ShopDto fromEntity(Shop shop){
        return ShopDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .description(shop.getDescription())
                .rejectReason(shop.getRejectReason())
                .closeReason(shop.getCloseReason())
                .category(shop.getCategory())
                .status(shop.getStatus())
                .owner(shop.getOwner())
                .build();
    }
}
