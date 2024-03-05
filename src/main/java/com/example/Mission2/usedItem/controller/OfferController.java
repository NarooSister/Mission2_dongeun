package com.example.Mission2.usedItem.controller;

import com.example.Mission2.usedItem.dto.OfferDto;
import com.example.Mission2.usedItem.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items/{itemId}/offers")
public class OfferController {
    private final OfferService offerService;

    //CREATE
    //구매 제안 등록
    @PostMapping
    public OfferDto create(
            @PathVariable("itemId") Long itemId
    ){
        return offerService.createOffer(itemId);
    }

    //READ ALL
    //판매자가 모든 구매 제안 열람
    @GetMapping
    public List<OfferDto> readAll(
            @PathVariable("itemId") Long itemId
    ){
      return offerService.readOfferAll(itemId);
    }

    //READ ONE
    //구매자가 제안 상세 열람
    @GetMapping("/{offerId}")
    public OfferDto readOne(
            @PathVariable("itemId") Long itemId,
            @PathVariable("offerId") Long offerId
    ){
        return offerService.readOneOffer(itemId, offerId);
    }

    //UPDATE
    //판매자가 구매 제안 수락 또는 거절
    @PutMapping("/{offerId}/accept")
    public OfferDto update(
            @PathVariable("itemId") Long itemId,
            @PathVariable("offerId") Long offerId,
            @RequestParam("accept") Boolean accept
    ){
        return offerService.updateOffer(itemId, offerId, accept);
    }

    //UPDATE
    //구매자가 구매 확정을 결정
    @PutMapping("/{offerId}/confirm")
    public OfferDto updateConfirm(
            @PathVariable("itemId") Long itemId,
            @PathVariable("offerId") Long offerId,
            //true = 승인, false = 반려
            @RequestParam("confirm") Boolean confirm
    ){
        return offerService.updateConfirm(itemId, offerId, confirm);
    }

    //DELETE
    //구매자가 구매 제안 삭제
    @DeleteMapping("/{offerId}")
    public void delete(
            @PathVariable("itemId") Long itemId,
            @PathVariable("offerId") Long offerId

    ){
        offerService.deleteOffer(itemId, offerId);
    }

}
