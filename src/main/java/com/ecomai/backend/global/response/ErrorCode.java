package com.ecomai.backend.global.response;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public enum ErrorCode {
    /*
     * 공통 에러
     */
    INTERNAL_SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "COMMON_500",
            "서버 내부 오류가 발생했습니다."
    ),

    ACCESS_DENIED(
            HttpStatus.FORBIDDEN,
            "AUTH_403",
            "권한이 없습니다."
    ),

    FORBIDDEN(//본인 글 아닌 경우
            HttpStatus.FORBIDDEN,
            "COMMON_403",
            "접근 권한이 없습니다."
    ),

    INVALID_REQUEST(
            HttpStatus.BAD_REQUEST,
            "COMMON_400",
            "잘못된 요청입니다."
    ),

    UNAUTHORIZED(
            HttpStatus.UNAUTHORIZED,
            "AUTH_401",
            "인증이 필요합니다."
    ),

    //@Valid 검증 실패용 에러
    INVALID_INPUT_VALUE(
            HttpStatus.BAD_REQUEST,
            "COMMON_400_1",
                    "올바르지 않은 입력값입니다."
    ),

    // 잘못된 HTTP 메서드 (예: GET인데 POST로 보냄)
    METHOD_NOT_ALLOWED(
            HttpStatus.METHOD_NOT_ALLOWED,
            "COMMON_405",
            "지원하지 않는 HTTP 메서드입니다."
    ),

    /*
     * 회원 관련 에러
     */
    MEMBER_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "MEMBER_404",
            "회원을 찾을 수 없습니다."
    ),

    DUPLICATE_EMAIL(
            HttpStatus.CONFLICT,
            "MEMBER_409",
            "이미 존재하는 이메일입니다."
    ),

    INVALID_PASSWORD(
            HttpStatus.UNAUTHORIZED,
            "MEMBER_401",
            "비밀번호가 일치하지 않습니다."
    ),

    INVALID_INQUIRY_STATUS(
            HttpStatus.BAD_REQUEST,
            "INQUIRY_400",
            "현재 상태에서는 수정할 수 없습니다."
    ),

    INVALID_INQUIRY_STATUS_FILTER(
            HttpStatus.BAD_REQUEST,
            "INQUIRY_400_1",
            "잘못된 문의 상태입니다."
    ),

    /*
     * 문의 관련 에러
     */
    INQUIRY_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "INQUIRY_404",
            "문의가 존재하지 않습니다."
    );

    /**
     * HTTP 상태 코드
     */
    private final HttpStatus status;

    /**
     * 비즈니스 에러 코드
     */
    private final String code;

    /**
     * 사용자 메시지
     */
    private final String message;

    ErrorCode(
            HttpStatus status,
            String code,
            String message
    ) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
