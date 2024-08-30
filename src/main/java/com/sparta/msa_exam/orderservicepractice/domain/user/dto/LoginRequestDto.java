package com.sparta.msa_exam.orderservicepractice.domain.user.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String username;
    private String password;
}
