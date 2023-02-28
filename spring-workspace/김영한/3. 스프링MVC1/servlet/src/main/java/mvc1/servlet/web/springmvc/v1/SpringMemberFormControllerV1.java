package mvc1.servlet.web.springmvc.v1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

// 스프링이 스프링 빈으로 등록
@Controller
public class SpringMemberFormControllerV1 {

    @RequestMapping("/springmvc/v1/members/new-form")
    public ModelAndView process() {
        ModelAndView mv = new ModelAndView("new-form");
        mv.addObject("form", "Spring MVC 방식 V1 컨트롤러 - 로그인 폼");
        return mv;
    }
}
