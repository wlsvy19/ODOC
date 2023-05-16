package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

	private final MemberRepository memberRepository;

    //@GetMapping("/")
    public String home() {
		// localhost:8089 입력시 localhost:8089/items 로 이동됨
        // return "redirect:/items";

		return "home";
    }

	// 로그인 유무에 따라 보여지는 화면 다르도록
	@GetMapping("/")
	public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
		// @CookieValue: Spring에서 제공하는 쿠키정보 쉽게 가져오는 어노테이션
		
		// 1. 로그인 정보가 없으면
		if(memberId == null) {
			return "home";
		}

		// 2. 로그인 시도
		Member loginMember = memberRepository.findById(memberId);

		// 2-1. 로그인 정보가 없을 경우
		if (loginMember == null) {
			return "home";
		}

		// 2-2 로그인 정보가 있을 경우, 회원 정보를 담아 뷰단으로 보냄
		model.addAttribute("member", loginMember);
		return "/login/loginHome";
	}
}