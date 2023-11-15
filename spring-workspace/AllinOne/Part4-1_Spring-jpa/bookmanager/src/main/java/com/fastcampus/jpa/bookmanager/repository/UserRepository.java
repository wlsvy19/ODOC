package com.fastcampus.jpa.bookmanager.repository;

import com.fastcampus.jpa.bookmanager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

/**
 * SpringDataJpa가 지원 해주는 영역
 */
public interface UserRepository extends JpaRepository<User, Long> {
	// Query-Method: 메서드의 이름을 분석하여 알맞는 쿼리 실행, 접두사 자동 인식
	Set<User> findByName(String name);

	User findByEmail(String email);

	User getByEmail(String email);

	User readByEmail(String email);

	User queryByEmail(String email);

	User searchByEmail(String email);

	User streamByEmail(String email);

	User findUserByEmail(String email);

	User findSomethingByEmail(String email);

	// 네이밍 규칙에 의해 에러
	//User findByByEmail(String email);

	// 쿼리에 LIMIT 1 붙음
	List<User> findFirst1ByName(String name);
	List<User> findFirst2ByName(String name);

	// 쿼리에 LIMIT 붙음
	List<User> findTop1ByName(String name);

	List<User> findLast1ByName(String name);
}
