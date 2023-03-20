package mvc1.servlet.web.frontcontroller.v3.controller;

import mvc1.servlet.web.frontcontroller.ModelView;
import mvc1.servlet.web.frontcontroller.v3.ControllerV3;

import java.util.Map;

public class MemberFormControllerV3 implements ControllerV3 {
    @Override
    public ModelView process(Map<String, String> paramMap) {
        ModelView mv = new ModelView("new-form");
        mv.getModel().put("form", "Front 컨트롤러 버전3 - 회원가입 폼");

        return mv;
    }
}
