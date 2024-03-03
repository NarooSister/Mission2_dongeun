package com.example.Mission2.repository;

import com.example.Mission2.entity.UsedItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<UsedItem, Long> {
}
