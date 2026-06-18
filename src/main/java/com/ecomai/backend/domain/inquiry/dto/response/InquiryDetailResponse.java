package com.ecomai.backend.domain.inquiry.dto.response;

import com.ecomai.backend.domain.inquiry.enums.InquiryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 문의 상세 조회 응답
 */
@Builder
@Schema(description = "문의 상세 조회 응답")
public record InquiryDetailResponse(

        @Schema(description = "문의 ID")
        Long inquiryId,

        @Schema(description = "문의 제목")
        String title,

        @Schema(description = "문의 내용")
        String content,

        @Schema(description = "문의 상태")
        InquiryStatus status,

        @Schema(description = "문의 상태(한글)")
        String statusDisplay,

        @Schema(description = "문의 생성 시각")
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm:ss",
                timezone = "Asia/Seoul"
        )
        LocalDateTime createdAt
) {
}
