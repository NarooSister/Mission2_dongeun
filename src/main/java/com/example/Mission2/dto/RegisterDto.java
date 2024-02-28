package com.example.Mission2.dto;

import lombok.Data;

@Data
public class RegisterDto {
    //회원가입, 로그인에 필요한 username과 password
    private String username;
    private String password;
}
