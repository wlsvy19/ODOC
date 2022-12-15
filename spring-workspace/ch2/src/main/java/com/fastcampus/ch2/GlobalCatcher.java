package com.fastcampus.ch2;

import java.io.FileNotFoundException;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice("com.fastcampus.ch2") //지정된 패키지에서 발생한 예외만 처리
//@ControllerAdvice // 예외처리는 컨트롤러를 통해서 해야 함, 모든 컨트롤러에서 처리하는 예외들을 처리할때 사용하는 어노테이션
public class GlobalCatcher {
    @ExceptionHandler(Exception.class) // 예외발생 했을때 호출됨. 어떤 예외를 다룰것인가 매개변수로
    public String catcher(Exception ex, Model m) {
        System.out.println("GlobalCatcher에서의 예외처리");
        m.addAttribute("ex", ex);
        return "error";
    }

    @ExceptionHandler({NullPointerException.class, FileNotFoundException.class}) 
    public String catcher2(Exception ex, Model m) {
        m.addAttribute("ex", ex);
        return "error";
    }
}
