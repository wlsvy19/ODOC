package spring.core.autowired;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import spring.core.member.Member;

import java.util.Optional;

/*@Autowired 테스트*/
public class AutowiredTest {
    @Test
    void autowiredOption() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);

    }

    
    static class TestBean {

        /*Member가 스프링 컨테이너에 스프링빈으로 등록된게 아니기 때문에 true면 예외터짐*/
        @Autowired(required = false)
        /*false일 경우 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출 안됨*/
        public void setNoBean1(Member noBean1) {
            System.out.println("noBean1 = " + noBean1);
        }

        /*호출은 되는데, Null값으로 들어옴*/
        @Autowired
        public void setNoBean2(@Nullable Member noBean2) {
            System.out.println("noBean2 = " + noBean2);
        }

        /*자동 주입할 대상이 없으면 Optional.empty 가 입력*/
        @Autowired
        public void setNoBean3(Optional<Member> noBean3) {
            System.out.println("noBean3 = " + noBean3);
        }
    }
}
