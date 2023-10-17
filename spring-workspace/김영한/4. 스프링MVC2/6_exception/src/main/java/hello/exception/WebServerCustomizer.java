package hello.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

// 스프링에 컴포넌트로 등록 해야 동작
// 스프링 부트에서 제공하는 에러 페이지 사용을 위해 @Component 주석 처리
//@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

	@Override
	public void customize(ConfigurableWebServerFactory factory) {
		// 에러 페이지를 만들고 이동, path는 컨트롤러에 매핑 되는 경로임(ErrorPageController)
		ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
		ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
		
		// 2. 런타임시 발생 하는 에러
		ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");

		// 등록
		factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
	}
}
