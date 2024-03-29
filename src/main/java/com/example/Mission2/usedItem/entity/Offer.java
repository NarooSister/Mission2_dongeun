package com.example.Mission2.usedItem.entity;

import com.example.Mission2.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Enumerated(EnumType.STRING)
    private OfferStatus status;

    @Setter
    @ManyToOne
    private UsedItem item;

    @Setter
    @ManyToOne
    private UserEntity buyer;
}
