package gdtcs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import gdtcs.util.LoginInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private static final long MAX_AGE_SECONDS = 36000;

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                //.allowedOrigins("*") // 추후 특정 포트만 허용하도록 변경해야함
                .allowedOriginPatterns("*") // 운영환경에서 .allowedOriginPatterns("https://example.com", "https://another.com") 등 특정 오리진만 허용하도록 수정
                .allowCredentials(true) // true: 인증 정보를 포함한 요청 여부 -> allowedOrigins에 특정 오리진 명시 해야함
                .maxAge(MAX_AGE_SECONDS);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**") // 모든 경로에 대해 인터셉터 적용
                .excludePathPatterns("/api/sample/**", "/api/login", "/javadoc/**", "/api-docs/**", "/swagger-ui/**", "/api/video/**", "/api/base/getReportImageFile", "/api/base/getWorkerProfileImage", "/api/common/getLogoImage");
    }
}
