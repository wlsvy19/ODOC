package hello.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 스프링 MVC는 컨트롤러(핸들러) 밖으로 예외가 던져진 경우 예외를 해결하고, 동작을 새로 정의할 수 있는 방법을 제공
 */
@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		try {
			// localhost:8090/api/members/bad
			if (ex instanceof IllegalArgumentException) {
				// 서버내부에서 IllegalArgumentException 가 터지면 400 에러로 변경
				log.info("IllegalArgumentException 리졸버 -> 400 에러로 변경");
				// Exception 을 sendError 로 변경
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
				return new ModelAndView(); // 정상적인 리턴은 예외를 무시함
			}
		} catch (IOException e) {
			log.error("resolver Exception: ", e);
		}
		return null;
	}
}
