package com.example.hellospring.repository;

import com.example.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

/**
 * Repository: 기계적인 용어
 */
public interface MemberRepository {
    Member save(Member member);

    // 시스템에서 자동 증가되는 형식의 id
    Optional<Member> findById(Long id);

    Optional<Member> findByName(String name);

    List<Member> findAll();
}
