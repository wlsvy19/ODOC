package com.example.hellospring;

import com.example.hellospring.repository.JdbcMemberRepository;
import com.example.hellospring.repository.JdbcTemplateMemberRepository;
import com.example.hellospring.repository.MemberRepository;
import com.example.hellospring.repository.MemoryMemberRepository;
import com.example.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 자바 코드로 직접 스프링 빈 등록 하기
 */


@Configuration
public class SpringConfig {
    /**
     * DataSource는 데이터베이스 커넥션을 획득할 때 사용하는 객체이다.
     * 스프링 부트는 데이터베이스 커넥션 정보를 바탕으로 DataSource를 생성하고 빈으로 만들어 둔다.
     * 그래서 DI를 받을 수 있다.
     */
    private DataSource dataSource;

    /**
     * 개방-폐쇄 원칙(OCP, Open-Closed Principle): 확장에는 열려 있고 수정에는 닫혀 있다.
     * 스프링의 DI(의존성 주입)를 사용하면 기존 코드를 전혀 손대지 않고 설정만으로 구현 클래스를 변경할 수 있다.
     */
    @Autowired
    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        // 인터페이스를 두고, 구현체를 바꿔끼기 할 수 있음 -> 다형성

        // return new MemoryMemberRepository();
        // return new JdbcMemberRepository(dataSource);
        return new JdbcTemplateMemberRepository(dataSource);
    }
}