package spring.core.discount;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import spring.core.annotation.MainDiscountPolicy;
import spring.core.member.Grade;
import spring.core.member.Member;

@Component
// @Qualifier("mainDiscountPolicy") /*빈 이 여러개일 때 사용 -> @Autowired에서 명시하여 사용*/
// @Primary  /*빈 이 여러개일 때 사용 -> 우선순위 지정*/
@MainDiscountPolicy /*직접만든 어노테이션으로 하기*/
public class RateDiscountPolicy implements DiscountPolicy {
    private int discountPercent = 10;

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return price * discountPercent / 100;
        } else {
            return 0;
        }
    }
}
