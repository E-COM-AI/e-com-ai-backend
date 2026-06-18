package com.ecomai.backend.domain.inquiry.entity;

import com.ecomai.backend.domain.inquiry.enums.InquiryStatus;
import com.ecomai.backend.domain.member.entity.Member;
import com.ecomai.backend.global.entity.BaseEntity;
import com.ecomai.backend.global.exception.BusinessException;
import com.ecomai.backend.global.response.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 고객 문의 Entity
 * - AI 분석 결과 포함 구조
 */
@Entity
@Table(name = "inquiries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Inquiry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //(fetch = FetchType.EAGER)는 N+1문제
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // AI 분석 결과
    private String category;
    private String sentiment;

    @Column(name = "toxicity_score")
    private Double toxicityScore;

    private Boolean toxicity;

    @Column(name = "priority_score", precision = 5, scale = 2)
    private BigDecimal priorityScore;

    private String priority;

    @Column(name = "ai_summary", columnDefinition = "TEXT")
    private String aiSummary;

    @Column(name = "ai_processed", nullable = false)
    @Builder.Default
    private boolean aiProcessed = false;

    /**
     * 문의 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private InquiryStatus status = InquiryStatus.OPEN;

    /**
     * AI 처리 완료 상태 변경
     */
    public void markAiProcessed() {
        this.aiProcessed = true;
    }

    /**
     * 상태 변경
     */
    public void changeStatus(InquiryStatus status) {
        this.status = status;
    }

    /**
     * 문의 수정
     * OPEN(오픈,답변 대기) 상태일 때만 수정 가능
     */
    public void update(String title, String content) {
        validateOpenStatus(); // 공통 상태 검증 로직 분리
        this.title = title;
        this.content = content;
    }

    /**
     * 문의 취소 (Soft Delete)
     * OPEN(오픈,답변 대기) 상태일 때만 취소 가능
     */
    public void cancel() {
        validateOpenStatus(); // 취소할 때도 OPEN 상태인지 검증!
        super.delete();       // 뚱이님이 말씀하신 부모(super)의 BaseEntity.delete() 호출!
    }

    /**
     * 본인 문의 검증 (소유권 검증)
     */
    public void validateOwner(Long memberId) {
        if (!this.member.getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
    }

    /**
     * OPEN 상태 검증 공통 메서드 (내부 캡슐화)
     */
    private void validateOpenStatus() {
        if (this.status != InquiryStatus.OPEN) {
            throw new BusinessException(ErrorCode.INVALID_INQUIRY_STATUS);
        }
    }

}