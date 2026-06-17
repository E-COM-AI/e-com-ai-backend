package com.ecomai.backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


/**
 * 현재 로그인한 사용자 정보 꺼내는 유틸
 */
@Component
public class SecurityUtil {

    /**
     * 현재 로그인한 memberId 반환
     *
     * @return memberId (JWT에서 넣은 값)
     */
    public Long getCurrentMemberId() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return (Long) auth.getPrincipal();
    }
}
