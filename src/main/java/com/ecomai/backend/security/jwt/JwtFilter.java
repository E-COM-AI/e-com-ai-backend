package com.ecomai.backend.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT 인증 필터
 *
 * 모든 요청마다 실행됨
 */
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // 1. 토큰 존재 여부 확인
        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            // 2. 토큰 유효성 검사
            if (jwtProvider.validateToken(token)) {

                Long memberId = jwtProvider.getMemberId(token);

                // 3. 인증 객체 생성 (실무 최소 구조)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                memberId,
                                null,
                                Collections.emptyList()
                        );

                // 4. SecurityContext 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}