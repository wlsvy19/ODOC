package mvc1.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * LEVEL: TRACE > DEBUG > INFO > WARN > ERROR
 * 개발 서버는 debug 출력
 * 운영 서버는 info 출력
 */

// 롬복에서 제공, Logger선언 필요 없음 -> log. 가능
@Slf4j
@RestController
public class LogTestController {
    // private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/log-test")
    public String logTest() {
        String name = "spring...";

        // 1레벨> 다 보여 주기
        log.trace("trace log={}", name);

        // 2레벨> 개발서버에서 디버그만 보여주기
        log.debug("debug log={}", name);

        // 3레벨> 운영서버에는 info부터
        log.info("info log={}", name);

        // 4레벨> 경고
        log.warn("warn log={}", name);

        // 5레벨> 에러(가장심각)
        log.error("error log={}", name);

        return "ok";
    }
}