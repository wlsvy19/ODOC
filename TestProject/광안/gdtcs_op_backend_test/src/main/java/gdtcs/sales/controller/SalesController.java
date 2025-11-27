package gdtcs.sales.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import gdtcs.sales.service.SalesService;

@Controller
@ResponseBody
@RequestMapping("/api/sales")
public class SalesController {
	@Autowired
	private SalesService service;

	@PostMapping("/getTaxCompanyList")
	public ResponseEntity<List<Map<String, Object>>> getTaxCompanyList(@RequestBody Map<String, Object> param)
			throws Exception {
		List<Map<String, Object>> list = service.getTaxCompanyList(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getTaxCompanyDetailList")
	public ResponseEntity<Map<String, Object>> getTaxCompanyDetailList(@RequestBody Map<String, Object> param)
			throws Exception {
		Map<String, Object> resultMap = service.getTaxCompanyDetailList(param);

		return ResponseEntity.ok(resultMap);
	}

	@PostMapping("/getTaxCompanyCardList")
	public ResponseEntity<List<Map<String, Object>>> getTaxCompanyCardList(@RequestBody Map<String, Object> param)
			throws Exception {
		List<Map<String, Object>> list = service.getTaxCompanyCardList(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/addTaxCompany")
	public ResponseEntity<Map<String, Object>> addTaxCompany(@RequestBody Map<String, Object> param) throws Exception {
		int result = service.addTaxCompany(param);
		Map<String, Object> resultMap = new HashMap<>();

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
				resultMap.put("ERROR_MSG", "이미 등록된 사업자번호입니다.");
				break;
			default:
				resultMap.put("ERROR_CODE", result);
				resultMap.put("ERROR_MSG", "등록 완료되었습니다.");
				break;
		}
		return ResponseEntity.ok(resultMap);
	}

	@PostMapping("/setTaxCompany")
	public ResponseEntity<Map<String, Object>> setTaxCompany(@RequestBody Map<String, Object> param) throws Exception {
		int result = service.setTaxCompany(param);

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
			default:
				resultMap.put("ERROR_CODE", result);
				resultMap.put("ERROR_MSG", "수정이 완료되었습니다.");
				break;
		}

		return ResponseEntity.ok(resultMap);
	}

	@PostMapping("/delTaxCompany")
	public ResponseEntity<Map<String, Object>> delTaxCompany(@RequestBody Map<String, Object> param) throws Exception {
		int result = service.delTaxCompany(param);
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
				resultMap.put("ERROR_MSG", "등록된 카드가 존재하여 삭제할 수 없습니다.");
				break;
			default:
				resultMap.put("ERROR_CODE", result);
				resultMap.put("ERROR_MSG", result + "개 데이터 삭제 완료하였습니다.");
				break;
		}
		return ResponseEntity.ok(resultMap);
	}

	@PostMapping("/addCompanyCard")
	public ResponseEntity<Map<String, Object>> addCompanyCard(@RequestBody Map<String, Object> param) throws Exception {
		int result = service.addCompanyCard(param);
		Map<String, Object> resultMap = new HashMap<>();

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
				resultMap.put("ERROR_MSG", "이미 등록된 카드번호입니다.");
				break;
			default:
				resultMap.put("ERROR_CODE", result);
				resultMap.put("ERROR_MSG", "등록 완료되었습니다.");
				break;
		}
		return ResponseEntity.ok(resultMap);
	}

	@PostMapping("/setCompanyCard")
	public ResponseEntity<Map<String, Object>> setCompanyCard(@RequestBody Map<String, Object> param) throws Exception {
		int result = service.setCompanyCard(param);

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
			default:
				resultMap.put("ERROR_CODE", result);
				resultMap.put("ERROR_MSG", "수정이 완료되었습니다.");
				break;
		}

		return ResponseEntity.ok(resultMap);
	}

	@PostMapping("/delCompanyCard")
	public ResponseEntity<Map<String, Object>> delCompanyCard(@RequestBody Map<String, Object> param) throws Exception {
		int result = service.delCompanyCard(param);
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
			default:
				resultMap.put("ERROR_CODE", result);
				resultMap.put("ERROR_MSG", "삭제 성공");
				break;
		}
		return ResponseEntity.ok(resultMap);
	}

	@PostMapping("/getTaxbillPublishList")
	public ResponseEntity<List<Map<String, Object>>> getTaxbillPublishList(@RequestBody Map<String, Object> param)
			throws Exception {
		List<Map<String, Object>> list = service.getTaxbillPublishList(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getTaxbillPublishHist")
	public ResponseEntity<List<Map<String, Object>>> getTaxbillPublishHist(@RequestBody Map<String, Object> param)
			throws Exception {
		List<Map<String, Object>> list = service.getTaxbillPublishHist(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/downloadTaxbill")
	public void downloadTaxbill(@RequestBody List<Map<String, Object>> param, HttpServletResponse response)
			throws Exception {
		service.downloadTaxbill(param, response);
	}

	@PostMapping("/getDailyTrafficProcessDay")
	public ResponseEntity<List<Map<String, Object>>> getDailyTrafficProcessDay(@RequestBody Map<String, Object> param)
			throws Exception {

		List<Map<String, Object>> list = service.getDailyTrafficProcessDay(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getDailyTrafficProcessMonth")
	public ResponseEntity<List<Map<String, Object>>> getDailyTrafficProcessMonth(@RequestBody Map<String, Object> param)
			throws Exception {

		List<Map<String, Object>> list = service.getDailyTrafficProcessMonth(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getDailyTrafficProcessYear")
	public ResponseEntity<List<Map<String, Object>>> getDailyTrafficProcessYear(@RequestBody Map<String, Object> param)
			throws Exception {

		List<Map<String, Object>> list = service.getDailyTrafficProcessYear(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getDailyViolation")
	public ResponseEntity<List<Map<String, Object>>> getDailyViolation(@RequestBody Map<String, Object> param)
			throws Exception {

		List<Map<String, Object>> list = service.getDailyViolation(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getReducedVehicleListEx")
	public ResponseEntity<List<Map<String, Object>>> getReducedVehicleListEx(@RequestBody Map<String, Object> param)
			throws Exception {
		List<Map<String, Object>> list = service.getReducedVehicleListEx(param);
		return ResponseEntity.ok(list);
	}

	@PostMapping("/getReducedVehicleListBs")
	public ResponseEntity<List<Map<String, Object>>> getReducedVehicleListBs(@RequestBody Map<String, Object> param)
			throws Exception {
		List<Map<String, Object>> list = service.getReducedVehicleListBs(param);
		return ResponseEntity.ok(list);
	}

	@PostMapping("/getDiscountSummary")
	public ResponseEntity<Map<String, Object>> getDiscountSummary(@RequestBody Map<String, Object> param)
			throws Exception {
		Map<String, Object> res = service.getDiscountSummary(param);

		return ResponseEntity.ok(res);
	}

	@PostMapping("/getDiscountSurchargeList")
	public ResponseEntity<List<Map<String, Object>>> getDiscountSurchargeList(@RequestBody Map<String, Object> param)
			throws Exception {
		List<Map<String, Object>> list = service.getDiscountSurchargeList(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getDailyExemptDetail")
	public ResponseEntity<List<Map<String, Object>>> getDailyExemptDetail(@RequestBody Map<String, Object> param)
			throws Exception {
		List<Map<String, Object>> list = service.getDailyExemptDetail(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getBusanContinueTrafficDCSum")
	public ResponseEntity<List<Map<String, Object>>> getBusanContinueTrafficDCSum(
			@RequestBody Map<String, Object> param) throws Exception {

		List<Map<String, Object>> list = service.getBusanContinueTrafficDCSum(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getBusanContinueTrafficDCDayFin")
	public ResponseEntity<List<Map<String, Object>>> getBusanContinueTrafficDCDayFin(
			@RequestBody Map<String, Object> param) throws Exception {

		List<Map<String, Object>> list = service.getBusanContinueTrafficDCDayFin(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getRefundList")
	public ResponseEntity<List<Map<String, Object>>> getRefundList(@RequestBody Map<String, Object> param)
			throws Exception {
		List<Map<String, Object>> list = service.getRefundList(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getRefundListBs")
	public ResponseEntity<List<Map<String, Object>>> getRefundListBs(@RequestBody Map<String, Object> param)
			throws Exception {
		List<Map<String, Object>> list = service.getRefundListBs(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getDailyExemptSummaryEx")
	public ResponseEntity<List<Map<String, Object>>> getDailyExemptSummaryEx(@RequestBody Map<String, Object> param)
			throws Exception {
		List<Map<String, Object>> list = service.getDailyExemptSummaryEx(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getDailyReductionSummaryEx")
	public ResponseEntity<List<Map<String, Object>>> getDailyReductionSummaryEx(@RequestBody Map<String, Object> param)
			throws Exception {
		List<Map<String, Object>> list = service.getDailyReductionSummaryEx(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getDailyExemptSummaryBs")
	public ResponseEntity<List<Map<String, Object>>> getDailyExemptSummaryBs(@RequestBody Map<String, Object> param)
			throws Exception {
		List<Map<String, Object>> list = service.getDailyExemptSummaryBs(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getSummaryByOBUType")
	public ResponseEntity<List<Map<String, Object>>> getSummaryByOBUType(@RequestBody Map<String, Object> param)
			throws Exception {
		List<Map<String, Object>> list = service.getSummaryByOBUType(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getDailyTollTrends")
	public ResponseEntity<Map<String, Object>> getDailyTollTrends(@RequestBody Map<String, Object> param)
			throws Exception {
		Map<String, Object> resultMap = service.getDailyTollTrends(param);

		return ResponseEntity.ok(resultMap);
	}

	@PostMapping("/getBusanContinueTrafficDCRealTime")
	public ResponseEntity<List<Map<String, Object>>> getBusanContinueTrafficDCRealTime(
			@RequestBody Map<String, Object> param) throws Exception {

		List<Map<String, Object>> list = service.getBusanContinueTrafficDCRealTime(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getBusanContinueTrafficDCRefund")
	public ResponseEntity<List<Map<String, Object>>> getBusanContinueTrafficDCRefund(
			@RequestBody Map<String, Object> param) throws Exception {

		List<Map<String, Object>> list = service.getBusanContinueTrafficDCRefund(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getBusanContinueTrafficDCTrfSum")
	public ResponseEntity<List<Map<String, Object>>> getBusanContinueTrafficDCTrfSum(
			@RequestBody Map<String, Object> param) throws Exception {

		List<Map<String, Object>> list = service.getBusanContinueTrafficDCTrfSum(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getListSysCode")
	public ResponseEntity<List<Map<String, Object>>> getListSysCode(@RequestBody Map<String, Object> param)
			throws Exception {

		List<Map<String, Object>> list = service.getListSysCode(param);

		return ResponseEntity.ok(list);
	}

	/* 월보 */
	@PostMapping("/getMonthTollFeeReport")
	public ResponseEntity<List<Map<String, Object>>> getWorkDoc(@RequestBody Map<String, Object> param)
			throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 통행량 (전체)
		list.add(service.getMonthTollFeeReportTrafficAll(param));
		// 통행량 (전체)-기타(수입)
		list.add(service.getMonthTollFeeReportTrafficAllExIn(param));
		// 통행량 (유료)
		list.add(service.getMonthTollFeeReportTrafficPremium(param));
		// 통행량 (기타차량 이용현황)
		list.add(service.getMonthTollFeeReportTrafficEtcCar(param));
		// 통행량 (연속통행 할인)
		list.add(service.getMonthTollFeeReportTrafficContinuous(param));
		// 통행량 (출퇴근 할인)
		list.add(service.getMonthTollFeeReportTrafficCommute(param));
		// 통행량 (면제)
		list.add(service.getMonthTollFeeReportTrafficExemption(param));

		// 세부면제 현황
		list.add(service.getMonthTollFeeReportExemptionDetail(param));
		// 하이패스 이용 현황
		list.add(service.getMonthTollFeeReporHiPassUse(param));

		// 통행료 (전체)
		list.add(service.getMonthTollFeeReportTollAll(param));
		// 통행료 (전체)- 기타(수입)
		list.add(service.getMonthTollFeeReportTollAllExIn(param));
		// 통행료 (연속통행 할인 금액)
		list.add(service.getMonthTollFeeReportTollContinuous(param));
		// 통행료 (출퇴근 할인 금액)
		list.add(service.getMonthTollFeeReportTollCommute(param));

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getMonthTollConstrunctionReport")
	public ResponseEntity<Map<String, List<Map<String, Object>>>> getMonthTollConstrunctionReport(
			@RequestBody Map<String, Object> param) throws Exception {
		Map<String, List<Map<String, Object>>> result = service.getMonthTollConstrunctionReport(param);

		return ResponseEntity.ok(result);
	}

	/* 일별사무실유형현황 조회 */
	@PostMapping("/getDailyViolationCorrectionSummary")
	public ResponseEntity<List<Map<String, Object>>> getDailyViolationCorrectionSummary(
			@RequestBody Map<String, Object> param) throws Exception {
		List<Map<String, Object>> list = service.getDailyViolationCorrectionSummary(param);
		return ResponseEntity.ok(list);
	}

	@PostMapping("/getJabicallPassList")
	public ResponseEntity<List<Map<String, Object>>> getJabicallPassList(@RequestBody Map<String, Object> param)
			throws Exception {

		List<Map<String, Object>> list = service.getJabicallPassList(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/getGdtcsPassList")
	public ResponseEntity<List<Map<String, Object>>> getGdtcsPassList(@RequestBody Map<String, Object> param)
			throws Exception {

		List<Map<String, Object>> list = service.getGdtcsPassList(param);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/addJabicallFile")
	public ResponseEntity<Map<String, Object>> addJabicallFile(@RequestBody Map<String, Object> param)
			throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("params", param);
		int result = service.addJabicallFile(params);

		Map<String, Object> resultMap = new HashMap<>();

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
				resultMap.put("ERROR_MSG", "추가하였습니다.");
				break;
		}

		return ResponseEntity.ok(resultMap);
	}
	
	@PostMapping("/getSmartTollOperationStatus")
	public ResponseEntity<Map<String, Object>> getBeforeAllInfo(@RequestBody Map<String, Object> param)
			throws Exception {

		Map<String, Object> resultMap = new HashMap<>();
		
		List<Map<String, Object>> beforeAfterAll = service.getAllInfo(param);
		List<Map<String, Object>> beforeExem = service.getBeforeExemInfo(param);
		List<Map<String, Object>> preReg = service.getPreRegisterInfo(param);
		List<Map<String, Object>> preRegNormal = service.getPreRegisterNormalInfo(param);
		List<Map<String, Object>> afterResult = service.getAfterResultInfo(param);
		List<Map<String, Object>> afterExem = service.getAfterExemInfo(param);
		
		resultMap.put("BEFORE_AFTER_ALL", beforeAfterAll);
		resultMap.put("BEFORE_EXEM", beforeExem);
		resultMap.put("PRE_REG", preReg);
		resultMap.put("PRE_REG_NORMAL", preRegNormal);
		resultMap.put("AFTER_RESULT", afterResult);
		resultMap.put("AFTER_EXEM", afterExem);
		
		return ResponseEntity.ok(resultMap);
	}
	
}

