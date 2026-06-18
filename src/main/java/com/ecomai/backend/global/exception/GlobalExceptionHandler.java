package com.ecomai.backend.global.exception;

import com.ecomai.backend.global.response.ApiResponse;
import com.ecomai.backend.global.response.ApiErrorResponse;
import com.ecomai.backend.global.response.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpRequestMethodNotSupportedException;
/**
 * 전역 예외 처리
 *
 * 모든 예외를 한 곳에서 처리
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler{
    /**
     * BusinessException 처리
     *
     * 서비스 로직에서 발생하는
     * 비즈니스 예외 처리
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException exception
    ) {
        log.warn("Business Exception: {}", exception.getMessage());
        return createErrorResponse(
                exception.getErrorCode()
        );
    }

    /**
     * 처리하지 못한 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(
            Exception exception
    ) {
        log.error("Internal Server Error: ", exception);
        return createErrorResponse(
                ErrorCode.INTERNAL_SERVER_ERROR
        );
    }

    /**
     * @Valid 등 유효성 검사 실패 시 발생하는 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        log.warn("Validation Failed: {}", exception.getMessage());
        return createErrorResponse(ErrorCode.INVALID_INPUT_VALUE);
    }


    /**
     * 권한 부족 시 발생하는 예외 처리
     * (Spring Security 권한 부족 예외)
     */
    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
    protected ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(
            Exception exception
    ) {
        log.warn("Access Denied: {}", exception.getMessage());
        return createErrorResponse(ErrorCode.ACCESS_DENIED);
    }

    /**
     * 지원하지 않는 HTTP 메서드 호출 시 발생하는 예외 처리
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ApiResponse<Void>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception
    ) {
        log.warn("Method Not Allowed: {}", exception.getMessage());
        return createErrorResponse(ErrorCode.METHOD_NOT_ALLOWED);
    }


    /**
     * 공통 에러 응답 생성
     *
     * ErrorCode 기반으로
     * ResponseEntity 생성
     */
    private ResponseEntity<ApiResponse<Void>> createErrorResponse(
            ErrorCode errorCode
    ) {

        ApiErrorResponse errorResponse =
                ApiErrorResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.fail(errorResponse));
    }
}
