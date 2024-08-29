package com.sparta.msa_exam.orderservicepractice.domain.user.service;

import com.sparta.msa_exam.orderservicepractice.domain.user.domain.User;
import com.sparta.msa_exam.orderservicepractice.domain.user.dto.UserRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.user.dto.UserResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.user.repository.UserRepository;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ErrorCode;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ServiceException;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
        return UserResponseDto.convertToUserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateUser(Long userId, UserRequestDto userRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));

        checkUpdateUser(userRequestDto, user);

        return UserResponseDto.convertToUserResponseDto(user);
    }

    private void checkUpdateUser(UserRequestDto userRequestDto, User user) {
        if (!userRequestDto.getUsername().isEmpty()) {
            checkUsername(userRequestDto.getUsername());
            user.updateUsername(userRequestDto.getUsername());
        }

        if (!userRequestDto.getPassword().isEmpty()) {
            user.updatePassword(passwordEncoder.encode(userRequestDto.getPassword()));
        }

        if (!userRequestDto.getNickname().isEmpty()) {
            checkNickname(userRequestDto.getNickname());
            user.updateNickname(userRequestDto.getNickname());
        }

        if (!userRequestDto.getAddress().isEmpty()) {
            user.updateAddress(userRequestDto.getAddress());
        }

        if (!userRequestDto.getRequest().isEmpty()) {
            user.updateRequest(userRequestDto.getRequest());
        }

        if (!userRequestDto.getRegionName().isEmpty()) {
            user.updateRegionName(userRequestDto.getRegionName());
        }
    }

    private void checkUsername(String username) {
        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }
    }

    private void checkNickname(String nickname) {
        // email 중복확인
        Optional<User> checkNickname = userRepository.findByNickname(nickname);
        if (checkNickname.isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임 입니다.");
        }
    }
}
