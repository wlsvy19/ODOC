package gdtcs.sales.service.impl;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.apache.poi.xssf.usermodel.*;

import gdtcs.sales.mapper.SalesMapper;
import gdtcs.sales.service.SalesService;

@Service("SalesService")
public class SalesServiceImpl implements SalesService {

    @Resource(name = "salesMapper")
    private SalesMapper salesMapper;

    @Override
    public List<Map<String, Object>> getTaxCompanyList(Map<String, Object> param) {
        return salesMapper.getTaxCompanyList(param);
    }

    @Override
    public Map<String, Object> getTaxCompanyDetailList(Map<String, Object> param) {
        return salesMapper.getTaxCompanyDetailList(param);
    }

    @Override
    public List<Map<String, Object>> getTaxCompanyCardList(Map<String, Object> param) {
        return salesMapper.getTaxCompanyCardList(param);
    }

    @Override
    public int addTaxCompany(Map<String, Object> param) {
        if (isCompanyExist(param))
            return -2;
        return salesMapper.insertTaxCompany(param);
    }

    public int setTaxCompany(Map<String, Object> param) {
        return salesMapper.updateTaxCompany(param);
    }

    @Override
    public int delTaxCompany(Map<String, Object> param) {
        if (hasCompanyCard(param))
            return -2;

        return salesMapper.deleteTaxCompany(param);
    }

    @Override
    public int addCompanyCard(Map<String, Object> param) {
        if (isCompanyCardExist(param))
            return -2;
        return salesMapper.insertCompanyCard(param);
    }

    public int setCompanyCard(Map<String, Object> param) {
        return salesMapper.updateCompanyCard(param);
    }

    @Override
    public int delCompanyCard(Map<String, Object> param) {
        return salesMapper.deleteCompanyCard(param);
    }

    @Override
    public List<Map<String, Object>> getTaxbillPublishList(Map<String, Object> param) {
        return salesMapper.getTaxbillPublishList(param);
    }

    @Override
    public List<Map<String, Object>> getTaxbillPublishHist(Map<String, Object> param) {
        return salesMapper.getTaxbillPublishHist(param);
    }

    @Override
    public void downloadTaxbill(List<Map<String, Object>> param, HttpServletResponse response) throws Exception {
        FileInputStream taxbillSample = new FileInputStream("src/main/resources/file/taxbill.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook(taxbillSample);
        XSSFSheet sheet = workbook.getSheetAt(0);

        for (int i = 0; i < param.size(); i++) {
            XSSFRow row = sheet.getRow(i + 6);
            // TODO 날짜 받아오는거 수정하면 여기도 수정
            row.getCell(1).setCellValue("날짜yyyymmdd");
            row.getCell(2).setCellValue(String.valueOf(param.get(i).get("CO_NO")));
            row.getCell(4).setCellValue(String.valueOf(param.get(i).get("CO_NM1")));
            row.getCell(5).setCellValue(String.valueOf(param.get(i).get("REPR_NM")));
            row.getCell(9).setCellValue(String.valueOf(param.get(i).get("EMAIL")));
            row.getCell(11).setCellValue(String.valueOf(param.get(i).get("SUP_PRICE")));
            row.getCell(12).setCellValue(String.valueOf(param.get(i).get("VAT")));
            row.getCell(14).setCellValue("일자2자리");
            row.getCell(15).setCellValue("전자카드");
            row.getCell(17).setCellValue(String.valueOf(param.get(i).get("COUNT")));
            row.getCell(19).setCellValue(String.valueOf(param.get(i).get("SUP_PRICE")));
            row.getCell(20).setCellValue(String.valueOf(param.get(i).get("VAT")));
            row.getCell(51).setCellValue("01");
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @Override
    public boolean isCompanyExist(Map<String, Object> param) {
        int count = salesMapper.countCompany(param);
        if (count > 0)
            return true;
        return false;
    }

    @Override
    public boolean isCompanyCardExist(Map<String, Object> param) {
        int count = salesMapper.countCard(param);
        if (count > 0)
            return true;
        return false;
    }

    @Override
    public boolean hasCompanyCard(Map<String, Object> param) {
        int count = salesMapper.countCompanyCard(param);
        if (count > 0)
            return true;
        return false;
    }

    @Override
    public List<Map<String, Object>> getDailyTrafficProcessDay(Map<String, Object> param) throws Exception {
        return salesMapper.getDailyTrafficProcessDay(param);
    }

    @Override
    public List<Map<String, Object>> getDailyTrafficProcessMonth(Map<String, Object> param) throws Exception {
        return salesMapper.getDailyTrafficProcessMonth(param);
    }

    @Override
    public List<Map<String, Object>> getDailyTrafficProcessYear(Map<String, Object> param) throws Exception {
        return salesMapper.getDailyTrafficProcessYear(param);
    }

    @Override
    public List<Map<String, Object>> getDailyViolation(Map<String, Object> param) throws Exception {
        return salesMapper.getDailyViolation(param);
    }

    @Override
    public List<Map<String, Object>> getReducedVehicleListEx(Map<String, Object> param) throws Exception {
        return salesMapper.selectReducedVehicleListEx(param);
    }

    @Override
    public List<Map<String, Object>> getReducedVehicleListBs(Map<String, Object> param) throws Exception {
        return salesMapper.selectReducedVehicleListBs(param);
    }

    @Override
    public Map<String, Object> getDiscountSummary(Map<String, Object> param) throws Exception {
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("discountList", salesMapper.selectDiscountList(param));
        res.put("discountSummary", salesMapper.selectDiscountSummary(param));
        return res;
    }

    @Override
    public List<Map<String, Object>> getDiscountSurchargeList(Map<String, Object> param) throws Exception {
        return salesMapper.selectDiscountSurchargeList(param);
    }

    @Override
    public List<Map<String, Object>> getDailyExemptDetail(Map<String, Object> param) throws Exception {
        return salesMapper.selectDailyExemptDetail(param);
    }

    @Override
    public List<Map<String, Object>> getBusanContinueTrafficDCDayFin(Map<String, Object> param) throws Exception {
        return salesMapper.getBusanContinueTrafficDCDayFin(param);
    }

    @Override
    public List<Map<String, Object>> getBusanContinueTrafficDCSum(Map<String, Object> param) throws Exception {
        return salesMapper.getBusanContinueTrafficDCSum(param);
    }

    @Override
    public List<Map<String, Object>> getRefundList(Map<String, Object> param) throws Exception {
        return salesMapper.selectRefundList(param);
    }

    @Override
    public List<Map<String, Object>> getRefundListBs(Map<String, Object> param) throws Exception {
        return salesMapper.selectRefundListBs(param);
    }

    @Override
    public List<Map<String, Object>> getDailyExemptSummaryEx(Map<String, Object> param) throws Exception {
        return salesMapper.selectDailyExemptSummaryEx(param);
    }

    @Override
    public List<Map<String, Object>> getDailyReductionSummaryEx(Map<String, Object> param) throws Exception {
        return salesMapper.selectDailyReductionSummaryEx(param);
    }

    @Override
    public List<Map<String, Object>> getDailyExemptSummaryBs(Map<String, Object> param) throws Exception {
        return salesMapper.selectDailyExemptSummaryBs(param);
    }

    @Override
    public List<Map<String, Object>> getSummaryByOBUType(Map<String, Object> param) throws Exception {
        return salesMapper.selectSummaryByOBUType(param);
    }

    @Override
    public Map<String, Object> getDailyTollTrends(Map<String, Object> param) {
        Map<String, Object> res = new HashMap<String, Object>();
        // 1. 전체 통행량 증감상황
        res.put("DailyAvgContrast", salesMapper.selectDailyTollTrendsDailyAvgContrast(param));
        // 2. 유형별 통행량
        res.put("TollTraffic", salesMapper.selectDailyTollTrendsTollTraffic(param));
        // 3. 통행료 수입
        res.put("TollSummary", salesMapper.selectDailyTollTrendsTollSummary(param));
        // 4. 추이분석
        res.put("DailyAvgContrastQuater", salesMapper.selectDailyTollTrendsDailyAvgContrastQuater(param));
        return res;
    }

    @Override
    public List<Map<String, Object>> getBusanContinueTrafficDCRealTime(Map<String, Object> param) throws Exception {
        return salesMapper.getBusanContinueTrafficDCRealTime(param);
    }

    @Override
    public List<Map<String, Object>> getBusanContinueTrafficDCRefund(Map<String, Object> param) throws Exception {
        return salesMapper.getBusanContinueTrafficDCRefund(param);
    }

    @Override
    public List<Map<String, Object>> getBusanContinueTrafficDCTrfSum(Map<String, Object> param) throws Exception {
        return salesMapper.getBusanContinueTrafficDCTrfSum(param);
    }

    @Override
    public List<Map<String, Object>> getListSysCode(Map<String, Object> param) throws Exception {
        return salesMapper.getListSysCode(param);
    }

    /* 월보 시작 */
    @Override
    public Map<String, Object> getMonthTollFeeReportTrafficAll(Map<String, Object> param) throws Exception {
        return salesMapper.selectMonthTollFeeReportTrafficAll(param);
    }

    @Override
    public Map<String, Object> getMonthTollFeeReportTrafficAllExIn(Map<String, Object> param) throws Exception {
        return salesMapper.selectMonthTollFeeReportTrafficAllExIn(param);
    }

    @Override
    public Map<String, Object> getMonthTollFeeReportTrafficPremium(Map<String, Object> param) throws Exception {
        return salesMapper.selectMonthTollFeeReportTrafficPremium(param);
    }

    @Override
    public Map<String, Object> getMonthTollFeeReportTrafficEtcCar(Map<String, Object> param) throws Exception {
        return salesMapper.selectMonthTollFeeReportTrafficEtcCar(param);
    }

    @Override
    public Map<String, Object> getMonthTollFeeReportTrafficContinuous(Map<String, Object> param) throws Exception {
        return salesMapper.selectMonthTollFeeReportTrafficContinuous(param);
    }

    @Override
    public Map<String, Object> getMonthTollFeeReportTrafficCommute(Map<String, Object> param) throws Exception {
        return salesMapper.selectMonthTollFeeReportTrafficCommute(param);
    }

    @Override
    public Map<String, Object> getMonthTollFeeReportTrafficExemption(Map<String, Object> param) throws Exception {
        return salesMapper.selectMonthTollFeeReportTrafficExemption(param);
    }

    @Override
    public Map<String, Object> getMonthTollFeeReportExemptionDetail(Map<String, Object> param) throws Exception {
        return salesMapper.selectMonthTollFeeReportExemptionDetail(param);
    }

    @Override
    public Map<String, Object> getMonthTollFeeReporHiPassUse(Map<String, Object> param) throws Exception {
        return salesMapper.selectMonthTollFeeReporHiPassUse(param);
    }

    @Override
    public Map<String, Object> getMonthTollFeeReportTollAll(Map<String, Object> param) throws Exception {
        return salesMapper.selectMonthTollFeeReportTollAll(param);
    }

    @Override
    public Map<String, Object> getMonthTollFeeReportTollAllExIn(Map<String, Object> param) throws Exception {
        return salesMapper.selectMonthTollFeeReportTollAllExIn(param);
    }

    @Override
    public Map<String, Object> getMonthTollFeeReportTollContinuous(Map<String, Object> param) throws Exception {
        return salesMapper.selectMonthTollFeeReportTollContinuous(param);
    }

    @Override
    public Map<String, Object> getMonthTollFeeReportTollCommute(Map<String, Object> param) throws Exception {
        return salesMapper.selectMonthTollFeeReportTollCommute(param);
    }
    /* 월보 끝 */

    @Override
    public Map<String, List<Map<String, Object>>> getMonthTollConstrunctionReport(Map<String, Object> param) {
        Map<String, List<Map<String, Object>>> result = new HashMap<>();

        String month = param.get("WORK_DATE").toString().substring(4, 6);
        String year = param.get("WORK_DATE").toString().substring(0, 4);

        param.put("MONTH", param.get("WORK_DATE"));
        result.put("month", salesMapper.selectMonthTollConstrunctionReport(param));

        param.put("LASTYEAR", (Integer.valueOf(year) - 1) + month);
        result.put("lastYear", salesMapper.selectMonthTollConstrunctionReport(param));

        param.put("SUM", year);
        result.put("sum", salesMapper.selectMonthTollConstrunctionReport(param));

        return result;
    }

    /* 일별사무실유형현황 조회 */
    @Override
    public List<Map<String, Object>> getDailyViolationCorrectionSummary(Map<String, Object> param) throws Exception {
        return salesMapper.selectDailyViolationCorrectionSummary(param);
    }

    @Override
    public List<Map<String, Object>> getJabicallPassList(Map<String, Object> param) throws Exception {
        return salesMapper.selectJabicallPassList(param);
    }

    @Override
    public List<Map<String, Object>> getGdtcsPassList(Map<String, Object> param) throws Exception {
        return salesMapper.selectGdtcsPassList(param);
    }

    @Override
    public int addJabicallFile(Map<String, Object> param) throws Exception {
        return salesMapper.updateJabicallFile(param);
    }
    @Override
    public List<Map<String, Object>> getAllInfo(Map<String, Object> param) throws Exception {
        return salesMapper.selectAllInfo(param);
    }
    @Override
    public List<Map<String, Object>> getBeforeExemInfo(Map<String, Object> param) throws Exception {
        return salesMapper.selectBeforeExemInfo(param);
    }
    @Override
    public List<Map<String, Object>> getPreRegisterInfo(Map<String, Object> param) throws Exception {
        return salesMapper.selectPreRegisterInfo(param);
    }
    @Override
    public List<Map<String, Object>> getPreRegisterNormalInfo(Map<String, Object> param) throws Exception {
        return salesMapper.selectPreRegisterNormalInfo(param);
    }
    @Override
    public List<Map<String, Object>> getAfterResultInfo(Map<String, Object> param) throws Exception {
        return salesMapper.selectAfterResultInfo(param);
    }
    @Override
    public List<Map<String, Object>> getAfterExemInfo(Map<String, Object> param) throws Exception {
        return salesMapper.selectAfterExemInfo(param);
    }
    
}
