package com.example.Mission2.entity;
import jakarta.persistence.*;
import lombok.*;

import javax.swing.text.StyleContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Column(nullable = false)
    private String title;
    @Setter
    @Column(nullable = false)
    private String description;
    @Setter
    @Column(nullable = false)
    private Integer price;
    @Setter
    private String imageUrl;
    @Setter
    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    @ManyToOne
    private UserEntity user;

    @OneToMany(mappedBy = "item")
    private List<Offer> offer = new ArrayList<>();
}

