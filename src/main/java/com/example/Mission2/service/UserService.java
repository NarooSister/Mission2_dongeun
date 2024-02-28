package com.example.Mission2.service;

import com.example.Mission2.dto.UserDto;
import com.example.Mission2.entity.CustomUserDetails;
import com.example.Mission2.entity.UserEntity;
import com.example.Mission2.jwt.JwtRequestDto;
import com.example.Mission2.jwt.JwtTokenUtils;
import com.example.Mission2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final CustomUserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;

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

    //임시 사용자 -> 일반 사용자
    //추가 정보 입력 후 ROLE 변경
    public void updateUserInfo(String username, UserDetails userDetails){

        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        //username으로 유저 확인
        if (optionalUser.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

       UserEntity target = optionalUser.get();
       CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
       target.setRealname(customUserDetails.getRealname());
       target.setNickname(customUserDetails.getNickname());
       target.setAge(customUserDetails.getAge());
       target.setEmail(customUserDetails.getEmail());
       target.setPhone(customUserDetails.getPhone());

       //정보 추가가 모두 됐는지 확인
        if(customUserDetails.fillInfo()){
            target.setRole("ROLE_USER");
        }
        //저장
        userRepository.save(target);
    }


}
