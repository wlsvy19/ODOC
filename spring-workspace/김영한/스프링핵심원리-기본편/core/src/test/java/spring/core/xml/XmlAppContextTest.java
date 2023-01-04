package spring.core.xml;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import spring.core.member.MemberService;

/*XML 파일로 스프링 설정하기 - 스프링 컨테이너에 빈 등록 등등*/
public class XmlAppContextTest {

    @Test
    void xmlAppContext() {
        /*설정 정보가 .java 파일에서 .xml 파일로 변경 됐지만 돌아가는건 똑같음*/
        ApplicationContext ac = new GenericXmlApplicationContext("appConfig.xml"); // resources폴더 아래 읽음
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        Assertions.assertThat(memberService).isInstanceOf(MemberService.class);
    }
}
