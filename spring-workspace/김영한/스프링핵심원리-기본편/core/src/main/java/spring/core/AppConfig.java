package spring.core;

import spring.core.discount.FixDiscountPolicy;
import spring.core.discount.RateDiscountPolicy;
import spring.core.member.MemberService;
import spring.core.member.MemberServiceImpl;
import spring.core.member.MemoryMemberRepository;
import spring.core.order.OrderService;
import spring.core.order.OrderServiceImpl;

/*앱 전체를 설정하고 구성하는 감독 역할 -> 실제 동작에 필요한 구현 객체 생성*/
/*객체를 생성하고 연결하는 역할에만 집중 -> 관심사의 분리 완수*/
public class AppConfig {
    public MemberService memberService() {
        // 실제로 어떤 구현체를 쓸지 결정
        /*MemberServiceImpl 입장에선 AppConfig가 의존관계를 주입 -> 의존관계 주입 or 의존성 주입*/
        return new MemberServiceImpl(new MemoryMemberRepository());
    }

    public OrderService orderService() {
        return new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
    }
}
