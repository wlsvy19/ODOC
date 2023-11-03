package hello.typeconverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

/**
 * 숫자 -> 문자 바꿔주는 컨버터 직접 만들기
 * 테스트 코드를 통한 테스트
 */

@Slf4j
public class IntegerToStringConverter implements Converter<Integer, String> {
	@Override
	public String convert(Integer source) {
		log.info("직접만든 숫자->문자 컨버터 소스 = {}", source);
		return String.valueOf(source);
	}
}
