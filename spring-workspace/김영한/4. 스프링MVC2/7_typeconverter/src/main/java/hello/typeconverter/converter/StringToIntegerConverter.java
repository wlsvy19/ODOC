package hello.typeconverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

/**
 * 문자 -> 숫자 바꿔주는 컨버터 직접 만들기
 * 테스트 코드를 통한 테스트
 */
@Slf4j
public class StringToIntegerConverter implements Converter<String, Integer> {
	@Override
	public Integer convert(String source) {
		log.info("컨버터 source = {}", source);
		return Integer.valueOf(source);
	}
}
