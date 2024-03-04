package com.example.Mission2.usedItem.repo;

import com.example.Mission2.usedItem.entity.UsedItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<UsedItem, Long> {
}
