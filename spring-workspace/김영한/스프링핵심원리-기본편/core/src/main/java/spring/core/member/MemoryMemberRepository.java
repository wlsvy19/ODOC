package spring.core.member;

import java.util.HashMap;
import java.util.Map;

// 어떤 DB를 사용할지 확정되지 않은 상태라고 가정
public class MemoryMemberRepository implements MemberRepository {

    // Map에 데이터 저장
    private static Map<Long, Member> store = new HashMap<>();

    @Override
    public void save(Member member) {
        store.put(member.getId(), member);
    }

    @Override
    public Member findById(Long memberId) {
        return store.get(memberId);
    }
}
