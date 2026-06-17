package com.ecomai.backend.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JWT 생성 / 검증 담당 클래스
 */
@Component
public class JwtProvider {

    private final String secret = "my-secret-key-my-secret-key-my-secret-key";
    private final long validity = 1000L * 60 * 60; // 1시간

    private final Key key = Keys.hmacShaKeyFor(secret.getBytes());

    /**
     * 토큰 생성
     */
    public String createToken(Long memberId, String email) {

        Date now = new Date();
        Date expire = new Date(now.getTime() + validity);

        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(expire)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * memberId 추출
     */
    public Long getMemberId(String token) {
        return Long.valueOf(
                parseClaims(token).getSubject()
        );
    }

    /**
     * 토큰 유효성 검사
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * claims 파싱
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}