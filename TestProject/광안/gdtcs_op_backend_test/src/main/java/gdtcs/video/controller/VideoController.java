package gdtcs.video.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import gdtcs.common.service.ServerInfoService;
import gdtcs.video.service.VideoService;


/**
 * 
 * @파일명		: VideoController
 * @프로그램설명	: 차량 영상을 제공하는 컨트롤러
 * @작성자		: 박형철
 * @작성일		: 2024. 07. 26.
 * @version 0.1
 * @개정이력
 *
 * <pre>
 *  수정일            수정자              수정내용
 *  -------    --------    ---------------------------
 *	 2024. 07. 26.   박형철     최초생성
 * </pre>
 *
 */
@Controller
@ResponseBody
@RequestMapping("/api/video")
public class VideoController {
	protected Log log = LogFactory.getLog(this.getClass());

	ClassPathResource file_no_image = new ClassPathResource("image/no_image.png");
	
    @Resource(name = "videoService")
    private VideoService videoService;
    
    @Autowired
	private ServerInfoService serverInfo;
    
    /**
	 *  
	 * @Method Name	: getEtcsImage
	 * @Method 설명	: ETCS 차량 영상을 가져온다
	 * @작성자 		: 박형철
	 * @작성일 		: 2024. 07. 26.
	 *
	 * @param dto
	 * @param model
	 * @return
	 * @throws IOException
	 *
	 */
	@RequestMapping(value = "/etcs/getImage")
	public void getEtcsImage(@RequestParam Map<String, Object> param, HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//log.info("VideoController.getEtcsImage() Started.");
		String IC_CODE = (String) request.getParameter("IC_CODE");
		String tolling_time = (String) request.getParameter("tolling_time");
		String work_number = (String) request.getParameter("work_number");
		String processing_number = (String) request.getParameter("processing_number");
		String filepathStartOffset = (String) request.getParameter("filepathStartOffset");
		//log.info("Variables are initialized.");
		File file = null;
		try {
			//.jar로 배포하는 SpringBoot 환경에서는 getFile()로 resource 디렉토리 내의 파일을 로딩하면 오류가 발생한다.
			//file = file_no_image.getFile();
			file = getFileForSpringBoot(file_no_image);
		} catch (Exception e) {
			log.info("Error is occured while loading image/no_image.png. " + e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		// log.info("No image file is loaded.");

		if(isValidParameter(tolling_time, work_number, processing_number)){
			//log.info("Variables are valid.");
			try {
				List<Map<String, Object>> imageList = (List<Map<String, Object>>) videoService.selectEtcsImageList(IC_CODE, tolling_time, work_number, processing_number);
				//log.info("imageList.size(): " + imageList.size());
				if(imageList != null && imageList.size() > 0){
					Map image = imageList.get(0);

					String FTP_HOST = serverInfo.getFTP_VIDEO().get(IC_CODE).get("SVR_ADDR").toString();
					String FTP_PORT = serverInfo.getFTP_VIDEO().get(IC_CODE).get("SVR_PORT").toString();
					String FTP_AUTH_ID = serverInfo.getFTP_VIDEO().get(IC_CODE).get("ACCNT_ID").toString();
					String FTP_AUTH_PW = serverInfo.getFTP_VIDEO().get(IC_CODE).get("ACCNT_PW").toString();

					String filename = image.get("filename").toString();
					//String filePath = image.get("directory_path")+"/"+filename;
					String filePath = image.get("directory_path").toString().substring(Integer.parseInt(filepathStartOffset))+"/"+filename;
					//log.info("filePath: " + filePath);
					//System.out.println("filePath: " + filePath);

					file = downloadFileFromServerUsingFTP(FTP_HOST, FTP_PORT, FTP_AUTH_ID, FTP_AUTH_PW, filePath);

					if(file == null){
						log.info("Downloaded file is null.");
						deleteTempFile(file);
						file = file_no_image.getFile();
					}
				} else {
					// log.info("The information of the image is not existed in DB.");
					file = file_no_image.getFile();
				}

			} catch (IOException e) {
				log.info("FTP error occured");
			} catch (NullPointerException e) {
				log.info("No Image");
			}
		}

		sendFileToHTTPClient(file, response);
		deleteTempFile(file);
		// log.info("VideoController.getEtcsImage() Finished.");
	}
	/**
	 *
	 * @Method Name	: getEtcsImagePlate
	 * @Method 설명	: ETCS 번호판 영상을 가져온다
	 * @작성자 		: 박형철
	 * @작성일 		: 2024. 07. 26.
	 *
	 * @param dto
	 * @param model
	 * @return
	 * @throws IOException
	 *
	 */
	@RequestMapping(value = "/etcs/getImagePlate")
	public void getEtcsImagePlate(@RequestParam Map<String, Object> param, HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//log.info("VideoController.getImagePlate() Started.");
		String IC_CODE = (String) request.getParameter("IC_CODE");
		String tolling_time = (String) request.getParameter("tolling_time");
		String work_number = (String) request.getParameter("work_number");
		String processing_number = (String) request.getParameter("processing_number");
		String filepathStartOffset = (String) request.getParameter("filepathStartOffset");
		//log.info("Variables are initialized.");
		File file = null;
		try {
			//.jar로 배포하는 SpringBoot 환경에서는 getFile()로 resource 디렉토리 내의 파일을 로딩하면 오류가 발생한다.
			//file = file_no_image.getFile();
			file = getFileForSpringBoot(file_no_image);
		} catch (Exception e) {
			//log.info("Error is occured while loading getImagePlate - image/no_image.png. " + e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		// log.info("getImagePlate No image file is loaded.");

		if(isValidParameter(tolling_time, work_number, processing_number)){
			//log.info("getImagePlate Variables are valid.");
			try {
				List<Map<String, Object>> imageList = (List<Map<String, Object>>) videoService.selectEtcsImageList(IC_CODE, tolling_time, work_number, processing_number);

				if(imageList != null && imageList.size() > 0){
					Map image = imageList.get(0);

					String FTP_HOST = serverInfo.getFTP_VIDEO().get(IC_CODE).get("SVR_ADDR").toString();
					String FTP_PORT = serverInfo.getFTP_VIDEO().get(IC_CODE).get("SVR_PORT").toString();
					String FTP_AUTH_ID = serverInfo.getFTP_VIDEO().get(IC_CODE).get("ACCNT_ID").toString();
					String FTP_AUTH_PW = serverInfo.getFTP_VIDEO().get(IC_CODE).get("ACCNT_PW").toString();

					String filename = image.get("filename").toString();
					filename = filename.substring(0, filename.indexOf('.'))+"-plate.jpg";
					//String filePath = image.get("directory_path")+"/"+filename;
					String filePath = image.get("directory_path").toString().substring(Integer.parseInt(filepathStartOffset))+"/"+filename;
					//log.info("getImagePlate filePath: " + filePath);

					file = downloadFileFromServerUsingFTP(FTP_HOST, FTP_PORT, FTP_AUTH_ID, FTP_AUTH_PW, filePath);

					if(file == null){
						log.info("getImagePlate Downloaded file is null.");
						deleteTempFile(file);
						file = file_no_image.getFile();
					}
				} else {
					log.info("getImagePlate The information of the image is not existed in DB.");
					file = file_no_image.getFile();
				}

			} catch (IOException e) {
				log.info("getImagePlate FTP error occured");
			} catch (NullPointerException e) {
				log.info("getImagePlate No Image");
			}
		}

		sendFileToHTTPClient(file, response);
		deleteTempFile(file);
		// log.info("getImagePlate VideoController.getImagePlate() Finished.");
	}

	public boolean isValidParameter(String tolling_time, String work_number, String processing_number){
		if( "".equals(tolling_time)
				|| "".equals(work_number)
				|| "".equals(processing_number)){
			return false;
		}
		return true;
	}

	public File downloadFileFromServerUsingFTP(String FTP_HOST, String FTP_PORT, String FTP_AUTH_ID, String FTP_AUTH_PW, String filePath) throws NumberFormatException, SocketException{
		File file = null;
		FileOutputStream fileOutputStream = null;
		FTPClient ftpClient = null;
		try {
			ftpClient = new FTPClient();

			//ftpClient.setControlEncoding("UTF-8");

			ftpClient.connect(FTP_HOST, Integer.parseInt(FTP_PORT));

			ftpClient.enterLocalPassiveMode(); // 영상 FTP 서버가 사용하는 통신 방식이다. 개발할 때는 개발용 FTP 서버가 PASSIVE 모드로 설정되었는지 확인해야 한다.
			//ftpClient.enterLocalActiveMode();
			//ftpClient.enterRemotePassiveMode();
			//ftpClient.enterRemoteActiveMode(host, Integer.parseInt(FTP_PORT))

			//ftpClient.setSoTimeout(30000);

			if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				log.info("failed to connect.");
				deleteTempFile(file);
				return null;
			}

			if(!ftpClient.login(FTP_AUTH_ID, FTP_AUTH_PW)){
				log.info("failed to authenticate.");
				deleteTempFile(file);
				return null;
			}

			ftpClient.setFileType(FTP.BINARY_FILE_TYPE); // 영상 FTP 서버를 리눅스에 설치했을 때 잘 동작한다.
			//ftpClient.setFileType(FTP.ASCII_FILE_TYPE);

			file = File.createTempFile("temp_gdtcs_", ".jpg");

			fileOutputStream = new FileOutputStream(file);

			try {
				if (!ftpClient.retrieveFile(filePath, fileOutputStream)) {
					log.info("VideoController - 번호판 이미지 다운로드 실패");
					deleteTempFile(file);
					file = file_no_image.getFile();
					return file;
				}
			} catch (IOException e) {
				log.error("번호판 대체 이미지 ");
				// 실패 시 대체 이미지 반환
				deleteTempFile(file);
				file = file_no_image.getFile();
				return file;
			}

		} catch (IOException e) {
			if(e.getMessage().indexOf("Connection is not open") > -1){
				log.info("FTP connection is not open.");
			}
			deleteTempFile(file);
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
					//log.info("downloadFileFromServerUsingFTP FileOutputStream closed.");
				} catch (IOException e) {
					log.error("Error while closing FileOutputStream.", e);
				}
			}
			if (ftpClient != null && ftpClient.isConnected()) {
				try {
					ftpClient.logout();
					ftpClient.disconnect();
				} catch (IOException e) {
					log.error("Error while disconnecting FTPClient.", e);
				}
			}
		}
		return file;
	}

	public static void sendFileToHTTPClient(File file, HttpServletResponse response) throws IOException {
		response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Type", "image/jpeg");
		response.setHeader("Content-Length", String.valueOf(file.length()));
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");

		FileInputStream fileInputStream = null;
		OutputStream outputStream = null;

		try {
			fileInputStream = new FileInputStream(file);
			outputStream = response.getOutputStream();

			byte[] buffer = new byte[1024];
			int readCount;
			while ((readCount = fileInputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, readCount);
			}
			outputStream.flush();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
					System.out.println("##### sendFileToHTTPClient FileInputStream closed #####");
				} catch (IOException e) {
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
					System.out.println("##### sendFileToHTTPClient OutputStream closed #####");

				} catch (IOException e) {
					System.out.println("##### Error sendFileToHTTPClient while closing OutputStream: " + e);
				}
			}
		}
	}


	public static boolean deleteTempFile(File file){
		if(file == null){
			return false;
		}

		if(file.getName().indexOf("temp_") > -1){
			file.delete();
		}

		return true;
	}

	public static File getFileForSpringBoot(ClassPathResource resource) throws IOException {
		InputStream is = null;
		FileOutputStream fos = null;
		File file = File.createTempFile("temp_gdtcs_", resource.getFilename());

		try {
			is = resource.getInputStream();
			fos = new FileOutputStream(file);

			int read;
			byte[] bytes = new byte[1024];

			while ((read = is.read(bytes)) != -1) {
				fos.write(bytes, 0, read);
			}
		} finally {
			if (is != null) {
				try {
					is.close();
					System.out.println("##### getFileForSpringBoot InputStream closed #####");
				} catch (IOException e) {
					System.out.println("##### getFileForSpringBoot Error while closing InputStream: " + e);
				}
			}
			if (fos != null) {
				try {
					fos.close();
					System.out.println("##### getFileForSpringBoot FileOutputStream closed #####");
				} catch (IOException e) {
					System.out.println("##### getFileForSpringBootError while closing FileOutputStream: " + e);
				}
			}
		}

		return file;
	}

}
