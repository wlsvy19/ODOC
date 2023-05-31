package hello.login.web.session;

import hello.login.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 세션 테스트
 */
public class SessionManagerTest {
	SessionManager sessionManager = new SessionManager();

	@Test
	void sessionTest() {
		// 서버 -> 클라이언트: 세션 생성
		// HttpServletResponse는 인터페이스라 구현체가 없음 -> 스프링에서 제공하는 Mock 사용
		MockHttpServletResponse response = new MockHttpServletResponse();
		Member member = new Member();
		sessionManager.createSession(member, response);

		// 클라이언트 -> 서버: 요청에 응답 쿠키 저장 되는지?
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setCookies(response.getCookies()); // mySessionId = asjldkjqwlkrejlkqasnd.....

		// 세션 조회 테스트
		Object result = sessionManager.getSession(request);
		assertThat(result).isEqualTo(member);

		// 세션 만료 테스트
		sessionManager.expire(request); // 세션제거 했음
		Object expired = sessionManager.getSession(request);
		assertThat(expired).isNull(); // expired가 null이냐?
	}
}
