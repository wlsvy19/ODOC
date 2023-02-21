package mvc1.servlet.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import mvc1.servlet.basic.HelloData;
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
// http://localhost:8081/request-body-json
// 객체로 JSON 데이터 보내기 -> Body 선택, raw 선택 후 JSON 로 해서 보내기
// 보낼데이터: {"username": "hello", "age":20}
@WebServlet(name = "requestBodyJsonServlet", urlPatterns = "/request-body-json")
public class RequestBodyJsonServlet extends HttpServlet {
    // JSON 파싱(String -> JSON): Spring Boot에서 jackson라이브러리 기본적으로 제공
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        // 여기까지는 단순 Text 데이터 임
        System.out.println("messageBody = " + messageBody);

        //  {"username": "hello", "age":20} -> JSON형식으로 변환(파싱) -> HelloData 객체 타입(자바 객체)으로 변환
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        System.out.println("helloData.getUsername() = " + helloData.getUsername());
        System.out.println("helloData.getAge() = " + helloData.getAge());

    }
}
