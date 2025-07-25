package gdtcs.settle.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import gdtcs.settle.service.SettleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@ResponseBody
@RequestMapping("/api/settle")
public class SettleController {
	@Autowired
	private SettleService service;
	
	@PostMapping("/getRefundRequestList")
	public ResponseEntity<List<Map<String, Object>>> getRefundRequestList(@RequestBody Map<String, Object> param) throws Exception{
		List<Map<String, Object>> list = service.getRefundRequestList(param);
		
		return ResponseEntity.ok(list);
	}
	@PostMapping("/getRefundResponseList")
	public ResponseEntity<List<Map<String, Object>>> getRefundResponseList(@RequestBody Map<String, Object> param) throws Exception{
		List<Map<String, Object>> list = service.getRefundResponseList(param);
		
		return ResponseEntity.ok(list);
	}
	
	@PostMapping("/getRefundInfoDetailList")
	public ResponseEntity<List<Map<String, Object>>> getRefundDetailList(@RequestBody Map<String, Object> param) throws Exception{
		List<Map<String, Object>> list = service.getRefundDetailList(param);
		
		return ResponseEntity.ok(list);
	}
	
	@PostMapping("/getRefundInfoDetail")
	public ResponseEntity<Map<String, Object>> getRefundInfoDetail(@RequestBody Map<String, Object> param) throws Exception{
		Map<String, Object> res = service.getRefundInfoDetail(param);
		
		return ResponseEntity.ok(res);
	}

	@PostMapping("/setRefundInfoBatch") 
	public ResponseEntity<Map<String, Object>> setRefundInfoBatch(@RequestBody Map<String, Object> param) throws Exception{
		Map<String, Object> res = service.setRefundInfoBatch(param);
		return ResponseEntity.ok(res);
	}

	/* 환불 명세 관리 - 목록조회 */
    @PostMapping("/getRefundManagementList")
    public ResponseEntity<List<Map<String, Object>>> getRefundManagementList(@RequestBody Map<String, Object> param) throws Exception{
    	List<Map<String, Object>> res = service.getRefundManagementList(param);

        return ResponseEntity.ok(res);
    }

	/* 환불 명세 관리 - 차량별 조회 */
    @PostMapping("/getRefundManagementDetailList")
    public ResponseEntity<List<Map<String, Object>>> getRefundManagementDetailList(@RequestBody Map<String, Object> param) throws Exception{
    	List<Map<String, Object>> res = service.getRefundManagementDetailList(param);

        return ResponseEntity.ok(res);
    }

	/* 환불 명세 관리 - 일괄처리 */
    @SuppressWarnings("unchecked")
	@PostMapping("/setRefundManagementBatch")
    public ResponseEntity<Map<String, Object>> setRefundManagementBatch(@RequestBody Map<String, Object> param) throws Exception{
    	List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("LIST");
    	List<Map<String, Object>> data = new ArrayList<>();
    	for (Map<String, Object> item : list) {
    		item.put("IC_CODE", param.get("IC_CODE"));
    		item.put("START_DATE", param.get("START_DATE"));
    		item.put("END_DATE", param.get("END_DATE"));
    		data.addAll(service.getRefundManagementDetailList(item));
    	}
    	Map<String, Object> batchParam = new HashMap<String, Object> ();
    	batchParam.put("REFUND_TYPE", param.get("REFUND_TYPE"));
    	batchParam.put("ADMIN", param.get("ADMIN"));
    	batchParam.put("data", data);
    	Map<String, Object> res = service.setRefundInfoBatch(batchParam);
        return ResponseEntity.ok(res);
    }
}
