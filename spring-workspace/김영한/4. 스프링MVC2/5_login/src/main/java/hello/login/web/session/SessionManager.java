package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션 관리 객체 - 쿠키에 세션ID(랜덤)를 담아 클라이언트에 전달 
 */
@Component // 스프링 컨테이너에 이 클래스를 스프링빈 으로 등록
public class SessionManager {
	public static final String SESSION_COOKIE_NAME = "mySessionId";
	// ConcurrentHashMap: 동시요청 처리 가능(멀티스레드)
	private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

	/**
	 * 세션 생성
	 */
	public void createSession(Object value, HttpServletResponse response) {
		// 1. sessionId 생성 (임의의 추정 불가능한 랜덤 값)
		// UUID: Universally Unique
		String sessionId = UUID.randomUUID().toString();

		// 2. 세션 저장소에 sessionId와 보관할 값 저장
		sessionStore.put(sessionId, value);

		// 3. sessionId로 응답 쿠키를 생성
		Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);

		// 4. 클라이언트에 전달
		response.addCookie(mySessionCookie);
	} // end createSession()

	/**
	 * 세션 조회
	 */
	public Object getSession(HttpServletRequest request) {
		Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
		if (sessionCookie == null) {
			return null;
		}
		return sessionStore.get(sessionCookie.getValue());
	} // end getSession()

	/**
	 * 세션 만료
	 */
	public void expire(HttpServletRequest request) {
		Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
		// 세션이 있으면 세션을 제거
		if (sessionCookie != null) {
			sessionStore.remove(sessionCookie.getValue());
		}
	} // end expire()

	public Cookie findCookie(HttpServletRequest request, String cookieName) {
		// 쿠키가 없을경우 null 리턴
		if (request.getCookies() == null) {
			return null;
		}
		// 쿠키가 있을 경우
		// 1) 배열을 스트림으로 변환 후 필터 적용해서 반복문 돌림
		return Arrays.stream(request.getCookies()) // 2) request.getCookies() 순회
				.filter(cookie -> cookie.getName().equals(cookieName)) // 3) cookie.getName()이 있으면
				.findAny() // 4) 그걸 찾음
				.orElse(null); // 5) 아니면 null
	} // end findCookie()

}
