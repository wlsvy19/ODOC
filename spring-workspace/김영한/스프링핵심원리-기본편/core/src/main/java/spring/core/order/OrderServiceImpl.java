package spring.core.order;

import spring.core.discount.DiscountPolicy;
import spring.core.discount.FixDiscountPolicy;
import spring.core.discount.RateDiscountPolicy;
import spring.core.member.Member;
import spring.core.member.MemberRepository;
import spring.core.member.MemoryMemberRepository;

/*실질적인 할인 정책 클라이언트*/
/*DIP위반-> 클라이언트가 추상화에 의존하지만, 구체화에도 의존중( = new ...)*/
/*여기서 사용하는 필드는 2개 */
public class OrderServiceImpl implements OrderService {
    /*어떤 회원이 주문 했는가?*/
    // private final MemberRepository memberRepository = new MemoryMemberRepository();
    /*DIP를 지키기 위해 인터페이스만 선언*/
    /* * final키워드: 생성자를 통해 할당이 되야 함*/
    private final MemberRepository memberRepository;

    
    /*어떤 할인정책을 사용 할 것인가?*/
    /*마치 로미오 배역을 맡은 디카프리오가 줄리엣 역할을 할 배우를 직접 캐스팅 하는 느낌*/
    //private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

    /*할인 정책을 바꾸려면? 코드의 수정이 일어날 수 밖에 없는 구조*/
    //private final DiscountPolicy rateDiscountPolicy = new RateDiscountPolicy();

    /*DIP 만족(추상에만 의존하도록 변경) -> Null 값 -> 누군가 구현객체를 대신 생성하고 주입 -> AppConfig 등장(감독)*/
    private final DiscountPolicy discountPolicy;

    /*생성자 주입을 통해 DIP 원칙 지키기 -> AppConfig에서 어떤거 사용할지 정함*/
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    /*구현체에 의존하지 않게 되면서 실행에만 집중 -> 관심사의 분리 완수*/
    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        
        // 최종 할인된 가격
        int discountPrice = discountPolicy.discount(member, itemPrice);

        // 단일책임 원칙 잘지킴 - Order 서비스 입장에선 discount에 대해 알 필요가 없음 
        // discount정책 수정이 order에 영향을 주지 않음
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
