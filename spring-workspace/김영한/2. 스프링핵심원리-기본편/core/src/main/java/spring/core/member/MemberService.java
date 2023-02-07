package spring.core.member;

import spring.core.member.Member;

// 서비스: 사용자 친화적
public interface MemberService {

    void join(Member member);
    Member findMember(Long memberID);
}
