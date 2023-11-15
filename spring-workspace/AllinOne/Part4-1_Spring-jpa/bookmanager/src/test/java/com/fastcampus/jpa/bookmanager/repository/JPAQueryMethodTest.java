package com.fastcampus.jpa.bookmanager.repository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
