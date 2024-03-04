package com.example.Mission2.shop.dto;

import com.example.Mission2.shop.entity.Goods;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class GoodsDto {
    private Long id;
    private String name;            //상품 이름
    private String imageUrl;        //이미지 URL
    private String description;     //상품 설명
    private Integer price;          //상품 가격
    private Integer stock;          //상품 재고

    public static GoodsDto fromEntity(Goods goods){
        return GoodsDto.builder()
                .id(goods.getId())
                .name(goods.getName())
                .imageUrl(goods.getImageUrl())
                .description(goods.getDescription())
                .price(goods.getPrice())
                .stock(goods.getStock())
                .build();
    }
}
