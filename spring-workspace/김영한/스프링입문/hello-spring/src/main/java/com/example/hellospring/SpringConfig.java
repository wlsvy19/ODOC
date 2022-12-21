package com.example.hellospring;

import com.example.hellospring.aop.TimeTraceAop;
import com.example.hellospring.repository.MemberRepository;
import com.example.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 자바 코드로 직접 스프링 빈 등록 하기
 */

// 스프링 컨테이너에서 @Congifuration 붙은걸 찾고 @Bean 붙은걸 찾음
@Configuration
public class SpringConfig {
    /**
     * DataSource는 데이터베이스 커넥션을 획득할 때 사용하는 객체이다.
     * 스프링 부트는 데이터베이스 커넥션 정보를 바탕으로 DataSource를 생성하고 빈으로 만들어 둔다.
     * 그래서 DI를 받을 수 있다.
     */

    /**
     * 개방-폐쇄 원칙(OCP, Open-Closed Principle): 확장에는 열려 있고 수정에는 닫혀 있다.
     * 스프링의 DI(의존성 주입)를 사용하면 기존 코드를 전혀 손대지 않고 설정만으로 구현 클래스를 변경할 수 있다.
     */

    // JDBC 사용
    /*
    private DataSource dataSource;
    @Autowired
    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }
*/

    // JPA 사용
 /*
    private EntityManager em;

    @Autowired
    public SpringConfig(EntityManager em) {
        this.em = em;
 }
  */

    private final MemberRepository memberRepository;

    //@Autowired // 클래스에 필드 1개일때 @Autowired 생략 가능
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean // 스프링 컨테이너에 빈 등록
    public MemberService memberService() {
        // 인자로 넘긴 memberRepository에서 실제 구현체를 Spring Data Jpa에서 자동으로 만들고 스프링 빈으로 등록 함
        return new MemberService(memberRepository);
    }


/*    @Bean
    public MemberRepository memberRepository() {
        // 인터페이스를 두고, 구현체를 바꿔끼기 할 수 있음 -> 다형성

        // return new MemoryMemberRepository();
        // return new JdbcMemberRepository(dataSource);
        // return new JdbcTemplateMemberRepository(dataSource);
        //return new JpaMemberRepository(em);
        //

    }*/
    
/*

    @Bean // AOP를 스프링 Bean으로 직접 등록 후 사용
    public TimeTraceAop timeTraceAop() {
        return new TimeTraceAop();

    }
 */
}