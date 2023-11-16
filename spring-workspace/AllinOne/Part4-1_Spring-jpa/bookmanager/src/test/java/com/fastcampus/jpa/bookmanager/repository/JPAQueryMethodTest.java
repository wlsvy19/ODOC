package com.fastcampus.jpa.bookmanager.repository;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class JPAQueryMethodTest {

	@Autowired
	UserRepository userRepository;

	@Test
	void a_select() {
		System.out.println(userRepository.findByName("dennis"));
	}

	@Test
	void b_findByEmail() {
		System.out.println("##### findByEmail: " + userRepository.findByEmail("wlsvy19@gamil.com"));
	}

	@Test
	void c_getByEmail() {
		System.out.println("##### getByEmail" + userRepository.getByEmail("martin@another.com"));
	}

	@Test
	void d_readByEmail() {
		System.out.println("##### readByEmail" + userRepository.readByEmail("wlsvy19@gamil.com"));
	}

	@Test
	void e_queryByEmail() {
		System.out.println("##### queryByEmail" + userRepository.queryByEmail("wlsvy19@gamil.com"));
	}

	@Test
	void f_searchByEmail() {
		System.out.println("##### searchByEmail" + userRepository.searchByEmail("wlsvy19@gamil.com"));
	}

	@Test
	void g_streamByEmail() {
		System.out.println("##### streamByEmail" + userRepository.streamByEmail("wlsvy19@gamil.com"));
	}

	@Test
	void h_findUserByEmail() {
		System.out.println("##### findUserByEmail" + userRepository.findUserByEmail("wlsvy19@gamil.com"));
	}

	@Test
	void i_findSomethingByEmail() {
		System.out.println("##### findSomethingByEmail" + userRepository.findSomethingByEmail("wlsvy19@gamil.com"));
	}

	/*
	@Test
	void j_findByByEmail() {
		// System.out.println("##### findByByEmail" + userRepository.findByByEmail("wlsvy19@gamil.com"));
	}
	 */

	@Test
	void j_findFirst1ByName() {
		// 쿼리에 LIMIT 1 붙음
		System.out.println("##### findFirst1ByName" + userRepository.findFirst1ByName("leejp"));
	}

	@Test
	void k_findFirst2ByName() {
		// 쿼리에 LIMIT 2 붙음
		System.out.println("##### findFirst2ByName" + userRepository.findFirst2ByName("leejp"));
	}

	@Test
	void l_findTop1ByName() {
		// 쿼리에 LIMIT 빠짐
		System.out.println("##### findLast1ByName" + userRepository.findLast1ByName("martin"));
	}

	@Test
	void m_findLast1ByName() {
		// 쿼리에 LIMIT 1 붙음
		System.out.println("##### findLast1ByName" + userRepository.findTop1ByName("leejp"));
	}

	@Test
	void n_findByEmailAndName() {
		// WHERE 조건에 AND 붙음
		System.out.println("##### findByEmailAndName" + userRepository.findByEmailAndName("wlsvy19@gamil.com", "leejp"));
	}

	@Test
	void o_findByEmailOrName() {
		// WHERE 조건에 OR 붙음
		System.out.println("##### findByEmailOrName" + userRepository.findByEmailOrName("wlsvy19@gamil.com", "leejp"));
	}

	@Test
	void p_findByCreatedAtAfter() {
		// WHERE 에 > 붙음
		System.out.println("##### findByCreatedAtAfter" + userRepository.findByCreatedAtAfter(LocalDateTime.now().minusDays(1L))); // 전날
	}

	@Test
	void q_findByUpdatedAtBefore() {
		// WHERE 에 <
		System.out.println("##### q_findByUpdatedAtBefore" + userRepository.findByUpdatedAtBefore(LocalDateTime.now()));
	}

	@Test
	void r_findByIdAfter() {
		// WHERE 에 >
		System.out.println("##### r_findByIdAfter" + userRepository.findByIdAfter(4L));
	}

	@Test
	void s_findByCreatedAtGreaterThan() {
		// WHERE에 >
		System.out.println("##### s_findByCreatedAtGreaterThan" + userRepository.findByCreatedAtGreaterThan(LocalDateTime.now().minusDays(1L)));
	}

	@Test
	void t_findByCreatedAtGreaterThanEqual() {
		// where user0_.created_at>=?
		System.out.println("##### t_findByCreatedAtGreaterThanEqual" + userRepository.findByCreatedAtGreaterThanEqual(LocalDateTime.now().minusDays(1L)));
	}

	@Test
	void u_findByCreatedAtBetween() {
		// where user0_.created_at between ? and ?
		System.out.println("##### u_findByCreatedAtBetween" + userRepository.findByCreatedAtBetween(LocalDateTime.now().minusDays(1L), LocalDateTime.now().plusDays(1L)));
	}

	@Test
	void v_findByIdBetween() {
		// Between은 양끝 값 포함임
		// where user0_.id between ? and ?
		System.out.println("##### v_findByIdBetween" + userRepository.findByIdBetween(1L, 4L));
	}

	@Test
	void w_findByIdGreaterThanEqualAndIdLessThanEqual() {
		// Between 풀어 쓴 거랑 똑같음
		// where user0_.id>=? and user0_.id<=?
		System.out.println("##### w_findByIdGreaterThanEqualAndIdLessThanEqual" + userRepository.findByIdGreaterThanEqualAndIdLessThanEqual(1L, 4L));
	}

	@Test
	void x_findByIdIsNotNull() {
		// null 체크 쿼리
		// where user0_.id is not null
		System.out.println("##### x_findByIdIsNotNull" + userRepository.findByIdIsNotNull());
	}

	/*
	@Test
	void y_findByAddressIsNotEmpty() {
		// where
		//        exists (
		// 에러: IsNotEmpty can only be used on collection properties! ->
		System.out.println("##### y_findByAddressIsNotEmpty" + userRepository.findByAddressIsNotEmpty());
	}
	*/

	@Test
	void findByNameIn() {
		//  where user0_.name in (? , ?)
		System.out.println("##### findByNameIn" + userRepository.findByNameIn(Lists.newArrayList("leejp", "martin")));
	}

	@Test
	void findByNameLike() {
		// LIKE는 %
		System.out.println("##### findByNameLike" + userRepository.findByNameLike("%ej%"));
		System.out.println("##### findByNameLike" + userRepository.findByNameLike("lee%"));
		System.out.println("##### findByNameLike" + userRepository.findByNameLike("%jp"));
	}

	/// 아래 3개 메서드는 LIKE를 풀어쓴 메서드 ///
	@Test
	void findByNameStartingWith() {
		//  where user0_.name like ? escape ?
		System.out.println("##### findByNameStartingWith" + userRepository.findByNameStartingWith("lee"));
	}

	@Test
	void findByNameEndingWith() {
		// where user0_.name like ? escape ?
		System.out.println("##### findByNameEndingWith" + userRepository.findByNameEndingWith("jp"));
	}

	@Test
	void findByNameContains() {
		// user0_.name like ? escape ?
		System.out.println("##### findByNameContains" + userRepository.findByNameContains("ej"));
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	void findIs() {
		// IS 키워드가 붙으면 단순 findBy~ 랑 똑같지만 가독성을 위해 사용
		System.out.println("##### findUserByNameIs" + userRepository.findUserByNameIs("leejp"));
		System.out.println("##### findUserByName" + userRepository.findUserByName("leejp"));
		System.out.println("##### findUserByNameEquals" + userRepository.findUserByNameEquals("leejp"));
	}
}
