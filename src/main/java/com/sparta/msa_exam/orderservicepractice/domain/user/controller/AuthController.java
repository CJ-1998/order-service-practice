package com.sparta.msa_exam.orderservicepractice.domain.user.controller;

import com.sparta.msa_exam.orderservicepractice.domain.user.dto.AuthRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.user.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signup(@RequestBody AuthRequestDto authRequestDto) {
        authService.signup(authRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // 쿠키 삭제
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true); // HttpOnly 설정
        cookie.setMaxAge(0);  // MaxAge를 0으로 설정하여 쿠키 삭제
        cookie.setSecure(false); // Secure 속성을 제거하여 HTTP 환경에서 테스트 가능하도록 설정 (https : true)

        response.addCookie(cookie);

        return ResponseEntity.ok().body("Logged out successfully");
    }

    @GetMapping("/status")
    public ResponseEntity<?> checkAuthStatus(HttpServletRequest request) {

        /// SecurityContextHolder 에서 인증 정보 가져 오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증이 되어 있는지 확인
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);

        Map<String, Boolean> response = new HashMap<>();
        response.put("isAuthenticated", isAuthenticated);

        return ResponseEntity.ok(response);
    }
}


