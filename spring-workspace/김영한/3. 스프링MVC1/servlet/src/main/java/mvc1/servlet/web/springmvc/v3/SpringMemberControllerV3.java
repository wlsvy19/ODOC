package mvc1.servlet.web.springmvc.v3;

import mvc1.servlet.domain.member.Member;
import mvc1.servlet.domain.member.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/springmvc/v3/members")
public class SpringMemberControllerV3 {
    private MemberRepository memberRepository = MemberRepository.getInstance();

    // @RequestMapping(value = "/new-form", method = RequestMethod.GET)
    @GetMapping("/new-form")
    public String newForm(Model model) {
        model.addAttribute("form", "Spring MVC 방식 V3 컨트롤러 - 로그인 폼");
        return "new-form";
    }

    // @RequestMapping(value = "/save", method = RequestMethod.POST)
    @PostMapping("/save")
    public String save(@RequestParam("username") String username, @RequestParam("age") int age, Model model) {
        Member member = new Member(username, age);
        memberRepository.save(member);
        model.addAttribute("member", member);
        return "save-result";
    }

    // @RequestMapping(method = RequestMethod.GET)
    @GetMapping
    public String members(Model model) {
        List<Member> members = memberRepository.findAll();

        model.addAttribute("members", members);
        model.addAttribute("form", "Spring MVC 방식 V3 컨트롤러 - 회원 리스트");

        return "members";
    }

}