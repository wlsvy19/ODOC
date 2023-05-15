package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {
	private static Map<Long, Member> store = new HashMap<>();
	private static long sequence = 0L;

	public Member save(Member member) {
		// 회원가입시 시퀀스 1개 +
		member.setId(++sequence);
		log.info("멤버 save: member={}", member);
		store.put(member.getId(), member);
		return member;
	} // end save()

	// 특정 회원 찾기
	public Member findById(Long id) {
		return store.get(id);
	} // end findById()

	// 특정 로그인ID 찾기 - Optional 넣는이유: 회원이 있을수도 없을수도 있어서
	public Optional<Member> findByLoginId(String loginId) {
		// 1. 모든 회원 목록 가져오기
//		List<Member> all = findAll();
//		for (Member m : all) {
//			// 반복문 돌면서 매개변수로 넘어온 회원id 있는지 확인
//			if (m.getLoginId().equals(loginId)) {
//				return Optional.of(m);
//			}
//		}
		// return Optional.empty();

		// 위의 코드를 한줄로 변환가능
		// List를 쭉 돌면서 filter안에 있는 조건식이 맞으면 findFirst()를 통해 해당 객체 반환 
		return findAll().stream().filter(member -> member.getLoginId().equals(loginId)).findFirst();
	} // end findByLoginId()

	// 전체 회원 찾기
	public List<Member> findAll() {
		// Map의 value들만 리턴
		return new ArrayList<>(store.values());
	} // end findAll()

	// 테스트할때 초기화
	public void clearStore() {
		store.clear();
	}
}
