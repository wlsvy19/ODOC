package gdtcs.day.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import gdtcs.day.service.DayService;
import gdtcs.video.service.VideoService;

@Controller
@ResponseBody
@RequestMapping("/api/day")
public class DayController {
	@Autowired
	private DayService service;

	protected Log log = LogFactory.getLog(this.getClass());
	
	@PostMapping("/getDayFinList")
	public ResponseEntity<List<Map<String, Object>>> getDayFinList(@RequestBody Map<String, Object> param) throws Exception{
		List<Map<String, Object>> list = service.getDayFinList(param);
		
		return ResponseEntity.ok(list);
	}
	@PostMapping("/getDayFinMdfyList")
	public ResponseEntity<List<Map<String, Object>>> getDayFinMdfyList(@RequestBody Map<String, Object> param) throws Exception{
		List<Map<String, Object>> list = service.getDayFinMdfyList(param);
		
		return ResponseEntity.ok(list);
	}
	@PostMapping("/getDayFinLockList")
	public ResponseEntity<List<Map<String, Object>>> getDayFinLockList(@RequestBody Map<String, Object> param) throws Exception{
		List<Map<String, Object>> list = service.getDayFinLockList(param);
		
		return ResponseEntity.ok(list);
	}
	@PostMapping("/getDayFinUnlockList")
	public ResponseEntity<List<Map<String, Object>>> getDayFinUnlockList(@RequestBody Map<String, Object> param) throws Exception{
		List<Map<String, Object>> list = service.getDayFinUnlockList(param);
		
		return ResponseEntity.ok(list);
	}
	@PostMapping("/getDayFinReport")
	public ResponseEntity<Map<String, List<Map<String, Object>>>> getDayFinReport(@RequestBody Map<String, Object> param) throws Exception{
		Map<String, List<Map<String, Object>>> result = service.getDayFinReport(param);
				
		return ResponseEntity.ok(result);
	}
	@PostMapping("/getDayFinPeriodReport")
	public ResponseEntity<List<Map<String, Object>>> getDayFinPeriodReport(@RequestBody Map<String, Object> param) throws Exception{
		List<Map<String, Object>> result = service.getDayFinPeriodReport(param);
				
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/setDayFin")
	public ResponseEntity<Map<String, Object>> setDayFin(@RequestBody Map<String, Object> param) throws Exception{
		Map<String, Object> resultMap = service.setDayFin(param);
		
		return ResponseEntity.ok(resultMap);
	}
	@PostMapping("/setDayFinCancel")
	public ResponseEntity<Map<String, Object>> setDayFinCancel(@RequestBody Map<String, Object> param) throws Exception{
		Map<String, Object> resultMap = service.setDayFinCancel(param);
		
		return ResponseEntity.ok(resultMap);
	}
	@PostMapping("/setDayFinLock")
	public ResponseEntity<Map<String, Object>> setDayFinLock(@RequestBody Map<String, Object> param) throws Exception{
		Map<String, Object> resultMap = service.setDayFinLock(param);
		
		return ResponseEntity.ok(resultMap);
	}
	@PostMapping("/setDayFinUnlock")
	public ResponseEntity<Map<String, Object>> setDayFinUnlock(@RequestBody Map<String, Object> param) throws Exception{
		Map<String, Object> resultMap = service.setDayFinUnlock(param);
		
		return ResponseEntity.ok(resultMap);
	}
	@PostMapping("/getDayFinReportNew")
	public ResponseEntity<Map<String, List<Map<String, Object>>>> getDayFinReportNew(@RequestBody Map<String, Object> param) throws Exception{
		Map<String, List<Map<String, Object>>> result = service.getDayFinReportNew(param);
				
		return ResponseEntity.ok(result);
	}
	@PostMapping("/getDayFinPeriodReportNew")
	public ResponseEntity<List<Map<String, Object>>> getDayFinPeriodReportNew(@RequestBody Map<String, Object> param) throws Exception{
		List<Map<String, Object>> result = service.getDayFinPeriodReportNew(param);
				
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/setAuditStatus")
	public ResponseEntity<Map<String, Object>> setAuditStatus(@RequestBody Map<String, Object> param) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
//		int updateLowCnt = service.setImageAuditStatus(param);
		int updateLowCnt = 0;
		
		resultMap.put("CNT", updateLowCnt);
		
		log.info("upadted hipass_image_data low count:" + updateLowCnt);
		
		return ResponseEntity.ok(resultMap);
	}
}

