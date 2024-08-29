package com.sparta.msa_exam.orderservicepractice.domain.user.dto;

import com.sparta.msa_exam.orderservicepractice.domain.region.domain.Region;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.UserRole;
import lombok.Data;

@Data
public class AuthRequestDto {

    private String username;
    private String password;
    private String nickname;
    private String address;
    private String request;
    private UserRole role;
    private Region region;
}
