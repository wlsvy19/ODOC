package mvc1.servlet.basic.request;

import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

// 포스트맨에서 테스트
// HTTP Body에 단순 문자 데이터 담아 보내기 -> Body 선택, raw 선택 후 Text 로 해서 보내기
// http://localhost:8081/request-body-string
// 보낼데이터: hello
@WebServlet(name="requestBodyStringServlet", urlPatterns = "/request-body-string")
public class RequestBodyStringServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 메시지 body의 내용을 바이트 코드로 얻기
        ServletInputStream inputStream = request.getInputStream();
        // 바이트 -> String
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        System.out.println("messageBody = " + messageBody);
    }
}