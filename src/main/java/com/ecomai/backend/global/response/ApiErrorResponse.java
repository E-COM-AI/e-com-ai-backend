package com.ecomai.backend.global.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiErrorResponse {
    private final String code;//에러코드
    private final String message; //에러메시지
}
