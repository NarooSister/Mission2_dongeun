package com.example.Mission2.service;

import com.example.Mission2.AuthenticationFacade;
import com.example.Mission2.dto.OfferDto;
import com.example.Mission2.entity.*;
import com.example.Mission2.repository.ItemRepository;
import com.example.Mission2.repository.OfferRepository;
import com.example.Mission2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfferService {
    private final ItemRepository itemRepository;
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade facade;

    //CREATE
    // Buyer: 구매 제안 등록
    public OfferDto createOffer(Long itemId){
        //구매자 정보 가지고 오기
        String username = facade.getUserEntity().getUsername();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


        //구매할 아이템 정보 가지고 오기
        UsedItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //구매자가 판매자인지 확인
        if(item.getUser().getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //대기중 상태의 offer 생성
        Offer offer = Offer.builder()
                .status(OfferStatus.WAITING)
                .item(item)
                .buyer(user)
                .build();

        return OfferDto.fromEntity(offerRepository.save(offer));

    }

    //READ ALL
    // Seller : 판매자가 모든 구매 제안 열람
    public List<OfferDto> readOfferAll(Long itemId){
        //구매할 아이템 정보 가지고 오기
        UsedItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //판매자만 조회 가능
        if(!item.getUser().getUsername().equals(facade.getUserEntity().getUsername())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"판매자만 모든 제안을 조회 가능합니다.");
        }

        //itemId 기준으로 모든 구매제안 불러오기
       return offerRepository.findByItemId(itemId).stream()
               .map(OfferDto::fromEntity)
               .toList();
    }

    //READ ONE
    //Buyer : 구매자가 제안 상세 열람
    public OfferDto readOneOffer(Long itemId, Long offerId){
        //구매자 정보 가지고 오기
        String username = facade.getUserEntity().getUsername();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //구매 제안 가지고 오기
        Offer targetOffer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //구매자만 조회 가능
        if(!targetOffer.getBuyer().getUsername().equals(facade.getUserEntity().getUsername())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"자신의 제안만 조회 가능합니다.");
        }

        Offer offer = offerRepository.findByItemIdAndBuyerId(itemId, user.getId());
        return OfferDto.fromEntity(offer);
    }

    //UPDATE
    // Seller : 판매자가 구매 제안 수락 또는 거절
    public OfferDto updateOffer(Long itemId, Long offerId, Boolean accept){
        //구매할 아이템 정보 가지고 오기
        UsedItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        //판매자 가지고 오기
        UserEntity user = item.getUser();

        //구매 제안 가지고 오기
        Offer targetOffer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //수락 또는 거절
        if(accept){
            targetOffer.setStatus(OfferStatus.ACCEPT);
        } else {
            targetOffer.setStatus(OfferStatus.REJECT);
        }

        return OfferDto.fromEntity(offerRepository.save(targetOffer));
    }

    //UPDATE
    // Buyer : 구매자가 구매 확정을 결정
    public OfferDto updateConfirm(Long itemId, Long offerId, Boolean confirm){
        //구매할 아이템 정보 가지고 오기
        UsedItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //구매 제안 가지고 오기
        Offer targetOffer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 확정 또는 거절
        if(confirm){
            //offer 상태 변경
            targetOffer.setStatus(OfferStatus.CONFIRMED);
            //item 상태 변경 (sold_out)
            item.setStatus(ItemStatus.SOLD_OUT);
            //저장
            itemRepository.save(item);

            //확정되지 않은 다른 구매 제안의 상태는 모두 거절로 변경
            //즉, 같은 item 의 구매 제안 중 상태가 waiting 인 구매 제안의 상태 변경
            List<Offer> offerList = offerRepository.findByItemIdAndStatus(itemId, OfferStatus.WAITING);
            for(Offer offer : offerList){
                offer.setStatus(OfferStatus.REJECT);
            }
            return OfferDto.fromEntity(offerRepository.save(targetOffer));

        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"구매 제안 확정이 취소되었습니다.");
        }
    }

    //DELETE
    // Buyer : 구매자가 구매 제안 삭제
    public void deleteOffer(Long itemId, Long offerId){
        //구매할 아이템 정보 가지고 오기
        UsedItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //구매 제안 가지고 오기
        Offer targetOffer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //구매자만 삭제 가능
        if(!targetOffer.getBuyer().getUsername().equals(facade.getUserEntity().getUsername())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"자신의 제안만 조회 가능합니다.");
        }
        offerRepository.deleteById(offerId);

    }

}
