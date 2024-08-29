package com.sparta.msa_exam.orderservicepractice.domain.user.controller;

import com.sparta.msa_exam.orderservicepractice.domain.user.dto.UserRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.user.dto.UserResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.user.service.UserService;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseBody;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "User 관련 log")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseBody<UserResponseDto>> getUser(@PathVariable("userId") Long userId) {
        UserResponseDto userResponseDto = userService.getUser(userId);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(userResponseDto));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ResponseBody<UserResponseDto>> updateUser(@PathVariable("userId") Long userId,
                                                                    @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.updateUser(userId, userRequestDto);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(userResponseDto));
    }
}
