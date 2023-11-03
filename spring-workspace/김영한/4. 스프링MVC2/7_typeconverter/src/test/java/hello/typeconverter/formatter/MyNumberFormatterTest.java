package hello.typeconverter.formatter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Locale;

import static org.assertj.core.api.Assertions.*;

class MyNumberFormatterTest {
	MyNumberFormatter formatter = new MyNumberFormatter();

	@Test
	void parse() throws ParseException {
		Number result = formatter.parse("1,000", Locale.KOREA);
		// parse() 의 결과가 Long 이기 때문에 isEqualTo(1000L) 을 통해 비교할 때 마지막에 L 을 넣어주어야함
		assertThat(result).isEqualTo(1000L); // Long 타입 주의
	}

	@Test
	void print() {
		String result = formatter.print(1000, Locale.KOREA);
		assertThat(result).isEqualTo("1,000");
	}
}
