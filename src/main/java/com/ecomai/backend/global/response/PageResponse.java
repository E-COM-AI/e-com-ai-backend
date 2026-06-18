package com.ecomai.backend.global.response;

import lombok.Builder;

import java.util.List;

/**
 * 공통 페이징 응답
 *
 * Page 객체를 외부로 직접 노출하지 않고
 * 필요한 정보만 전달하기 위한 DTO
 */
@Builder
public record PageResponse<T>(

        /**
         * 현재 페이지 데이터
         */
        List<T> items,

        /**
         * 현재 페이지 번호
         */
        int page,

        /**
         * 페이지 크기
         */
        int size,

        /**
         * 전체 데이터 수
         */
        long totalElements,

        /**
         * 전체 페이지 수
         */
        int totalPages
) {
}
