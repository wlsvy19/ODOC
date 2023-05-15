package hello.login;

import hello.login.domain.item.Item;
import hello.login.domain.item.ItemRepository;
import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TestDataInit {

	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;

	/**
	 * 테스트용 데이터 추가
	 */
	@PostConstruct
	public void init() {
		itemRepository.save(new Item("itemA", 10000, 10));
		itemRepository.save(new Item("itemB", 20000, 20));
		itemRepository.save(new Item("itemC", 25000, 18));
		itemRepository.save(new Item("itemD", 50000, 5));

		Member member1 = new Member();
		member1.setLoginId("test1");
		member1.setPassword("test!");
		member1.setName("테스트1");
		memberRepository.save(member1);

		Member member2 = new Member();
		member2.setLoginId("test2");
		member2.setPassword("test@");
		member2.setName("테스트2");
		memberRepository.save(member2);

		Member member3 = new Member();
		member2.setLoginId("1");
		member2.setPassword("1");
		member2.setName("테스트3");
		memberRepository.save(member3);
	} // end init()

}