package hello.typeconverter.formatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * 1000 단위로 쉼표가 들어가는 포맷을 적용.
 * 반대도 처리해주는 포맷터도 적용.
 */
@Slf4j
public class MyNumberFormatter implements Formatter<Number> {

	@Override
	public Number parse(String text, Locale locale) throws ParseException {
		log.info("text={}, locale={}", text, locale);
		// "1,000" 처럼 숫자 중간의 쉼표를 적용하려면 자바가 기본으로 제공하는 NumberFormat 객체를 사용
		NumberFormat format = NumberFormat.getInstance(locale);

		// parse(): 문자를 숫자로 변환
		return format.parse(text);
	}

	// Number 타입은 Integer, Long 과 같은 숫자 타입의 부모 클래스
	@Override
	public String print(Number object, Locale locale) {
		log.info("object={}, locale={}", object, locale);
		return NumberFormat.getInstance(locale).format(object);
	}
}
