package mvc1.servlet.basic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// http://localhost:8081/hello?username=이진표 -> http://localhost:8081/hello?username=%EC%9D%B4%EC%A7%84%ED%91%9C
// 이진표가 16진수로 표현된 상황(복붙해서 다른곳에서 사용 하려면 디코딩 필요)
// URL에는 한글과 띄어쓰기가 불가능해서 인코딩을 해야함(여기선 utf-8로 했음)
// 웹 브라우저는 자동으로 URL을 인코딩 함
@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {
    // HTTP 요청을 통해 매핑된 URL이 호출되면 서블릿 컨테이너는 service 메서드를 실행
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("HelloServlet.service");

        System.out.println("request = " + request);
        System.out.println("response = " + response);

        String username = request.getParameter("username");
        System.out.println("username = " + username);
        
        // 단순 문자로 응답
        response.setContentType("text/plain");
        // 응답 할때 인코딩 -> 안하면 깨져서 나옴
        response.setCharacterEncoding("utf-8");
        // HTTP body 에 데이터 담겨서 응답
        response.getWriter().write("hello " + username);
    }
}
