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
import org.springframework.security.core.context.SecurityContextHolder;
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
        //유저 존재 확인
        UserEntity userEntity = getUserEntity();

        String profileDir = "media/profile/";
        log.info(profileDir);
        // 주어진 Path를 기준으로, 없는 모든 디렉토리를 생성하는 메서드
        try {
            Files.createDirectories(Path.of(profileDir));
        } catch (IOException e) {
            // 폴더를 만드는데 실패하면 기록을하고 사용자에게 알림
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String username = getUserEntity().getUsername();

        // 실제 파일 이름을 경로와 확장자를 포함하여 만들기 ("profile_{username}.{png}")
        String originalFilename = image.getOriginalFilename();
        String[] fileNameSplit = originalFilename.split("\\.");
        String extension = fileNameSplit[fileNameSplit.length - 1];
        String profileFilename = "profile_" + username +"." + extension;
        log.info(profileFilename);

        //경로
        String profilePath = profileDir + profileFilename;
        log.info(profilePath);

        // 실제로 해당 위치에 파일을 저장
        try {
            image.transferTo(Path.of(profilePath));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Long id = getUserEntity().getId();
        // User에 아바타 위치를 저장
        // http://localhost:8080/static/{id}/profile_{username}.{확장자}
        String requestPath = String.format("/static/%d/%s", id, profileFilename);
        log.info(requestPath);
        UserEntity target = getUserEntity();
        target.setImageUrl(requestPath);

        // 5. 응답하기
        return UserDto.fromEntity(userRepository.save(target));

    }


    //일반 사용자 -> 사업자 사용자
    //BusinessNum 입력 후 허가 받음.


    //UserEntity 가져오는 메소드 만들어서 사용
    private UserEntity getUserEntity(){
        UserDetails userDetails =
                (UserDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


}
