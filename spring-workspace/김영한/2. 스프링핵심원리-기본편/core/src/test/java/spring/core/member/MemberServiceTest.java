package spring.core.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spring.core.AppConfig;

public class MemberServiceTest {
    //MemberService memberService = new MemberServiceImpl();
    MemberService memberService;

//    AppConfig appConfig = new AppConfig();
//    MemberService memberService = appConfig.memberService();


    /*테스트 전에 무조건 수행되는 코드*/
    @BeforeEach
    public void beforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
    }


    @Test
    void join() {
        // given: 환경이 주어지고
        Member member = new Member(1L, "memberA", Grade.VIP);

        // when: 이렇게 했을 때
        memberService.join(member);
        Member findMember = memberService.findMember(1L); // ID를 다르게 설정하여 Test

        // then: 결과가 이렇다.
        Assertions.assertThat(member).isEqualTo(findMember); // member와 찾은 member가 같은 객체냐?
    }
}
