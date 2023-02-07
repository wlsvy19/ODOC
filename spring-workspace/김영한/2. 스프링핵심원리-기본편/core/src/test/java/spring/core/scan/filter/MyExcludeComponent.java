package spring.core.scan.filter;

import java.lang.annotation.*;

/*@ComponentScan 에서 제외할 컴포넌트*/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyExcludeComponent {
}
