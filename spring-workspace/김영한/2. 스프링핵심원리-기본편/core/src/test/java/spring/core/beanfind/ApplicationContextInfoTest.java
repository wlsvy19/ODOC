package spring.core.beanfind;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.core.AppConfig;

/*스프링 Bean 등록이 잘 되는지 테스트*/

/*JUnit5부터 public 안해도 됨*/

class ApplicationContextInfoTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames(); // 스프링에 등록된 모든 빈 이름 조회

        // iter + tab : 향상된 for문 자동 완성
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName); // 빈 이름으로 빈 객체(인스턴스) 조회
            System.out.println("name = " + beanDefinitionName + ", object = " + bean);
        }
    }

    @Test
    @DisplayName("사용한 애플리케이션 빈 출력하기")
    void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();

        // iter + tab : 향상된 for문 자동 완성
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);// bean 정보 조회

            /*ROLE_APPLICATION: 직접 등록한 애플리케이션 빈*/
            /*ROLE_INFRASTRUCTURE: 스프링 내부에서 사용하는 빈*/
            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) { // 내가 사용한 빈 들의 정보
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("name = " + beanDefinitionName + ", object = " + bean);
            }
        }
    }
}
