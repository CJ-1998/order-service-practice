package com.sparta.msa_exam.orderservicepractice.domain.user.dto;

import com.sparta.msa_exam.orderservicepractice.domain.user.domain.User;
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

    public static UserResponseDto convertToUserResponseDto(User user) {
        return UserResponseDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
    }
}
