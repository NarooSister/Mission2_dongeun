package com.example.Mission2.dto;

import com.example.Mission2.entity.UserEntity;
import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String realname;
    private String nickname;
    private Integer age;
    private String email;
    private String phone;
    private String imageUrl;
    private String role;

    public static UserDto fromEntity(UserEntity entity){
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .realname(entity.getRealname())
                .nickname(entity.getNickname())
                .age(entity.getAge())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .imageUrl(entity.getImageUrl())
                .role(entity.getRole())
                .build();
    }
}
