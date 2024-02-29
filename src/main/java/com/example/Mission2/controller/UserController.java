package com.example.Mission2.controller;

import com.example.Mission2.dto.UserDto;
import com.example.Mission2.entity.CustomUserDetails;
import com.example.Mission2.jwt.JwtRequestDto;
import com.example.Mission2.service.CustomUserDetailsManager;
import com.example.Mission2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final CustomUserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    //회원가입
    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String passwordCheck
    ){
      //비밀번호 한번 더 확인하기
       if (password.equals(passwordCheck))
            manager.createUser(CustomUserDetails.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .build());
        return String.format("%s님 회원 가입이 완료되었습니다.", username);
    }

    //로그인
    @PostMapping("/login")
    public String login(
            @RequestBody
            JwtRequestDto dto
    ){
        //token값 String으로 볼 수 있음
        return "token 값 : " + userService.loginUser(dto);
    }

    //마이페이지
    @GetMapping("/my-page")
    public String myPage(
            @RequestParam
            String username
    ){
        manager.loadUserByUsername(username);
        return "my page";
    }

    //유저 정보 추가한 뒤 일반 사용자로 전환
    @PostMapping("/update-info")
    public String updateUserInfo(
            @RequestParam("username") String username,
            @RequestBody CustomUserDetails user
    ){
       userService.updateUserInfo(username, user);
        return "update";
    }

    //프로필 이미지 등록
    @PutMapping("/image")
    public UserDto image(
            @RequestParam("image") MultipartFile image
            ){
        return userService.updateUserImage(image);

    }
    //사업자 사용자로 전환신청


}
