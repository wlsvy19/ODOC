package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 에러 발생시 사용되는 컨트롤러
 */
@Slf4j
@Controller
public class ErrorPageController {
	//RequestDispatcher 상수로 정의되어 있음
	public static final String ERROR_EXCEPTION = "javax.servlet.error.exception";
	public static final String ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
	public static final String ERROR_MESSAGE = "javax.servlet.error.message";
	public static final String ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
	public static final String ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name";
	public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";

	@RequestMapping("/error-page/404")
	public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
		log.info("에러페이지 컨트롤러에서 errorPage 404() 호출");
		printErrorInfo(request);
		return "error-page/404";
	}

	// 3
	@RequestMapping("/error-page/500")
	public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
		log.info("에러페이지 컨트롤러에서 errorPage 500() 호출");
		printErrorInfo(request);
		return "error-page/500";
	}

	private void printErrorInfo(HttpServletRequest request) {
		log.info("ERROR_EXCEPTION: {}", request.getAttribute(ERROR_EXCEPTION));
		log.info("ERROR_EXCEPTION_TYPE: {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
		log.info("ERROR_MESSAGE: {}", request.getAttribute(ERROR_MESSAGE));
		log.info("ERROR_REQUEST_URI: {}", request.getAttribute(ERROR_REQUEST_URI));
		log.info("ERROR_SERVLET_NAME: {}", request.getAttribute(ERROR_SERVLET_NAME));
		log.info("ERROR_STATUS_CODE: {}", request.getAttribute(ERROR_STATUS_CODE));
		log.info("### dispatchType: {}", request.getDispatcherType());
	}

	// 요청 헤더에 Accept가 application/json 이면 JSON 형식으로 응답함
	// ResponseEntity: Http Body에 데이터를 직접 넣음
	@RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> errorPage500Api(HttpServletRequest request, HttpServletResponse response) {
		log.info("API errorPage 500 입니다.");

		HashMap<Object, Object> result = new HashMap<>();
		Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
		result.put("status", request.getAttribute(ERROR_STATUS_CODE));
		result.put("message", ex.getMessage());
		Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		return new ResponseEntity(result, HttpStatus.valueOf(statusCode));
	}
}
