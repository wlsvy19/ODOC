package mvc1.servlet.basic.request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * GET 방식으로 URL에 파라미터를 담아 서버에 전송 하는 방법
 * http://localhost:8081/request-param?username=hello&age=20
 */

@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("RequestParamServlet.service 호출");

        System.out.println("[전체 파라미터 조회] - START");
        // 요청할때 URL에 기입한 파라미터 모두 호출
        Enumeration<String> parameterNames = request.getParameterNames();

        request.getParameterNames().asIterator().forEachRemaining(paramName
                -> System.out.println(paramName + "=" + request.getParameter(paramName)));

        System.out.println("[전체 파라미터 조회] - END");
        System.out.println();

        // 단일 파라미터 조회
        System.out.println("[단일 파라미터 조회] - START");
        String username = request.getParameter("username");
        System.out.println("username = " + username);
        String age = request.getParameter("age");
        System.out.println("age = " + age);
        System.out.println();
        System.out.println("[단일 파라미터 조회] - END");

        // http://localhost:8081/request-param?username=hello&age=20&username=hello2
        System.out.println("[이름이 같은 복수 파라미터 조회] - START");
        String[] usernames = request.getParameterValues("username");
        for (String name : usernames) {
            System.out.println("name = " + name);
        }
        System.out.println("[이름이 같은 복수 파라미터 조회] - END");
    }
}
