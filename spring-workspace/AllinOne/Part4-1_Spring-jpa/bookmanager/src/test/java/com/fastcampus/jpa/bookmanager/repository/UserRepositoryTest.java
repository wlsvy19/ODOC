package com.fastcampus.jpa.bookmanager.repository;

import com.fastcampus.jpa.bookmanager.domain.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class UserRepositoryTest {
	@Autowired
	private UserRepository userRepository;

	@Test
	// @Transactional
	void crud() {
		//userRepository.save(new User(7L, "test", "test@test@.com"));
		// System.out.printf("User 모든 목록 " + userRepository.findAll());

//		for (User user : userRepository.findAll()) {
//			System.out.println("user = " + user);
//		}
		// 위에 축약한게 아래, 람다식 활용
		//userRepository.findAll().forEach(System.out::println);


		// 모든 유저 목록 조회
		System.out.println("##### 테스트1. 모든 유저 리스트 #####");
		List<User> users1 = userRepository.findAll();
		users1.forEach(System.out::println);

		// Sort 해서 유저목록 조회: 이름 역순 정렬
		System.out.println("##### 테스트2. 이름 역정렬한 유저 리스트  #####");
		List<User> users2 = userRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));
		users2.forEach(System.out::println);

//		List<Long> ids = new ArrayList<>();
//		ids.add(1L);
//		ids.add(3L);
//		ids.add(5L);
		// 위에 코드를 한줄로 축약
		// where 절 넣어서 유저 리스트 출력
		System.out.println("##### 테스트3. 특정 ID(WHERE절 사용) 유저 리스트 #####");
		List<User> users3 = userRepository.findAllById(Lists.newArrayList(1L, 3L, 5L));
		users2.forEach(System.out::println);

		// 모든 유저 추가 테스트
		System.out.println("##### 테스트4. 모든 유저 추가 후 saveAll() #####");
		User insertTestUser4 = new User("test1 user", "test1@test.com");
		User insertTestUser5 = new User("test2 user", "test2@test.com");
		// userRepository.saveAll(Lists.newArrayList(insertTestUser4, insertTestUser5));
		List<User> users4 = userRepository.findAll();
		users4.forEach(System.out::println);

		// 특정 유저 추가 테스트
		System.out.println("##### 테스트5. 1명의 유저 추가 save() #####");
		User insertTestUser6 = new User("test3 user", "test3@test.com");
		// userRepository.save(insertTestUser6);
		users4.forEach(System.out::println);

		System.out.println("##### 테스트6. getOne() #####");
		// getOne() 은 세션이 필요함, 세션유지 위해선 @Transactional 필요
		// getOne() 은 Entity에 대해 lazyLoading 임
		// User user4 = userRepository.getOne(1L);
		// System.out.println("user4 = " + user4);

		System.out.println("##### 테스트7. findById() #####");
		Optional<User> id = userRepository.findById(5L);
		System.out.println("id = " + id);
	}
}