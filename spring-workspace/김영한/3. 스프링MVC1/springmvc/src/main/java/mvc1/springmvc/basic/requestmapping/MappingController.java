package mvc1.springmvc.basic.requestmapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


// HHTP Body에 응답을 담아 보내버림
@RestController
public class MappingController {

    private Logger log = LoggerFactory.getLogger(MappingController.class);

    /**
     * 기본 요청
     * 둘다 허용 /hello-basic, /hello-basic/
     * HTTP 메서드 모두 허용 GET, HEAD, POST, PUT, PATCH, DELETE
     */
    @RequestMapping(value = {"/hello-basic", "/hello-go"}, method = RequestMethod.GET)
    public String helloBasic() {
        log.info("helloBasic!!");
        return "ok";
    }

    /**
     * HTTP 메서드 매핑
     * method 특정 HTTP 메서드 요청만 허용 * GET, HEAD, POST, PUT, PATCH, DELETE
     */
    @RequestMapping(value = "/mapping-get-v1", method = RequestMethod.GET)
    public String mappingGetV1() {
        log.info("mappingGetV1");
        return "ok";
    }

    /**
     * HTTP 메서드 매핑 축약
     * 편리한 축약 애노테이션 (코드보기)
     *
     * @GetMapping
     * @PostMapping
     * @PutMapping
     * @DeleteMapping
     * @PatchMapping
     */
    @GetMapping(value = "/mapping-get-v2")
    public String mappingGetV2() {
        log.info("mapping-get-v2");
        return "ok";
    }

    /**
     * PathVariable(경로 변수) 사용: URL에 값 자체가 들어가 있는 상태
     * 변수명이 같으면 생략 가능
     * http://localhost:8082/mapping/userA
     *
     * @PathVariable("userId") String userId -> @PathVariable userId
     */
    @GetMapping("/mapping/{userId}")
    // public String mappingPath(@PathVariable("userId") String data) {
    public String mappingPath(@PathVariable String userId) { // 변수명 맞춘 경우
        log.info("mappingPath userId={}", userId);
        return "ok";
    }

    /**
     * PathVariable 사용 - 다중
     * http://localhost:8082/mapping/users/userA/orders/100
     */
    @GetMapping("/mapping/users/{userId}/orders/{orderId}")
    public String mappingPath(@PathVariable String userId, @PathVariable Long orderId) {
        log.info("mappingPath userId={}, orderId={}", userId, orderId);
        return "ok";
    }

    /**
     * 특정 파라미터 조건 매핑
     * 파라미터가 있어야 매핑 됨 - 거의 사용 안하는 추세
     * params="mode",
     * params="!mode"
     * params="mode=debug"
     * params="mode!=debug" (! = )
     * params = {"mode=debug","data=good"}
     * http://localhost:8082/mapping-param?mode=debug    <- 호출 o
     * http://localhost:8082/mapping-param    <- 호출 x
     */
    @GetMapping(value = "/mapping-param", params = "mode=debug")
    public String mappingParam() {
        log.info("mappingParam");
        return "ok";
    }
}