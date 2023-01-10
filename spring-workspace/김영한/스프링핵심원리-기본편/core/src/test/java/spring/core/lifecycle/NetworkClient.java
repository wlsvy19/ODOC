package spring.core.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/*스프링 빈 생명주기 */
public class NetworkClient  {
    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, URL = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /*서비스 시작 시 url에 연결*/
    public void connect() {
        System.out.println("connect: " + url);
    }
    
    /*서비스 시작 후 특정 메서드를 호출*/
    public void call(String message) {
        System.out.println("call: " + url + ", message = " + message) ;
    }

    /*서비스 종료 시 호출*/
    public void disconnect() {
        System.out.println("close: " + url);
    }

    /*초기화: 스프링 빈의 생성과 의존관계 주입이 끝나면 호출  -> 거의 사용 안함*/
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        System.out.println("NetworkClient.afterPropertiesSet");
//        connect();
//        call("초기화 연결 메시지");
//    }

    /*소멸: 스프링 컨테이너가 내려가면 서 호출 -> 거의 사용 안함*/
//    @Override
//    public void destroy() throws Exception {
//        System.out.println("NetworkClient.destroy");
//        disconnect();
//    }

    /*InitializingBean: 초기화-스프링 빈의 생성과 의존관계 주입이 끝나면 호출*/
    @PostConstruct /*이거써라*/
    public void init(){
        connect();
        call("초기화 연결 메시지");
    }

    @PreDestroy /*이거써라*/
    /*DisposableBean : 소멸-스프링 컨테이너가 내려가면 서 호출*/
    public void close() {
        disconnect();
    }
}
