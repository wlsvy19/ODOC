package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

	private final MemberRepository memberRepository;
	private final SessionManager sessionManager;

	// 1. 기본
    //@GetMapping("/")
    public String home() {
		// localhost:8089 입력시 localhost:8089/items 로 이동됨
        // return "redirect:/items";

		return "home";
    }

	// 2.
	// 로그인 유무에 따라 보여지는 화면 다르도록, 쿠키 사용
	// @GetMapping("/")
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

	// 3. 세션 사용
	// @GetMapping("/")
	public String homeLoginV2(HttpServletRequest request, Model model) {
		// 세션 관리자에 저장된 회원 정보 조회
		Member member = (Member) sessionManager.getSession(request);

		// 1. 회원정보가 없으면 home으로 이동
		if(member == null) {
			return "home";
		}

		// 2. 회원정보가 있으면 로그인 처리
		model.addAttribute("member", member);
		return "/login/loginHome";
	}

	// 4. HttpSession 사용
	// @GetMapping("/")
	public String homeLoginV3(HttpServletRequest request, Model model) {
		// 새로운 사용자가 로그인->세션 생성 하면 안됨 , 그래서 false
		HttpSession session = request.getSession(false);
		if(session == null) {
			return "home";
		}

		Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
		// 세션에 값이 없으면 home으로 이동
		if(loginMember == null) {
			return "home";
		}

		// 세션이 유지되면 로그인으로 이동
		model.addAttribute("member", loginMember);
		return "/login/loginHome";
	}

	// 5. @SessionAttribute 사용: session이 있는지 자동 체크
	@GetMapping("/")
	public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)Member loginMember, Model model) {
		// 세션에 값이 없으면 home으로 이동
		if(loginMember == null) {
			return "home";
		}

		// 세션이 유지되면 로그인으로 이동
		model.addAttribute("member", loginMember);
		return "/login/loginHome";
	}
}