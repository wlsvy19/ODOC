package gdtcs.common.controller;

import gdtcs.common.service.CommonService;
import gdtcs.video.controller.VideoController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 
 * @파일명		: CommonController
 * @프로그램설명	: 공통 정보 제공용 컨트롤러
 * @작성자		: 이진표
 * @작성일		: 2024. 05. 24.
 * @version 0.1
 * @개정이력
 *
 * <pre>
 *  수정일            수정자              수정내용
 *  -------    --------    ---------------------------
 *	 2024. 05. 24.   이진표     최초생성
 * </pre>
 *
 */
@Controller
@ResponseBody
@RequestMapping("/api/common")
public class CommonController {
	protected Log log = LogFactory.getLog(this.getClass());

    @Resource(name="commonService")
    private CommonService commonService;
    
    @PostMapping("/getImagePath")
    public ResponseEntity<Map<String, Object>> getImagePath(@RequestBody Map<String, Object> param) throws Exception {
        Map<String, Object> map = commonService.getImagePath(param);
        return ResponseEntity.ok(map);
    }

    /**
	 *  
	 * @Method Name	: getFare
	 * @Method 설명	: 통행요금 정보를 제공한다.
	 * @작성자 		: 박형철
	 * @작성일 		: 2024. 07. 18.
	 *
	 * @param Map<String, Object> param
	 * @return ResponseEntity<List<Map<String, Object>>>
	 * @throws Exception
	 *
	 */
    @PostMapping("/getFare")
    public ResponseEntity<List<Map<String, Object>>> getFare(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = commonService.getFare(param);

        return ResponseEntity.ok(list);
    }
    
    /**
	 *  
	 * @Method Name	: getRevNo
	 * @Method 설명	: 개정정보를 제공한다.
	 * @작성자 		: 노영재
	 * @작성일 		: 2024. 07. 25.
	 *
	 * @param Map<String, Object> param
	 * @return ResponseEntity<List<Map<String, Object>>>
	 * @throws Exception
	 *
	 */
    @PostMapping("/getRevNo")
    public ResponseEntity<List<Map<String, Object>>> getRevNo(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = commonService.getRevNo(param);

        return ResponseEntity.ok(list);
    }
    
    /**
	 *  
	 * @Method Name	: getLogoImage
	 * @Method 설명	: 로고 이미지를 제공한다.
	 * @작성자 		: 박형철
	 * @작성일 		: 2024. 09. 20.
	 *
	 * @param Map<String, Object> param
	 * @throws Exception
	 *
	 */
    @GetMapping("/getLogoImage")
    public void getLogoImage(@RequestParam Map<String, Object> param, HttpServletResponse response) throws Exception{
    	log.debug("CommonController.getLogoImage() Started.");
    	
    	File file = File.createTempFile("temp_gdtcs_", "");
    	commonService.getLogoImage(file);		
		VideoController.sendFileToHTTPClient(file, response);
		VideoController.deleteTempFile(file);
		
		log.debug("CommonController.getLogoImage() Finished.");
    }
}
