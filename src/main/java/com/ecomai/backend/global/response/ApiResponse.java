package com.ecomai.backend.global.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {
    //성공 여부
    private final boolean success;
    //실제 응답 데이터
    private final T data;
    //에러 정보 - 성공 시 null
    private final ApiErrorResponse error;

    //데이터 반환이 없는 성공 응답 메서드 (Void용)
    public static ApiResponse<Void> success() {
        return ApiResponse.<Void>builder()
                .success(true)
                .data(null)
                .error(null)
                .build();
    }

    //성공 응답 생성 메서드
    public static <T> ApiResponse<T> success(T data) {

        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .error(null)
                .build();
    }

    //실패 응답 생성 메서드
    public static ApiResponse<Void> fail(ApiErrorResponse error) {

        return ApiResponse.<Void>builder()
                .success(false)
                .data(null)
                .error(error)
                .build();
    }


}
