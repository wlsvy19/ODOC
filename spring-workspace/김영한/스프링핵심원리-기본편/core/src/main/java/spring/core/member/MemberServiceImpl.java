package spring.core.member;

import spring.core.member.Member;
import spring.core.member.MemberRepository;
import spring.core.member.MemberService;
import spring.core.member.MemoryMemberRepository;

public class MemberServiceImpl implements MemberService {
    
    // memberRepository가 실제 할당할 때 구현체에 의존을 함 -> MemberServiceImpl는 추상화와 구현체 모두 의존 해버림 -> DIP 위반
    private final MemberRepository memberRepository = new MemoryMemberRepository();

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberID) {
        return memberRepository.findById(memberID);
    }
}
