package mvc1.servlet.web.springmvc.old;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// @Component: 스프링 빈으로 스프링 컨테이너에 등록됨 -> 핸들러 매핑 해서 찾아야 함

@Component("/springmvc/old-controller")
public class OldController implements Controller {
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("OldController.handleRequest");

        // application.properties 에서 접두, 접미사 설정

        return new ModelAndView("new-form");
    }
}
