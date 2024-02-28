package com.example.Mission2.controller;

import com.example.Mission2.entity.CustomUserDetails;
import com.example.Mission2.jwt.JwtRequestDto;
import com.example.Mission2.service.CustomUserDetailsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final CustomUserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    @PostMapping("/register")
    public String Register(
            @RequestBody String username,
            @RequestBody String password,
            @RequestBody String passwordCheck
    ){
        //비밀번호 한번 더 확인하기
        if (password.equals(passwordCheck))
            manager.createUser(CustomUserDetails.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .build());
        return String.format("%s님 회원 가입이 완료되었습니다.", username);
    }

    //마이페이지
    @GetMapping("/my-page")
    public String myPage(
            @RequestBody
            String username
    ){
        manager.loadUserByUsername(username);
        return "my page";
    }

}
