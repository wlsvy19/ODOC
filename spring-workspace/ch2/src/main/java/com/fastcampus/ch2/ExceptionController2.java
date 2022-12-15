package com.fastcampus.ch2;

import java.io.FileNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
//com.fastcampus.ch2.MyException
@ResponseStatus(HttpStatus.BAD_REQUEST) // 500 -> 400
class MyException extends RuntimeException{
    MyException(String msg){
        super(msg);
    }
    
    MyException() {
        this("");
    }
    
}

@Controller
public class ExceptionController2 {
    @RequestMapping("/ex3") // 반환형이 void면 URL에서 매핑.jsp파일 찾음
    public String main() throws Exception {
        throw new Exception("예외가 발생!!!!");
    }

    @RequestMapping("/ex4")
    public String main2() throws Exception {
        throw new FileNotFoundException("예외가 발생!!!!");
    }
}
