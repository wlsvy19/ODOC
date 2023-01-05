package spring.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.core.AppConfig;
import spring.core.member.MemberRepository;
import spring.core.member.MemberServiceImpl;
import spring.core.member.MemoryMemberRepository;
import spring.core.order.OrderServiceImpl;

import static org.assertj.core.api.Assertions.*;

public class ConfigSingletonTest {
    @Test
    void configTest() {
        /*AppConfig에서 memberService와  orderService를 사용할 경우, new MemoryMemberRepository()가 2번 일어나는데 이 두 객체는 다를까?*/
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        /*Impl로 꺼내야 테스트용도  get메서드 사용 가능*/
        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();

        System.out.println("memberService -> memberRepository1 = " + memberRepository1);
        System.out.println("orderService -> memberRepository2 = " + memberRepository2);
        System.out.println("memberRepository3 = " + memberRepository);

        assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
        assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);

        /*new MemoryMemberRepository()가 두번 일어나는데 어떻게 객체가 같을까? -> @Configuration이 붙으면 여러 과정을 통해 싱글톤이 보장 됨*/
    }

    /*@Configuration이 붙으면?*/
    @Test
    void configDeep() {
        /*인자로 넘긴 클래스도 스프링 빈으로 등록*/
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig bean = ac.getBean(AppConfig.class);
        /*AppConfig를 상속받아 임의로 만들어진 클래스 AppConfig.CGLIB 이 나옴*/
        System.out.println("bean.getClass() = " + bean.getClass());
    }
}
