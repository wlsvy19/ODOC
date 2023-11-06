package hello.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;

/**
 * 업로드 점진적 발전
 * 서블릿1 버전
 */
@Slf4j
@Controller
@RequestMapping("/servlet/v1")
public class ServletUploadControllerV1 {

	// localhost:8092/servlet/v1/upload
	@GetMapping("/upload")
	public String newFile() {
		return "upload-form";
	}

	@PostMapping("/upload")
	public String saveFileV1(HttpServletRequest request) throws ServletException, IOException {
		log.info("서블릿 버전1) request = {}", request);

		String itemName = request.getParameter("itemName");
		log.info("서블릿 버전1) itemName={}", itemName);

		// parts는 muiltiPart 폼 데이터 받음
		Collection<Part> parts = request.getParts();
		log.info("서블릿 버전1) parts={}", parts);
		return "upload-form";
	}
}
