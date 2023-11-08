package hello.upload.controller;


import hello.upload.domain.Item;
import hello.upload.domain.ItemRepository;
import hello.upload.domain.UploadFile;
import hello.upload.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/** localhost:8092
 * 스프링으로 관심사의 분리를 적용해 만든 파일 업로드와 다운로드
 * <p>
 * application.properteis에서 파일이 저장될 경로 잘 설정 해야함 (한글x)
 * 파일저장경로   C:/sw/ODOC/spring-workspace/upload_test/
 */

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {
	// 의존성 주입
	private final ItemRepository itemRepository;
	private final FileStore fileStore;

	@GetMapping("/items/new")
	public String newItem(@ModelAttribute ItemForm form) {
		return "item-form";
	}

	// 폼의 데이터를 저장하고 보여주는 화면으로 리다이렉트
	@PostMapping("/items/new") // 화면 폼에서 넘어오는 Item 받음
	public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
		// attache 파일 저장
		UploadFile attachFile = fileStore.storeFile(form.getAttachFile());

		// 이미지 파일들 저장
		List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

		// DB에 저장
		Item item = new Item();
		item.setItemName(form.getItemName());
		item.setAttachFile(attachFile);
		item.setImageFiles(storeImageFiles);
		itemRepository.save(item);

		redirectAttributes.addAttribute("itemId", item.getId());

		// 제출 후 상품 목록들 보여주는 화면으로 리다이렉트
		return "redirect:/items/{itemId}";
	}

	// 단순 상품을 보여주는 화면
	@GetMapping("/items/{id}")
	public String items(@PathVariable Long id, Model model) {
		Item item = itemRepository.findById(id);
		model.addAttribute("item", item);
		return "item-view";
	}

	// <img> 태그로 이미지를 조회할 때 사용
	// UrlResource 로 이미지 파일을 읽어서 @ResponseBody 로 이미지 바이너리를 반환
	@ResponseBody
	@GetMapping("/images/{filename}")
	public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
		// file:C:/sw/ODOC/spring-workspace/upload_test/
		System.out.println("파일경로: " + fileStore.getFullPath(filename));
		return new UrlResource("file:" + fileStore.getFullPath(filename));
	}

	// 파일을 [다운로드] 할 때 실행
	@GetMapping("/attach/{itemId}")
	public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {
		Item item = itemRepository.findById(itemId);
		String storeFileName = item.getAttachFile().getStoreFileName();

		// 다운로드 받을 때 업로드한 파일명을 알아야 하니까 필요
		String uploadFileName = item.getAttachFile().getUploadFileName();

		// 다운로드시 한글 or 특수문자 깨지는거 방지
		UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

		log.info("uploadFileName = {}", uploadFileName);
		String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);

		// 응답헤더에 Content-Disposition: attachment; filename="test2.png" -> attachment 여야 브라우저가 다운로드 함
		String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
				.body(resource);

	}
}
