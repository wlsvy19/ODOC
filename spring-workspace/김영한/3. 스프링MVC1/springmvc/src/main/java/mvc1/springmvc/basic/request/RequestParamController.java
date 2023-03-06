package mvc1.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import mvc1.springmvc.basic.HelloData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

// HTTP 요청 파라미터 - @RequestParam

@Slf4j
@Controller
public class RequestParamController {
    /**
     * 반환 타입이 없으면서 이렇게 응답에 값을 직접 집어넣으면, view 조회X
     * 쿼리 파라미터 전송 - http://localhost:8082/request-param-v1?username=leejp&age=32
     * 폼 전송 - http://localhost:8082/basic/hello-form.html
     */
    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("username = {} , age = {}", username, age);

        response.getWriter().write("ok");
    }

    /**
     * @RequestParam 사용
     * - 파라미터 이름으로 바인딩
     * @ResponseBody 추가
     * - View 조회를 무시하고, 응답으로 HTTP message body에 직접 해당 내용 입력
     * http://localhost:8082/request-param-v2?username=hello&age=20
     */
    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(@RequestParam("username") String memberName, @RequestParam("age") int memberAge) {
        log.info("username = {} , age = {}", memberName, memberAge);
        return "ok";
    }

    /**
     * @RequestParam 사용
     * HTTP 파라미터 이름이 변수 이름과 같으면 @RequestParam(name="xx") 생략 가능
     * http://localhost:8082/request-param-v3?username=hello&age=20
     */
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(@RequestParam String username, @RequestParam int age) {
        log.info("username = {} , age = {}", username, age);
        return "ok";
    }

    /**
     * @RequestParam 사용
     * String, int 등의 단순 타입이면 @RequestParam 도 생략 가능
     * http://localhost:8082/request-param-v4?username=hello&age=20
     */
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * 파라미터 필수 여부 - requestParamRequired
     *
     * @RequestParam.required /request-param-required -> username이 없으므로 예외
     * 주의!
     * /request-param-required?username= -> 빈문자 "" 로 인식, 통과
     * 주의!
     * /request-param-required
     * int age -> null을 int에 입력하는 것은 불가능, 따라서 Integer 변경해야 함(또는 다음에 나오는 defaultValue 사용)
     */
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(@RequestParam(required = true) String username, @RequestParam(required = false) Integer age) {

        // int는 원시타입 -> null 불가능
        // int num = null;

        // Integer는 객체 -> null 가능
        Integer num2 = null;
        String str = null;

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * @RequestParam 기본 값 적용
     * - defaultValue 사용: 넘어오는 파라미터가 없을경우 사용
     * <p>
     * 참고: defaultValue는 빈 문자의 경우에도 적용: username=  이라고 해도 guest 찍힘
     * /request-param-default?username=
     */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(@RequestParam(required = true, defaultValue = "guest") String username, @RequestParam(required = false, defaultValue = "-1") int age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * @RequestParam Map, MultiValueMap
     * 파라미터를 Map으로 조회하기 - requestParamMap
     * Map(key=value)
     * MultiValueMap(key=[value1, value2, ...]) ex) (key=userIds, value=[id1, id2])
     * 몇개의 쿼리스트링이 와도 가능
     * http://localhost:8082/request-param-map?username=hello&age=20
     */
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }

    /**
     * @ModelAttribute 사용: 객체를 파라미터로 받음
     * 참고: model.addAttribute(helloData) 코드도 함께 자동 적용됨, 뒤에 model을 설명할 때 자세히 설명
     * http://localhost:8082/model-attribute-v1?username=hello&age=20
     */
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {
        // HelloData helloData = new HelloData();
        // helloData.setUsername(username);
        // helloData.setAge(age);

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        log.info("helloData = {}", helloData.toString());

        return "ok";
    }


    /**
     * @ModelAttribute 생략
     * String, int, Integer 등과 같은 단순 타입은 @RequestParam 사용 -> 생략 가능
     * 그 외 나머지 형태들(객체) @ModelAttiribute 사용 -> 생략 가능
     */
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) {
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        log.info("helloData = {}", helloData.toString());

        return "ok";
    }
}