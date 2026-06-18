package com.ecomai.backend.domain.inquiry.dto.response;

import com.ecomai.backend.domain.inquiry.enums.InquiryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "사용자 문의 목록 조회 응답")
@Builder
public record InquiryListResponse(

        @Schema(
                description = "문의 ID",
                example = "1"
        )
        Long inquiryId,

        @Schema(
                description = "문의 제목",
                example = "배송 문의"
        )
        String title,

        @Schema(
                description = "문의 상태",
                example = "OPEN"
        )
        InquiryStatus status,

        @Schema(description = "문의 상태(한글)", example = "답변 대기")
        String statusDisplay,

//        @Schema(
//                description = "AI 분석 완료 여부",
//                example = "true"
//        )
//        boolean aiProcessed,

        @Schema(
                description = "문의 생성 시각",
                example = "2026-06-18 14:30:00"
        )
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdAt
) {
}
