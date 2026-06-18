package com.ecomai.backend.security.config;

import com.ecomai.backend.security.jwt.JwtFilter;
import com.ecomai.backend.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 최소 Security 설정
 */
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 1. CSRF 비활성화 (REST API)
                .csrf(csrf -> csrf.disable())
                // Spring Security 기본 로그인 페이지 비활성화
                .formLogin(form -> form.disable())
                // HTTP Basic 인증 비활성화
                .httpBasic(httpBasic -> httpBasic.disable())

                // 2. 세션 사용 안함 (JWT = Stateless)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // URL 권한 설정
                .authorizeHttpRequests(auth -> auth

                        // 회원가입 / 로그인 허용
                        .requestMatchers(
                                "/api/v1/auth/**"
                        ).permitAll()

                        // Swagger 허용
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .anyRequest().authenticated()
                )

                // 4. JWT Filter 등록
                .addFilterBefore(
                        new JwtFilter(jwtProvider),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    /**
     * 비밀번호 암호화 Bean
     *
     * BCrypt 방식 사용 (실무 표준)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
