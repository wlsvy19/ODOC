package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import hello.typeconverter.type.IpPortToStringConverter;
import hello.typeconverter.type.StringToIpPortConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 이렇게 타입 컨버터를 하나하나 직접 찾아서 타입 변환에 사용하는 것은 매우 불편하다.
 * 그래서 스프링은 개별 컨버터를 모아두고 그것들을 묶어서 편리하게 사용할 수 있는 기능을 제공하는데
 * 이것이 바로 컨버전 서비스( ConversionService )이다.
 *
 * 데이터만 넣으면 타입이 바로 변환되어 편하게 쓸 수 있음
 */
public class ConversionServiceTest {

	@Test
	void conversionService() {
		// 컨버터 등록
		DefaultConversionService conversionService = new DefaultConversionService();
		conversionService.addConverter(new StringToIntegerConverter());
		conversionService.addConverter(new IntegerToStringConverter());
		conversionService.addConverter(new StringToIpPortConverter());
		conversionService.addConverter(new IpPortToStringConverter());
		
		// 컨버터 사용
		// 문자 10 -> 숫자 10
		Integer result = conversionService.convert("10", Integer.class);
		System.out.println("result = " + result);

		assertThat(conversionService.convert("10", Integer.class)).isEqualTo(10);
		assertThat(conversionService.convert(10, String.class)).isEqualTo("10");

		IpPort ipPort = conversionService.convert("127.0.0.1:8080", IpPort.class);
		assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));

		String ipPortString = conversionService.convert(new IpPort("127.0.0.1", 8080), String.class);
		assertThat(ipPortString).isEqualTo("127.0.0.1:8080");
	}
}
