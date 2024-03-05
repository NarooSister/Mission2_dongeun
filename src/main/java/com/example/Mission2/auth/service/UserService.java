package com.example.Mission2.auth.service;

import com.example.Mission2.FileFacade;
import com.example.Mission2.ImageCategory;
import com.example.Mission2.auth.AuthenticationFacade;
import com.example.Mission2.auth.dto.UserDto;
import com.example.Mission2.auth.entity.CustomUserDetails;
import com.example.Mission2.auth.entity.UserEntity;
import com.example.Mission2.auth.jwt.JwtRequestDto;
import com.example.Mission2.auth.jwt.JwtTokenUtils;
import com.example.Mission2.auth.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final CustomUserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;
    private final AuthenticationFacade facade;
    private final FileFacade fileFacade;

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

    //프로필 이미지 등록
    public UserDto updateUserImage(MultipartFile image) {
        UserEntity target = (UserEntity) fileFacade.uploadImage(ImageCategory.USER, image);

        // 5. 응답하기
        return UserDto.fromEntity(userRepository.save(target));

    }


    //일반 사용자 -> 사업자 사용자
    //USER가 BusinessNum 입력 후 사업자 등록 신청
    public void updateBusinessNum(String businessNum){
        //유저 정보 가져오기
        UserEntity userEntity = facade.getUserEntity();
        //ROLE_USER 인지 확인
        if(!userEntity.getRole().equals("ROLE_USER"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "일반 사용자만이 사업자 사용자 전환 신청이 가능합니다.");

        userEntity.setBusinessNum(businessNum);
        userRepository.save(userEntity);
    }


  /*  //UserEntity 가져오는 메소드 만들어서 사용
      //변경 : facade 만들어서 사용
    private UserEntity getUserEntity(){
        UserDetails userDetails =
                (UserDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }*/


}
