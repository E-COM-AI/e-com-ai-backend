package com.ecomai.backend.domain.inquiry.repository;

import com.ecomai.backend.domain.inquiry.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InquiryRepository
        extends JpaRepository<Inquiry, Long> {

    /**
     * 문의 단건 조회
     *
     * Soft Delete 처리된 데이터는 제외
     *
     * @param inquiryId 문의 ID
     * @return 문의 정보
     */
    Optional<Inquiry> findByIdAndIsDeletedFalse(
            Long inquiryId
    );

    /**
     * 로그인 회원의 문의 목록 조회
     *
     * Soft Delete 제외
     */
    Page<Inquiry> findAllByMember_IdAndIsDeletedFalse(
            Long memberId,
            Pageable pageable
    );

    /**
     * 로그인 회원의 문의 목록 조회
     *
     * Soft Delete 처리된 데이터는 제외
     * 생성일 기준 최신순 조회 예정
     *
     * @param memberId 회원 ID
     * @param pageable 페이지 정보
     * @return 문의 목록
     */
    Page<Inquiry> findAllByMemberIdAndIsDeletedFalse(
            Long memberId,
            Pageable pageable
    );
}