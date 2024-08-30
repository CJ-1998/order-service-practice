package com.sparta.msa_exam.orderservicepractice.domain.user.controller;

import com.sparta.msa_exam.orderservicepractice.domain.user.dto.AuthRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

}
