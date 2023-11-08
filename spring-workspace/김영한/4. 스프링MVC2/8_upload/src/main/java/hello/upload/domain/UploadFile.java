package hello.upload.domain;

import lombok.Data;

@Data
public class UploadFile {
	// 따로 만드는 이유는, 다른 고객이 이미지 파일명을 같게 설정 할 수 있어서

	// 고객이 업로드한 파일명
	private String uploadFileName;

	// 서버 내부에서 관리하는 파일명
	private String storeFileName;

	public UploadFile(String uploadFileName, String storeFileName) {
		this.uploadFileName = uploadFileName;
		this.storeFileName = storeFileName;
	}
}
