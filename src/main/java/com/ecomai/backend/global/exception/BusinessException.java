package com.ecomai.backend.global.exception;
import com.ecomai.backend.global.response.ErrorCode;
import lombok.Getter;

/**
 * 서비스 비즈니스 예외
 */
@Getter
public class BusinessException extends RuntimeException {
    /**
     * 발생한 에러 코드
     */
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {

        super(errorCode.getMessage());

        this.errorCode = errorCode;
    }
}
