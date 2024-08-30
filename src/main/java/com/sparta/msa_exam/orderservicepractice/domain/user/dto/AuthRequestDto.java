package com.sparta.msa_exam.orderservicepractice.domain.user.dto;

import com.sparta.msa_exam.orderservicepractice.domain.user.domain.UserRole;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequestDto {

    @Size(min = 4, max = 10)  // 길이를 4~10자로 제한
    @Pattern(regexp = "^[a-z0-9]+$")  // 알파벳 소문자와 숫자로만 구성
    private String username;

    @Size(min = 8, max = 15)
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+$")  // 알파벳 대소문자, 숫자, 특수문자로 구성
    private String password;

    @Size(min = 4, max = 10)
    @Pattern(regexp = "^[a-z0-9]+$")
    private String nickname;

    private String address;
    @Size(max = 50)
    private String request;
    private UserRole role;
    private String regionName;
}
