package gdtcs.inter.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import gdtcs.inter.util.CenterDB;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import gdtcs.inter.service.InterService;

/**
 * 송수신관리에 관한 컨트롤러 클래스
 *
 * @author 이진표
 * @version 1.0
 * @see <pre>
 *  == 개정이력(Modification Information) ==
 *
 *       수정일               수정자                     수정내용
 *  ----------------    ------------    ---------------------------
 *    2025.05.27            이진표                   최초 생성
 *
 *      </pre>
 * @since 2024.05.27
 */

@Controller
@RequestMapping("/api/inter")
public class InterController {

    @Resource(name = "interService")
    private InterService interService;

    /**
     * @Method Name : getInter
     * @Method 설명 : 송수신관리에서 정산요청을 하기전에 데이터를 조회한다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 13.
     */
    @PostMapping("/getInter")
    public ResponseEntity<Map<String, Object>> getInter(@RequestBody Map<String, Object> param) throws Exception {
        Map<String, Object> result = interService.getInter(param);
        return ResponseEntity.ok(result);
    }

    /**
     * @Method Name : getTaxi
     * @Method 설명 : 송수신관리에서 공차택시 수신 여부를 조회한다.
     * @작성자 : 이진표
     * @작성일 : 2025. 02. 10.
     */
    @PostMapping("/getTaxi")
    public ResponseEntity<Map<String, Object>> getTaxi(@RequestBody Map<String, Object> param) throws Exception {
        Map<String, Object> result = interService.getTaxi(param);
        return ResponseEntity.ok(result);
    }

    /**
     * @Method Name : getPreRegi
     * @Method 설명 : 송수신관리에서 사전등록 수신 여부를 조회한다.
     * @작성자 : 이진표
     * @작성일 : 2025. 02. 10.
     */
    @PostMapping("/getPreRegi")
    public ResponseEntity<Map<String, Object>> getPreRegi(@RequestBody Map<String, Object> param) throws Exception {
        Map<String, Object> result = interService.getPreRegi(param);
        return ResponseEntity.ok(result);
    }


    /**
     * @Method Name : checkIsHoliday
     * @Method 설명 :  송수신관리에서 특정일면제는 공차택시/사전등록 수신여부와 상관없이 집계처리를 한다.
     * @작성자 : 이진표
     * @작성일 : 2025. 02. 25.
     */
    @PostMapping("/checkIsHoliday")
    public ResponseEntity<Boolean> checkIsHoliday(@RequestBody Map<String, Object> param) throws Exception {
        boolean isHoliday = interService.checkIsHoliday(param);
        System.out.println("##### isHoliday = " + isHoliday);
        return ResponseEntity.ok(isHoliday);
    }




    /**
     * @Method Name : transTaxiPre
     * @Method 설명 : 송수신관리에서 공차택시/사전등록 집계처리를 한다.
     * @작성자 : 이진표
     * @작성일 : 2025. 02. 18.
     */
    @PostMapping("/requestReDayFin")
    public ResponseEntity<Map<String, Object>> requestReDayFin(@RequestBody Map<String, Object> param) throws Exception {

        String workDate = (String) param.get("WORK_DATE");
        String formattedWorkDate = LocalDate.parse(workDate, DateTimeFormatter.ofPattern("yyyyMMdd")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> list = interService.transTaxiPre(param);
            if (list != null && !list.isEmpty()) {
                Map<String, Object> firstEntry = list.get(0);
                String code = (String) firstEntry.get("CODE");
                String message = (String) firstEntry.get("MESSAGE");
                boolean isHoliday = (Boolean) param.get("IS_HOLIDAY");

                if ("SUCCESS".equals(code)) {
                    // 성공 -> 교통량 재집계 SP_TRAFFIC_RECREATE 호출
                    interService.transTaxiPreTraffic(param);
                    if(!isHoliday) {
                        result.put("message", "[" + formattedWorkDate + "] 공차택시/사전등록을 반영한 일마감 재집계 및 교통량 반영에 성공했습니다.");
                    } else {
                        result.put("message", "[" + formattedWorkDate + "] 특정일면제를 반영한 일마감 재집계 및 교통량 반영에 성공했습니다.");
                    }

                } else if ("FAIL".equals(code)) {
                    if(!isHoliday) {
                        result.put("message", "[" + formattedWorkDate + "] 공차택시/사전등록을 반영한 일마감 재집계 및 교통량 반영에 실패했습니다.");
                    } else {
                        result.put("message", "[" + formattedWorkDate + "] 특정일면제를 반영한 일마감 재집계 및 교통량 반영에 실패했습니다.");
                    }
                } else {
                    result.put("message", "알 수 없는 프로시저 응답 코드 입니다.");
                }
            } else {
                result.put("message", "SP_DAYFIN_INIT_AFTER 프로시저 호출 결과가 비어 있습니다.");
            }
        } catch (Exception e) {
            result.put("message", e);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * @Method Name : getCenterLockYn
     * @Method 설명 : 송수신관리에서 외부연계서버로 전문을 전송하기전 광안센터 DB에 접속하여 LOCK 여부를 조회한다.
     * @작성자 : 이진표
     * @작성일 : 2024. 12. 17.
     */
    @PostMapping("/getCenterLockYn")
    public ResponseEntity<Map<String, Object>> getCenterLockYn(@RequestBody Map<String, Object> param) {
        try {
            Map<String, Object> result = interService.getCenterLockYn(param);
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", -1);
            errorResponse.put("message", "DB 연결 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", -1);
            errorResponse.put("message", "알 수 없는 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * @Method Name : getIsDayFin
     * @Method 설명 : 송수신관리에서 외부연계서버로 전문을 전송하기 전에 일마감 확인을 한다.
     * @작성자 : 이진표
     * @작성일 : 2024. 12. 28.
     */
    @PostMapping("/getIsDayFin")
    public ResponseEntity<Map<String, Object>> getIsDayFin(@RequestBody Map<String, Object> param) throws Exception {
        Map<String, Object> result = interService.getIsDayFin(param);
        return ResponseEntity.ok(result);
    }

    /**
     * @Method Name : centerLockCheck
     * @Method 설명 : 송수신관리에서 다중사용자 이슈를 위해 버튼을 클릭하면 센터 락 여부를 확인한다.
     * @작성자 : 이진표
     * @작성일 : 2025. 02. 11.
     */
    @PostMapping("/centerLockCheck")
    public ResponseEntity<Map<String, Object>> centerLockCheck(@RequestBody Map<String, Object> param) throws Exception {
        Map<String, Object> result = CenterDB.selectICRCVLOCK_M((String) param.get("IC_CODE"), (String) param.get("CALC_DATE"));
        return ResponseEntity.ok(result);
    }

    /**
     * @Method Name : requestInter
     * @Method 설명 : 송수신관리에서 일마감 전송을 위해 외부연계서버로 전문을 전송한다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 29.
     */
    @PostMapping("/requestInter")
    public ResponseEntity<Map<String, Object>> setInter(@RequestBody Map<String, Object> param) {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> serviceResult = interService.requestInter(param);
            int status = (int) serviceResult.get("status");
            if (status == 0) {
                response.put("SUCCESS_MSG", serviceResult.get("message"));
                response.put("RESULT_CODE", serviceResult.get("status"));
            } else if (status == 1) { // 실패
                response.put("FAIL_MSG", serviceResult.get("message"));
                response.put("RESULT_CODE", serviceResult.get("status"));
            } else if (status == -1) {
                response.put("UNKNOWN_MSG", serviceResult.get("message"));
                response.put("RESULT_CODE", serviceResult.get("status"));
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("ERROR_CODE", "500");
            response.put("ERROR_MSG", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * @Method Name : getCenterInter
     * @Method 설명 : 센터송수신내역을 조회한다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 13.
     */
    @PostMapping("/getCenterInter")
    public ResponseEntity<List<Map<String, Object>>> getCenterInter(@RequestBody Map<String, Object> param)
            throws Exception {
        List<Map<String, Object>> list = interService.getCenterInter(param);
        return ResponseEntity.ok(list);
    }

    /**
     * @Method Name : getCenterInter
     * @Method 설명 : 센터송수신내역조회 에서 조회조건에 파일종류를 불러온다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 15.
     */
    @PostMapping("/getFileDivNm")
    public ResponseEntity<List<Map<String, Object>>> getFileDivNm(@RequestBody Map<String, Object> param)
            throws Exception {
        List<Map<String, Object>> list = interService.getFileDivNm(param);
        return ResponseEntity.ok(list);
    }
}