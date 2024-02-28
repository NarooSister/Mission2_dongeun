package com.example.Mission2.config;

import com.example.Mission2.jwt.JwtTokenFilter;
import com.example.Mission2.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager manager;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http
                // csrf 보안 해제
                .csrf(AbstractHttpConfigurer::disable)
                // url에 따른 요청 인가
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/users/login",
                                "/users/register"
                        )
                        .permitAll()

                        // ROLE에 따른 접근 설정
                        .requestMatchers(
                                "/users/my-page",
                                "/users/update-info"
                        )
                        .hasAnyRole("TEMPORARY_USER", "USER", "BUSINESS_USER", "ADMIN")

                        .requestMatchers("/auth/admin-role")
                        .hasRole("ADMIN")

                        .anyRequest()
                        .permitAll()
                )
                // JWT를 사용하기 때문에 보안 관련 세션 해제
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // JWT 필터를 권한 필터 앞에 삽입
                .addFilterBefore(
                        new JwtTokenFilter(
                                jwtTokenUtils,
                                manager
                        ),
                        AuthorizationFilter.class
                )
        ;
        return http.build();
    }

    @Bean
    // 비밀번호 암호화 클래스
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
