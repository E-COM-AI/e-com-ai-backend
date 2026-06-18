package com.ecomai.backend.domain.inquiry.service;

import com.ecomai.backend.domain.inquiry.dto.request.CreateInquiryRequest;
import com.ecomai.backend.domain.inquiry.dto.response.CreateInquiryResponse;
import com.ecomai.backend.domain.inquiry.dto.response.InquiryListResponse;
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
@Transactional(readOnly = true)
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
    public PageResponse<InquiryListResponse> getMyInquiries(Pageable pageable) {

        // 현재 인증된 회원의 고유 식별자(ID)를 보안 컨텍스트에서 획득
        Long memberId = securityUtil.getCurrentMemberId();

        Page<Inquiry> inquiryPage =
                inquiryRepository.findAllByMember_IdAndIsDeletedFalse(
                        memberId,
                        pageable
                );

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
}
