package hello.login;

import hello.login.web.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

// 필터 등록하기 위한 설정 클래스
@Configuration
public class WebConfig {

	// 스프링 부트가 WAS를 기동할 때 자동으로 필터를 등록
	@Bean
	public FilterRegistrationBean logFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
		// 어떤 필터를 적용할 것인지 세팅
		filterRegistrationBean.setFilter(new LogFilter());

		// 필터는 체인으로 동작, 따라서 순서가 필요함, 낮을수록 먼저 동작
		filterRegistrationBean.setOrder(1);

		// URL패턴 정하기: 모든 URL에 적용
		filterRegistrationBean.addUrlPatterns("/*");

		return filterRegistrationBean;
	}
}
