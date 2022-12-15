package com.fastcampus.ch2;

import java.io.FileNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ExceptionController {

    @ExceptionHandler({ NullPointerException.class, FileNotFoundException.class })
    public String catcher2(Exception ex, Model m) {
        System.out.println("catcher() in ExceptionController");
        System.out.println("m=" + m);
        m.addAttribute("ex", ex);
        return "error";
    }

    @ExceptionHandler(Exception.class) // 예외발생 했을때 호출됨. 어떤 예외를 다룰것인가 매개변수로
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // error.jsp가 성공적으로 보여져셔200뜸->상태코드 500으로 변경
    public String catcher(Exception ex, Model m) {
        System.out.println("Exception 컨트롤러에서의 예외처리");
        m.addAttribute("ex", ex);
        return "error";
    }

    @RequestMapping("ex") // 반환형이 void면 URL에서 매핑.jsp파일 찾음
    public String main() throws Exception {
        throw new Exception("예외가 발생!!!!");
    }

    @RequestMapping("ex2")
    public String main2() throws Exception {
        throw new FileNotFoundException("예외가 발생!!!!");
    }
}
