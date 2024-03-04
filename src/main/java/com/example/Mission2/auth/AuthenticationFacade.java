package com.example.Mission2.auth;

import com.example.Mission2.auth.entity.UserEntity;
import com.example.Mission2.auth.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class AuthenticationFacade {
    private final UserRepository userRepository;
    public UserEntity getUserEntity() {
        UserDetails userDetails =
                (UserDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
