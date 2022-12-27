package com.example.hellospring.service;

import com.example.hellospring.domain.Member;
import com.example.hellospring.repository.JpaMemberRepository;
import com.example.hellospring.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * AOP(Aspect Oriented Programming)
 * 문제점의 발생-시간측정 로직은 핵심 관심사항아 아님, 시간측정 로직은 공통 관심 사항, 시간 측정 로직과 핵심 비즈니스 로직이 섞여 유지보수 여려움
 * 시간 측정 로직을 별도의 공통 로직으로 만들기 매우 어려움, 시간 측정로직을 변경할 때 마다 모든 로직을 찾아 변경 해야함
 * 
 * 공통관심사항과 핵심관심사항을 분리 하자
 */

/**
 * Service: Service 스러운, 사람 친화적인 용어
 */

// @Service // 컴포넌트 스캔 방법
@Transactional // JPA사용하여 데이터 저장&변경에 있어야 함
public class MemberService {
    EntityManager em;
    private final MemberRepository memberRepository = new JpaMemberRepository(em);


    // repository의 실제 구현체를 찾음, 스프링 컨테이너에서 서비스와 리포지토리를 자동 연결 해줌
    // @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원가입
     */
    public Long join(Member member) {
        // AOP 상황만들기: 모든 메서드의 실행 시간을 측정해야 한다면?
        long start = System.currentTimeMillis();

        // 같은 이름있는 중복회원 x
        try {
            validateDuplicateMember(member);
            memberRepository.save(member);
            return member.getId();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
//            System.out.println("join() 걸린시간 = " + timeMs + "ms");
        }


    }

    private void validateDuplicateMember(Member member) {
        // AOP 상황만들기: 모든 메서드의 실행 시간을 측정해야 한다면?
        long start = System.currentTimeMillis();
        try {
            memberRepository.findByName(member.getName())
                    .ifPresent(m -> {
                        throw new IllegalStateException("이미 존재하는 회원입니다.");
                    });
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
//            System.out.println("validateDuplicateMember() 걸린시간 = " + timeMs + "ms");
        }
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        // AOP 상황만들기: 모든 메서드의 실행 시간을 측정해야 한다면?
        long start = System.currentTimeMillis();

        try {
            return memberRepository.findAll();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
//            System.out.println("findMembers() 걸린시간 = " + timeMs + "ms");
        }
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
