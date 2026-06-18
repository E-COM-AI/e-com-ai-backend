package com.ecomai.backend.domain.inquiry.controller;
import com.ecomai.backend.domain.inquiry.dto.request.CreateInquiryRequest;
import com.ecomai.backend.domain.inquiry.dto.response.CreateInquiryResponse;
import com.ecomai.backend.domain.inquiry.dto.response.InquiryListResponse;
import com.ecomai.backend.domain.inquiry.service.InquiryService;
import com.ecomai.backend.global.response.ApiResponse;
import com.ecomai.backend.global.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 문의 API
 *
 * 회원 문의 관련 API 제공
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inquiries")
@Tag(
        name = "문의 API",
        description = "고객 문의 관련 API"
)
public class InquiryController {
    /**
     * 문의 서비스
     */
    private final InquiryService inquiryService;

    /**
     * 문의 등록
     * 로그인한 회원의 문의를 등록한다.
     *
     * @param request 문의 등록 요청 정보
     * @return 등록된 문의 정보
     */
    @PostMapping
    @Operation(
            summary = "문의 등록",
            description = "현재 로그인한 회원이 문의를 등록합니다."
    )
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<CreateInquiryResponse>> createInquiry(
            @Valid
            @RequestBody
            CreateInquiryRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED) // ✨ 201 Created로 설정
                .body(ApiResponse.success(inquiryService.createInquiry(request)));
    }

    /**
     * 내 문의 목록 조회
     */
    @GetMapping
    @Operation(
            summary = "내 문의 목록 조회",
            description = "현재 로그인한 회원의 문의 목록을 조회합니다."
    )
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ApiResponse<PageResponse<InquiryListResponse>> getMyInquiries(

            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {

        return ApiResponse.success(
                inquiryService.getMyInquiries(pageable)
        );
    }
}
