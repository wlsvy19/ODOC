package com.example.hellospring.service;

import com.example.hellospring.domain.Member;
import com.example.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

// DB와 연결x, 휘발성인 메모리에 저장했기 때문에 프로젝트 재시작하면 데이터 날라감

/**
 * 단위테스트: 순수 자바 코드만 가지고 테스트
 */

class MemberServiceTest {
    MemberService memberService;
    MemoryMemberRepository memberRepository;

    
    // 테스트할때 MemberService를 하나의 객체로 생성 하기 위해 멤버변수들을 new로 생성하지 않고,  DI(Dependency Injection) 함
    // service입장에서 repository를 외부에서 주입 해 줌
    @BeforeEach // 테스트 코드가 실행 되기 전
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    // 같은 객체 공유로 인해 테스트 실패 방지 -> 테스트 하나 끝나면 map을 초기화
    @AfterEach // 각각의 테스트 코드가 하나하나 실행된 후
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void 회원가입() {
        // given : 주어진 환경
        Member member = new Member();
        member.setName("hello");

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
/*

        try {
            memberService.join(member2);

        } catch (IllegalStateException  e) {
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.123");
        }
*/

        // then
    }

    @Test
    void 회원찾기() {
    }

    @Test
    void findOne() {
    }
}