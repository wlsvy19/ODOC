package spring.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.core.discount.DiscountPolicy;
import spring.core.discount.RateDiscountPolicy;
import spring.core.member.MemberRepository;
import spring.core.member.MemberService;
import spring.core.member.MemberServiceImpl;
import spring.core.member.MemoryMemberRepository;
import spring.core.order.OrderService;
import spring.core.order.OrderServiceImpl;

/*구성영역*/

/*앱 전체를 설정하고 구성하는 감독 역할 -> 실제 동작에 필요한 구현 객체 생성*/
/*객체를 생성하고 연결하는 역할에만 집중 -> 관심사의 분리 완수*/
@Configuration
public class AppConfig {

    /*메소드명만 보면 역할이 보임*/

    @Bean
    public MemberService memberService() {
        /*실제로 어떤 구현체를 쓸지 결정*/
        /*MemberServiceImpl 입장에선 AppConfig가 의존관계를 주입 -> 의존관계 주입 or 의존성 주입*/
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public MemberRepository memberRepository() {
        /*추후 DB 변경시 아래 한굴만 고쳐주면 됨*/
        /*리턴 타입만 보면 실제 구현이 보임*/
        return new MemoryMemberRepository();
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        /*정액 할인 정책*/
        //return new FixDiscountPolicy();

        /*정률 할인 정책으로 변경/*
        /*사용역역의 어떠한 코드도 변경할 필요 x */
        /*<의존관계 주입> : 앱 실행 시점에 실제 구현 객체를 생성하고 클라이언트에 전달해서 클라이언트와 서버의 실제 의존관계 연결*/
        return new RateDiscountPolicy();
    }
}
