package hello.upload.controller;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 데이터를 전달할, 이미지 저장할 폼
 */
@Data
public class ItemForm {
	private Long itemId;
	private String itemName;

	// 이미지 다중 업로드 하기 위해 MultipartFile 사용
	private List<MultipartFile> imageFiles;

	private MultipartFile attachFile;
}
