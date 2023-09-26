package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 세션이 제공하는 정보 확인
 * 로그인후 http://localhost:8089/session-info 새로고침 하여 확인
 */

@Slf4j
@RestController
public class SessionInfoController {


	@GetMapping("/session-info")
	public String sessionInfo(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if(session == null){
			return "세션이 존재하지 않습니다.";
		}

		log.info("###########################세션정보############################");
		// 세션의 여러 속성들 출력
		session.getAttributeNames().asIterator()
				.forEachRemaining(name -> log.info("session name={}, value={}", name, session.getAttribute(name)));

		log.info("SessionId={}", session.getId());

		// 세션 유효시간: 1800초-30분
		log.info("MaxInactiveInterval={}", session.getMaxInactiveInterval());

		// 세션 생성 시간
		log.info("CreationTime()={}", new Date(session.getCreationTime()));
		
		// 세션과 연결된 사용자가 최근에 서버에 접근한 시간
		log.info("LastAccessedTime()={}", new Date(session.getLastAccessedTime()));

		// 새로만들어진 세션이냐?
		log.info("isNew={}", session.isNew());

		return "세션 출력";
	}
}
