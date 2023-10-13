package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 인터셉터: Spring MVC에서 제공, 필터는 서블릿에서 제공
 * 왠만하면 인터셉터 써라
 */
@Slf4j
public class LogInterceptor implements HandlerInterceptor {
	public static final String LOG_ID = "logID";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestURI = request.getRequestURI();
		String uuid = UUID.randomUUID().toString();
		request.setAttribute(LOG_ID, uuid);

		if (handler instanceof HandlerMethod) {
			HandlerMethod hm = (HandlerMethod) handler; // 호출한 컨트롤러 메서드의 모든 정보가 포함되어 있음
		}

		log.info("인터셉터 Request: [{}] [{}] [{}]", uuid, requestURI, handler);
		return true; // 다음 인터셉터 or 컨트롤러 호출
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		log.info("postHandle 호출, ModelAndView값: [{}]", modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		String requestURI = request.getRequestURI();
		Object logId = (String) request.getAttribute(LOG_ID);
		log.info("인터셉터 Response: [{}] [{}] [{}]", logId, requestURI, handler);
		if (ex != null) {
			log.error("afterCompletion() 에서 발생한 에러: ", ex);
		}
	}
}
