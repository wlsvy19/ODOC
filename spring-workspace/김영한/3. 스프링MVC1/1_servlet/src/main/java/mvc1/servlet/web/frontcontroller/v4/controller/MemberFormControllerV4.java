package mvc1.servlet.web.frontcontroller.v4.controller;

import mvc1.servlet.web.frontcontroller.v4.ControllerV4;

import java.util.Map;

public class MemberFormControllerV4 implements ControllerV4 {

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        model.put("form", "Front 컨트롤러 버전4 - 회원가입 폼");
        return "new-form";
    }
}
