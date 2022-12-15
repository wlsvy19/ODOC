package com.fastcampus.ch4.controller;

import com.fastcampus.ch4.domain.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class SimpleRestController {

    @GetMapping("/ajax")
    public String ajax() {
        return "ajax";
    }

    @PostMapping("/send")
    @ResponseBody
    public Person test(@RequestBody Person p) {
        // jackson-databind: 클라이언트에서 넘어온 문자열을 JAVA 객체로 자동 변환
        System.out.println("p = " + p);
        p.setName("ABC");
        p.setAge(p.getAge() + 10);

        // 클라이언트에 응답할 때 jacson-databind에 의해 JSON으로 자동 변환 됨
        return p;
    }

    @GetMapping("/test")
    public String test() {
        return "test"; // 단순 view파일 반환시에는 @Controller 써야함
    }
}