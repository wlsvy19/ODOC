package com.fastcampus.jpa.bookmanager.repository;

import com.fastcampus.jpa.bookmanager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * SpringDataJpa가 지원 해주는 영역
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
