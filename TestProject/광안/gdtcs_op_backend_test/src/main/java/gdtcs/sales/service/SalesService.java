package gdtcs.sales.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public interface SalesService {
    List<Map<String, Object>> getTaxCompanyList(Map<String, Object> param) throws Exception;

    Map<String, Object> getTaxCompanyDetailList(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getTaxCompanyCardList(Map<String, Object> param) throws Exception;

    int addTaxCompany(Map<String, Object> param) throws Exception;

    int setTaxCompany(Map<String, Object> param) throws Exception;

    int delTaxCompany(Map<String, Object> param) throws Exception;

    int addCompanyCard(Map<String, Object> param) throws Exception;

    int setCompanyCard(Map<String, Object> param) throws Exception;

    int delCompanyCard(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getTaxbillPublishList(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getTaxbillPublishHist(Map<String, Object> param) throws Exception;

    void downloadTaxbill(List<Map<String, Object>> param, HttpServletResponse response) throws Exception;

    boolean isCompanyExist(Map<String, Object> param) throws Exception;

    boolean isCompanyCardExist(Map<String, Object> param) throws Exception;

    boolean hasCompanyCard(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getDailyTrafficProcessDay(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getDailyTrafficProcessMonth(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getDailyTrafficProcessYear(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getDailyViolation(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getReducedVehicleListEx(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getReducedVehicleListBs(Map<String, Object> param) throws Exception;

    Map<String, Object> getDiscountSummary(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getDiscountSurchargeList(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getDailyExemptDetail(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getBusanContinueTrafficDCDayFin(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getBusanContinueTrafficDCSum(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getRefundList(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getRefundListBs(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getDailyExemptSummaryEx(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getDailyReductionSummaryEx(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getDailyExemptSummaryBs(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getSummaryByOBUType(Map<String, Object> param) throws Exception;

    Map<String, Object> getDailyTollTrends(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getBusanContinueTrafficDCRealTime(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getBusanContinueTrafficDCRefund(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getBusanContinueTrafficDCTrfSum(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getListSysCode(Map<String, Object> param) throws Exception;

    /* 월보 시작 */
    Map<String, Object> getMonthTollFeeReportTrafficAll(Map<String, Object> param) throws Exception;

    Map<String, Object> getMonthTollFeeReportTrafficAllExIn(Map<String, Object> param) throws Exception;

    Map<String, Object> getMonthTollFeeReportTrafficPremium(Map<String, Object> param) throws Exception;

    Map<String, Object> getMonthTollFeeReportTrafficEtcCar(Map<String, Object> param) throws Exception;

    Map<String, Object> getMonthTollFeeReportTrafficContinuous(Map<String, Object> param) throws Exception;

    Map<String, Object> getMonthTollFeeReportTrafficCommute(Map<String, Object> param) throws Exception;

    Map<String, Object> getMonthTollFeeReportTrafficExemption(Map<String, Object> param) throws Exception;

    Map<String, Object> getMonthTollFeeReportExemptionDetail(Map<String, Object> param) throws Exception;

    Map<String, Object> getMonthTollFeeReporHiPassUse(Map<String, Object> param) throws Exception;

    Map<String, Object> getMonthTollFeeReportTollAll(Map<String, Object> param) throws Exception;

    Map<String, Object> getMonthTollFeeReportTollAllExIn(Map<String, Object> param) throws Exception;

    Map<String, Object> getMonthTollFeeReportTollContinuous(Map<String, Object> param) throws Exception;

    Map<String, Object> getMonthTollFeeReportTollCommute(Map<String, Object> param) throws Exception;
    /* 월보 끝 */

    Map<String, List<Map<String, Object>>> getMonthTollConstrunctionReport(Map<String, Object> param) throws Exception;

    /* 일별사무실유형현황 조회 */
    List<Map<String, Object>> getDailyViolationCorrectionSummary(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getJabicallPassList(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getGdtcsPassList(Map<String, Object> param) throws Exception;

    public int addJabicallFile(Map<String, Object> param) throws Exception;
    
    List<Map<String, Object>> getAllInfo(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getBeforeExemInfo(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getPreRegisterInfo(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getPreRegisterNormalInfo(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getAfterResultInfo(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getAfterExemInfo(Map<String, Object> param) throws Exception;

}
