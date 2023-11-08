package hello.upload.file;

import hello.upload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

	// 파일 저장될 경로
	@Value("${file.dir}")
	private String fileDir;

	public String getFullPath(String filename) {
		return fileDir + filename;
	}

	// 1개의 파일 저장 메서드
	public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
		// 업로드한 파일이 없으면 null값 반환
		if (multipartFile.isEmpty()) {
			return null;
		}
		String originalFilename = multipartFile.getOriginalFilename();
		String storeFileName = createStoreFileName(originalFilename);

		multipartFile.transferTo(new File(getFullPath(storeFileName)));
		return new UploadFile(originalFilename, storeFileName);
	}

	// 이미지 여러개 올라올 때 저장하는 메서드
	public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
		// 업로드한 파일들을 리스트에 계속 담음
		List<UploadFile> storeFileResult = new ArrayList<>();

		for (MultipartFile multipartFile : multipartFiles) {
			if (!multipartFile.isEmpty()) {
				storeFileResult.add(storeFile(multipartFile));
			}
		}
		return storeFileResult;
	}

	// 오리지널 파일 name을 넘기면 DB에 저장되는 유일한 파일이름 저장 하는 메서드
	private String createStoreFileName(String originalFilename) {
		// 확장자
		String extension = extractExt(originalFilename);
		String uuid = UUID.randomUUID().toString();
		return uuid + "." + extension;
	}

	// 확장자만 가져오는 메서드 (.png , .jpeg 등등)
	private String extractExt(String originalFilename) {
		// 위치를 가져옴
		int position = originalFilename.lastIndexOf(".");
		// . 다음 값(확장) 리턴
		return originalFilename.substring(position + 1);
	}
}
