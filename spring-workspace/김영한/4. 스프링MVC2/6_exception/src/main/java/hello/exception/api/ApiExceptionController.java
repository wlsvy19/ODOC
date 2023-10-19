package hello.exception.api;

import hello.exception.exception.BadRequestException;
import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * API 예외 처리를 위한 컨트롤러
 * 아래 코드는 POST MAN 에서 테스트
 */

@Slf4j
@RestController
public class ApiExceptionController {
 	// POST 맨에서 테스트 - GET 요청 보낼때 Header에 Accept를 application/json 으로 세팅
	// localhost:8090/api/members/spring -> 정상
	// localhost:8090/api/members/ex -> 예외인데, HTML로 에러 페이지가 나옴, JSON 이 반환 되야 함
	@GetMapping("/api/members/{id}")
	public MemberDto getMember(@PathVariable("id") String id) {
		if (id.equals("ex")) {
			throw new RuntimeException("잘못된 사용자 입니다.");
		}
		// localhost:8090/api/members/bad
		if (id.equals("bad")) {
			throw new IllegalArgumentException("잘못된 입력 입니다.");
		}
		// localhost:8090/api/members/user-ex
		if (id.equals("user-ex")) {
			throw new UserException("사용자 오류 - 내가만든 Exception");
		}
		return new MemberDto(id, "hello " + id);
	}

	// 스프링 부트가 기본으로 제공하는 ExceptionResolve 3개
	// 1. ResponseStatusExceptionResolver 사용: Http 응답 코드 변경
	// localhost:8090/api/response-status-ex1
	@GetMapping("/api/response-status-ex1")
	public String responseStatusEx1() {
		throw new BadRequestException();
	}

	// 2. ResponseStatusException 사용:
	// localhost:8090/api/response-status-ex2
	@GetMapping("/api/response-status-ex2")
	public String responseStatusEx2() {
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
	}

	// 3. DefaultHandlerExceptionResolve 사용(우선순위 가장 낮음): 스프링 내부 예외 처리
	// localhost:8090/api/default-handler-ex?data=hello
	@GetMapping("/api/default-handler-ex")
	public String defaultException(@RequestParam Integer data) {
		return "ok";
	}


	@Data
	@AllArgsConstructor
	static class MemberDto {
		private String memberId;
		private String name;
	}
}
