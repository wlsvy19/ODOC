package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.login.LoginForm;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

	private final LoginService loginService;

	private final SessionManager sessionManager;

	@GetMapping("/login")
	public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
		return "/login/loginForm";
	} // end loginForm()

	// 쿠키사용
	// @PostMapping("/login")
	public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
		// 로그인 실패시
		if (bindingResult.hasErrors()) {
			return "/login/loginForm";
		}
		Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
		if (loginMember == null) {
			bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			return "/login/loginForm";
		}

		// 로그인 성공시
		// Todo 로그인 성공 처리: 보여지는 화면이 달라짐 -> 쿠키 사용해서 정보 저장
		// 세션쿠키: 쿠키에 시간정보를 주지 않음, 브라우저 종료시 소멸
		Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
		response.addCookie(idCookie);
		return "redirect:/";
	} // end login()

	// 세션사용
	// @PostMapping("/login")
	public String loginV2(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
		// 로그인 실패시
		if (bindingResult.hasErrors()) {
			return "/login/loginForm";
		}
		Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
		if (loginMember == null) {
			bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			return "/login/loginForm";
		}

		// 로그인 성공시
		// 세션 관리자를 통해 세션을 생성하고 회원 데이터 보관
		sessionManager.createSession(loginMember, response);
		return "redirect:/";
	} // end login()

	// 서블릿에서 제공하는 HttpSession 사용
	@PostMapping("/login")
	public String loginV3(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request) {
		// 로그인 실패시
		if (bindingResult.hasErrors()) {
			return "/login/loginForm";
		}
		Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
		
		// 로그인 정보가 일치하지 않으면
		if (loginMember == null) {
			bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			return "/login/loginForm";
		}

		// 로그인 성공시
		// 세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
		HttpSession session = request.getSession();
		// 세션에 로그인 회원 정보 보관
		session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
		return "redirect:/";
	} // end login()

	//@PostMapping("/logout")
	public String logout(HttpServletResponse response){
		// 쿠키삭제 메서드 호출
		expireCookie(response, "memberId");
		
		// 홈 화면으로 리다이렉트
		return "redirect:/";
	} // end logout()

	// @PostMapping("/logout")
	public String logoutV2(HttpServletRequest request){
		// 세션삭제 메서드 호출: 로그아웃시 해당 세션의 정보 제거
		sessionManager.expire(request);

		// 홈 화면으로 리다이렉트
		return "redirect:/";
	} // end logout()

	@PostMapping("/logout")
	public String logoutV3(HttpServletRequest request){
		// HttpSession 삭제
		// false: 세션 있으면 가져오고 없으면 null
		HttpSession session = request.getSession(false);

		if(session!=null){
			// 세션과 세션안에 데이터 모두 삭제
			session.invalidate();
		}
		// 홈 화면으로 리다이렉트
		return "redirect:/";
	} // end logout()

	private void expireCookie(HttpServletResponse response, String cookieName) {
		Cookie cookie = new Cookie(cookieName, null);
		// 로그아웃 -> 쿠키삭제: 시간 0으로 설정
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
}
