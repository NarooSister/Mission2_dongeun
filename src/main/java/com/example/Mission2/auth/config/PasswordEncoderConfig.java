package com.example.Mission2.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class PasswordEncoderConfig {

    //CustomUserDetailsManager 에서 관리자 유저 추가하면서
    // WebSecurityConfig 와 customUserDetailsManager 사이에서
    // PasswordEncoder 를 서로 참조하며 순환참조 문제가 생겨 분리

    @Bean
    // 비밀번호 암호화 클래스
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
