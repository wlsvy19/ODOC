package mvc1.servlet.domain.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려
 */

public class MemberRepository {
    private static Map<Long, Member> store = new HashMap<>();

    // 회원가입 할 때마다 ID 1씩 증가
    private static long sequence = 0L;

    // 싱글톤(톰캣 띄울때만 스프링 사용한거고, 서블릿 할때 싱글톤이 아니기 때문에 싱글톤패턴으로 객체 생성) -> private으로 생성자 막음
    private static final MemberRepository instance = new MemberRepository();

    public static MemberRepository getInstance() {
        return instance;
    }

    private MemberRepository() {
    }

    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    // ID로 특정 회원 1명 찾기
    public Member findById(Long id) {
        return store.get(id);
    }

    // 모든 회원 찾기
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    // 테스트 코드에서 회원가입 모두 삭제 시 사용
    public void clearStore() {
        store.clear();
    }
}
