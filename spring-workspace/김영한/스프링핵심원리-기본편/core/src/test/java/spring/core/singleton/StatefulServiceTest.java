package spring.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    @Test
    @DisplayName("상태유지로 인한 싱글톤 문제점 테스트")
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        /*Thread A: A사용자가 10000원을 주문*/
        statefulService1.orderStateful("userA", 10000);

        /*Thread B: B사용자가 20000원을 주문*/
        statefulService2.orderStateful("userB", 20000);

        /*Thread A: A사용자가 주문 금액 조회*/
        /*A금액 10000이 나와야 하는데 this.price=price 땜에 B사용자의 금액 20000이 나와버리는 문제*/
        int Aprice = statefulService1.getPrice();
        System.out.println("Aprice = " + Aprice);

        Assertions.assertThat(statefulService2.getPrice()).isEqualTo(20000);
    }

    @Test
    @DisplayName("상태유지로 인한 싱글톤 문제점 해결")
    void statelessServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        /*Thread A: A사용자가 10000원을 주문*/
        int userAprice = statefulService1.orderStateless("userA", 10000);

        /*Thread B: B사용자가 20000원을 주문*/
        int userBprice = statefulService2.orderStateless("userB", 20000);


        System.out.println("userAprice = " + userAprice);
        System.out.println("userBprice = " + userBprice);
    }

    static class TestConfig {
        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }
}