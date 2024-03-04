package com.example.Mission2.auth.entity;

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
    @Setter
    private String realname;          //실제 이름
    @Setter
    private String nickname;        //별명
    @Setter
    private Integer age;          //연령대
    @Setter
    private String email;
    @Setter
    private String phone;
    @Setter
    private String imageUrl;        //프로필 이미지
    @Setter
    private String businessNum;     //사업자 등록번호
    @Setter
    private String role;           //역할 및 권한

    //TODO ManyToMany를 사용한 authoritiy 관계설정 해보기
    /*@ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;*/

}