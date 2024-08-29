package com.sparta.msa_exam.orderservicepractice.domain.user.service;

import com.sparta.msa_exam.orderservicepractice.domain.user.domain.User;
import com.sparta.msa_exam.orderservicepractice.domain.user.dto.UserResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.user.repository.UserRepository;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ErrorCode;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
        return UserResponseDto.convertToUserResponseDto(user);
    }
}
