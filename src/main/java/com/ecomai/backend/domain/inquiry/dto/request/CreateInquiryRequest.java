package com.ecomai.backend.domain.inquiry.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 문의 등록 요청 DTO
 */
@Getter
@NoArgsConstructor
@Schema(description = "문의 등록 요청")
public class CreateInquiryRequest {

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 255, message = "제목은 255자 이하만 가능합니다.")
    @Schema(
            description = "문의 제목",
            example = "배송 문의"
    )
    private String title;

    @NotBlank(message = "문의 내용은 필수입니다.")
    @Schema(
            description = "문의 내용",
            example = "언제 배송되나요?"
    )
    private String content;
}
