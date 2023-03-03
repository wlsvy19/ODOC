package mvc1.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Slf4j
@RestController
public class RequestHeaderController {
    @RequestMapping("/headers")
    public String headers(HttpServletRequest request, HttpServletResponse response, HttpMethod httpMethod, Locale locale, @RequestHeader MultiValueMap<String, String> headerMap, @RequestHeader("host") String host, @CookieValue(value = "myCookie", required = false) String cookie) {
        log.info("----------------------------------------");
        log.info("request={}", request);
        log.info("response={}", response);
        log.info("httpMethod={}", httpMethod);

        // local의 우선순위가 제일 높은것 사용, localeResolver사용하면 여러 locale 관리 가능
        log.info("locale={}", locale);

        // 헤더 정보, MultiValueMap: 하나의 key에 여러 value를 받을 수 있음
        log.info("headerMap={}", headerMap);

        // 호스트 정보
        log.info("header host={}", host);

        log.info("myCookie={}", cookie);

        return "ok";
    }
}