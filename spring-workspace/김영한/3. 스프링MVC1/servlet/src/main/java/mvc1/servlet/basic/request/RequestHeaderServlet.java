package mvc1.servlet.basic.request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(name = "requestHeaderServlet", urlPatterns = "/request-header")
public class RequestHeaderServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        printStartLine(request);
        printHeaders(request);
        printHeaderUtils(request);
        printEtc(request);
    }

    private static void printStartLine(HttpServletRequest request) {
        System.out.println("*****HTTP 요청 시작*****");
        // HTTP 메서드 종류: GET
        System.out.println("request.getMethod() = " + request.getMethod());
        // HTTP 프로토콜 종류: HTTP / 1.1
        System.out.println("request.getProtocol() = " + request.getProtocol());
        // 스키마: http
        System.out.println("request.getScheme() = " + request.getScheme());
        // 요청이 들어온 URL: http://localhost:8080/request-header
        System.out.println("request.getRequestURL() = " + request.getRequestURL());
        // 요청이 들어온 URL(포트/ 뒤에 붙는게 URI): /request-header
        System.out.println("request.getRequestURI() = " + request.getRequestURI());
        // 쿼리 스트링: username=hi
        System.out.println("request.getQueryString() = " + request.getQueryString());
        // HTTPS 사용 유무
        System.out.println("request.isSecure() = " + request.isSecure());
        System.out.println("*****HTTP 요청 끝*****");
        System.out.println();
    }

    //Header 모든 정보 조회
    private void printHeaders(HttpServletRequest request) {
        System.out.println("--- HTTP 헤더 - start ---");

        // 예전 방식의 헤더 정보 가져오기
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//            System.out.println(headerName + ": " + request.getHeader(headerName));
//        }

        request.getHeaderNames().asIterator().forEachRemaining(headerName -> System.out.println(headerName + ": " + request.getHeader(headerName)));
        request.getHeader("host");

        System.out.println("--- HTTP 헤더 - end ---");
        System.out.println();
    }

    //Header 편리한 조회
    private void printHeaderUtils(HttpServletRequest request) {
        System.out.println("--- Header 편의 조회 start ---");
        System.out.println("[Host 편의 조회]");
        System.out.println("request.getServerName() = " + request.getServerName()); //Host 헤더
        System.out.println("request.getServerPort() = " + request.getServerPort()); //Host 헤더
        System.out.println();

        // 웹 브라우저의 언어
        System.out.println("[Accept-Language 편의 조회]");
        request.getLocales().asIterator().forEachRemaining(locale -> System.out.println("locale = " + locale));
        System.out.println("request.getLocale() = " + request.getLocale());

        System.out.println();
        System.out.println("[cookie 편의 조회]");
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                System.out.println(cookie.getName() + ": " + cookie.getValue());
            }
        }
        System.out.println();
        System.out.println("[Content 편의 조회]");
        System.out.println("request.getContentType() = " + request.getContentType());
        System.out.println("request.getContentLength() = " + request.getContentLength());
        System.out.println("request.getCharacterEncoding() = " + request.getCharacterEncoding());
        System.out.println("--- Header 편의 조회 end ---");
        System.out.println();
    }

    //기타 정보
    private void printEtc(HttpServletRequest request) {
        System.out.println("--- 기타 조회 start ---");
        System.out.println("[Remote 정보]");
        System.out.println("request.getRemoteHost() = " + request.getRemoteHost()); //
        System.out.println("request.getRemoteAddr() = " + request.getRemoteAddr()); //
        System.out.println("request.getRemotePort() = " + request.getRemotePort()); //
        System.out.println();
        System.out.println("[Local 정보]");
        System.out.println("request.getLocalName() = " + request.getLocalName()); //
        System.out.println("request.getLocalAddr() = " + request.getLocalAddr()); //
        System.out.println("request.getLocalPort() = " + request.getLocalPort()); //
        System.out.println("--- 기타 조회 end ---");
        System.out.println();
    }
}
