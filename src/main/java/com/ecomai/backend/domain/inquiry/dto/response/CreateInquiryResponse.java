package com.ecomai.backend.domain.inquiry.dto.response;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 문의 등록 응답 DTO
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "문의 등록 응답")
public class CreateInquiryResponse {
    @Schema(description = "문의 ID")
    Long inquiryId;

    @Schema(description = "문의 제목")
    String title;

    @Schema(description = "문의 상태")
    String status;

    @Schema(description = "문의 상태(한글)")
    String statusDisplay;

    @Schema(description = "생성 시각")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime createdAt;
}
