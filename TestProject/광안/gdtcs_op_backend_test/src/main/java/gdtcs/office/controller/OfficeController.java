package gdtcs.office.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import gdtcs.office.service.OfficeService;
import gdtcs.util.ResponseCode;
import gdtcs.util.TCSException;

@Controller
@ResponseBody
@RequestMapping("/api/office")
public class OfficeController {
	
	@Resource(name = "officeService")
    private OfficeService officeService;

	@PostMapping("/getViolationList")
    public ResponseEntity<List<Map<String, Object>>> getViolationList(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getViolationList(param);

        return ResponseEntity.ok(list);
    }
	
	@PostMapping("/getViolationHistList")
    public ResponseEntity<List<Map<String, Object>>> getViolationHistList(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getViolationHistList(param);

        return ResponseEntity.ok(list);
    }
		
	@PostMapping("/chkDayFin")
    public ResponseEntity<Map<String, Object>> chkDayFin(@RequestBody List<Map<String, Object>> param) throws Exception{
		Map<String, Object> list = officeService.chkDayFin(param);

        return ResponseEntity.ok(list);
    }
	
	@PostMapping("/getLcarPl")
    public ResponseEntity<List<Map<String, Object>>> getLcarPl(@RequestBody List<Map<String, Object>> param) throws Exception{
		List<Map<String, Object>> list = officeService.getLcarPl(param);

        return ResponseEntity.ok(list);
    }
	
	@PostMapping("/setViolationAudit")
    public ResponseEntity<Map<String, Object>> setViolationAudit(@RequestBody Map<String, Object> param) throws Exception{
        Map<String, Object> resultMap = officeService.setViolationAudit(param);

        return ResponseEntity.ok(resultMap);
    }
	
	@PostMapping("/setViolationAuditBatch")
    public ResponseEntity<Map<String, Object>> setViolationAuditBatch(@RequestBody List<Map<String, Object>> param) throws Exception{
        Map<String, Object> resultMap = officeService.setViolationAuditBatch(param);

        int successCount = (int) resultMap.get("SUCCESS_COUNT");
        int failureCount = (int) resultMap.get("FAILURE_COUNT");

        Map<String, Object> result = new HashMap<>();

        switch (successCount) {
            case 0:
                result.put("ERROR_CODE", "0");
                result.put("ERROR_MSG", "처리된 데이터가 없습니다.");
                break;
            case -1:
                result.put("ERROR_CODE", "-1");
                result.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
                break;
            default:
                if (failureCount > 0) {
                    result.put("ERROR_CODE", "1");
                    result.put("ERROR_MSG", "총 " + successCount + "건 처리 성공, " + failureCount + "건 처리 실패.");
                } else {
                    result.put("ERROR_CODE", "1");
                    result.put("ERROR_MSG", "총 " + successCount + "건이 성공적으로 처리되었습니다.");
                }
                break;
        }

        return ResponseEntity.ok(result);
    }
	
	@PostMapping("/getEcardBl")
    public ResponseEntity<List<Map<String, Object>>> getEcardPl(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getEcardBl(param);

        return ResponseEntity.ok(list);
    }
	
	@PostMapping("/getExemptPl")
    public ResponseEntity<List<Map<String, Object>>> getExemptPl(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getExemptPl(param);

        return ResponseEntity.ok(list);
    }
	
	@PostMapping("/getMemberInfo")
    public ResponseEntity<List<Map<String, Object>>> getMemberInfo(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getMemberInfo(param);

        return ResponseEntity.ok(list);
    }

	@PostMapping("/getLocationSearchResultDetail")
    public ResponseEntity<Map<String, Object>> getLocationSearchResultDetail(@RequestBody Map<String, Object> param) throws Exception{
        Map<String, Object> list = officeService.getLocationSearchResultDetail(param);

        return ResponseEntity.ok(list);
    }
	
	@PostMapping("/getImageList")
    public ResponseEntity<List<Map<String, Object>>> getImageList(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getImageList(param);

        return ResponseEntity.ok(list);
    }
	
	@PostMapping("/getImageDetail")
    public ResponseEntity<Map<String, Object>> getImageDetail(@RequestBody Map<String, Object> param) throws Exception{
        Map<String, Object> list = officeService.getImageDetail(param);

        return ResponseEntity.ok(list);
    }
	
	@PostMapping("/getImageHistList")
    public ResponseEntity<List<Map<String, Object>>> getImageHistList(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getImageHistList(param);
        
        return ResponseEntity.ok(list);
    }

	@PostMapping("/setImageAudit")
    public ResponseEntity<Map<String, Object>> setImageAudit(@RequestBody Map<String, Object> param) throws Exception{
		Map<String, Object> res = officeService.setImageAudit(param);
		
        return ResponseEntity.ok(res);
    }

	@SuppressWarnings("unchecked")
	@PostMapping("/setImageAuditBatch")
    public ResponseEntity<Map<String, Object>> setImageAuditBatch(@RequestBody Map<String, Object> param) throws Exception{
		String option = (String) param.get("option");
		Map<String, Object> response = new HashMap<String, Object>();
		int successCount = 0;
		if (option.equals("3")) throw new TCSException(ResponseCode.IMG_CORR_BATCH_NOT_ALLOWED);
		List<Map<String, Object>> data = (List<Map<String, Object>>) param.get("data");
		for (Map<String, Object> item : data) {
			item.put("NEW_IMG_TYPE_DTL", option);
			item.put("IMG_VLTN_MAKE_YN", "N");
			item.put("NEW_CAR_TYPE", item.get("HAND_CAR_TYPE"));
			item.put("NEW_CAR_NO", item.get(option.equals("1") ? "CAR_NO" : (option.equals("2") ? "REP_CAR_NO" : "HAND_CAR_NO")));
			officeService.setImageAudit(item);
			successCount++;
		}
		response.put("successCount", successCount);
        return ResponseEntity.ok(response);
    }
	
	@PostMapping("/getImageCorrectionResultList")
    public ResponseEntity<List<Map<String, Object>>> getImageCorrectionResultList(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getImageCorrectionResultList(param);

        return ResponseEntity.ok(list);
    }
	
	@PostMapping("/getProcessList")
    public ResponseEntity<List<Map<String, Object>>> getProcessList(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getProcessList(param);

        return ResponseEntity.ok(list);
    }
	
	@PostMapping("/getReductionExEcardList")
    public ResponseEntity<List<Map<String, Object>>> getReductionExEcardList(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getReductionExEcardList(param);

        return ResponseEntity.ok(list);
    }

	@PostMapping("/setReductionExCorrection") 
	public ResponseEntity<Map<String, Object>> setReductionExCorrection(@RequestBody Map<String, Object> param) throws Exception{
		Map<String, Object> res = officeService.setReductionExCorrection(param);
		return ResponseEntity.ok(res);
	}

	@PostMapping("/setReductionExCorrectionWelfare") 
	public ResponseEntity<Map<String, Object>> setReductionExCorrectionWelfare(@RequestBody Map<String, Object> param) throws Exception{
		Map<String, Object> res = officeService.setReductionExCorrectionWelfare(param);
		return ResponseEntity.ok(res);
	}
	
	@PostMapping("/getReductionExObuList")
    public ResponseEntity<List<Map<String, Object>>> getReductionExObuList(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getReductionExObuList(param);

        return ResponseEntity.ok(list);
    }
	
	@PostMapping("/getReductionExWelfareList")
    public ResponseEntity<List<Map<String, Object>>> getReductionExWelfareList(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getReductionExWelfareList(param);

        return ResponseEntity.ok(list);
    }
	
	@PostMapping("/getCheatReductionExList")
    public ResponseEntity<List<Map<String, Object>>> getCheatReductionExList(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getCheatReductionExList(param);

        return ResponseEntity.ok(list);
    }
	
	@PostMapping("/getReductionBsList")
    public ResponseEntity<List<Map<String, Object>>> getReductionBsList(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getReductionBsList(param);

        return ResponseEntity.ok(list);
    }

	@PostMapping("/getReductionBsDetail")
    public ResponseEntity<Map<String, Object>> getReductionBsDetail(@RequestBody Map<String, Object> param) throws Exception{
        Map<String, Object> list = officeService.getReductionBsDetail(param);

        return ResponseEntity.ok(list);
    }

	@PostMapping("/setReductionBsCorrection") 
	public ResponseEntity<Map<String, Object>> setReductionBsCorrection(@RequestBody Map<String, Object> param) throws Exception{
		Map<String, Object> res = officeService.setReductionBsCorrection(param);
		return ResponseEntity.ok(res);
	}

	@PostMapping("/setReductionBsCorrectionTaxi") 
	public ResponseEntity<Map<String, Object>> setReductionBsCorrectionTaxi(@RequestBody Map<String, Object> param) throws Exception{
		Map<String, Object> res = officeService.setReductionBsCorrectionTaxi(param);
		return ResponseEntity.ok(res);
	}

	@PostMapping("/setReductionBsCorrectionTaxiBatch") 
	public ResponseEntity<Map<String, Object>> setReductionBsCorrectionTaxiBatch(@RequestBody Map<String, Object> param) throws Exception{
		Map<String, Object> res = officeService.setReductionBsCorrectionTaxiBatch(param);
		return ResponseEntity.ok(res);
	}
	
	@PostMapping("/getCheatReductionBsList")
    public ResponseEntity<List<Map<String, Object>>> getCheatReductionBsList(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getCheatReductionBsList(param);

        return ResponseEntity.ok(list);
    }
	
	@PostMapping("/getPayCorrectionList")
    public ResponseEntity<List<Map<String, Object>>> getPayCorrectionList(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getPayCorrectionList(param);

        return ResponseEntity.ok(list);
    }
	
	@PostMapping("/getPreRegistrationList")
    public ResponseEntity<List<Map<String, Object>>> getPreRegistrationList(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getPreRegistrationList(param);

        return ResponseEntity.ok(list);
    }
	
	@PostMapping("/getEtcIncomeList")
    public ResponseEntity<List<Map<String, Object>>> getEtcIncomeList(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getEtcIncomeList(param);
        
        return ResponseEntity.ok(list);
    }
	
	@PostMapping("/getWorkerList")
    public ResponseEntity<List<Map<String, Object>>> getWorkerList(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getWorkerList(param);
        
        return ResponseEntity.ok(list);
    }
	
	@PostMapping("/addEtcIncome")
	public ResponseEntity<Map<String, Object>> addEtcIncome(@RequestBody Map<String, Object> param) throws Exception{
		int check = officeService.chkEtcIncome(param);
		
		Map<String, Object> resultMap = new HashMap<>();
		
		if(check != 0) {
			//기타수입내역존재
			resultMap.put("ERROR_CODE", "0");
			resultMap.put("ERROR_MSG", "해당 차량번호의 수입내역이 존재합니다.");
			return ResponseEntity.ok(resultMap);
		}
		
        int result = officeService.addEtcIncome(param);
        
        switch (result) {
		case 0: 
			resultMap.put("ERROR_CODE", "0");
			resultMap.put("ERROR_MSG", "추가할 수 없습니다.");
            break;
		case -1: 
			resultMap.put("ERROR_CODE", "-1");
			resultMap.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
			break;
		case -2: 
			resultMap.put("ERROR_CODE", "-2");
			resultMap.put("ERROR_MSG", "같은 값이 존재합니다.");
			break;
		default:
			resultMap.put("ERROR_CODE", result);
			resultMap.put("ERROR_MSG", "정상처리되었습니다.");
	        break;
		}

        return ResponseEntity.ok(resultMap);
    }
	
	@PostMapping("/delEtcIncome")
	public ResponseEntity<Map<String, Object>> delEtcIncome(@RequestBody List<Map<String, Object>> param) throws Exception{
        int result = officeService.delEtcIncome(param);
        System.out.print(result);
        Map<String, Object> resultMap = new HashMap<>();
        
        switch (result) {
		case 0: 
			resultMap.put("ERROR_CODE", "0");
			resultMap.put("ERROR_MSG", "삭제할 수 없습니다.");
            break;
		case -1: 
			resultMap.put("ERROR_CODE", "-1");
			resultMap.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
			break;
		case -2: 
			resultMap.put("ERROR_CODE", "-2");
			resultMap.put("ERROR_MSG", "같은 값이 존재합니다.");
			break;
		default:
			resultMap.put("ERROR_CODE", result);
			resultMap.put("ERROR_MSG", "삭제되었습니다.");
	        break;
		}

        return ResponseEntity.ok(resultMap);
    }
	
	@PostMapping("/setEtcIncome")
	public ResponseEntity<Map<String, Object>> setEtcIncome(@RequestBody Map<String, Object> param) throws Exception{
        int result = officeService.setEtcIncome(param);
        
        Map<String, Object> resultMap = new HashMap<>();
        
        switch (result) {
		case 0: 
			resultMap.put("ERROR_CODE", "0");
			resultMap.put("ERROR_MSG", "수정할 수 없습니다.");
            break;
		case -1: 
			resultMap.put("ERROR_CODE", "-1");
			resultMap.put("ERROR_MSG", "알 수 없는 오류가 발생했습니다.");
			break;
		case -2: 
			resultMap.put("ERROR_CODE", "-2");
			resultMap.put("ERROR_MSG", "같은 값이 존재합니다.");
			break;
		default:
			resultMap.put("ERROR_CODE", result);
			resultMap.put("ERROR_MSG", "수정되었습니다.");
	        break;
		}

        return ResponseEntity.ok(resultMap);
    }
	
	@PostMapping("/getWorkNo")
    public ResponseEntity<List<Map<String, Object>>> getWorkNo(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getWorkNo(param);
        
        return ResponseEntity.ok(list);
    }
	
	@PostMapping("/getLaneNo")
    public ResponseEntity<List<Map<String, Object>>> getLaneNo(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getLaneNo(param);
        
        return ResponseEntity.ok(list);
    }
	
	@PostMapping("/getViolationState")
    public ResponseEntity<List<Map<String, Object>>> getViolationState(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getViolationState(param);
        
        return ResponseEntity.ok(list);
    }

    @PostMapping("/getCorrectionCount")
    public ResponseEntity<List<Map<String, Object>>> getCorrectionCount(@RequestBody Map<String, Object> param) throws Exception{
        List<Map<String, Object>> list = officeService.getCorrectionCount(param);

        return ResponseEntity.ok(list);
    }
}
