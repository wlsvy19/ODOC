package spring.core.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.core.member.Member;
import spring.core.member.MemberRepository;
import spring.core.member.MemberService;
import spring.core.member.MemoryMemberRepository;

@Component
public class MemberServiceImpl implements MemberService {
    
    /*memberRepository가 실제 할당할 때 구현체에 의존을 함 -> MemberServiceImpl는 추상화와 구현체 모두 의존 해버림 -> DIP 위반*/
    // private final MemberRepository memberRepository = new MemoryMemberRepository();
    
    /*단지, 인터페이스에 의존만 함*/
    private final MemberRepository memberRepository;


    /*생성자를 통해 주입 -> 구현체에 대한 코드는 어디에도 없음 -> DIP(추상화에만 의존) 지킴*/

    /*@Autowired: AppConfig에선 @Bean 붙은걸 찾았지만, AutoConfig에서 사용하기 위해서 의존관계를 자동으로 주입 시켜줌*/
    /*마치 ac.getBean(MemberRepository.class) 처럼*/
    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /*구현체에 의존하지 않게 되면서 실행에만 집중 -> 관심사의 분리 완수*/
    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberID) {
        return memberRepository.findById(memberID);
    }

    /*싱글톤 테스트 용도*/
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
