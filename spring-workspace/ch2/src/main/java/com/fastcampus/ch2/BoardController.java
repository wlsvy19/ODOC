package com.fastcampus.ch2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {
    @GetMapping("/list")
    public String list(HttpServletRequest request) { // 게시물 목록 보여주는 메소드
        if (!loginCheck(request)) {
            return "redirect:/login/login?toURL=" + request.getRequestURL(); // 로그인 안했으면 로그인 화면으로 이동
        }
        return "boardList"; // 로그인 했으면 게시판 화면으로 이동
    }

    private boolean loginCheck(HttpServletRequest request) {
        // 1. 세션 얻기
        HttpSession session = request.getSession();

        // 2. 세션에 ID있는지 확인
//        if (session.getAttribute("id") != null) {
//            return true; // ID가 있는 상태
//        } else
//            return false; // ID가 없는상태 즉, 로그인 안한 상태

        return session.getAttribute("id") != null; // ID가 null이 아니면 true, null이면 false
    }
}
