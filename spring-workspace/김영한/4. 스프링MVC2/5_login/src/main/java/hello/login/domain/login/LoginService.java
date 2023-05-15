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
//		Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
//		Member member = findMemberOptional.get();
//		if (member.getPassword().equals(password)) {
//			return member;
//		} else {
//			return null;
//		}

		// Optional 이면 filter를 걸 수 있음
		return memberRepository.findByLoginId(loginId)
				.filter(member -> member.getPassword().equals(password))
				.orElse(null);
	}
}
