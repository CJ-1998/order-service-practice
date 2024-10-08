package com.sparta.msa_exam.orderservicepractice.domain.user.jwt;

import com.sparta.msa_exam.orderservicepractice.domain.user.domain.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j(topic = "JWT 관련 로그")
@Component
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;

    private Key key;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(String username, UserRole role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // JWT를 Authorization 헤더에 추가
    public void addJwtToHeader(String token, HttpServletResponse res) {
        if (!token.startsWith(BEARER_PREFIX)) {
            token = BEARER_PREFIX + token;
        }
        res.setHeader(AUTHORIZATION_HEADER, token);
    }

    // JWT Cookie 에 저장
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8")
                    .replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
            cookie.setPath("/");

            // SameSite=None 설정 및 Secure 설정
            cookie.setSecure(true);  // HTTPS 환경에서만 쿠키 전송
            cookie.setHttpOnly(true);  // 자바스크립트에서 쿠키 접근 차단
            cookie.setMaxAge((int) TOKEN_TIME / 1000);  // 쿠키의 만료 시간 설정 (초 단위)

            // SameSite 설정은 HttpServletResponse에 직접 설정해야 함
            cookie.setAttribute("SameSite", "None"); // SameSite=None 설정

            // Response 객체에 Cookie 추가
            res.addCookie(cookie);

        } catch (UnsupportedEncodingException e) {
            log.error("Unsupported Encoding, 지원되지 않는 인코딩 입니다.");
        }
    }

    // JWT 토큰 substring
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(BEARER_PREFIX.length());
        }
        log.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // HttpServletRequest 에서 Authorization 헤더의 JWT 가져오기
    public String getTokenFromRequest(HttpServletRequest req) {
        // Authorization 헤더에서 JWT 가져오기
        String bearerToken = req.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7); // "Bearer " 접두사 제거
        }

        log.error("No JWT token found in request headers");
        return null;
    }

    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
//    public String getTokenFromRequest(HttpServletRequest req) {
//        Cookie[] cookies = req.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
//
//                    log.info(cookie.getName());
//                    log.info(cookie.getValue());
//
//                    try {
//                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
//                    } catch (UnsupportedEncodingException e) {
//                        log.error("Unsupported Encoding, 지원되지 않는 인코딩 입니다.");
//                        return null;
//                    }
//                }
//            }
//        }
//        log.error("No Cookie, JWT를 가진 쿠키가 없습니다.");
//        return null;
//    }
}
