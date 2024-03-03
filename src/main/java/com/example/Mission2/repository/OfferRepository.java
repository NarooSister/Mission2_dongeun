package com.example.Mission2.repository;

import com.example.Mission2.dto.OfferDto;
import com.example.Mission2.entity.Offer;
import com.example.Mission2.entity.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByItemId(Long id);
    Offer findByItemIdAndBuyerId(Long itemId, Long buyerId);
    List<Offer> findByItemIdAndStatus(Long itemId, OfferStatus status);

}
