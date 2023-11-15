package com.fastcampus.jpa.bookmanager.repository;

import com.fastcampus.jpa.bookmanager.domain.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.*;

@TestMethodOrder(MethodOrderer.MethodName.class) // method 순차 실행 적용(알파벳순)
@SpringBootTest
class JPADefaultMethodTest {
	@Autowired
	private UserRepository userRepository;

	@Test
	void a_findAll() {
		// 모든 유저 목록 조회
		// userRepository.save(new User(7L, "test", "test@test@.com"));
		// System.out.printf("User 모든 목록 " + userRepository.findAll());

//		for (User user : userRepository.findAll()) {
//			System.out.println("user = " + user);
//		}
		// 위에 축약한게 아래, 람다식 활용
		//userRepository.findAll().forEach(System.out::println);

		System.out.println("##### 테스트1. 모든 유저 리스트 #####");
		List<User> users = userRepository.findAll();
		users.forEach(System.out::println);
	}

	@Test
	void b_sort() {
		// Sort 해서 유저목록 조회: 이름 역순 정렬
		System.out.println("##### 테스트2. 이름 역정렬한 유저 리스트  #####");
		List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));
		users.forEach(System.out::println);
	}

	@Test
	void c_findAllById() {
//		List<Long> ids = new ArrayList<>();
//		ids.add(1L);
//		ids.add(3L);
//		ids.add(5L);
		// 위에 코드를 아래 한줄로 축약
		// where 절 넣어서 유저 리스트 출력
		System.out.println("##### 테스트3. 특정 ID(WHERE절 사용) 유저 리스트 #####");
		List<User> users = userRepository.findAllById(Lists.newArrayList(1L, 3L, 5L));
		users.forEach(System.out::println);
	}

	@Test
	void d_saveAll() {
		// 모든 유저 추가 테스트
		System.out.println("##### 테스트4. 모든 유저 추가 후 saveAll() #####");
		User insertTestUser4 = new User("test1 user", "test1@test.com");
		User insertTestUser5 = new User("test2 user", "test2@test.com");
		userRepository.saveAll(Lists.newArrayList(insertTestUser4, insertTestUser5));
		List<User> users = userRepository.findAll();
		users.forEach(System.out::println);
	}

	@Test
	void e_save() {
		// 특정 유저 추가 테스트
		System.out.println("##### 테스트5. 1명의 유저 추가 save() #####");
		User insertTestUser6 = new User("test3 user", "test3@test.com");
		userRepository.save(insertTestUser6);
		List<User> users = userRepository.findAll();
		users.forEach(System.out::println);
	}

	@Test
	@Transactional
	void f_getOne() {
		System.out.println("##### 테스트6. getOne() #####");
		// getOne() 은 세션이 필요함, 세션유지 위해선 @Transactional 필요
		// getOne() 은 Entity에 대해 lazyLoading 임
		User user = userRepository.getOne(1L);
		System.out.println("user = " + user);
	}

	@Test
	void g_findById() {
		System.out.println("##### 테스트7. findById() #####");
		Optional<User> id = userRepository.findById(5L);
		System.out.println("id = " + id);
	}

	@Test
		//@Transactional // 붙지않은 상태에서 save하면 그대로 반영 됨
	void h_flush() {
		System.out.println("##### 테스트8. flush() #####");
		// @Transactional 이 붙고, save를 하면 저장안됨 -> flsh를 해야 DB에 반영
		userRepository.save(new User("new User2", "new_user@test.com"));
		userRepository.flush(); //영속성 컨텍스트의 변경 내용을 DB에 동기화
		userRepository.findAll().forEach(System.out::println);
	}

	@Test
	void i_saveAndFlush() {
		System.out.println("##### 테스트9. saveAndFlush() #####");
		userRepository.saveAndFlush(new User("new User!!", "new_user@test.com"));
		userRepository.findAll().forEach(System.out::println);
	}

	// 카운트 메서드
	@Test
	void j_count() {
		long count = userRepository.count();
		System.out.println("count = " + count);
	}

	@Test
	void k_existsById() {
		// ID값 1인 데이터 있냐?
		boolean exists = userRepository.existsById(13L);
		System.out.println("exists = " + exists);
	}

	//삭제 메서드
	@Test
	void l_delete() {
		long deleteId = 9L;
		//userRepository.delete(userRepository.findById(deleteId).orElseThrow(RuntimeException::new));
		boolean existsId = userRepository.existsById(deleteId);
		if (!existsId) {
			System.out.println(deleteId + " 아이디 삭제됨");
		}
	}


	@Test
	void m_deleteAll() {
		// 모든 데이터 삭제 메서드, 삭제시 각각의 데이터가 있는지 select 후 삭제 함
		userRepository.deleteAll();
		// 인자를 줘서 여러개 삭제
		//userRepository.deleteAll(userRepository.findAllById(Lists.newArrayList(14L, 15L, 16L, 17L, 18L)));
		long count = userRepository.count();
		System.out.println("count = " + count);
	}

	@Test
	void n_deleteInBatch() {
		// deleteInBatch는 where뒤에 or로 묶어서 쿼리가 한번만 시행됨
		userRepository.deleteInBatch(userRepository.findAllById(Lists.newArrayList(2L, 4L, 5L)));
	}

	@Test
	void o_deleteAllInBatch() {
		// delete from user  해버림; 즉 where절 없음
		userRepository.deleteAllInBatch();
	}

	// 페이징 처리 간편함
	@Test
	void p_paging() {
		// PageRequest.of 의 첫번째 페이지의 index는 0부터 시작함
		Page<User> page = userRepository.findAll(PageRequest.of(1, 3)); // 2번째 페이지, 한페이지당 크기는3
		System.out.println("page: " + page);
		System.out.println("Total Elements: " + page.getTotalElements());
		System.out.println("Total Pages: " + page.getTotalPages());
		System.out.println("NumberOfElements: " + page.getNumberOfElements());
		System.out.println("Sort: " + page.getSort());
		System.out.println("Size: " + page.getSize()); // 페이징할 때 나누는 크기

		page.getContent().forEach(System.out::println);
	}

	// ExampleMatcher -> 쿼리에 LIKE 사용, 단방향
	@Test
	void q_queryByExample() {
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withIgnorePaths("name") // 매칭에서 제외
				//.withMatcher("email", endsWith())
				.withMatcher("email", startsWith());// 매핑에서 포함

		Example<User> example = Example.of(new User("ma", "jinpyo"), matcher);
		userRepository.findAll(example).forEach(System.out::println);
	}

	@Test
	void r_queryByExample2() {
		User user = new User();
		user.setEmail("slow");

		// contains: %like% 양방향 LIKE 검색
		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("email", contains());
		Example<User> example = Example.of(user, matcher);
		userRepository.findAll(example).forEach(System.out::println);
	}

	@Test
	void s_save내부동작() {
		// 여기 save는 insert
		userRepository.save((new User("david", "david@test.com")));
		User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
		user.setEmail("wlsvy19@gamil.com");

		// 여기 save는 update
		userRepository.save(user);
	}
}