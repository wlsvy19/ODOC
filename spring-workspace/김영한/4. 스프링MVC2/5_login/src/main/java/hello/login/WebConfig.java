package hello.login;

import hello.login.web.argumentresolver.LoginMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

// 필터 등록하기 위한 설정 클래스
@Configuration
public class WebConfig implements WebMvcConfigurer {

	// 스프링 부트가 WAS를 기동할 때 자동으로 필터를 등록
	// @Bean
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

	// @Bean
	public FilterRegistrationBean loginCheckFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
		// 어떤 필터를 적용할 것인지 세팅
		filterRegistrationBean.setFilter(new LoginCheckFilter());

		// 필터는 체인으로 동작, 따라서 순서가 필요함, 낮을수록 먼저 동작
		filterRegistrationBean.setOrder(2);

		// URL패턴 정하기: 모든 URL에 적용
		filterRegistrationBean.addUrlPatterns("/*");

		return filterRegistrationBean;
	}

	@Override // 로그, 로그 인증 관련은 인터셉터 사용
	public void addInterceptors(InterceptorRegistry registry) {
		// 로그 인터셉터 등록
		registry.addInterceptor(new LogInterceptor())
				.order(1)
				.addPathPatterns("/**")
				.excludePathPatterns("/css/**", "/*.ico", "/error"); // 제외할 항목

		// 로그인 인증 인터셉터 등록
		registry.addInterceptor(new LoginCheckInterceptor())
				.order(2)
				.addPathPatterns("/**") // 모든 경로에 대해 적용
				.excludePathPatterns("/", "/members/add", "/login", "/logout", "/css/**", "/*.ico", "/error"); // 제외할 경로 정밀하게 설정 가능
	}

	// @Login 사용하기 위함
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		// 등록
		resolvers.add(new LoginMemberArgumentResolver());
	}
}
