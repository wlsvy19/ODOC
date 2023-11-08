package hello.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * 스프링을 활용한 파일 업로드(서블릿보단 편함)
 */

@Slf4j
@Controller
@RequestMapping("/spring")
public class SpringUploadController {

	// C:\sw\ODOC 에 업로드 한 파일 생성 저장됨
	@Value("${file.dir}")
	private String fileDir;

	// localhost:8092/spring/upload
	@GetMapping("/upload")
	public String newFile() {
		return "upload-form";
	}

	@PostMapping("/upload")
	public String saveFile(@RequestParam String itemName, @RequestParam MultipartFile file, HttpServletRequest request) throws IOException {
		log.info("스프링 버전 request = {}", request);
		log.info("스프링 버전 itemName = {}", itemName);
		log.info("스프링 버전 multipartFile = {}", file);
		if (!file.isEmpty()) {
			String fullPath = fileDir + file.getOriginalFilename();
			log.info("파일저장 fullPath = {}", fullPath);
			file.transferTo(new File(fullPath));
		}
		return "upload-form";
	}
}
