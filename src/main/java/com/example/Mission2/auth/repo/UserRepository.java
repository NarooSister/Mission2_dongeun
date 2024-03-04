package com.example.Mission2.auth.repo;

import com.example.Mission2.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
    List<UserEntity> findByBusinessNumIsNotNullAndRoleContains(String role);
}
