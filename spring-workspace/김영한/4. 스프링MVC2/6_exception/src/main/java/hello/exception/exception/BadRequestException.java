package hello.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 원래, API 상에서 Exception 터지면 500으로 처리하는데 400으로 바꿈
// 스프링 부트가 기본제공: ResponseStatusExceptionResolver.java -> HTTP 상태 코드 지정
// reason 은 resources/messages.properties 를 바로보고 있어서 설정 가능
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
public class BadRequestException extends RuntimeException{
}
