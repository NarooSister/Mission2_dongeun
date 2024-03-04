package com.example.Mission2.usedItem.dto;

import com.example.Mission2.usedItem.entity.Offer;
import com.example.Mission2.usedItem.entity.OfferStatus;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OfferDto {
    private Long id;
    private OfferStatus status;
    private Long itemId;
    private Long buyerId;

    public static OfferDto fromEntity(Offer offer){
        return OfferDto.builder()
                .id(offer.getId())
                .status(offer.getStatus())
                .itemId(offer.getItem().getId())
                .buyerId(offer.getBuyer().getId())
                .build();

    }
}
