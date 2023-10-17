package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
public class ServletExController {

	// 1. localhost:8090/error-ex
	@GetMapping("/error-ex")
	public void errorEx(){
		throw new RuntimeException("예외 발생");
	}

	// localhost:8090/error-404
	@GetMapping("/error-404")
	public void error404(HttpServletResponse response) throws IOException {
		response.sendError(404, "404 오류");
	}

	// localhost:8090/error-400
	@GetMapping("/error-400")
	public void error400(HttpServletResponse response) throws IOException {
		response.sendError(400, "400 오류");
	}

	// localhost:8090/error-500
	@GetMapping("/error-500")
	public void error500(HttpServletResponse response) throws IOException {
		response.sendError(500, "500 오류");
	}
}
