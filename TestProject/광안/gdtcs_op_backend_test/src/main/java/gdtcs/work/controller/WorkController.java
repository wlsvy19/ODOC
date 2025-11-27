package gdtcs.work.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import gdtcs.work.service.WorkService;

/**
 * 근무관리에 관한 컨트롤러 클래스
 *
 * @author 이진표
 * @since 2024.05.27
 * @version 1.0
 * @see
 * 
 *      <pre>
 *  == 개정이력(Modification Information) ==
 *
 *       수정일               수정자                     수정내용
 *  ----------------    ------------    ---------------------------
 *    2025.05.27            이진표                   최초 생성
 *
 *      </pre>
 */

@Controller
@RequestMapping("/api/work")
public class WorkController {

    @Resource(name = "workService")
    private WorkService workService;

    /**
     * 
     * @Method Name : getWorkStat
     * @Method 설명 : 근무상태를 조회한다
     * @작성자 : 이진표
     * @작성일 : 2024. 07. 01.
     */
    @PostMapping("/getWorkStat")
    public ResponseEntity<List<Map<String, Object>>> getWorkStat(@RequestBody Map<String, Object> param)
            throws Exception {
        List<Map<String, Object>> list = workService.getWorkStat(param);
        return ResponseEntity.ok(list);
    }

    /**
     * 
     * @Method Name : getWorkFin
     * @Method 설명 : 근무마감처리를 위한 데이터를 조회한다.
     * @작성자 : 이진표
     * @작성일 : 2024. 10. 02.
     */
    @PostMapping("/getWorkFin")
    public ResponseEntity<List<Map<String, Object>>> getWorkFin(@RequestBody Map<String, Object> param)
            throws Exception {
        List<Map<String, Object>> list = workService.getWorkFin(param);
        return ResponseEntity.ok(list);
    }

    /**
     * 
     * @Method Name : setWorkFinForce
     * @Method 설명 : ETCS 강제근무종료를 위해 프로시저를 호출한다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 13.
     */
    @PostMapping("/setWorkFinForce")
    public ResponseEntity<List<Map<String, Object>>> setWorkFinForce(@RequestBody Map<String, Object> param)
            throws Exception {
        List<Map<String, Object>> list = workService.setWorkFinForce(param);
        return ResponseEntity.ok(list);
    }

    /**
     * 
     * @Method Name : setWorkFinForceTCS
     * @Method 설명 : TCS 강제근무종료를 한다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 13.
     */
    @PostMapping("/setWorkFinForceTCS")
    public ResponseEntity<Map<String, Object>> setWorkFinForceTCS(@RequestBody Map<String, Object> param)
            throws Exception {
        int changedRows = workService.setWorkFinForceTCS(param);
        System.out.println("changedRows: " + changedRows);
        Map<String, Object> result = new HashMap<>();
        switch (changedRows) {
        case 0:
            result.put("ERROR_CODE", 0);
            result.put("ERROR_MSG", "근무상태가 업데이트 되지 않았습니다. 관리자한테 문의하세요.");
            break;
        case -1:
            result.put("ERROR_CODE", -1);
            result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다. 관리자한테 문의하세요.");
            break;
        default:
            result.put("SUCCESS_CODE", changedRows);
            result.put("SUCCESS_MSG", "TCS 강제근무종료가 성공적으로 이루어졌습니다.");
            break;
        }

        return ResponseEntity.ok(result);
    }

    /**
     * 
     * @Method Name : getWorkDoc
     * @Method 설명 : 근무확인서를 조회한다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 13.
     */
    @PostMapping("/getWorkDoc")
    public ResponseEntity<List<Map<String, Object>>> getWorkDoc(@RequestBody Map<String, Object> param)
            throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.add(workService.getWorkDocInfo(param));
        list.add(workService.getWorkDoc(param));
        list.add(workService.getWorkDocCar(param));
        return ResponseEntity.ok(list);
    }

    /**
     * 
     * @Method Name : getPassPrim
     * @Method 설명 : 통행원시를 데이터를 조회한다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 13.
     */
    @PostMapping("/getPassPrim")
    public ResponseEntity<List<Map<String, Object>>> getPassPrim(@RequestBody Map<String, Object> param)
            throws Exception {
        List<Map<String, Object>> list = workService.getPassPrim(param);
        return ResponseEntity.ok(list);
    }

    /**
     * 
     * @Method Name : getPassPrimDtl
     * @Method 설명 : 통행원시 상세데이터를 조회한다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 13.
     */
    @PostMapping("/getPassPrimDtl")
    public ResponseEntity<List<Map<String, Object>>> getPassPrimDtl(@RequestBody Map<String, Object> param)
            throws Exception {
        List<Map<String, Object>> list = workService.getPassPrimDtl(param);
        return ResponseEntity.ok(list);
    }

    /**
     * 
     * @Method Name : getWorkNm
     * @Method 설명 : 통행원시시(상세)에서 조회를 위해 근무번호를 가져온다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 13.
     */
    @PostMapping("/getWorkNm")
    public ResponseEntity<List<Map<String, Object>>> getWorkNm(@RequestBody Map<String, Object> param)
            throws Exception {
        List<Map<String, Object>> list = workService.getWorkNm(param);
        return ResponseEntity.ok(list);
    }

    /**
     * 
     * @Method Name : getWorkDocWorkNm
     * @Method 설명 : 근무확인서에서 종료된 근무를 위해 근무번호를 가져온다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 13.
     */
    @PostMapping("/getWorkDocWorkNm")
    public ResponseEntity<List<Map<String, Object>>> getWorkDocWorkNm(@RequestBody Map<String, Object> param)
            throws Exception {
        List<Map<String, Object>> list = workService.getWorkDocWorkNm(param);
        return ResponseEntity.ok(list);
    }

    /**
     * 
     * @Method Name : getPassPrimDate
     * @Method 설명 : 통행원시시(상세)에서 근무번호 선택시 거기에 해당하는 조회시간을 자동으로 가져온다.
     * @작성자 : 이진표
     * @작성일 : 2024. 11. 13.
     */
    @PostMapping("/getPassPrimDate")
    public ResponseEntity<Map<String, Object>> getPassPrimDate(@RequestBody Map<String, Object> param)
            throws Exception {
        Map<String, Object> map = workService.getPassPrimDate(param);
        return ResponseEntity.ok(map);
    }

}
