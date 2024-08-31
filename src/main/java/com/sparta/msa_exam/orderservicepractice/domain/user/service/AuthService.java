package com.sparta.msa_exam.orderservicepractice.domain.user.service;

import com.sparta.msa_exam.orderservicepractice.domain.region.domain.Region;
import com.sparta.msa_exam.orderservicepractice.domain.region.repository.RegionRepository;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.User;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.UserRole;
import com.sparta.msa_exam.orderservicepractice.domain.user.dto.AuthRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.user.repository.UserRepository;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ErrorCode;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ServiceException;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j(topic = "Auth 관련 로그")
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(AuthRequestDto authRequestDto) {
        String username = authRequestDto.getUsername();
        String password = passwordEncoder.encode(authRequestDto.getPassword());
        String nickname = authRequestDto.getNickname();
        String address = authRequestDto.getAddress();
        String request = authRequestDto.getRequest();
        UserRole role = authRequestDto.getRole();
        Region region = findRegion(authRequestDto.getRegionName());

        checkUsername(username);
        checkNickname(nickname);

        // 사용자 등록
        User user = new User(username, password, nickname, address, request, role, region);

        userRepository.save(user);
    }

    private Region findRegion(String regionName) {
        return regionRepository.findByName(regionName)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
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

    @Cacheable(cacheNames = "userCache", key = "args[0]")
    public User readUser(String username) {
        log.info("Cache 동작 여부 확인");
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));
    }
}
