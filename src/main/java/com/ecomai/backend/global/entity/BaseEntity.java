package com.ecomai.backend.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 공통 엔티티
 * - 생성/수정/삭제 정보 관리
 */
@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * 업데이트 시각 갱신 (service layer에서 호출)
     */
    public void markUpdated() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * soft delete 처리
     */
    public void delete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}