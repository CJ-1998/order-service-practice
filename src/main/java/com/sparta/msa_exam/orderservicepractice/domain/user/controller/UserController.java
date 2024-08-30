package com.sparta.msa_exam.orderservicepractice.domain.user.controller;

import com.sparta.msa_exam.orderservicepractice.domain.user.domain.UserRole;
import com.sparta.msa_exam.orderservicepractice.domain.user.dto.UserRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.user.dto.UserResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.user.security.UserDetailsImpl;
import com.sparta.msa_exam.orderservicepractice.domain.user.service.UserService;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseBody;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseUtil;
import java.nio.file.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
                                                                    @RequestBody UserRequestDto userRequestDto,
                                                                    @AuthenticationPrincipal UserDetailsImpl userDetails)
            throws AccessDeniedException {
        UserResponseDto userResponseDto = userService.updateUser(userId, userRequestDto, userDetails);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(userResponseDto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseBody<UserResponseDto>> deleteUser(@PathVariable("userId") Long userId,
                                                                    @AuthenticationPrincipal UserDetailsImpl userDetails)
            throws AccessDeniedException {
        UserResponseDto userResponseDto = userService.deleteUser(userId, userDetails);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(userResponseDto));
    }

    @Secured(UserRole.Authority.ADMIN) // 관리자용
    @GetMapping
    public ResponseEntity<ResponseBody<Page<UserResponseDto>>> getUsers(Pageable pageable,
                                                                        @RequestParam(value = "username", required = false) String username) {
        Page<UserResponseDto> users;

        if (username == null || username.isEmpty()) {
            // 전체 조회
            users = userService.getAllUsers(pageable);
        } else {
            // 특정 사용자 이름으로 검색
            users = userService.searchUsersByUsername(username, pageable);
        }

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(users));
    }

}
