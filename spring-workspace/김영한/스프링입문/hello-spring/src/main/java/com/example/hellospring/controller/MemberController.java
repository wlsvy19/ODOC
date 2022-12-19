package com.example.hellospring.controller;

import com.example.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController {
    // 컨트롤러에서 서비스를 받아씀 -> 의존관계
    
    // 어차피 같은 기능 하는 서비스를 여러 군대에서 사용할 경우 new로 계속 생성할 필요가 없음
    // 스프링 컨테이너에 해당 서비스 객체를 하나만 가지고 있도록 하는게 좋음(싱글톤)
    // private final MemberService memberService = new MemberService();

    private final MemberService memberService;

    // DI:Dependency Injection - 컨트롤러 생성시 서비스를 자동으로 연결 시켜줌
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
}
