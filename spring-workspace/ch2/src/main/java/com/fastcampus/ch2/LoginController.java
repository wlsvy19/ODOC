package com.fastcampus.ch2;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("login")
public class LoginController {
    @GetMapping("/login")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 1. 세션 종료
        session.invalidate();
        // 2. 홈으로 이동
        return "redirect:/";
        
    }

    @PostMapping("/login")
    public String login(@CookieValue("id") String cookieId, @CookieValue("JSESSIONID") String sessionId ,String id, String pwd, String toURL, boolean rememberId, HttpServletRequest request,
            
            HttpServletResponse response) throws Exception {
        // 1. id와 pwd 확인
        if (!loginCheck(id, pwd)) {
            // 2-1. ID와 패스워드가 일치하지 않으면 loginForm으로 이동
            String msg = URLEncoder.encode("아이디 또는 패스워드가 일치하지 않습니다.", "utf-8");

            return "redirect:/login/login?msg=" + msg; // 리다이렉트는 GET으로 가니까 위에 메소드로 다시 이동
        }

        // 2-2. ID와 패스워드가 일치하면,
        // 세션 객체 얻어오기
        HttpSession session = request.getSession();
        // 세션 객체에 ID 저장
        session.setAttribute("id", id);
        if (rememberId) {
            // 아이디저장 체크이면 쿠키를 생성
            Cookie cookie = new Cookie("id", id);
            // 2. 응답에 저장
            response.addCookie(cookie);
        } else {
            // 아이디 저장 체크 아니면 쿠키 삭제
            Cookie cookie = new Cookie("id", id);
            cookie.setMaxAge(0); // 쿠키삭제
            // 2. 응답에 저장
            response.addCookie(cookie);
        }
        // 1. 쿠키를 생성

        // 3. 홈으로 이동
        toURL = toURL==null || toURL.equals("") ? "/" : toURL;
        return "redirect:" + toURL;
    }

    private boolean loginCheck(String id, String pwd) {
        return "asdf".equals(id) && "1234".equals(pwd);
    }

}
