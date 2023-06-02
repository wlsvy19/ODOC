package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("log filter init...");
	}

	// 클라이언트에서 요청이 올때마다 doFilter 메서드 호출
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		log.info("log filter doFilter");

		// HTTP요청에 대해 필터적용 하기 위해 다운 캐스팅, ServletRequest 부모라서 기능이 별로 없음
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String requestURI = httpServletRequest.getRequestURI();

		// HTTP 요청을 구분하기 위해 생성
		String uuid = UUID.randomUUID().toString();

		try{
			log.info("###############요청: [{}] [{}]", uuid, requestURI);
			
			// chain: 다음 필터 호출, 필터가 없으면 서블릿 호출됨(여기선 dispatcher 서블릿->컨트롤러 호출)
			// 이 로직을 타지 않으면 서블릿이 호출되지 않기 때문에 화면이 멈춰버림
			chain.doFilter(request, response);
		} catch(Exception e) {
			throw  e;
		} finally {
			log.info("###############응답: [{}][{}]", uuid, requestURI);
		}
	}

	@Override
	public void destroy() {
		log.info("log filter destroy...");
	}
}
