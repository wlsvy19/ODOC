package spring.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/*컴포넌트 스캔이 적용 된 AppConfig*/
@Configuration

/*@Component붙은 클래스를 찾아 자동으로 스프링 빈에 등록 시킴*/
@ComponentScan(
        /*뺄거 지정 -> 원활한 테스트를 위해 @Configuration 들어간 모든 클래스를 제외 함 */
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class),
        
        /*어디부터 스캔 할 건지 지정 -> 지정 하지 않으면?? @ComponentScan붙은 클래스가 속한 패키지 부터 스캔함( spring.core)*/
        basePackages = "spring.core"
)
public class AutoAppConfig {

}
