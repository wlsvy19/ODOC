package spring.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/*컴포넌트 스캔이 적용 된 AppConfig*/
@Configuration

/*@Component붙은 클래스를 찾아 자동으로 스프링 빈에 등록 시킴*/
@ComponentScan(
        /*뺄거 지정 -> 여기서 AppConfig 클래스가 @Configuration 붙어 있어서 빼줄라고 지정 */
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {

}
