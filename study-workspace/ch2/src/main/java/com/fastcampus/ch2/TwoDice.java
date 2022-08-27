package com.fastcampus.ch2;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
// 서버가 제공하는 리소스 2가지: 동적, 정적 리소스
public class TwoDice { //동적 리소스: 실행될 때 마다 주사위가 달라짐 ex) 실시간 스트리밍
    @RequestMapping("/rollDice")
    public void main(HttpServletResponse response) throws IOException {
        int idx = (int) (Math.random() * 6) + 1;

        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("</head>");
        out.println("<body>");
        out.println("<img src='resources/img/dice" + idx + ".jpg'>"); // 정적리소스: 파일형태로 되어있어서 바뀌지 않음, ex: .js, .css, .html ...
        out.println("</body>");
        out.println("</html>");
    } // end main
} // end class
