package spring.core.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.core.common.MyLogger;

import javax.servlet.http.HttpServletRequest;
/*여러 클라이언트의 요청이 들어올때 클라이언트 개개인의 요청 처리가 가능한지 테스트*/
/*CoreApplication.java 에서 Springboot 실행 후, http://localhost:9090/log-demo 들어가서 테스트*/
@Controller
@RequiredArgsConstructor /*자동으로 생성자 생성되고, 주입 됨*/
public class LogDemoController {
    private final LogDemoService logDemoService;
//    private final ObjectProvider<MyLogger> myLoggerProvider;
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody /*뷰 페이지 없어도 됨, 메서드의 리턴값을 그대로 클라이언트한테 응답으로 보냄*/
    public String logDemo(HttpServletRequest request) throws InterruptedException {
//        MyLogger myLogger = myLoggerProvider.getObject();

        String requestURL = request.getRequestURL().toString();

        /*프록시를 사용한 가짜 MyLogger 클래스인가? -> EnhancerBySpringCGLIB 가짜 프록시 객체 만들어 주입*/
        System.out.println("myLogger = " + myLogger.getClass());

        myLogger.setRequestURL(requestURL);
        Thread.sleep(1000); /*1초의 지연을 줘서 테스트 해보기 -> 4덩이씩 나오나?*/
        myLogger.log("controller test...");
        logDemoService.logic("testId");
        return "OK";
    }
}