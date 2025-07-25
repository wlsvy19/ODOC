package gdtcs.base.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import gdtcs.base.service.BaseService;
import gdtcs.common.service.ServerInfoService;
import gdtcs.video.controller.VideoController;
import gdtcs.video.service.VideoService;
import gdtcs.video.util.FileCommunication;

/**
 *
 * @파일명		: BaseController
 * @프로그램설명	: 시스템 관련 기초 정보 조회 및 관리용 컨트롤러
 * @작성자		: 박형철
 * @작성일		: 2024. 06. 13.
 * @version 0.1
 * @개정이력
 *
 * <pre>
 *  수정일            수정자              수정내용
 *  -------    --------    ---------------------------
 *	 2024. 06. 13.   박형철     최초생성
 * </pre>
 *
 */
@Controller
@ResponseBody
@RequestMapping("/api/base")
public class BaseController {
    protected Log log = LogFactory.getLog(this.getClass());

    ClassPathResource file_no_image = new ClassPathResource("image/no_image.png");

    @Autowired
    private ServerInfoService serverInfo;

    @Autowired
    FileCommunication fileCommunication;

    @Resource(name = "baseService")
    private BaseService baseService;

    /**
     *
     * @Method Name : getMenuInfo
     * @Method 설명   : 메뉴 목록을 가져온다.
     * @작성자         : 이진표
     * @작성일         : 2024. 05. .27
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<List<Map<String, Object>>>
     * @throws Exception
     *
     */
    @PostMapping("/getMenuInfo")
    public ResponseEntity<List<Map<String, Object>>> getMenuInfo(@RequestBody Map<String, Object> param) throws Exception {
        List<Map<String, Object>> list = baseService.getMenuInfo(param);
        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: getSystemLargeCode
     * @Method 설명	: 시스템의 대분류 코드 목록을 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 06. 13.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<List<Map<String, Object>>>
     * @throws Exception
     *
     */
    @PostMapping("/getSystemCodeList")
    public ResponseEntity<List<Map<String, Object>>> getSystemCodeList(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = baseService.getSystemCodeList(param);

        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: setSystemLargeCode
     * @Method 설명	: 시스템의 대분류 코드를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 06. 13.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<List<Map<String, Object>>>
     * @throws Exception
     *
     */
    @PostMapping("/setSystemLargeCode")
    public ResponseEntity<Map<String, Object>> setSystemLargeCode(@RequestBody Map<String, Object> param) throws Exception{
        int changedRows = baseService.setSystemLargeCode(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", changedRows);
                result.put("ERROR_MSG", "수정이 완료되었습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: addSystemLargeCode
     * @Method 설명	: 시스템의 대분류 코드를 추가한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 06. 13.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<List<Map<String, Object>>>
     * @throws Exception
     *
     */
    @PostMapping("/addSystemLargeCode")
    public ResponseEntity<Map<String, Object>> addSystemLargeCode(@RequestBody Map<String, Object> param) throws Exception{
        int changedRows = baseService.addSystemLargeCode(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            case -2:
                result.put("ERROR_CODE", "-2");
                result.put("ERROR_MSG", "같은 값이 존재합니다.");
                break;
            default:
                result.put("ERROR_CODE", changedRows);
                result.put("ERROR_MSG", "추가하였습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: delSystemLargeCode
     * @Method 설명	: 시스템의 대분류 코드를 삭제한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 06. 13.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<List<Map<String, Object>>>
     * @throws Exception
     *
     */
    @PostMapping("/delSystemLargeCode")
    public ResponseEntity<Map<String, Object>> delSystemLargeCode(@RequestBody Map<String, Object> param) throws Exception{
        int changedRows = baseService.delSystemLargeCode(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "삭제할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            case -2:
                result.put("ERROR_CODE", "-2");
                result.put("ERROR_MSG", "해당 대분류 코드에 속한 소분류 코드가 있어서 삭제할 수 없습니다.");
                break;
            default:
                result.put("ERROR_CODE", changedRows);
                result.put("ERROR_MSG", "삭제하였습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: getSystemSmallCode
     * @Method 설명	: 시스템의 소분류 코드 목록을 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 06. 13.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<List<Map<String, Object>>>
     * @throws Exception
     *
     */
    @PostMapping("/getSystemSmallCode")
    public ResponseEntity<List<Map<String, Object>>> getSystemSmallCode(@RequestBody Map<String, Object> param) throws Exception{

        List<Map<String, Object>> list = baseService.getSystemSmallCode(param);

        return ResponseEntity.ok(list);
    }


    /**
     *
     * @Method Name	: setSystemSmallCode
     * @Method 설명	: 시스템의 소분류 코드를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 06. 13.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<List<Map<String, Object>>>
     * @throws Exception
     *
     */
    @PostMapping("/setSystemSmallCode")
    public ResponseEntity<Map<String, Object>> setSystemSmallCode(@RequestBody Map<String, Object> param) throws Exception{
        int changedRows = baseService.setSystemSmallCode(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", changedRows);
                result.put("ERROR_MSG", "수정이 완료되었습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: addSystemSmallCode
     * @Method 설명	: 시스템의 소분류 코드를 추가한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 06. 13.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<List<Map<String, Object>>>
     * @throws Exception
     *
     */
    @PostMapping("/addSystemSmallCode")
    public ResponseEntity<Map<String, Object>> addSystemSmallCode(@RequestBody Map<String, Object> param) throws Exception{
        int changedRows = baseService.addSystemSmallCode(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            case -2:
                result.put("ERROR_CODE", "-2");
                result.put("ERROR_MSG", "같은 값이 존재합니다.");
                break;
            default:
                result.put("ERROR_CODE", changedRows);
                result.put("ERROR_MSG", "추가하였습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: delSystemSmallCode
     * @Method 설명	: 시스템의 소분류 코드를 삭제한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 06. 13.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<List<Map<String, Object>>>
     * @throws Exception
     *
     */
    @PostMapping("/delSystemSmallCode")
    public ResponseEntity<Map<String, Object>> delSystemSmallCode(@RequestBody Map<String, Object> param) throws Exception{
        int changedRows = baseService.delSystemSmallCode(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "삭제할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            case -2:
                result.put("ERROR_CODE", "-2");
                result.put("ERROR_MSG", "해당 대분류 코드에 속한 소분류 코드가 있어서 삭제할 수 없습니다.");
                break;
            default:
                result.put("ERROR_CODE", changedRows);
                result.put("ERROR_MSG", "삭제하였습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: requestRevision
     * @Method 설명	: 개정을 요청한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 07. 28.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<List<Map<String, Object>>>
     * @throws Exception
     *
     */
    @PostMapping("/requestRevision")
    public ResponseEntity<Map<String, Object>> requestRevision(@RequestBody Map<String, Object> param) throws Exception{
        int changedRows = baseService.requestRevision(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 0:
                result.put("ERROR_CODE", "");
                result.put("ERROR_MSG", "");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            case -2:
                result.put("ERROR_CODE", "-2");
                result.put("ERROR_MSG", "연결에 실패하였습니다.");
                break;
            default:
                result.put("ERROR_CODE", changedRows);
                result.put("ERROR_MSG", "개정을 요청하였습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: getIcInformation
     * @Method 설명	: 영업소 정보를 제공한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 1.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<List<Map<String, Object>>>
     * @throws Exception
     *
     */
    @PostMapping("/getIcInformation")
    public ResponseEntity<List<Map<String, Object>>> getIcInformation(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = baseService.getIcInformation(param);

        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: setIcInformation
     * @Method 설명	: 영업소 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 11.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/setIcInformation")
    public ResponseEntity<Map<String, Object>> setIcInformation(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 과거 정보 기록 테이블에 저장 */
        int changedRows = baseService.addIcInformationHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "수정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setIcInformation(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: getRevisionHistory
     * @Method 설명	: 개정 기록을 제공한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 01.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<List<Map<String, Object>>>
     * @throws Exception
     *
     */
    @PostMapping("/getRevisionHistory")
    public ResponseEntity<List<Map<String, Object>>> getRevisionHistory(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = baseService.getRevisionHistory(param);

        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: getLaneRevisionStatus
     * @Method 설명	: 차로별 개정 현황을 제공한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<List<Map<String, Object>>>
     * @throws Exception
     *
     */
    @PostMapping("/getLaneRevisionStatus")
    public ResponseEntity<List<Map<String, Object>>> getLaneRevisionStatus(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = baseService.getLaneRevisionStatus(param);

        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: getIcRevision
     * @Method 설명	: 영업소의 현재 개정 상태를 제공한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<Map<String, Object>>
     * @throws Exception
     *
     */

    @PostMapping("/getIcRevision")
    public ResponseEntity<List<Map<String, Object>>> getIcRevision(@RequestBody Map<String, Object> param) throws Exception
    {
        List<Map<String, Object>> list = baseService.getIcRevision(param);

        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: setIcRevisionBase
     * @Method 설명	: 기초정보 개정 내역을 갱신한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/setIcRevisionBase")
    public ResponseEntity<Map<String, Object>> setIcRevisionBase(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addIcRevisionHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "기초 정보 개정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setIcRevisionBase(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "기초 개정 정보 수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: setIcRevisionWorker
     * @Method 설명	: 근무자 개정 내역을 갱신한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/setIcRevisionWorker")
    public ResponseEntity<Map<String, Object>> setIcRevisionWorker(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addIcRevisionHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "근무자 정보 개정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setIcRevisionWorker(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "근무자 개정 정보 수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: setIcRevisionFare
     * @Method 설명	: 기준 요금 개정 내역을 갱신한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/setIcRevisionFare")
    public ResponseEntity<Map<String, Object>> setIcRevisionFare(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addIcRevisionHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "요금 정보 개정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setIcRevisionFare(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "요금 개정 정보 수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: setIcRevisionBlDiscount
     * @Method 설명	: 무효할인면제 개정 내역을 갱신한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/setIcRevisionBlDiscount")
    public ResponseEntity<Map<String, Object>> setIcRevisionBlDiscount(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addIcRevisionHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "무효할인면제 정보 개정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setIcRevisionBlDiscount(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "무효할인면제 개정 정보 수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: setIcRevisionHoliday
     * @Method 설명	: 휴일일자 개정 내역을 갱신한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/setIcRevisionHoliday")
    public ResponseEntity<Map<String, Object>> setIcRevisionHoliday(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addIcRevisionHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "휴일일자 정보 개정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setIcRevisionHoliday(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "휴일일자 개정 정보 수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: setIcRevisionCommute
     * @Method 설명	: 출퇴근할인 정보 개정 내역을 갱신한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/setIcRevisionCommute")
    public ResponseEntity<Map<String, Object>> setIcRevisionCommute(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addIcRevisionHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "출퇴근할인 정보 개정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setIcRevisionCommute(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "출퇴근할인 개정 정보 수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: setIcRevisionBlEcard
     * @Method 설명	: 무효전자카드 정보 개정 내역을 갱신한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/setIcRevisionBlEcard")
    public ResponseEntity<Map<String, Object>> setIcRevisionBlEcard(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addIcRevisionHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "무효전자카드 정보 개정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setIcRevisionBlEcard(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "무효전자카드 개정 정보 수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: setIcRevisionContinuousDiscount
     * @Method 설명	: 연속통행할인 정보 개정 내역을 갱신한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/setIcRevisionContinuousDiscount")
    public ResponseEntity<Map<String, Object>> setIcRevisionContinuousDiscount(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addIcRevisionHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "연속통행할인 정보 개정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setIcRevisionContinuousDiscount(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "연속통행할인 개정 정보 수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: setIcRevisionBlTcard
     * @Method 설명	: 무효교통카드 정보 개정 내역을 갱신한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/setIcRevisionBlTcard")
    public ResponseEntity<Map<String, Object>> setIcRevisionBlTcard(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addIcRevisionHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "무효교통카드 정보 개정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setIcRevisionBlTcard(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "무효교통카드 개정 정보 수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: setIcRevisionHourDiscount
     * @Method 설명	: 시간별할인 정보 개정 내역을 갱신한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/setIcRevisionHourDiscount")
    public ResponseEntity<Map<String, Object>> setIcRevisionHourDiscount(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addIcRevisionHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "시간별할인 정보 개정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setIcRevisionHourDiscount(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "시간별할인 개정 정보 수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: setIcRevisionBlObu
     * @Method 설명	: 무효OBU 정보 개정 내역을 갱신한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/setIcRevisionBlObu")
    public ResponseEntity<Map<String, Object>> setIcRevisionBlObu(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addIcRevisionHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "무효OBU 정보 개정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setIcRevisionBlObu(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "무효OBU 개정 정보 수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: setIcRevisionHolidayException
     * @Method 설명	: 명절(특정일)면제 정보 개정 내역을 갱신한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/setIcRevisionHolidayException")
    public ResponseEntity<Map<String, Object>> setIcRevisionHolidayException(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addIcRevisionHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "명절(특정일)면제 정보 개정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setIcRevisionHolidayException(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "명절(특정일)면제 개정 정보 수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: setIcRevisionBlEcardChanges
     * @Method 설명	: 전자카드 B/L 변경분 개정 지시 내역을 추가한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 05.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/setIcRevisionBlEcardChanges")
    public ResponseEntity<Map<String, Object>> setIcRevisionBlEcardChanges(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addIcRevisionHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "전자카드 B/L 변경분 정보 개정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: setIcRevisionPlEcard
     * @Method 설명	: 전자카드면제 정보 개정 내역을 갱신한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/setIcRevisionPlEcard")
    public ResponseEntity<Map<String, Object>> setIcRevisionPlEcard(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addIcRevisionHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "전자카드면제 정보 개정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setIcRevisionPlEcard(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "전자카드면제 개정 정보 수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: setIcRevisionDiffFare
     * @Method 설명	: 차등요금제 정보 개정 내역을 갱신한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 02.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/setIcRevisionDiffFare")
    public ResponseEntity<Map<String, Object>> setIcRevisionDiffFare(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addIcRevisionHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "차등요금제 정보 개정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setIcRevisionDiffFare(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "차등요금제 개정 정보 수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: getLaneList
     * @Method 설명	: 차로 목록을 제공한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 06.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/getLaneList")
    public ResponseEntity<List<Map<String, Object>>> getLaneList(@RequestBody Map<String, Object> param) throws Exception
    {
        List<Map<String, Object>> list = baseService.getLaneList(param);
        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: setLane
     * @Method 설명	: 차로 정보를 갱신한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 06.
     *
     * @param Map<String, Object> param
     * @return Map<String, Object>
     * @throws Exception
     *
     */
    @PostMapping("/setLane")
    public ResponseEntity<Map<String, Object>> setLane(@RequestBody Map<String, Object> param) throws Exception
    {
        int changedRows = baseService.setLane(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "차로 정보 수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: getFareInfo
     * @Method 설명	: 기준 요금 정보를 제공한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 07.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/getFareInfo")
    public ResponseEntity<List<Map<String, Object>>> getFareInfo(@RequestBody Map<String, Object> param) throws Exception
    {
        List<Map<String, Object>> list = baseService.getFareInfo(param);
        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: setFareInfo
     * @Method 설명	: 기준 요금 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 07.
     *
     * @param Map<String, Object> param
     * @return Map<String, Object>
     * @throws Exception
     *
     */
    @PostMapping("/setFareInfo")
    public ResponseEntity<Map<String, Object>> setFareInfo(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addFareInfoHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "요금 정보 수정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setFareInfo(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "요금 정보 수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: getDiffFare
     * @Method 설명	: 차등 요금 정보를 제공한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 16.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/getDiffFare")
    public ResponseEntity<List<Map<String, Object>>> getDiffFare(@RequestBody Map<String, Object> param) throws Exception
    {
        List<Map<String, Object>> list = baseService.getDiffFare(param);
        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: setDiffFare
     * @Method 설명	: 차등 요금 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 16.
     *
     * @param Map<String, Object> param
     * @return Map<String, Object>
     * @throws Exception
     *
     */
    @PostMapping("/setDiffFare")
    public ResponseEntity<Map<String, Object>> setDiffFare(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addDiffFareHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "요금 정보 수정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setDiffFare(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "요금 정보 수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: getWorkerList
     * @Method 설명	: 근무자 목록을 제공한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 11.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/getWorkerList")
    public ResponseEntity<List<Map<String, Object>>> getWorkerList(@RequestBody Map<String, Object> param) throws Exception
    {
        List<Map<String, Object>> list = baseService.getWorkerList(param);
        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: getAvailableWorkerNo
     * @Method 설명	: 사용가능한 WORKER_NO 목록을 조회한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/getAvailableWorkerNo")
    public ResponseEntity<List<Map<String, Object>>> getAvailableWorkerNo(@RequestBody Map<String, Object> param) throws Exception
    {
        List<Map<String, Object>> list = baseService.getAvailableWorkerNo(param);
        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: addWorkerInfo
     * @Method 설명	: 근무자 정보를 추가한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map<String, Object> param
     * @return ResponseEntity<List<Map<String, Object>>>
     * @throws Exception
     *
     */
    @PostMapping("/addWorkerInfo")
    public ResponseEntity<Map<String, Object>> addWorkerInfo(@RequestBody Map<String, Object> param) throws Exception{
        int changedRows = baseService.addWorkerInfo(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            case -2:
                result.put("ERROR_CODE", "-2");
                result.put("ERROR_MSG", "같은 값이 존재합니다.");
                break;
            default:
                result.put("ERROR_CODE", changedRows);
                result.put("ERROR_MSG", "추가하였습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: changeWorkerPassword
     * @Method 설명	: 근무자 비밀번호를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map<String, Object> param
     * @return Map<String, Object>
     * @throws Exception
     *
     */
    @PostMapping("/changeWorkerPassword")
    public ResponseEntity<Map<String, Object>> changeWorkerPassword(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 정보 갱신 */
        int changedRows = baseService.setWorkerPassword(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "비밀번호 변경이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            case -2:
                result.put("ERROR_CODE", "-2");
                result.put("ERROR_MSG", "기존 비밀번호가 일치하지 않습니다.");
                break;
            case -3:
                result.put("ERROR_CODE", "-3");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: setWorkerInfo
     * @Method 설명	: 근무자 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map<String, Object> param
     * @return Map<String, Object>
     * @throws Exception
     *
     */
    @PostMapping("/setWorkerInfo")
    public ResponseEntity<Map<String, Object>> setWorkerInfo(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 정보 갱신 */
        int changedRows = baseService.setWorkerInfo(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: setWorkerResign
     * @Method 설명	: 근무자 퇴사 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map<String, Object> param
     * @return Map<String, Object>
     * @throws Exception
     *
     */
    @PostMapping("/setWorkerResign")
    public ResponseEntity<Map<String, Object>> setWorkerResign(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 정보 갱신 */
        int changedRows = baseService.setWorkerResign(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: getWorkerProfileImage
     * @Method 설명	: 근무자 프로필 이미지를 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @GetMapping("/getWorkerProfileImage")
    public void getWorkerProfileImage(@RequestParam Map<String, Object> param, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        String filePath = request.getParameter("FILE_PTH").toString();
        String IC_CODE = request.getParameter("IC_CODE").toString();

        if(serverInfo.getPATH_UPLOAD_ROOT() == null
                || MapUtils.isEmpty(serverInfo.getPATH_UPLOAD_ROOT())
                || !serverInfo.getPATH_UPLOAD_ROOT().containsKey(IC_CODE)){
            serverInfo.getServerInfo(IC_CODE);
        }

        String DIRECTORY_PATH_WORKER_IMAGE = serverInfo.getPATH_UPLOAD_ROOT().get(IC_CODE).get("SVR_VAR_01")+"/images/worker/";
        //String DIRECTORY_PATH_WORKER_IMAGE = "C:/upload/images/worker/";

        File file = new File(DIRECTORY_PATH_WORKER_IMAGE+filePath);
        if(!file.exists()){
            file = VideoController.getFileForSpringBoot(file_no_image);
        }

        VideoController.sendFileToHTTPClient(file, response);
        VideoController.deleteTempFile(file);
    }

    /**
     *
     * @Method Name	: saveWorkerProfileImage
     * @Method 설명	: 근무자 프로필 이미지를 저장한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping(path = "/saveWorkerProfileImage", consumes = { "multipart/form-data" } )
    public ResponseEntity<Map<String, Object>> saveWorkerProfileImage(
            @RequestParam(name="IC_CODE") String IC_CODE,
            @RequestParam(name="WORKER_NO") String WORKER_NO,
            @RequestPart(name="PROFILE_IMAGE_FILE") MultipartFile file
    ) {
        Map<String, Object> result = new HashMap<>();

        try {
            log.info("IC_CODE: " + IC_CODE);

            if(serverInfo.getPATH_UPLOAD_ROOT() == null
                    || MapUtils.isEmpty(serverInfo.getPATH_UPLOAD_ROOT())
                    || !serverInfo.getPATH_UPLOAD_ROOT().containsKey(IC_CODE)){
                serverInfo.getServerInfo(IC_CODE);
            }

            String DIRECTORY_PATH_WORKER_IMAGE = serverInfo.getPATH_UPLOAD_ROOT().get(IC_CODE).get("SVR_VAR_01")+"/images/worker/";
            //String DIRECTORY_PATH_WORKER_IMAGE = "C:/upload/images/worker/";
            log.debug("DIRECTORY_PATH_REPORT_IMAGE: " + DIRECTORY_PATH_WORKER_IMAGE);
            String fileName = fileCommunication.receiveFile(file, DIRECTORY_PATH_WORKER_IMAGE);

            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("IC_CODE", IC_CODE);
            paramMap.put("WORKER_NO", WORKER_NO);
            paramMap.put("FILE_PTH", fileName);

            int changedRows = baseService.setWorkerProfileImage(paramMap);

            switch (changedRows) {
                case 1:
                    result.put("ERROR_CODE", "1");
                    result.put("ERROR_MSG", "보고서 이미지 저장이 완료되었습니다.");
                    result.put("FILE_PTH", fileName);
                    break;
                case 0:
                    result.put("ERROR_CODE", "0");
                    result.put("ERROR_MSG", "저장에 실패하였습니다.");
                    break;
                case -1:
                    result.put("ERROR_CODE", "-1");
                    result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                    break;
                default:
                    result.put("ERROR_CODE", "-99");
                    result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("ERROR_CODE", "-99");
            result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: removeWorker
     * @Method 설명	: 근무자 정보를 삭제한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map<String, Object> param
     * @return Map<String, Object>
     * @throws Exception
     *
     */
    @PostMapping("/removeWorker")
    public ResponseEntity<Map<String, Object>> removeWorker(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 정보 갱신 */
        int changedRows = baseService.removeWorker(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "삭제가 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "삭제할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: getProgramList
     * @Method 설명	: 프로그램 목록을 제공한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 13.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/getProgramList")
    public ResponseEntity<List<Map<String, Object>>> getProgramList(@RequestBody Map<String, Object> param) throws Exception
    {
        List<Map<String, Object>> list = baseService.getProgramList(param);
        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: setProgram
     * @Method 설명	: 프로그램 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 13.
     *
     * @param Map<String, Object> param
     * @return Map<String, Object>
     * @throws Exception
     *
     */
    @PostMapping("/setProgram")
    public ResponseEntity<Map<String, Object>> setProgram(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 정보 갱신 */
        int changedRows = baseService.setProgram(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "프로그램 수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: getReportImageInfo
     * @Method 설명	: 보고서용 이미지 정보를 제공한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 13.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/getReportImageInfo")
    public ResponseEntity<List<Map<String, Object>>> getReportImageInfo(@RequestBody Map<String, Object> param) throws Exception
    {
        List<Map<String, Object>> list = baseService.getReportImageInformation(param);
        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: getReportImageFile
     * @Method 설명	: 보고서 이미지를 가져온다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 13.
     *
     * @param Map<String, Object> param
     * @return void
     * @throws Exception
     *
     */
    @GetMapping("/getReportImageFile")
    public void getReportImageFile(@RequestParam Map<String, Object> param, HttpServletResponse response ) throws Exception
    {
        String RPT_APP_CODE = param.get("RPT_APP_CODE").toString();
        String IC_CODE = param.get("IC_CODE").toString();
        List<Map<String, Object>> list = baseService.getReportImageInformation(param);
        String IMG_PTH = list.get(0).get("IMG_PTH").toString();

        if(serverInfo.getPATH_UPLOAD_ROOT() == null
                || MapUtils.isEmpty(serverInfo.getPATH_UPLOAD_ROOT())
                || !serverInfo.getPATH_UPLOAD_ROOT().containsKey(IC_CODE)){
            serverInfo.getServerInfo(IC_CODE);
        }

        String DIRECTORY_PATH_REPORT_IMAGE = serverInfo.getPATH_UPLOAD_ROOT().get(IC_CODE).get("SVR_VAR_01")+"/images/reportimg/";
        //String DIRECTORY_PATH_REPORT_IMAGE = "C:/upload/images/reportimg/";

        File file = new File(DIRECTORY_PATH_REPORT_IMAGE+IMG_PTH);
        if(!file.exists()){
            file = VideoController.getFileForSpringBoot(file_no_image);
        }

        VideoController.sendFileToHTTPClient(file, response);
        VideoController.deleteTempFile(file);
    }

    /**
     *
     * @Method Name	: saveReportImageFile
     * @Method 설명	: 보고서 이미지를 저장하는 컨트롤러
     * @작성자 		: 박형철
     * @작성일 		: 2022. 10. 28.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping(path = "/saveReportImageFile", consumes = { "multipart/form-data" } )
    public ResponseEntity<Map<String, Object>> saveReportImage(
            @RequestParam(name="IC_CODE") String IC_CODE,
            @RequestParam(name="RPT_APP_CODE") String RPT_APP_CODE,
            @RequestPart(name="RPT_APP_IMAGE_FILE") MultipartFile file
    ) {
        Map<String, Object> result = new HashMap<>();

        try {
            log.info("IC_CODE: " + IC_CODE);
            log.info("RPT_APP_CODE: " + RPT_APP_CODE);

            if(serverInfo.getPATH_UPLOAD_ROOT() == null
                    || MapUtils.isEmpty(serverInfo.getPATH_UPLOAD_ROOT())
                    || !serverInfo.getPATH_UPLOAD_ROOT().containsKey(IC_CODE)){
                serverInfo.getServerInfo(IC_CODE);
            }

            String DIRECTORY_PATH_REPORT_IMAGE = serverInfo.getPATH_UPLOAD_ROOT().get(IC_CODE).get("SVR_VAR_01")+"/images/reportimg/";
            //String DIRECTORY_PATH_REPORT_IMAGE = "C:/upload/images/reportimg/";
            log.debug("DIRECTORY_PATH_REPORT_IMAGE: " + DIRECTORY_PATH_REPORT_IMAGE);
            String fileName = fileCommunication.receiveFile(file, DIRECTORY_PATH_REPORT_IMAGE);

            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("IC_CODE", IC_CODE);
            paramMap.put("RPT_APP_CODE", RPT_APP_CODE);
            paramMap.put("IMG_PTH", fileName);

            int changedRows = baseService.setReportImageInformation(paramMap);

            switch (changedRows) {
                case 1:
                    result.put("ERROR_CODE", "1");
                    result.put("ERROR_MSG", "보고서 이미지 저장이 완료되었습니다.");
                    break;
                case 0:
                    result.put("ERROR_CODE", "0");
                    result.put("ERROR_MSG", "저장에 실패하였습니다.");
                    break;
                case -1:
                    result.put("ERROR_CODE", "-1");
                    result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                    break;
                default:
                    result.put("ERROR_CODE", "-99");
                    result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("ERROR_CODE", "-99");
            result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: getContinuousPassIcList
     * @Method 설명	: 연속통행할인 영업소 목록을 제공한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 16.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/getContinuousPassIcList")
    public ResponseEntity<List<Map<String, Object>>> getContinuousPassIcList(@RequestBody Map<String, Object> param) throws Exception
    {
        List<Map<String, Object>> list = baseService.getContinuousPassIcList(param);
        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: getEcardDiscountCarType
     * @Method 설명	: 전자카드 차종별 할인율 정보를 제공한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/getEcardDiscountCarType")
    public ResponseEntity<List<Map<String, Object>>> getEcardDiscountCarType(@RequestBody Map<String, Object> param) throws Exception
    {
        List<Map<String, Object>> list = baseService.getEcardDiscountCarType(param);
        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: setEcardDiscountCarType
     * @Method 설명	: 전자카드 차종별 할인율 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map<String, Object> param
     * @return Map<String, Object>
     * @throws Exception
     *
     */
    @PostMapping("/setEcardDiscountCarType")
    public ResponseEntity<Map<String, Object>> setEcardDiscountCarType(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addEcardDiscountCarTypeHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "수정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정 기록을 추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setEcardDiscountCarType(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: getEcardDiscount
     * @Method 설명	: 전자카드 선/후불별 할인율 정보를 제공한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/getEcardDiscount")
    public ResponseEntity<List<Map<String, Object>>> getEcardDiscount(@RequestBody Map<String, Object> param) throws Exception
    {
        List<Map<String, Object>> list = baseService.getEcardDiscount(param);
        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: setEcardDiscount
     * @Method 설명	: 전자카드 차종별 할인율 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map<String, Object> param
     * @return Map<String, Object>
     * @throws Exception
     *
     */
    @PostMapping("/setEcardDiscount")
    public ResponseEntity<Map<String, Object>> setEcardDiscount(@RequestBody Map<String, Object> param) throws Exception
    {
        Map<String, Object> result = new HashMap<>();

        /* 정보 갱신 */
        int changedRows = baseService.setEcardDiscount(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: getHourDiscount
     * @Method 설명	: 전자카드 차종별 할인율 정보를 제공한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/getHourDiscount")
    public ResponseEntity<List<Map<String, Object>>> getHourDiscount(@RequestBody Map<String, Object> param) throws Exception
    {
        List<Map<String, Object>> list = baseService.getHourDiscount(param);
        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: setHourDiscount
     * @Method 설명	: 전자카드 차종별 할인율 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map<String, Object> param
     * @return Map<String, Object>
     * @throws Exception
     *
     */
    @PostMapping("/setHourDiscount")
    public ResponseEntity<Map<String, Object>> setHourDiscount(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addHourDiscountHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "수정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정 기록을 추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setHourDiscount(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: getDiscountExtra
     * @Method 설명	: 할인/할증 정보를 제공한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/getDiscountExtra")
    public ResponseEntity<List<Map<String, Object>>> getDiscountExtra(@RequestBody Map<String, Object> param) throws Exception
    {
        List<Map<String, Object>> list = baseService.getDiscountExtra(param);
        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: setDiscountExtra
     * @Method 설명	: 할인/할증 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map<String, Object> param
     * @return Map<String, Object>
     * @throws Exception
     *
     */
    @PostMapping("/setDiscountExtra")
    public ResponseEntity<Map<String, Object>> setDiscountExtra(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addDiscountExtraHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "수정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정 기록을 추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setDiscountExtra(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: getChargeExtra
     * @Method 설명	: 충전 할증률 정보를 제공한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/getChargeExtra")
    public ResponseEntity<List<Map<String, Object>>> getChargeExtra(@RequestBody Map<String, Object> param) throws Exception
    {
        List<Map<String, Object>> list = baseService.getChargeExtra(param);
        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: setChargeExtra
     * @Method 설명	: 충전 할증률 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map<String, Object> param
     * @return Map<String, Object>
     * @throws Exception
     *
     */
    @PostMapping("/setChargeExtra")
    public ResponseEntity<Map<String, Object>> setChargeExtra(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addChargeExtraHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "수정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정 기록을 추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setChargeExtra(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: getEcardChargeLimit
     * @Method 설명	: 전자카드 최대 충전 정보를 제공한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/getEcardChargeLimit")
    public ResponseEntity<List<Map<String, Object>>> getEcardChargeLimit(@RequestBody Map<String, Object> param) throws Exception
    {
        List<Map<String, Object>> list = baseService.getEcardChargeLimit(param);
        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: setEcardChargeLimit
     * @Method 설명	: 전자카드 최대 충전 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map<String, Object> param
     * @return Map<String, Object>
     * @throws Exception
     *
     */
    @PostMapping("/setEcardChargeLimit")
    public ResponseEntity<Map<String, Object>> setEcardChargeLimit(@RequestBody Map<String, Object> param) throws Exception
    {
        Map<String, Object> result = new HashMap<>();

        /* 정보 갱신 */
        int changedRows = baseService.setEcardChargeLimit(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: getObuDiscount
     * @Method 설명	: 할인단말기 할인율 정보를 제공한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 20.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/getObuDiscount")
    public ResponseEntity<List<Map<String, Object>>> getObuDiscount(@RequestBody Map<String, Object> param) throws Exception
    {
        List<Map<String, Object>> list = baseService.getObuDiscount(param);
        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: setObuDiscount
     * @Method 설명	: 할인단말기 할인율 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 19.
     *
     * @param Map<String, Object> param
     * @return Map<String, Object>
     * @throws Exception
     *
     */
    @PostMapping("/setObuDiscount")
    public ResponseEntity<Map<String, Object>> setObuDiscount(@RequestBody Map<String, Object> param) throws Exception
    {
        /* 현재 정보를 개정 내역 테이블에 추가 */
        int changedRows = baseService.addObuDiscountHistory(param);

        Map<String, Object> result = new HashMap<>();

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "수정 기록을 추가하였습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정 기록을 추가할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        /* 정보 갱신 */
        changedRows = baseService.setObuDiscount(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: getContinuousDiscount
     * @Method 설명	: 할인단말기 할인율 정보를 제공한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 20.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/getContinuousDiscount")
    public ResponseEntity<List<Map<String, Object>>> getContinuousDiscount(@RequestBody Map<String, Object> param) throws Exception
    {
        List<Map<String, Object>> list = baseService.getContinuousDiscount(param);
        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: createDateList
     * @Method 설명	: 특정 연도의 일자 목록을 생성한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 22.
     *
     * @param Map<String, Object> param
     * @return Map<String, Object>
     * @throws Exception
     *
     */
    @PostMapping("/createDateList")
    public ResponseEntity<Map<String, Object>> createDateList(@RequestBody Map<String, Object> param) throws Exception
    {
        Map<String, Object> result = new HashMap<>();

        int changedRows = baseService.createDateList(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "일자 정보 생성이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "생성할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     *
     * @Method Name	: getDateList
     * @Method 설명	: 일자 목록을 제공한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 23.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/getDateList")
    public ResponseEntity<List<Map<String, Object>>> getDateList(@RequestBody Map<String, Object> param) throws Exception
    {
        List<Map<String, Object>> list = baseService.getDateList(param);
        return ResponseEntity.ok(list);
    }

    /**
     *
     * @Method Name	: setDate
     * @Method 설명	: 일자 정보를 수정한다.
     * @작성자 		: 박형철
     * @작성일 		: 2024. 08. 23.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/setDate")
    public ResponseEntity<Map<String, Object>> setDate(@RequestBody Map<String, Object> param) throws Exception
    {
        Map<String, Object> result = new HashMap<>();

        int changedRows = baseService.setDate(param);

        switch (changedRows) {
            case 1:
                result.put("ERROR_CODE", "1");
                result.put("ERROR_MSG", "일자 정보 수정이 완료되었습니다.");
                break;
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "수정할 수 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                result.put("ERROR_CODE", "-99");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
        }

        return ResponseEntity.ok(result);
    }
    /**
     *
     * @Method Name : getVehicleManage
     * @Method 설명   : 차적 정보를 조회한다.
     * @작성자         : 전현진
     * @작성일         : 2025. 02. 20.
     *
     * @param Map<String, Object> param
     * @return List<Map<String, Object>>
     * @throws Exception
     *
     */
    @PostMapping("/getVehicleManage")
    public ResponseEntity<List<Map<String, Object>>> getVehicleManage(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = baseService.getVehicleManage(param);
        return ResponseEntity.ok(list);
    }

    /**
     * @Method Name : uploadExcelFile
     * @Method 설명 : 기초정보관리 - 차적DB관리 메뉴에서 데이터를 추가할 때 엑셀업로드를 사용한다.
     * @작성자 : 이진표
     * @작성일 : 2025. 02. 21.
     */
    @PostMapping("/excelVehicleManage")
    public ResponseEntity<String> uploadExcelFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("업로드된 파일이 비어 있습니다.");
            }

            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                return ResponseEntity.badRequest().body("엑셀 파일 형식(.xlsx, .xls)만 지원합니다.");
            }

            List<Map<String, String>> vehicleDataList = new ArrayList<>();
            Map<String, Integer> vehicleNumberMap = new HashMap<>();
            StringBuilder errorMessages = new StringBuilder();

            try (InputStream inputStream = file.getInputStream()) {
                Workbook workbook = WorkbookFactory.create(inputStream);
                Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    if (row == null || isRowEmpty(row)) {
                        continue;
                    }
                    Cell vehicleNumberCell = row.getCell(0); // A열 (차량 번호)
                    Cell vehicleTypeCell = row.getCell(1);  // B열 (차종)
                    String vehicleNumber = vehicleNumberCell != null ? vehicleNumberCell.toString().trim() : "";
                    String vehicleType = "";
                    if (vehicleTypeCell != null) {
                        if (vehicleTypeCell.getCellType() == Cell.CELL_TYPE_NUMERIC) { // 숫자 타입 처리
                            vehicleType = String.valueOf((int) vehicleTypeCell.getNumericCellValue());
                        } else if (vehicleTypeCell.getCellType() == Cell.CELL_TYPE_STRING) { // 문자열 타입 처리
                            vehicleType = vehicleTypeCell.getStringCellValue().trim();
                        }
                    }

                    // 차종 1~6 검증
                    if (!vehicleType.matches("^[1-6]$")) {
                        errorMessages.append("차종은 1~6까지만 입력 가능합니다: ")
                                .append("행 ").append(i + 1).append(" (").append(vehicleType).append(")\n엑셀파일 수정 후 다시 업로드 해주세요.");
                        return ResponseEntity.ok(errorMessages.toString());
                    }

                    // 차량번호 길이 검증 (16바이트 초과시 오류 메시지)
                    try {
                        if (vehicleNumber.getBytes("UTF-8").length > 16) {
                            errorMessages.append("차량번호가 16바이트를 초과했습니다. 확인 후 엑셀 업로드를 진행해주세요: ")
                                    .append("행 ").append(i + 1).append(" (").append(vehicleNumber).append(")\n엑셀파일 수정 후 다시 업로드 해주세요.");
                            return ResponseEntity.ok(errorMessages.toString());
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("문자열 인코딩 오류가 발생했습니다.");
                    }

                    // 차량번호 정규식 검증 (한글, 숫자, 하이픈(-))
                    if (!vehicleNumber.matches("^[가-힣0-9-]+$")) {
                        errorMessages.append("차량번호는 한글, 숫자, 하이픈(-)만 포함할 수 있습니다: ")
                                .append("행 ").append(i + 1).append(" (").append(vehicleNumber).append(")\n엑셀파일 수정 후 다시 업로드 해주세요.");
                        return ResponseEntity.ok(errorMessages.toString());
                    }

                    // 엑셀 올릴 때 중복 데이터 검증
                    if (!vehicleNumber.isEmpty() && !vehicleType.isEmpty()) {
                        if (vehicleNumberMap.containsKey(vehicleNumber)) {
                            int previousRow = vehicleNumberMap.get(vehicleNumber);
                            errorMessages.append("중복되는 차량번호가 있습니다: ")
                                    .append("행 ").append(previousRow).append(" (")
                                    .append(vehicleNumber).append(")와 행 ")
                                    .append(i + 1).append(" (").append(vehicleNumber)
                                    .append(")\n엑셀파일 수정 후 다시 업로드 해주세요.");
                            return ResponseEntity.ok(errorMessages.toString());
                        } else {
                            vehicleNumberMap.put(vehicleNumber, i + 1);
                        }
                        Map<String, String> vehicleData = new HashMap<>();
                        vehicleData.put("vehicleNumber", vehicleNumber);
                        vehicleData.put("vehicleType", vehicleType);
                        vehicleDataList.add(vehicleData);
                    }
                }
                workbook.close();
            }
            String resultMessage = baseService.processVehicleData(vehicleDataList);
            return ResponseEntity.ok(resultMessage);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 처리 중 서버 내부 에러가 발생했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("엑셀 파일 처리 중 에러가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * @Method Name : isRowEmpty
     * @Method 설명 : 차적DB 업로드시 비어있는 ROW를 검사한다.
     * @작성자 : 이진표
     * @작성일 : 2025. 02. 21.
     */
    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && !cell.toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @Method Name : addVehicleInfo
     * @Method 설명 : 기초정보관리 - 차적DB관리에서 단건으로 차량을 추가한다.
     * @작성자 : 이진표
     * @작성일 : 2025. 02. 21.
     */
    @PostMapping("/addVehicleInfo")
    public ResponseEntity<String> addVehicleInfo(@RequestBody Map<String, String> param)
            throws Exception {
        String result = baseService.addVehicleInfo(param);
        return ResponseEntity.ok(result);
    }

    /**
     * @Method Name : deleteVehicleInfo
     * @Method 설명 : 기초정보관리 - 차적DB관리에서 단건으로 차량을 삭제한다.
     * @작성자 : 이진표
     * @작성일 : 2025. 02. 21.
     */
    @PostMapping("/deleteVehicleInfo")
    public ResponseEntity<String> deleteVehicleInfo(@RequestBody Map<String, String> param)
            throws Exception {
        String result = baseService.deleteVehicleInfo(param);
        return ResponseEntity.ok(result);
    }

    /**
     * @Method Name : oneUpdateVehicleInfo
     * @Method 설명 : 기초정보관리 - 차적DB관리에서 단건으로 차종을 수정한다.
     * @작성자 : 이진표
     * @작성일 : 2025. 02. 21.
     */
    @PostMapping("/oneUpdateVehicleInfo")
    public ResponseEntity<String> oneUpdateVehicleInfo(@RequestBody Map<String, String> param)
            throws Exception {
        String result = baseService.oneUpdateVehicleInfo(param);
        return ResponseEntity.ok(result);
    }

}
