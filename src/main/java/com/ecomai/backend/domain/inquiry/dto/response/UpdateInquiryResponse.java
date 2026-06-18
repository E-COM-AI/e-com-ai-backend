package com.ecomai.backend.domain.inquiry.dto.response;

import com.ecomai.backend.domain.inquiry.enums.InquiryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 문의 수정 응답 DTO
 */
@Builder
@Schema(description = "문의 수정 응답")
public record UpdateInquiryResponse(

        @Schema(
                description = "문의 ID",
                example = "1"
        )
        Long inquiryId,

        @Schema(
                description = "문의 제목",
                example = "배송 문의 수정"
        )
        String title,

        @Schema(
                description = "문의 내용"
        )
        String content,

        @Schema(
                description = "문의 상태",
                example = "OPEN"
        )
        InquiryStatus status,

        @Schema(
                description = "문의 상태(한글)",
                example = "답변 대기"
        )
        String statusDisplay,

        @Schema(
                description = "수정 시각",
                example = "2026-06-18 16:00:00"
        )
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm:ss",
                timezone = "Asia/Seoul"
        )
        LocalDateTime updatedAt
) {
}