package com.fastcampus.jpa.bookmanager.repository;

import com.fastcampus.jpa.bookmanager.domain.Address;
import com.fastcampus.jpa.bookmanager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
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

	// WHERE 조건에 AND
	List<User> findByEmailAndName(String email, String name);

	List<User> findByEmailOrName(String email, String name);

	// WHERE 조건에 부등호 >, 이후에 만들어진 데이터
	List<User> findByCreatedAtAfter(LocalDateTime yesterday);

	// WHERE 조건에 부등호 <, 이전에 만들어진 데이터
	List<User> findByUpdatedAtBefore(LocalDateTime tomorrow);

	// >
	List<User> findByIdAfter(Long id);

	// >
	List<User> findByCreatedAtGreaterThan(LocalDateTime yesterday);

	// >=
	List<User> findByCreatedAtGreaterThanEqual(LocalDateTime yesterday);

	// Between
	List<User> findByCreatedAtBetween(LocalDateTime yesterday, LocalDateTime tomorrow);

	List<User> findByIdBetween(Long id1, Long id2);

	// Between 풀어쓴 메서드
	List<User> findByIdGreaterThanEqualAndIdLessThanEqual(Long id1, Long id2);

	// user0_.id is not null
	List<User> findByIdIsNotNull();

	// List<Address> findByAddressIsNotEmpty();

	List<User> findByNameIn(List<String> names);

	List<User> findByNameLike(String name);
	List<User> findByNameStartingWith(String name);
	List<User> findByNameEndingWith(String name);
	List<User> findByNameContains(String name);

	List<User> findUserByNameIs(String name);
	List<User> findUserByName(String name);
	List<User> findUserByNameEquals(String name);
}
