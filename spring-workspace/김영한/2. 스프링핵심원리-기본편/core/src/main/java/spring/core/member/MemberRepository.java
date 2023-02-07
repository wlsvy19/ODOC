package spring.core.member;

// 리포지토리: 뭔가..기계적인 용어
public interface MemberRepository {
    void save(Member member);
    Member findById(Long memberId);
}
