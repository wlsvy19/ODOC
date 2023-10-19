package hello.typeconverter.controller;

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
	@GetMapping("hello-v2")
	public String helloV2(@RequestParam Integer data) {
		System.out.println("data = " + data);
		return "ok";
	}
}
