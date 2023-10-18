package hello.exception.api;


import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ExceptionHandler 사용 -> ExceptionHandlerExceptionResolve 사용
 */

@Slf4j
@RestController
public class ApiExceptionV2Controller {
	// localhost:8090/api2/members/ex
	@GetMapping("/api2/members/{id}")
	public MemberDto getMember(@PathVariable("id") String id) {
		if (id.equals("ex")) {
			throw new RuntimeException("잘못된 사용자 입니다.");
		}
		// localhost:8090/api2/members/bad
		if (id.equals("bad")) {
			throw new IllegalArgumentException("잘못된 입력 입니다.");
		}
		// localhost:8090/api2/members/user-ex
		if (id.equals("user-ex")) {
			throw new UserException("사용자 오류 - 내가만든 Exception");
		}
		return new MemberDto(id, "hello " + id);
	}

	@Data
	@AllArgsConstructor
	static class MemberDto {
		private String memberId;
		private String name;
	}

}
