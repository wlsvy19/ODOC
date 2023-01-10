package spring.core.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 사용 -> 소멸전 콜백 스프링 종료*/
public class BeanLifeCycleTest {

    @Test
    public void lifeCycleTest() {
        /*close()하기 위해 ConfigurableApplicationContext 인터페이스 필요*/
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        /*1. 스프링 빈 호출*/
        NetworkClient client = ac.getBean(NetworkClient.class);
        ac.close();

    }

    @Configuration
    static class LifeCycleConfig {
        /*2. 스프링 컨테이너에 스프링 빈으로 등록되어 호출 됨*/
        //@Bean(initMethod = "init", destroyMethod = "close") /* <- 이런식으로 사용 많이 함, 'close' 'shutdown' 은 inferred에 기본 등록*/
        @Bean
        public NetworkClient networkClient() {
            /*3. 생성자 호출하여 객체 생성 -> 이때, url정보가 없기 때문에 null값임 */
            NetworkClient networkClient = new NetworkClient();

            /*4. 설정자를 통한 의존관계 주입*/
            networkClient.setUrl("http://spring-core.dev");

            /*5. InitializingBean, DisposableBean 를 통한 스프링 빈 동작 확인*/

            return networkClient;
        }
    }

}
