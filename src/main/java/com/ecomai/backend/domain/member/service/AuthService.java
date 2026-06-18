package com.ecomai.backend.domain.member.service;

import com.ecomai.backend.domain.member.dto.request.LoginRequest;
import com.ecomai.backend.domain.member.dto.request.SignupRequest;
import com.ecomai.backend.domain.member.dto.response.AuthResponse;
import com.ecomai.backend.domain.member.entity.Member;
import com.ecomai.backend.domain.member.enums.Role;
import com.ecomai.backend.domain.member.repository.MemberRepository;
import com.ecomai.backend.global.exception.BusinessException;
import com.ecomai.backend.global.response.ErrorCode;
import com.ecomai.backend.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 인증 서비스 (회원가입 / 로그인)
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    /**
     * 회원가입
     */
    public void signup(SignupRequest request) {

        // 비밀번호 확인
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 이메일 중복 체크
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 회원 생성
        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(Role.ROLE_USER)//엔티티의 @Builder.Default 되어있지만 명시적으로 설정
                .build();

        memberRepository.save(member);
    }

    /**
     * 로그인
     */
    public AuthResponse login(LoginRequest request) {

        // 1. 회원 조회
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.MEMBER_NOT_FOUND)
                );

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        // 3. JWT 발급
        String token = jwtProvider.createToken(
                member.getId(),
                member.getEmail(),
                member.getRole()
        );

        // 4. 응답 반환
        return AuthResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }
}