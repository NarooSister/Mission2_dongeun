package com.example.Mission2.usedItem.repo;

import com.example.Mission2.usedItem.entity.Offer;
import com.example.Mission2.usedItem.entity.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByItemId(Long id);
    Offer findByItemIdAndBuyerId(Long itemId, Long buyerId);
    List<Offer> findByItemIdAndStatus(Long itemId, OfferStatus status);

}
