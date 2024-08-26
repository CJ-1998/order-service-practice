package com.sparta.msa_exam.orderservicepractice.global.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON_0001", "잘못된 입력 값입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "COMMON_0002", "잘못된 타입입니다."),
    MISSING_INPUT_VALUE(HttpStatus.BAD_REQUEST,"COMMON_0003", "인자가 부족합니다."),
    NOT_EXIST_API(HttpStatus.BAD_REQUEST, "COMMON_0004", "요청 주소가 올바르지 않습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON_0005", "사용할 수 없는 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_0006", "서버 에러입니다."),
    INVALID_JSON(HttpStatus.BAD_REQUEST, "COMMON_0007", "JSON 파싱 오류입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON_0008", "요청한 리소스를 찾을 수 없습니다."),
    NULL_OR_EMPTY_VALUE(HttpStatus.BAD_REQUEST, "COMMON_0009", "값이 null이거나 비어있습니다."),

    // Authentication & Authorization
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_0001", "인증에 실패하였습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH_0002", "권한이 없습니다."),

    ;
    private final HttpStatus status;
    private final String code;
    private final String message;
}
