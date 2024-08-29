package com.sparta.msa_exam.orderservicepractice.domain.user.service;

import com.sparta.msa_exam.orderservicepractice.domain.region.domain.Region;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.User;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.UserRole;
import com.sparta.msa_exam.orderservicepractice.domain.user.dto.AuthRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(AuthRequestDto authRequestDto) {
        String username = authRequestDto.getUsername();
        String password = passwordEncoder.encode(authRequestDto.getPassword());
        String nickname = authRequestDto.getNickname();
        String address = authRequestDto.getAddress();
        String request = authRequestDto.getRequest();
        UserRole role = authRequestDto.getRole();
        Region region = authRequestDto.getRegion();

        checkUsername(username);
        checkNickname(nickname);

        // 사용자 등록
        User user = new User(username, password, nickname, address, request, role, region);
        userRepository.save(user);
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
