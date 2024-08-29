package com.sparta.msa_exam.orderservicepractice.domain.user.dto;

import com.sparta.msa_exam.orderservicepractice.domain.user.domain.User;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String username;
    private String nickname;
    private String address;
    private String request;
    private UserRole role;
    private String regionName;

    public static UserResponseDto convertToUserResponseDto(User user) {
        return UserResponseDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .address(user.getAddress())
                .request(user.getRequest())
                .role(user.getRole())
                .regionName(user.getRegion().getName())
                .build();
    }
}
