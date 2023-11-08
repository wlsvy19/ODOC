package hello.upload.domain;

import lombok.Data;

import java.util.List;

@Data
public class Item {
	// DB에 저장 할 필드
	private Long id;
	private String itemName;
	private UploadFile attachFile;
	// 이미지는 여러개 업로드
	private List<UploadFile> imageFiles;
}
