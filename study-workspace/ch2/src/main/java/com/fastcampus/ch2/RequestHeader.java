package com.fastcampus.ch2;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//요청 할 때 헤더에 어떤 정보가 나오는지 보기 위한 예제
@Controller
public class RequestHeader {
    @RequestMapping("/requestHeader")
    public void main(HttpServletRequest request) {
        
        Enumeration<String> e = request.getHeaderNames();

        while (e.hasMoreElements()) {
            String name = e.nextElement();
            //System.out.println(name + ":" + request.getHeader(name));
        }
    }
}