package com.ecomai.backend.domain.inquiry.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 문의 상태
 *
 * OPEN        : 접수됨
 * IN_PROGRESS : 처리중
 * CLOSED      : 완료
 */
@Getter
@RequiredArgsConstructor
public enum InquiryStatus {

    OPEN("답변 대기"),
    IN_PROGRESS("처리중"),
    CLOSED("답변 완료");

    private final String display;
}
