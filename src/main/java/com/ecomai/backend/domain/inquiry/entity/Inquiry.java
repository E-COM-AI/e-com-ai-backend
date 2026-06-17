package com.ecomai.backend.domain.inquiry.entity;

import com.ecomai.backend.domain.inquiry.enums.InquiryStatus;
import com.ecomai.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

    // FK (초기에는 단순 Long 유지 - 실무 기본 구조)
    @Column(name = "user_id", nullable = false)
    private Long userId;

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
    public void changeStatus(String status) {
        this.status = status;
    }
}