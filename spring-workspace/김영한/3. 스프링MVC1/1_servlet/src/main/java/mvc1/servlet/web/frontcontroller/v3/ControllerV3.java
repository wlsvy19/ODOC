package mvc1.servlet.web.frontcontroller.v3;

import mvc1.servlet.web.frontcontroller.ModelView;

import java.util.Map;

public interface ControllerV3 {
    // 서블릿 없음
    ModelView process(Map<String, String> paramMap);
}
