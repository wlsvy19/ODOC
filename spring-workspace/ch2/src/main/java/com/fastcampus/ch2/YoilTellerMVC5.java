package com.fastcampus.ch2;

import java.util.Calendar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class YoilTellerMVC5 {
    @RequestMapping("/getYoilMVC5") // http://localhost/ch2/getYoilMVC5
    // public ModelAndView main(@ModelAttribute("myDate") MyDate date) { // 아래와 동일
    public String main(@ModelAttribute MyDate date, Model model) { // 키를 소문자로 한거와 동일
        System.out.println("date=" + date);

        // 1. 유효성 검사
        if (!isValid(date)) {
            return "yoilError";
        }
        
        
        // @ModelAttribute 사용하면 아래 처리 하는부분 생략 가
        // 2. 처리
        //char yoil = getYoil(date);
        
        // 3. Model에 작업 결과 저장
        //model.addAttribute("myDate", date);
        //model.addAttribute("yoil", yoil);
        
        // 4. 작업 결과를 보여줄 View의 이름 반환
        return "yoil";
        

    }

    private @ModelAttribute("yoil")char getYoil(MyDate date) {
        return getYoil(date.getYear(), date.getMonth(), date.getDay());
    }

    private char getYoil(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        return " 일월화수목금토".charAt(dayOfWeek);
    }

    private boolean isValid(MyDate date) {
        return isValid(date.getYear(), date.getMonth(), date.getDay());
    }

    private boolean isValid(int year, int month, int day) {
        if (year == -1 || month == -1 || day == -1)
            return false;

        return (1 <= month && month <= 12) && (1 <= day && day <= 31); // 간단히 체크
    }
}