package spring.core.discount;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import spring.core.member.Grade;
import spring.core.member.Member;

@Component
//@Qualifier("fixDiscountPolicy") /*빈 이 여러개일 때 사용 -> @Autowired에서 명시하여 사용*/
public class FixDiscountPolicy implements DiscountPolicy {
    private int discountFixAmount = 1000; // 1000원 고정 할인

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) { // enum 타입은 == 사용
            return discountFixAmount;
        } else {
            return 0;
        }
    }
}
