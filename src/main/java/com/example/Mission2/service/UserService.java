package com.example.Mission2.service;

import com.example.Mission2.jwt.JwtRequestDto;
import com.example.Mission2.jwt.JwtResponseDto;
import com.example.Mission2.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final CustomUserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    //로그인
    public String loginUser(JwtRequestDto dto) {
        // 사용자가 제공한 username(id), password가 저장된 사용자인지 판단
        if (!manager.userExists(dto.getUsername()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        UserDetails userDetails
                = manager.loadUserByUsername(dto.getUsername());

        // 비밀번호 확인
        if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        // JWT 발급
      /*  String jwt = jwtTokenUtils.generateToken(userDetails);
        JwtResponseDto response = new JwtResponseDto();
        response.setToken(jwt);
        return response;*/
        return jwtTokenUtils.generateToken(userDetails);
    }
}
