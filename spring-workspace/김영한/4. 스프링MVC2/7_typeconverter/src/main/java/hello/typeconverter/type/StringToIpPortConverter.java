package hello.typeconverter.type;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

/**
 * "127.0.0.1:8080"  -> 숫자로 변경
 */
@Slf4j
public class StringToIpPortConverter implements Converter<String, IpPort> {
	@Override
	public IpPort convert(String source) {
		log.info("컨버트 source = {}", source);
		// 127.0.0.1:8080 을 : 기준으로 자름
		String[] split = source.split(":");
		// 앞쪽은 ip
		String ip = split[0];
		// 뒤쪽은 포트번호
		int port = Integer.parseInt(split[1]);

		// "127.0.0.1:8080" -> IpPort 객체
		return new IpPort(ip, port);
	}
}
