package com.example.Mission2.auth.controller;

import com.example.Mission2.auth.dto.UserDto;
import com.example.Mission2.auth.entity.CustomUserDetails;
import com.example.Mission2.auth.jwt.JwtRequestDto;
import com.example.Mission2.auth.service.CustomUserDetailsManager;
import com.example.Mission2.auth.service.UserService;
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
    //READ ALL
    @GetMapping("/my-page")
    public String myPage(
            @RequestParam
            String username
    ){
        manager.loadUserByUsername(username);
        return "my page";
    }

    //유저 정보 추가한 뒤 일반 사용자로 전환
    //임시 사용자 -> 일반 사용자 : 필요한 내용만 update
    //UPDATE
    @PostMapping("/update-info")
    public String updateUserInfo(
            @RequestParam("username") String username,
            @RequestBody CustomUserDetails user
    ){
       userService.updateUserInfo(username, user);
        return "update";
    }

    //TODO : 유저 정보 수정은 따로
    //CustomUserDetailsManage에 updateUser로 받아오기
    

    //프로필 이미지 등록
    @PutMapping("/image")
    public UserDto image(
            @RequestParam("image") MultipartFile image
            ){
        return userService.updateUserImage(image);

    }
    //사업자 사용자로 전환신청
    @PutMapping("/business")
    public String updateBusinessNum(
            @RequestParam("businessNum")
            String businessNum
    ){
        userService.updateBusinessNum(businessNum);
        return "사업자 번호 등록 완료";
    }


}
