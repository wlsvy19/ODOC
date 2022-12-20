package com.example.hellospring.controller;

import com.example.hellospring.domain.Member;
import com.example.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MemberController {
    // 컨트롤러에서 서비스를 받아씀 -> 의존관계

    // 어차피 같은 기능 하는 서비스를 여러 군대에서 사용할 경우 new로 계속 생성할 필요가 없음
    // 스프링 컨테이너에 해당 서비스 객체를 하나만 가지고 있도록 하는게 좋음(싱글톤)
    // private final MemberService memberService = new MemberService();

    //@Autowired // DI 방법: 필드 주입
    private final MemberService memberService;

    // DI:Dependency Injection - 컨트롤러 생성시 스프링 컨테이너에서 서비스를 자동으로 연결 시켜줌
    // 스프링 컨테이너 안에서 스프링 서비스가 등록되어어 컴포넌트 스캔을 통해 서비스를 찾아서 컨트롤러와 자동 연결 됨
    @Autowired // 컴포넌트 스캔 방법, DI 방법: * 생성자 주입: 의존관계가 실행중에 동적으로 변할경우가 없으므로 생성자 주입을 사용하는게 가장 좋음
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(MemberForm form){ // input에 입력한 name 값이 MemberForm에 setName으로 들어감
        System.out.println("input 입력한 값: " + form.getName());
        Member member = new Member();
        member.setName(form.getName());
        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        for(Member member : members) {
            System.out.println("member.getId() = " + member.getId());
            System.out.println("member.getName() = " + member.getName());
        }
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
