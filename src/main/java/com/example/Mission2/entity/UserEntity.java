package com.example.Mission2.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Builder
@Table(name = "user_table")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    //실제 이름
    private String realname;
    //별명
    private String nickname;
    //연령대
    private Integer age;
    private String email;
    private String phone;
    //프로필 이미지
    private String imageUrl;
    //사업자 등록번호
    private String businessNum;
    //역할 및 권한
    private String role;

    //TODO ManyToMany를 사용한 authoritiy 관계설정 해보기
    /*@ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;*/

}