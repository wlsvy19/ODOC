package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
	private final MemberRepository memberRepository;

	/**
	 * @param loginId
	 * @param password
	 * @return null이면 로그인 실패
	 */
	
	// 실제 로그인 로직
	public Member login(String loginId, String password) {
		// Optional은 값이 존재할 수도 있고 존재하지 않을 수도 있는 컨테이너

//		Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
//		Member member = findMemberOptional.get();
//		if (member.getPassword().equals(password)) {
//			return member;
//		} else {
//			return null;
//		}

		// 풀어쓴 버전
		// Optional 이면 filter를 걸 수 있음
		return memberRepository.findByLoginId(loginId)
				.filter(member -> member.getPassword().equals(password))
				.orElse(null);
	} // end login()
}
