package spring.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.core.member.Grade;
import spring.core.member.Member;
import spring.core.member.MemberService;
import spring.core.member.MemberServiceImpl;
import spring.core.order.Order;
import spring.core.order.OrderService;
import spring.core.order.OrderServiceImpl;

public class OrderApp {
    public static void main(String[] args) {
//        MemberService memberService = new MemberServiceImpl();
//        OrderService orderService = new OrderServiceImpl();
//
//        long memberId = 1L;
//        Member member = new Member(memberId, "memberA", Grade.VIP);
//        memberService.join(member);
//
//        Order order = orderService.createOrder(memberId, "itemA", 10000);
//
//        // toString() 호출
//        System.out.println("order = " + order);
//        System.out.println("order.calculatePrice() = " + order.calculatePrice());

        /*DIP 를 잘 지킨 코드로 테스트*/
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();
//        OrderService orderService = appConfig.orderService();

//        long memberId = 1L;
//        Member member = new Member(memberId, "memberA", Grade.VIP);
//        memberService.join(member);

//        Order order = orderService.createOrder(memberId, "itemA", 20000);

        /*toString() 호출*/
//        System.out.println("order = " + order);
//        System.out.println("order.calculatePrice() = " + order.calculatePrice());

        /*---Spring을 사용하여 작성---*/
        /*ApplicationContext: Spring컨테이너 -> 스프링 Bean 관리 -> AppConfig에서 @붙은거 관리 -> 스프링 컨테이너에서 Bean객체로 생성하여 관리*/
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

        /*메소드 명, 리턴타입*/
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
        OrderService orderService = applicationContext.getBean("orderService", OrderService.class);

        long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 20000);
        System.out.println("order = " + order);
        System.out.println("order.calculatePrice() = " + order.calculatePrice());
    }
}
