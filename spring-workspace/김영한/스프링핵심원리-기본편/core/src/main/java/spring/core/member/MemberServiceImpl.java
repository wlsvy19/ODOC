package spring.core.member;

import spring.core.member.Member;
import spring.core.member.MemberRepository;
import spring.core.member.MemberService;
import spring.core.member.MemoryMemberRepository;

public class MemberServiceImpl implements MemberService {
    
    /*memberRepository가 실제 할당할 때 구현체에 의존을 함 -> MemberServiceImpl는 추상화와 구현체 모두 의존 해버림 -> DIP 위반*/
    // private final MemberRepository memberRepository = new MemoryMemberRepository();
    
    /*단지, 인터페이스에 의존만 함*/
    private final MemberRepository memberRepository;
    
    /*생성자를 통해 주입 -> 구현체에 대한 코드는 어디에도 없음 -> DIP(추상화에만 의존) 지킴*/
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
}
