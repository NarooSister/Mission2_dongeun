package com.example.Mission2.dto;

import com.example.Mission2.entity.ItemStatus;
import com.example.Mission2.entity.Offer;
import com.example.Mission2.entity.UsedItem;
import com.example.Mission2.entity.UserEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String title;
    private String description;
    private Integer price;
    private String imageUrl;
    private ItemStatus status;
    private List<OfferDto> offers;

    public static ItemDto fromEntity(UsedItem item){
        return ItemDto.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .price(item.getPrice())
                .imageUrl(item.getImageUrl())
                .status(item.getStatus())
                .build();
    }
}
