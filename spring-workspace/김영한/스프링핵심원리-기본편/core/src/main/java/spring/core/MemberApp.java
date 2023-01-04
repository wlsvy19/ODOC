package spring.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.core.member.Grade;
import spring.core.member.Member;
import spring.core.member.MemberService;
import spring.core.member.MemberServiceImpl;
import spring.core.order.OrderService;

// Test 클래스

public class MemberApp {
    public static void main(String[] args) {
//        MemberService memberService = new MemberServiceImpl();
//        Member member = new Member(1L, "memberA", Grade.VIP);
//        memberService.join(member);
//
//        Member findMember = memberService.findMember(1L);
//
//        System.out.println("new member = " + member.getName());
//        System.out.println("find Member = " + findMember.getName());


        /*DIP 를 잘 지킨 코드로 테스트*/
//        AppConfig appConfig = new AppConfig();
        /*memberService안에 구현체가 들어있음*/
//        MemberService memberService = appConfig.memberService();
//        Member member = new Member(1L, "memberA", Grade.VIP);
//
//        memberService.join(member);
//
//        Member findMember = memberService.findMember(1L);
//
//        System.out.println("new member = " + member.getName());
//        System.out.println("find Member = " + findMember.getName());

        /*---Spring을 사용하여 작성---*/
        /*ApplicationContext: Spring컨테이너 -> 스프링 Bean 관리 -> AppConfig에서 @붙은거 관리 -> 스프링 컨테이너에서 Bean객체로 생성하여 관리*/
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);


        /*메소드 명, 리턴타입*/
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);

        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);

        System.out.println("new member = " + member.getName());
        System.out.println("find Member = " + findMember.getName());
    }
}
