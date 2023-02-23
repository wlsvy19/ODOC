package mvc1.servlet.web.frontcontroller.v1;

import mvc1.servlet.web.frontcontroller.v1.controller.MemberFormControllerV1;
import mvc1.servlet.web.frontcontroller.v1.controller.MemberListControllerV1;
import mvc1.servlet.web.frontcontroller.v1.controller.MemberSaveControllerV1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// url패턴에 *: /front-controller/v1/ 이후에 모든 url 호출
@WebServlet(name = "frontControllerServletV1", urlPatterns = "/front-controller/v1/*")
public class FrontControllerServletV1 extends HttpServlet {
    private Map<String, ControllerV1> controllerMap = new HashMap<>();

    public FrontControllerServletV1() {
        controllerMap.put("/front-controller/v1/members/new-form", new MemberFormControllerV1());
        controllerMap.put("/front-controller/v1/members/save", new MemberSaveControllerV1());
        controllerMap.put("/front-controller/v1/members", new MemberListControllerV1());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV1.service 호출");

        String requestURI = request.getRequestURI();
        System.out.println("requestURI = " + requestURI);

        // key: /front-controller/v1/ ~~~  ,  value: 각각의 컨트롤러 로직 결과들
        ControllerV1 controller = controllerMap.get(requestURI);

        // 요청한 URL 정보 없을 경우
        if (controller == null) {
            // 헤더에서 상태 코드 확인
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
            // response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400

            return;
        }
        // 각각의 호출된 컨트롤러에서 상속받은 process 메서드 실행
        controller.process(request, response);
    }
}
