package hello.typeconverter.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * IP와 포트번호가 문자로 들어오면 숫자로 변경하는 예제
 * "127.0.0.1:8008"
 */
@Getter
@EqualsAndHashCode
public class IpPort {
	private String ip;
	private int port;

	public IpPort(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
}
