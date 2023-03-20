package mvc1.servlet.basic.response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// http://localhost:8081/response-header
// 네트워크탭 - 응답 헤더 확인해보기
@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // [status-line]
        response.setStatus(HttpServletResponse.SC_OK);
        // response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        // [response-headers]
        // 컨텐츠 타입에 charset 을 utf-8로 해야 응답할때 한글 안깨짐
        // response.setHeader("Content-Type", "text/plain; charset=utf-8");
        // 메서드로 따로 빼서 할 수 있음
        content(response);

        // 캐시 무효화
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

        // 과거의 캐시 까지 무효화
        response.setHeader("Pragma", "no-cache");

        // 임의의 헤더 만들기
        response.setHeader("my-header", "hello");

        // 쿠키 호출
        cookie(response);

        // 리다이렉트
        // redirect(response);


        // [메시지 바디]
        PrintWriter writer = response.getWriter();
        writer.println("응답 메시지");
    } // end service()

    // 응답 헤더 편의 메서드
    private void content(HttpServletResponse response) {
        //Content-Type: text/plain;charset=utf-8
        //Content-Length: 2
        //response.setHeader("Content-Type", "text/plain;charset=utf-8");
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        // response.setContentLength(2); //(생략시 자동 생성)
    } // end content()

    // 쿠키 편의 메서드
    private void cookie(HttpServletResponse response) {
        // Set-Cookie: myCookie=good; Max-Age=600;
        // response.setHeader("Set-Cookie", "myCookie=good; Max-Age=600"); // 600초 동안 유효한 쿠키
        Cookie cookie = new Cookie("myCookie", "good");
        cookie.setMaxAge(600); //600초
        response.addCookie(cookie);
    } // end cookie()

    // 리다이렉트 편의 메서드
    private void redirect(HttpServletResponse response) throws IOException {
        // Status Code 302
        // Location: /basic/hello-form.html
        // response.setStatus(HttpServletResponse.SC_FOUND); //302
        // response.setHeader("Location", "/basic/hello-form.html");
        response.sendRedirect("/basic/hello-form.html");
    } // end redirect()
}
