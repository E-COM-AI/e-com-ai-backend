package com.ecomai.backend.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 로그인 성공 응답 DTO
 */
@Getter
@Builder
public class AuthResponse {

    private Long memberId;
    private String email;
    private String accessToken;
    private String tokenType;
}