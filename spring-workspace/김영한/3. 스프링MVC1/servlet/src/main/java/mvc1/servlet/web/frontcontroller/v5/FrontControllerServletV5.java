package mvc1.servlet.web.frontcontroller.v5;

import mvc1.servlet.web.frontcontroller.ModelView;
import mvc1.servlet.web.frontcontroller.MyView;
import mvc1.servlet.web.frontcontroller.v3.ControllerV3;
import mvc1.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import mvc1.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import mvc1.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import mvc1.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import mvc1.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import mvc1.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import mvc1.servlet.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import mvc1.servlet.web.frontcontroller.v5.adapter.ControllerV4HandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        // 초기화
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }

    private void initHandlerMappingMap() {
        // V3
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        // V4
        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object handler = getHandler(request);
        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        MyHandlerAdapter adapter = getHandlerAdapter(handler);

        ModelView mv = adapter.handle(request, response, handler);

        String viewName = mv.getViewName();

        // 논리 경로 -> 물리 경로
        MyView view = viewResolver(viewName);

        view.render(mv.getModel(), request, response);
    }


    // 매핑 정보를 반환하는 메서드
    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        System.out.println("requestURI = " + requestURI);
        return handlerMappingMap.get(requestURI);
    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        for (MyHandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler 어뎁터를 찾을 수 없습니다. handler = " + handler);
    }

    private static MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
