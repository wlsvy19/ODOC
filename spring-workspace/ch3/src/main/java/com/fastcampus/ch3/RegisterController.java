package com.fastcampus.ch3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller // ctrl+shift+o 자동 import
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    UserDao userDao;

    final int FAIL = 0;

    @InitBinder
    public void toDate(WebDataBinder binder) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df, false));
        binder.setValidator(new UserValidator()); // UserValidator를 WebDataBinder의 로컬 validator로 등록
        //	List<Validator> validatorList = binder.getValidators();
        //	System.out.println("validatorList="+validatorList);
    }

    @GetMapping("/add")
    public String register() {
        return "registerForm"; // WEB-INF/views/registerForm.jsp
    }

    @PostMapping("/add")
    public String save(@Valid User user, BindingResult result, Model m) throws Exception {
        System.out.println("result=" + result);
        System.out.println("user=" + user);

        // User객체를 검증한 결과 에러가 없으면, registerForm을 이용해서 에러를 보여줘야 함.
        if (!result.hasErrors()) {
            // 2. DB에 신규 회원정보를 저장
            int rowCnt = userDao.insertUser(user);

            // FAIL이 아니면 회원가입 정보 이동
            if (rowCnt != FAIL) {
                return "registerInfo";
            }
        }
        return "registerForm";
    }

    private boolean isValid(User user) {
        return true;
    }
}