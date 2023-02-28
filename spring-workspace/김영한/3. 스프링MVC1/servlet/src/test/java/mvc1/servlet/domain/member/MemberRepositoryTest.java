package mvc1.servlet.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MemberRepositoryTest {
    // MemberRepository는 싱글톤 이라서 new로 객체 생성 안됨 -> getInstance() 사용
    MemberRepository memberRepository = MemberRepository.getInstance();

    // 테스트 끝나면 실행 되는 메서드 - 맴버 초기화
    @AfterEach
    void afterEach() {
        memberRepository.clearStore();
    }

    // 회원 1명을 저장하고 ID로 회원찾기가 잘 되는지 확인한다.
    @Test
    void save() {
        // given - 조건이 주어지고
        Member member = new Member("이진표", 32);

        // when - 실행 되면
        Member saveMember = memberRepository.save(member);

        // then - 결과
        Member findMember = memberRepository.findById(saveMember.getId());

        assertThat(findMember).isEqualTo(saveMember);

    }

    // 2명의 회원가입을 하고 총 회원이 2명인지 확인한다.
    @Test
    void findAll() {
        // given
        Member member1 = new Member("member1", 32);
        Member member2 = new Member("member2", 33);

        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        List<Member> result = memberRepository.findAll();


        // then
        assertThat(result.size()).isEqualTo(2);
        // result안에 member1, member2 객체 들어 있는지 확인
        assertThat(result).contains(member1, member2);

    }
}