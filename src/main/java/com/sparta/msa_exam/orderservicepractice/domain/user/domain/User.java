package com.sparta.msa_exam.orderservicepractice.domain.user.domain;

import com.sparta.msa_exam.orderservicepractice.domain.region.domain.Region;
import com.sparta.msa_exam.orderservicepractice.global.base.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false, nullable = false)
    private Long id;

    @Size(min = 4, max = 10)  // 길이를 4~10자로 제한
    @Pattern(regexp = "^[a-z0-9]+$")  // 알파벳 소문자와 숫자로만 구성
    @Column(nullable = false, unique = true)
    private String username;

    @Size(min = 8, max = 15)
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+$")  // 알파벳 대소문자, 숫자, 특수문자로 구성
    @Column(nullable = false)
    private String password;

    @Size(min = 4, max = 10)
    @Pattern(regexp = "^[a-z0-9]+$")
    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String address;

    @Size(min = 0, max = 50)
    @Column(nullable = false)
    private String request;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @OneToOne
    @JoinColumn(name = "region_id")
    private Region region;

    public User(String username, String password, String nickname, String address,
                String request, UserRole role, Region region) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
        this.request = request;
        this.role = role;
        this.region = region;
    }

}
