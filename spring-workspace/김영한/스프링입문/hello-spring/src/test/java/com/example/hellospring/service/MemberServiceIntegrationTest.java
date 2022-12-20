package com.example.hellospring.service;

import com.example.hellospring.domain.Member;
import com.example.hellospring.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

// 실제 DB와 연결

/**
 * 통합테스트: 순수 자바가 아닌 스프링 컨테이너까지 시스템에 올림, 좋은 테스트는 순수 자바코드만 가지고 하는것
 * SpringConfig 에서 MemberRepository의 return 값만 바꿔가며 테스트
 */

@SpringBootTest // 스프링 컨테이너와 테스트를 함께 실행
@Transactional  // 테스트케이스에 이 어노테이션이 있으면 테스트 시작 전에 트랜잭션을 시작하고 테스트 완료 후 항상 롤백 -> DB에 데이터가 남지 않아 다음 테스트 영향x
class MemberServiceIntegrationTest {
    MemberService memberService;
    MemberRepository memberRepository;

    @Autowired
    public MemberServiceIntegrationTest(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @Test
    void 회원가입() {
        // given : 주어진 환경
        Member member = new Member();
        member.setName("spring");

        // when : 언제
        Long saveId = memberService.join(member);

        // then : 결과
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");


        // when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

    }

}