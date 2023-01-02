package spring.core.order;

import spring.core.discount.DiscountPolicy;
import spring.core.discount.FixDiscountPolicy;
import spring.core.member.Member;
import spring.core.member.MemberRepository;
import spring.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {
    // 어떤 회원이 주문 했는가?
    private final MemberRepository memberRepository = new MemoryMemberRepository();

    // 어떤 할인정책을 사용 할 것인가?
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

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
