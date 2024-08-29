package com.sparta.msa_exam.orderservicepractice.domain.user.dto;

import lombok.Data;

@Data
public class UserRequestDto {
    private String username;
    private String password;
    private String nickname;
    private String address;
    private String request;
    private String regionName;
}
