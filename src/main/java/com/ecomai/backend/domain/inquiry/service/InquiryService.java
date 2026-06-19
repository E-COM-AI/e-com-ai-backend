package com.ecomai.backend.domain.inquiry.service;

import com.ecomai.backend.domain.inquiry.dto.request.CreateInquiryRequest;
import com.ecomai.backend.domain.inquiry.dto.request.UpdateInquiryRequest;
import com.ecomai.backend.domain.inquiry.dto.response.CreateInquiryResponse;
import com.ecomai.backend.domain.inquiry.dto.response.InquiryDetailResponse;
import com.ecomai.backend.domain.inquiry.dto.response.InquiryListResponse;
import com.ecomai.backend.domain.inquiry.dto.response.UpdateInquiryResponse;
import com.ecomai.backend.domain.inquiry.entity.Inquiry;
import com.ecomai.backend.domain.inquiry.enums.InquiryStatus;
import com.ecomai.backend.domain.inquiry.repository.InquiryRepository;
import com.ecomai.backend.domain.member.entity.Member;
import com.ecomai.backend.domain.member.repository.MemberRepository;
import com.ecomai.backend.global.exception.BusinessException;
import com.ecomai.backend.global.response.ErrorCode;
import com.ecomai.backend.global.response.PageResponse;
import com.ecomai.backend.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * 문의 서비스
 * 문의 관련 비즈니스 로직 담당
 */
@Service
@RequiredArgsConstructor
public class InquiryService {
    /**
     * 문의 Repository
     */
    private final InquiryRepository inquiryRepository;

    /**
     * 회원 Repository
     */
    private final MemberRepository memberRepository;

    /**
     * 현재 로그인 사용자 조회
     */
    private final SecurityUtil securityUtil;

    /**
     * 문의 등록
     *
     * 현재 로그인 회원의 문의를 생성한다.
     *
     * @param request 문의 등록 요청
     * @return 문의 등록 결과
     */
    @Transactional
    public CreateInquiryResponse createInquiry(CreateInquiryRequest request) {

        // 현재 로그인 회원 ID 조회
        Long memberId =
                securityUtil.getCurrentMemberId();

        // 회원 존재 여부 검증
        Member member =
                memberRepository.findById(memberId)
                        .orElseThrow(
                                () -> new BusinessException(
                                        ErrorCode.MEMBER_NOT_FOUND
                                )
                        );

        // 문의 엔티티 생성
        Inquiry inquiry =
                Inquiry.builder()
                        .member(member)
                        .title(request.getTitle())
                        .content(request.getContent())
                        //.status(InquiryStatus.OPEN) 상태 기본값 entity가 책임 @Builder.Default 해놨음
                        .build();

        // 문의 저장
        Inquiry savedInquiry =
                inquiryRepository.save(inquiry);
        inquiryRepository.flush();


        // 응답 DTO 반환
        return CreateInquiryResponse.builder()
                .inquiryId(savedInquiry.getId())
                .title(savedInquiry.getTitle())
                .status(savedInquiry.getStatus().name())
                .statusDisplay(inquiry.getStatus().getDisplay())
                .createdAt(savedInquiry.getCreatedAt())
                .build();
    }

    /**
     * 내 문의 목록 조회
     * * SecurityContext에서 현재 로그인한 회원의 ID를 추출하여
     * 해당 회원이 작성한 문의 목록을 페이징 처리하여 조회합니다.
     *
     * @param pageable 페이지 번호, 사이즈, 정렬 정보가 포함된 Pageable 객체
     * @return 조회된 문의 목록(Page<InquiryListResponse>)
     */
    @Transactional(readOnly = true)
    public PageResponse<InquiryListResponse> getMyInquiries(String status, Pageable pageable) {

        // 현재 인증된 회원의 고유 식별자(ID)를 보안 컨텍스트에서 획득
        Long memberId = securityUtil.getCurrentMemberId();

        Page<Inquiry> inquiryPage;

        // 상태 필터 없음
        if (status == null || status.isBlank()) {

            inquiryPage =
                    inquiryRepository.findAllByMemberIdAndIsDeletedFalse(
                            memberId,
                            pageable
                    );

        } else {

            InquiryStatus inquiryStatus;

            try {

                inquiryStatus = InquiryStatus.valueOf(
                        status.toUpperCase()
                );

            } catch (IllegalArgumentException e) {

                throw new BusinessException(
                        ErrorCode.INVALID_INQUIRY_STATUS_FILTER
                );
            }

            inquiryPage =
                    inquiryRepository.findAllByMemberIdAndStatusAndIsDeletedFalse(
                            memberId,
                            inquiryStatus,
                            pageable
                    );
        }


        List<InquiryListResponse> items =
                inquiryPage.getContent()
                        .stream()
                        .map(inquiry ->
                                InquiryListResponse.builder()
                                        .inquiryId(inquiry.getId())
                                        .title(inquiry.getTitle())
                                        .status(inquiry.getStatus())
                                        .statusDisplay(
                                                inquiry.getStatus().getDisplay()
                                        )
                                        .createdAt(inquiry.getCreatedAt())
                                        .build()
                        )
                        .toList();

        return PageResponse.<InquiryListResponse>builder()
                .items(items)
                .page(inquiryPage.getNumber())
                .size(inquiryPage.getSize())
                .totalElements(inquiryPage.getTotalElements())
                .totalPages(inquiryPage.getTotalPages())
                .build();
    }

    /**
     * 내 문의 상세 조회
     * * 본인의 문의가 맞는지를 검증한 후 상세 정보를 반환합니다.
     *
     * @param inquiryId 문의 ID
     * @return 문의 상세 정보
     */
    @Transactional(readOnly = true)
    public InquiryDetailResponse getMyInquiry(Long inquiryId) {

        // 1. 현재 사용자 식별자 획득
        Long memberId = securityUtil.getCurrentMemberId();

        // 2. 문의 존재 여부 확인 (Soft Delete 미포함)
        Inquiry inquiry = inquiryRepository.findByIdAndIsDeletedFalse(inquiryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INQUIRY_NOT_FOUND));

        // 3. 본인 소유의 문의인지 권한 검증
        if (!inquiry.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        // 4. 상세 정보 DTO 변환 및 반환
        return InquiryDetailResponse.builder()
                .inquiryId(inquiry.getId())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .status(inquiry.getStatus())
                .statusDisplay(inquiry.getStatus().getDisplay()) // Enum 메서드명 수정 반영
                .createdAt(inquiry.getCreatedAt())
                .build();
    }

    /**
     * 문의 수정
     * OPEN 상태에서만 수정 가능
     *
     * @param inquiryId 문의 ID
     * @param request 수정 요청
     */
    @Transactional
    public UpdateInquiryResponse updateInquiry(
            Long inquiryId,
            UpdateInquiryRequest request
    ) {

        Long memberId =
                securityUtil.getCurrentMemberId();

        Inquiry inquiry =
                inquiryRepository.findByIdAndIsDeletedFalse(
                                inquiryId
                        )
                        .orElseThrow(
                                () -> new BusinessException(
                                        ErrorCode.INQUIRY_NOT_FOUND
                                )
                        );

        /**
         * 본인 문의 검증
         */
        if (!inquiry.getMember().getId().equals(memberId)) {
            throw new BusinessException(
                    ErrorCode.FORBIDDEN
            );
        }

        /**
         * OPEN 상태만 수정 가능
         */
        if (inquiry.getStatus() != InquiryStatus.OPEN) {
            throw new BusinessException(
                    ErrorCode.INVALID_INQUIRY_STATUS
            );
        }

        inquiry.update(
                request.getTitle(),
                request.getContent()
        );

        return UpdateInquiryResponse.builder()
                .inquiryId(inquiry.getId())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .status(inquiry.getStatus())
                .statusDisplay(
                        inquiry.getStatus().getDisplay()
                )
                .updatedAt(inquiry.getUpdatedAt())
                .build();
    }

    /**
     * 문의 취소
     *
     * OPEN 상태에서만 취소 가능
     *
     * @param inquiryId 문의 ID
     */
    @Transactional
    public void cancelInquiry(
            Long inquiryId
    ) {

        Long memberId = securityUtil.getCurrentMemberId();

        Inquiry inquiry = inquiryRepository.findByIdAndIsDeletedFalse(inquiryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INQUIRY_NOT_FOUND));

        /**
         * 본인 문의 검증
         */
        inquiry.validateOwner(memberId);

        /**
         * Soft Delete 처리
         */
        inquiry.cancel();
    }

}
