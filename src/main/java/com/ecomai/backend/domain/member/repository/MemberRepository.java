package com.ecomai.backend.domain.member.repository;

import com.ecomai.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Member Repository
 *
 * - users 테이블(Member 엔티티) 접근 계층
 * - Spring Data JPA 기반 기본 CRUD 제공
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 이메일로 회원 조회
     *
     * 로그인 / 중복검사에서 사용
     *
     * @param email 사용자 이메일
     * @return Member Optional (없을 수도 있음)
     */
    Optional<Member> findByEmail(String email);

    /**
     * 이메일 존재 여부 체크
     *
     * 회원가입 시 중복 검사 용도
     *
     * @param email 사용자 이메일
     * @return true = 이미 존재
     */
    boolean existsByEmail(String email);
}
