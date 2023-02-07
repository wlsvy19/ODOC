package spring.core.common;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

@Component
/*앱실행시 request 스코프 빈이 생성안됨(호출시에 실행됨) -> 오류 -> Provier 사용 -> 호출시점까지 컨테이너에 올라가는거 지연*/
/*프록시를 사용하여 request 스코프 빈의 호출 시점을 지연 시킴*/
/*가짜 myLogger의 프록시 클래스를 만들어두고 HTTP request와 상관 없이 가짜 프록시 클래스를 다른 빈에 미리 주입 가능*/
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLogger {
    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        System.out.println("[" + uuid + "]" + "[" + requestURL + "] " + message);
    }

    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] request create..." + this);

    }

    @PreDestroy
    public void close() {
        System.out.println("[" + uuid + "] request scope bean close..." + this);
    }
}
