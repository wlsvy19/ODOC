package com.fastcampus.ch2;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegisterController {
    
    @InitBinder // 이 컨트롤러 내에서만 처리 가능
    public void toDate(WebDataBinder binder) {
        // ConversionService에 어떤 Converter들이 등록되어 있는지 보기
        //ConversionService conversionService = binder.getConversionService();
        //System.out.println("conversionService=" + conversionService);
        
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        binder.registerCustomEditor(Date.class, new CustomDateEditor(df, false)); -> vo에서 DateTimeFormat 으로 가능
        
        binder.registerCustomEditor(String[].class, new StringArrayPropertyEditor("#")); // String배열을 #로 짤라서 문자로 바꿔라
        //binder.registerCustomEditor(String[].class, "hobby", new StringArrayPropertyEditor("#")); // 특정필드만 적용 가능
        
        // 자동 검증
        //binder.setValidator(new UserValidator()); // UserValidator를 WebDataBinder의 로컬 validator로 등록
        //binder.addValidators(new UserValidator()); // GlobalValidator에 추가
        List<Validator> validator = binder.getValidators();
        System.out.println("validator List=" + validator);


    }
    
    //@RequestMapping(value="/register/add",method= {RequestMethod.POST, RequestMethod.POST}) // 둘다 허용, 배열로 사용 가능
   // @RequestMapping("/register/add") // 신규회원 가입 화면->단순화면만 보여줌, 기능x-> dispatcher 서블렛에서 등록 가능
    @GetMapping("/add")
    public String register() {
        return "registerForm"; // WEB-INF/views/registerForm.jsp
    }
    
    //@RequestMapping("/register/save", method=RequestMethod.POST)
    @PostMapping("/add") //srpig 4.3버전부터 PostMapping 추가->버전업 pom.xml에서 5.0.7로 하고 프로젝트우클릭-Maven-Project Update
    public String save(@Valid User user, BindingResult result, Model model) throws UnsupportedEncodingException {
        System.out.println("result=" + result);
        System.out.println("user=" + user);
        
        // 수동 검증: Validator를 직접 생성하고, validate()를 직접 호출
//        UserValidator userValidator = new UserValidator();
//        userValidator.validate(user, result); // BindingResult는 Errors의 자손, result에 검증결과 담김
        
        // User객체를 검증한 결고 에러가 있으면, registerForm을 이용해서 에러를 보여줘야 함
        if(result.hasErrors()) {
            return "registerForm";
        }
        
        //1. 회원가입 유효성 검사
//        if(!isValid(user)) {
//            String msg = URLEncoder.encode("id를 잘못 입력 했습니다.", "utf-8");
//            model.addAttribute("msg", msg);
//            return "forward:/register/add";
//          //return "redirect:/register/add?msg=" + msg; // URL재작성(rewriting)
//        }
        
        //2. DB에 신규회원 정보 저장
        return "registerInfo";
    }

    private boolean isValid(User user) {
        return true; // true는 어떤 아이디나 패스워드 입력해도 통과 ok
    }
}
