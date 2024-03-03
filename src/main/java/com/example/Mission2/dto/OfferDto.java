package com.example.Mission2.dto;

import com.example.Mission2.entity.Offer;
import com.example.Mission2.entity.OfferStatus;
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
