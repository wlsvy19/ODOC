package com.fastcampus.ch2;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// 년, 월, 일을 입력하면 요일을 알려주는 프로그램
@Controller
public class YoilTeller {
    // public static void main(String[] args) {
    @RequestMapping("/getYoil")
    public void main(HttpServletRequest request, HttpServletResponse response) {
        // 1. 입력
//        String year = args[0];
//        String month = args[1];
//        String day = args[2];

        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String day = request.getParameter("day");

        // 2. 작업
        int yyyy = Integer.parseInt(year);
        int mm = Integer.parseInt(month);
        int dd = Integer.parseInt(day);

        Calendar cal = Calendar.getInstance();
        cal.set(yyyy, mm - 1, dd);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // DAY_OF_WEEK 는 요일, 1:일요일, 2:월요일 ...
        char yoil = " 일월화수목금".charAt(dayOfWeek);

        // 3. 출력
//        System.out.println(year + "년" + month + "월" + day + "일은 ");
//        System.out.println(yoil + "요일입니다.");

        try {
            // 브라우저는 내가 보내는 내용을 모르기 때문에 맞춰줘야 함
            response.setContentType("text/html"); // text로 보냄
            response.setCharacterEncoding("utf-8"); // 인코딩을 설정해줘야 안깨짐
            PrintWriter out = response.getWriter();// response 객체에서 브라우저로의 출력 스트림을 얻
            out.println(year + "년" + month + "월" + day + "일은 ");
            out.println(yoil + "요일입니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
