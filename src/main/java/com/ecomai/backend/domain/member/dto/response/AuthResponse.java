package com.ecomai.backend.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 성공 응답 DTO
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private Long memberId;
    private String email;
    private String accessToken;
    private String tokenType;
}