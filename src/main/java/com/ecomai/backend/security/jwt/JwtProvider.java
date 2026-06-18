package com.ecomai.backend.security.jwt;

import com.ecomai.backend.domain.member.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 생성 / 검증 담당 클래스
 */
@Component
public class JwtProvider {

    /**
     * JWT 서명용 Key
     */
    private final SecretKey key;

    /**
     * Access Token 만료시간(ms)
     *
     * application.yml
     * jwt:
     *   expiration: 3600000
     */
    private final long expiration;

    /**
     * 생성자 주입
     *
     * @param secret JWT Secret Key
     * @param expiration Access Token 만료시간(ms)
     */
    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {
        this.key = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

        this.expiration = expiration;
    }

    /**
     * JWT Access Token 생성
     *
     * @param memberId 회원 PK
     * @param email 회원 이메일
     * @return JWT 문자열
     */
    public String createToken(Long memberId, String email, Role role) {

        Date now = new Date();

        Date expire = new Date(
                now.getTime() + expiration
        );

        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .claim("email", email)
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(expire)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * JWT에서 회원 ID 추출
     *
     * @param token JWT
     * @return memberId
     */
    public Long getMemberId(String token) {

        return Long.valueOf(
                parseClaims(token).getSubject()
        );
    }

    // 토큰에서 권한 정보 추출
    public String getRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    /**
     * JWT 유효성 검사
     *
     * @param token JWT
     * @return 유효 여부
     */
    public boolean validateToken(String token) {

        try {

            parseClaims(token);

            return true;

        } catch (Exception exception) {

            return false;
        }
    }

    /**
     * JWT Claims 추출
     *
     * @param token JWT
     * @return Claims
     */
    private Claims parseClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}