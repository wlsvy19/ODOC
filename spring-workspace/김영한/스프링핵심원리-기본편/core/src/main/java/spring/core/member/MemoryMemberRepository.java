package spring.core.member;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

// 어떤 DB를 사용할지 확정되지 않은 상태라고 가정

/*스프링 빈으로 등록하기 위해 사용*/
@Component /*memoryMemberRepository 스프링 컨테이너에 스프링 빈으로 등록*/
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
