package hello.typeconverter.controller;

import hello.typeconverter.type.IpPort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

	// localhost:8091/hello-v1?data=10
	@GetMapping("/hello-v1")
	public String helloV1(HttpServletRequest request) {
		// HTTP 요청 파라미터는 모두 문자로 처리
		// 10을 넣어도 문자10으로 인식
		String data = request.getParameter("data"); // 문자 타입이 넘어옴
		Integer intValue = Integer.valueOf(data);
		System.out.println("intValue = " + intValue);
		return "ok";
	}

	// 스프링MVC에서 @RequestParam 제공
	// localhost:8091/hello-v2?data=10
	// @@Configuration 을 통해 스프링 컨테이너에 자동 등록
	// ?data=10 쿼리 파라미터는 문자임
	// StringToInteger 컨버터가 자동 호출됨
	// http://localhost:8091/hello-v2?data=100,000    -> data = 100000 (,없어짐)
	@GetMapping("/hello-v2")
	public String helloV2(@RequestParam Integer data) {
		System.out.println("data = " + data);
		return "ok";
	}

	// localhost:8091/ip-port?ipPort=127.0.0.1:8080
	@GetMapping("/ip-port")
	public String ipPort(@RequestParam IpPort ipPort) {
		System.out.println("ipPort.getIp() = " + ipPort.getIp());
		System.out.println("ipPort.getPort() = " + ipPort.getPort());
		return "ok";
	}

}
