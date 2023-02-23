package mvc1.servlet.web.frontcontroller.v2;

import mvc1.servlet.web.frontcontroller.MyView;
import mvc1.servlet.web.frontcontroller.v1.ControllerV1;
import mvc1.servlet.web.frontcontroller.v2.controller.MemberFormControllerV2;
import mvc1.servlet.web.frontcontroller.v2.controller.MemberListControllerV2;
import mvc1.servlet.web.frontcontroller.v2.controller.MemberSaveControllerV2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// http://localhost:8081/front-controller/v2/members/new-form

@WebServlet(name = "frontControllerServletV2", urlPatterns = "/front-controller/v2/*")
public class FrontControllerServletV2 extends HttpServlet {
    private Map<String, ControllerV2> controllerMap = new HashMap<>();

    public FrontControllerServletV2() {
        controllerMap.put("/front-controller/v2/members/new-form", new MemberFormControllerV2());
        controllerMap.put("/front-controller/v2/members/save", new MemberSaveControllerV2());
        controllerMap.put("/front-controller/v2/members", new MemberListControllerV2());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        System.out.println("requestURI = " + requestURI);

        // key: /front-controller/v1/ ~~~  ,  value: 각각의 컨트롤러 로직 결과들
        ControllerV2 controller = controllerMap.get(requestURI);

        // 요청한 URL 정보 없을 경우
        if (controller == null) {
            // 헤더에서 상태 코드 확인
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
            // response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400

            return;
        }
        // 각각의 호출된 컨트롤러에서 상속받은 process 메서드 실행
        MyView view = controller.process(request, response);
        view.render(request, response);
    }
}
