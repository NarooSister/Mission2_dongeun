package com.example.Mission2.usedItem.dto;

import com.example.Mission2.usedItem.entity.ItemStatus;
import com.example.Mission2.usedItem.entity.UsedItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
