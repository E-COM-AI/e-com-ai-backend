package com.ecomai.backend.security;

import com.ecomai.backend.global.exception.BusinessException;
import com.ecomai.backend.global.response.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 현재 로그인한 사용자 정보 조회 유틸
 */
@Component
public class SecurityUtil {

    /**
     * SecurityContext 에 저장된 현재 로그인 사용자 ID 반환
     *
     * @return 로그인한 회원 ID
     * @throws BusinessException 인증 정보가 없거나 비정상인 경우
     */
    public Long getCurrentMemberId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보 자체가 없는 경우
        if (authentication == null) {
            throw new BusinessException(
                    ErrorCode.UNAUTHORIZED
            );
        }

        Object principal = authentication.getPrincipal();

        // Principal 타입 검증
        if (!(principal instanceof Long memberId)) {
            throw new BusinessException(
                    ErrorCode.UNAUTHORIZED
            );
        }

        return memberId;
    }
}