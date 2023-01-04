package spring.core.beandefinition;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import spring.core.AppConfig;
import spring.core.beanfind.ApplicationContextExtendsFindTest;
/*
ApplicationContext 비교

JAVA beanDefinitionName = memberService, 
JAVA beanDefinition = Root bean: class [null];
scope=; abstract=false; 
lazyInit=null; 
autowireMode=3; 
dependencyCheck=0; 
autowireCandidate=true; 
primary=false; 
factoryBeanName=appConfig;  -> 팩토리 메소드 패턴 사용
factoryMethodName=memberService;   -> 팩토리 메소드 패턴 사용
initMethodName=null; 
destroyMethodName=(inferred); 
defined in spring.core.AppConfig

XML beanDefinitionName = memberService, 
XML beanDefinition = Generic bean: class [spring.core.member.MemberServiceImpl]; 
scope=; abstract=false; lazyInit=false; 
autowireMode=0; 
dependencyCheck=0; 
autowireCandidate=true; 
primary=false; 
factoryBeanName=null; 
factoryMethodName=null; 
initMethodName=null; 
destroyMethodName=null; 
defined in class path resource [appConfig.xml]
 
 */
public class BeanDefinitionTest {
    AnnotationConfigApplicationContext javaAc = new AnnotationConfigApplicationContext(AppConfig.class);
    GenericXmlApplicationContext xmlAc = new GenericXmlApplicationContext("appConfig.xml"); // resources폴더 아래 읽음

    @Test
    @DisplayName("java파일 빈 설정 메타정보 확인")
    void findApplicationBeanJava() {
        String[] beanDefinitionNames = javaAc.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = javaAc.getBeanDefinition(beanDefinitionName);

            if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                System.out.println("JAVA beanDefinitionName = " + beanDefinitionName +
                        ", JAVA beanDefinition = " + beanDefinition);
            }
        }
    }

    @Test
    @DisplayName("xml파일 빈 설정 메타정보 확인")
    void findApplicationBeanXml() {
        String[] beanDefinitionNames = xmlAc.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = xmlAc.getBeanDefinition(beanDefinitionName);
            if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                System.out.println("XML beanDefinitionName = " + beanDefinitionName +
                        ", XML beanDefinition = " + beanDefinition);
            }
        }
    }
}
