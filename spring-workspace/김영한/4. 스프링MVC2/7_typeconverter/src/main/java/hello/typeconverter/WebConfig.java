package hello.typeconverter;

import hello.typeconverter.converter.IntegerToStringConverter;
import hello.typeconverter.converter.StringToIntegerConverter;
import hello.typeconverter.formatter.MyNumberFormatter;
import hello.typeconverter.type.IpPortToStringConverter;
import hello.typeconverter.type.StringToIpPortConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	// 사용 할 컨버터를 스프링 내부에 등록
	@Override
	public void addFormatters(FormatterRegistry registry) {
		// 주석해도 동작됨, 기본적인 컨버터는 스프링에서 제공함
		// 아래 MyNumberFormatter(숫자->문자) 사용하기 위해 주석
		// registry.addConverter(new StringToIntegerConverter());
		// registry.addConverter(new IntegerToStringConverter());

		// 스프링에서 제공하는 기본 컨버터가 아닌 우리가 만든 컨버터이기 때문에 주석처리시 에러남
		registry.addConverter(new StringToIpPortConverter());
		registry.addConverter(new IpPortToStringConverter());

		// 숫자3자리마다 , 찍는 컨버터 추가
		// localhost:8091/converter-view 에서 확인
		registry.addFormatter(new MyNumberFormatter());
	}
}
