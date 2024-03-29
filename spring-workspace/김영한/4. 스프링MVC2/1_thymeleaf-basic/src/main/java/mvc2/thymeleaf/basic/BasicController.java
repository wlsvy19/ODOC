package mvc2.thymeleaf.basic;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 타임리프 기본 문법을 다루는 컨트롤러
 */

@Controller
@RequestMapping("/basic")
public class BasicController {
    @GetMapping("/text-basic")
    public String textBasic(Model model) {
        model.addAttribute("data", "Hello Spring!");
        return "basic/text-basic";
    }

    /**
     * unescaped: HTML 태그 노출 허용
     */
    @GetMapping("text-unescaped")
    public String textUnescaped(Model model) {
        model.addAttribute("data", "Hello <b>Spring!</b>");
        return "basic/text-unescaped";
    }

    /**
     * 타임리프에서 변수를 사용할 때는 변수 표현식을 사용함
     */
    @GetMapping("variable")
    public String variable(Model model) {
        User userA = new User("userA", 10);
        User userB = new User("userB", 25);

        List<User> list = new ArrayList<>();
        list.add(userA);
        list.add(userB);

        Map<String, User> map = new HashMap<>();
        map.put("userA", userA);
        map.put("userB", userB);

        model.addAttribute("user", userA);
        model.addAttribute("users", list);
        model.addAttribute("userMap", map);

        return "basic/variable";
    }

    @Data
    static class User {
        private String username;
        private int age;

        public User(String username, int age) {
            this.username = username;
            this.age = age;
        }
    }

    /**
     * Session: 웹 브라우저 종료 하기 전 까지 유지 되는 객체
     */
//    @GetMapping("/basic-objects")
//    public String basicObjects(HttpSession session){
//        session.setAttribute("sessionData", "Hello Session!");
//        return "basic/basic-objects";
//    }

    /**
     * 스프링 부트 3.0 이상이라면 다음과 같이 작성하자.
     * 스프링 부트 3.0이라면 직접 model 에 해당 객체를 추가해서 사용해야 한다.
     */
    @GetMapping("/basic-objects")
    public String basicObjects(Model model, HttpServletRequest request,
                               HttpServletResponse response, HttpSession session) {
        session.setAttribute("sessionData", "Hello Session");
        model.addAttribute("request", request);
        model.addAttribute("response", response);
        model.addAttribute("servletContext", request.getServletContext());
        return "basic/basic-objects";
    }

    @Component("helloBean")
    static class HelloBean {
        public String hello(String data) {
            return "Hello " + data;
        }
    }

    /**
     * 유틸리티 객체와 날짜
     * 타임리프에서 자바8 날짜인 LocalDate , LocalDateTime , Instant 를 사용하려면 추가 라이브러리가 필요하다. 스프링 부트 타임리프를 사용하면 해당 라이브러리가 자동으로 추가되고 통합된다.
     * 라이브러리: thymeleaf-extras-java8time
     */
    @GetMapping("/date")
    public String date(Model model) {
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "basic/date";
    }

    /**
     * URL 링크 사용 방법
     */
    @GetMapping("/link")
    public String link(Model model) {
        model.addAttribute("param1", "data1");
        model.addAttribute("param2", "data2");
        return "basic/link";
    }

    /**
     * 리터럴: 소스 코드상에 고정된 값
     */
    @GetMapping("literal")
    public String literal(Model model) {
        model.addAttribute("data", "Spring!");
        return "basic/literal";
    }

    /**
     * 여러가지 연산
     */
    @GetMapping("operation")
    public String operation(Model model) {
        model.addAttribute("nullData", null);
        model.addAttribute("data", "Spring");
        return "basic/operation";
    }

    /**
     * 속성 값 설정
     */
    @GetMapping("attribute")
    public String attribute() {
        return "basic/attribute";
    }

    private void addUsers(Model model) {
        List<User> list = new ArrayList<>();
        list.add(new User("userA", 10));
        list.add(new User("userB", 20));
        list.add(new User("userC", 30));
        list.add(new User("userD", 40));
        list.add(new User("userE", 50));

        model.addAttribute("users", list);
    }

    /**
     * 반복 - each
     */
    @GetMapping("/each")
    public String each(Model model) {
        addUsers(model);
        ;
        return "basic/each";
    }

    /**
     * 조건부 평가 - if~unless, switch~case
     */
    @GetMapping("condition")
    public String condition(Model model) {
        addUsers(model);
        return "basic/condition";
    }

    /**
     * 주석
     */
    @GetMapping("comments")
    public String comments(Model model) {
        model.addAttribute("data", "Spring!");
        return "basic/comments";
    }

    /**
     * 블록
     */
    @GetMapping("block")
    public String block(Model model) {
        addUsers(model);
        return "basic/block";
    }

    /**
     * 자바스크립트 인라인: 자바스크립트에서 타임리프를 편리하게 사용 할 수 있는 기능 제공
     * 소스보기로 확인
     */
    @GetMapping("javascript")
    public String javascript(Model model) {
        model.addAttribute("user", new User("userA", 10));
        addUsers(model);
        return "basic/javascript";
    }


}