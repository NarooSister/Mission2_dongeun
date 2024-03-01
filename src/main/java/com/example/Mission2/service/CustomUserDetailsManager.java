package com.example.Mission2.service;

import com.example.Mission2.entity.CustomUserDetails;
import com.example.Mission2.entity.UserEntity;
import com.example.Mission2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Slf4j
//시스템상으로 회원정보를 관리하는 로직을 담당
//회원 생성, 수정, 삭제
public class CustomUserDetailsManager implements UserDetailsManager {
    private final UserRepository userRepository;
    public CustomUserDetailsManager(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        //admin 추가
        createUser(CustomUserDetails.builder()
                .username("admin")
                .password(passwordEncoder.encode("1234"))
                .role("ROLE_ADMIN")
                .build());
       /* //user 추가
        createUser(CustomUserDetails.builder()
                .username("user1")
                .password(passwordEncoder.encode("1234"))
                .realname("user1")
                .nickname("nickUser1")
                .age(15)
                .email("user1@Gmail.com")
                .phone("01012345678")
                .role("ROLE_USER")
                .build());
        //user 추가
        createUser(CustomUserDetails.builder()
                .username("admin")
                .password(passwordEncoder.encode("1234"))
                .role("ROLE_TEMPORARYUSER")
                .build());*/
    }

    @Override
    // formLogin 등 Spring Security 내부에서
    // 인증을 처리할때 사용하는 메서드이다.
    // User 호출
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser
                = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(username);

        UserEntity userEntity = optionalUser.get();
        return CustomUserDetails.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .realname(userEntity.getRealname())
                .nickname(userEntity.getNickname())
                .age(userEntity.getAge())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .imageUrl(userEntity.getImageUrl())
                .businessNum(userEntity.getBusinessNum())
                .role(userEntity.getRole())
                .build();
    }

    @Override
    // 편의를 위해 같은 규약으로 회원가입을 하는 메서드
    // User 생성
    public void createUser(UserDetails user) {
       //username 중복 확인
        if (userExists(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        try {
            CustomUserDetails userDetails =
                    (CustomUserDetails) user;
            UserEntity newUser = UserEntity.builder()
                    .username(userDetails.getUsername())
                    .password(userDetails.getPassword())
                    .build();

            //처음 생성하는 Admin인 경우 포함해서 구현
            if(userDetails.getRole() != null){
                newUser.setRole(userDetails.getRole());
            } else {
                newUser.setRole("ROLE_TEMPORARY_USER");
            }
            userRepository.save(newUser);
        } catch (ClassCastException e) {
            log.error("Failed Cast to: {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }
    @Override
    // User 확인
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void deleteUser(String username) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }
}
