package gdtcs.sales.mapper;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

@Mapper("salesMapper")
public interface SalesMapper {
	List<Map<String, Object>> getTaxCompanyList(Map<String, Object> param);

	Map<String, Object> getTaxCompanyDetailList(Map<String, Object> param);

	List<Map<String, Object>> getTaxCompanyCardList(Map<String, Object> param);

	int insertTaxCompany(Map<String, Object> param);

	int updateTaxCompany(Map<String, Object> param);

	int deleteTaxCompany(Map<String, Object> param);

	int insertCompanyCard(Map<String, Object> param);

	int updateCompanyCard(Map<String, Object> param);

	int deleteCompanyCard(Map<String, Object> param);

	List<Map<String, Object>> getTaxbillPublishList(Map<String, Object> param);

	List<Map<String, Object>> getTaxbillPublishHist(Map<String, Object> param);

	int countCompany(Map<String, Object> param);

	int countCard(Map<String, Object> param);

	int countCompanyCard(Map<String, Object> param);

	List<Map<String, Object>> getDailyTrafficProcessDay(Map<String, Object> param);

	List<Map<String, Object>> getDailyTrafficProcessMonth(Map<String, Object> param);

	List<Map<String, Object>> getDailyTrafficProcessYear(Map<String, Object> param);

	List<Map<String, Object>> getDailyViolation(Map<String, Object> param);

	List<Map<String, Object>> selectReducedVehicleListEx(Map<String, Object> param);

	List<Map<String, Object>> selectReducedVehicleListBs(Map<String, Object> param);

	List<Map<String, Object>> selectDiscountList(Map<String, Object> param); /* 할인내역보고서-목록조회 */

	List<Map<String, Object>> selectDiscountSummary(Map<String, Object> param); /* 할인내역보고서-소계 */

	List<Map<String, Object>> selectDiscountSurchargeList(Map<String, Object> param); /* 할인/할증처리내역 */

	List<Map<String, Object>> selectDailyExemptDetail(Map<String, Object> param); /* 일일 면제 세부내역 */

	List<Map<String, Object>> getBusanContinueTrafficDCDayFin(Map<String, Object> param); /* 부산시 연속통행 할인 현황 - 일마감 */

	List<Map<String, Object>> getBusanContinueTrafficDCSum(Map<String, Object> param); /* 부산시 연속통행 할인 현황 - 집계 */

	List<Map<String, Object>> selectRefundList(Map<String, Object> param); /* 환불대상 조회(도로공사) */

	List<Map<String, Object>> selectRefundListBs(Map<String, Object> param); /* 환불대상 조회(부산시) */

	List<Map<String, Object>> selectDailyExemptSummaryEx(Map<String, Object> param); /* 일별 감면차량 이용현황 - 도공면제 */

	List<Map<String, Object>> selectDailyReductionSummaryEx(Map<String, Object> param); /* 일별 감면차량 이용현황 - 도공할인 */

	List<Map<String, Object>> selectDailyExemptSummaryBs(Map<String, Object> param); /* 일별 감면현황-부산시 */

	List<Map<String, Object>> selectSummaryByOBUType(Map<String, Object> param); /* OBU 타입별 처리내역 */

	List<Map<String, Object>> selectDailyTollTrendsDailyAvgContrast(Map<String, Object> param); /*
																								 * 통행료 징수관련 동향 - 전체 통행량
																								 * 증감상황
																								 */

	List<Map<String, Object>> selectDailyTollTrendsTollTraffic(Map<String, Object> param); /* 통행료 징수관련 동향 - 유형별 통행량 */

	List<Map<String, Object>> selectDailyTollTrendsTollSummary(Map<String, Object> param); /* 통행료 징수관련 동향 - 통행료 수입 */

	List<Map<String, Object>> selectDailyTollTrendsDailyAvgContrastQuater(Map<String, Object> param); /*
																										 * 통행료 징수관련 동향 -
																										 * 추이분석
																										 */

	List<Map<String, Object>> getBusanContinueTrafficDCRealTime(Map<String, Object> param); /* 부산시 연속통행 할인 현황 - 실시간 */

	List<Map<String, Object>> getBusanContinueTrafficDCRefund(Map<String, Object> param); /* 부산시 연속통행 할인 현황 - 환불심사 */

	List<Map<String, Object>> getBusanContinueTrafficDCTrfSum(Map<String, Object> param); /* 부산시 연속통행 할인 현황 - 교통량집계 */

	List<Map<String, Object>> getListSysCode(Map<String, Object> param);

	/* 월보 시작 */
	Map<String, Object> selectMonthTollFeeReportTrafficAll(Map<String, Object> param) throws Exception;

	Map<String, Object> selectMonthTollFeeReportTrafficAllExIn(Map<String, Object> param) throws Exception;

	Map<String, Object> selectMonthTollFeeReportTrafficPremium(Map<String, Object> param) throws Exception;

	Map<String, Object> selectMonthTollFeeReportTrafficEtcCar(Map<String, Object> param) throws Exception;

	Map<String, Object> selectMonthTollFeeReportTrafficContinuous(Map<String, Object> param) throws Exception;

	Map<String, Object> selectMonthTollFeeReportTrafficCommute(Map<String, Object> param) throws Exception;

	Map<String, Object> selectMonthTollFeeReportTrafficExemption(Map<String, Object> param) throws Exception;

	Map<String, Object> selectMonthTollFeeReportExemptionDetail(Map<String, Object> param) throws Exception;

	Map<String, Object> selectMonthTollFeeReporHiPassUse(Map<String, Object> param) throws Exception;

	Map<String, Object> selectMonthTollFeeReportTollAll(Map<String, Object> param) throws Exception;

	Map<String, Object> selectMonthTollFeeReportTollAllExIn(Map<String, Object> param) throws Exception;

	Map<String, Object> selectMonthTollFeeReportTollContinuous(Map<String, Object> param) throws Exception;

	Map<String, Object> selectMonthTollFeeReportTollCommute(Map<String, Object> param) throws Exception;
	/* 월보 끝 */

	List<Map<String, Object>> selectMonthTollConstrunctionReport(Map<String, Object> param);

	/* 일별사무실유형현황 조회 */
	List<Map<String, Object>> selectDailyViolationCorrectionSummary(Map<String, Object> param);

	List<Map<String, Object>> selectJabicallPassList(Map<String, Object> param);

	List<Map<String, Object>> selectGdtcsPassList(Map<String, Object> param);

	int updateJabicallFile(Map param);
	
	List<Map<String, Object>> selectAllInfo(Map<String, Object> param) throws Exception;

	List<Map<String, Object>> selectBeforeExemInfo(Map<String, Object> param) throws Exception;

	List<Map<String, Object>> selectPreRegisterInfo(Map<String, Object> param) throws Exception;

	List<Map<String, Object>> selectPreRegisterNormalInfo(Map<String, Object> param) throws Exception;

	List<Map<String, Object>> selectAfterResultInfo(Map<String, Object> param) throws Exception;
	
	List<Map<String, Object>> selectAfterExemInfo(Map<String, Object> param) throws Exception;

}
