package spring.core.scan.filter;

import java.lang.annotation.*;

/*@ComponentScan 에 추가할 컴포넌트*/
@Target(ElementType.TYPE) /*클래스 레벨에 붙음*/
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyIncludeComponent {
}
