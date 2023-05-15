package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.web.login.LoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

	private final LoginService loginService;

	@GetMapping("/login")
	public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
		return "/login/loginForm";
	}

	@PostMapping("/login")
	public String login(@Valid@ModelAttribute LoginForm form, BindingResult bindingResult) {
		// 로그인 실패시
		if (bindingResult.hasErrors()) {
			return "/login/loginForm";
		}
		Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
		if (loginMember == null) {
			bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			return "/login/loginForm";
		}
		
		// Todo 로그인 성공 처리: 보여지는 화면이 달라짐 -> 쿠키 사용해서 정보 저장
		return "redirect:/";
	}

}
