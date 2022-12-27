package com.fastcampus.ch2;

import java.util.Calendar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

// 년, 월, 일을 입력하면 요일을 알려주는 프로그램
@Controller
public class YoilTellerMVC {
    // public static void main(String[] args) {
    @RequestMapping("/getYoilMVC") // http:localhost/ch2/getYoilMVC?year=2022&month=08&day=23
//  public void main(HttpServletRequest request, HttpServletResponse response) {
    public String main(int year, int month, int day, Model model) {
//    public ModelAndView main(int year, int month, int day) {
        ModelAndView mv = new ModelAndView(); // 반환형을 ModelAndView 객체를 사용하여 할 수 있음

        // 1. 유효성 검사
        if (!isValid(year, month, day)) {
            return "yoilError"; // WEB-INF/views/yoilError.jsp
            //mv.setViewName("yoilError");
            //return mv;
        }

        // 2. 요일 계산
        char yoil = getYoil(year, month, day);

        // 3. 계산한 결과를 model에 저장
//        model.addAttribute("year", year);
//        model.addAttribute("month", month);
//        model.addAttribute("day", day);
//        model.addAttribute("yoil", yoil);

         return "yoil"; // WEB-INF/views/yoil.jsp

        // 4. ModelAndView에 작업한 결과를 저장
//        mv.addObject("year", year);
//        mv.addObject("month", month);
//        mv.addObject("day", day);
//        mv.addObject("yoil", yoil);

        // 5. 작업 결과를 보여줄 뷰의 이름을 지정
       // mv.setViewName("yoil");

        //return mv;

    }

    private boolean isValid(int year, int month, int day) {
        // TODO Auto-generated method stub
        return true;
    }

    private char getYoil(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // DAY_OF_WEEK 는 요일, 1:일요일, 2:월요일 ...
        return " 일월화수목금".charAt(dayOfWeek);

    }
}
