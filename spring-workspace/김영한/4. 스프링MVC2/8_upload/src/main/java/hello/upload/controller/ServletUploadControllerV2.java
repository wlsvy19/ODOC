package hello.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * 업로드 점진적 발전
 * 서블릿2 버전: 파일을 특정 폴더에 업로드
 */
@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {


	// @Value는 application.properties 속성을 그대로 쓸 수 있음
	@Value("${file.dir}")
	private String fileDir;

	// localhost:8092/servlet/v2/upload
	@GetMapping("/upload")
	public String newFile() {
		return "upload-form";
	}

	@PostMapping("/upload")
	public String saveFileV2(HttpServletRequest request) throws ServletException, IOException {
		log.info("서블릿 버전2) request = {}", request);

		String itemName = request.getParameter("itemName");
		log.info("서블릿 버전2) itemName={}", itemName);

		// parts는 muiltiPart 폼 데이터 받음
		Collection<Part> parts = request.getParts();
		log.info("서블릿 버전2) parts={}", parts);

		for (Part part : parts) {
			log.info("=== PART ===");
			log.info("name={}", part.getName());
			Collection<String> headerNames = part.getHeaderNames();
			for (String headerName : headerNames) {
				log.info("header {} : {}", headerName, part.getHeader(headerName));
			}

			// 편의 메서드 제공
			// Content-Dispositions
			log.info("submittedFilename = {}", part.getSubmittedFileName());
			log.info("size = {}", part.getSize());

			// 데이터 읽기
			InputStream inputStream = part.getInputStream();
			String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
			log.info("body = {}", body);

			//파일에 저장하기
			if (StringUtils.hasText(part.getSubmittedFileName())) {
				String fullPath = fileDir + part.getSubmittedFileName();
				log.info("파일 저장 fullPath={}", fullPath);
				part.write(fullPath);
			}
		}
		return "upload-form";
	}
}
