package com.sparta.msa_exam.orderservicepractice.domain.user.domain;

public enum UserRole {
    USER(Authority.USER),       // 사용자 권한
    ADMIN(Authority.ADMIN),     // 관리자 권한
    OWNER(Authority.OWNER);     // 가게 주인 권한

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String OWNER = "ROLE_OWNER";
    }
}
