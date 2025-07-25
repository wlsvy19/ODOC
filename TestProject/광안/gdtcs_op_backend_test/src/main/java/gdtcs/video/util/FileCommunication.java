package gdtcs.video.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Component
public class FileCommunication {
	protected Log log = LogFactory.getLog(this.getClass());
	
	public String getRandomFileNameWithExt(String originalFileName){
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."), originalFileName.length());
		UUID uuid = UUID.randomUUID();
		LocalDateTime now = LocalDateTime.now();
		String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")); 
		return uuid.toString() + "_" + formatedNow + extension;
	}
	
	public String receiveFile(MultipartFile multipartFile, String uploadDirectoryPath) throws IllegalStateException, IOException{
		//multipartRequest.setCharacterEncoding("UTF-8");
		
		String fileName = multipartFile.getOriginalFilename();
		// 파일명 기반으로 허용할 파일 확장자 설정
		if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".gif") || fileName.endsWith(".png"))
		{
			// 고유한 이름 생성
			fileName = getRandomFileNameWithExt(fileName);
			
			// 저장할 파일 생성
			String filePath = uploadDirectoryPath + fileName;
			log.debug("filePath: " + filePath);
			File file = new File(filePath);
			
			// 권한설정
			file.setExecutable(false, true);
			file.setReadable(true);
			file.setWritable(false, true);
			
			// 경로에 해당하는 디렉토리가 없으면 생성한다.
			String directoryPath = file.getAbsolutePath().replace(file.getName(), "");
			File directory = new File(directoryPath);			
			if (!directory.isDirectory()) {
				log.debug("업로드 디렉토리가 없어서 생성하겠습니다. " + directoryPath);
				
				if(!directory.mkdirs()) {
					log.debug("업로드 디렉토리 생성에 실패하였습니다.");
					throw new FileNotFoundException();
				} else {
					log.debug("업로드 디렉토리를 생성하였습니다.");
				}
			}
			
			// 파일을 저장한다.
			if (multipartFile instanceof MultipartFile) {
				if (multipartFile != null && !multipartFile.isEmpty()) {
					multipartFile.transferTo(file);
				}
			}

		} else {
			log.debug("허용하지 않는 확장자입니다.");
			//throw new ServletException("허용하지 않는 확장자입니다.");
		}
		
		return fileName;
	}
}
