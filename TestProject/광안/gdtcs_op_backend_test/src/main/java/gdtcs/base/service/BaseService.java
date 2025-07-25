package gdtcs.base.service;

import java.util.List;
import java.util.Map;

public interface BaseService {

    public List<Map<String, Object>> getMenuInfo(Map<String, Object> param) throws Exception;
    public List<Map<String, Object>> getSystemCodeList(Map param) throws Exception;
    public int setSystemLargeCode(Map param) throws Exception;
    public int addSystemLargeCode(Map param) throws Exception;
    public int delSystemLargeCode(Map param) throws Exception;
    public List<Map<String, Object>> getSystemSmallCode(Map param) throws Exception;
    public int setSystemSmallCode(Map param) throws Exception;
    public int addSystemSmallCode(Map param) throws Exception;
    public int delSystemSmallCode(Map param) throws Exception;
    public boolean isLargeCodeExist(Map param) throws Exception;
    public boolean isSmallCodeExist(Map param) throws Exception;
    public boolean hasLargeCodeChild(Map param) throws Exception;
    public int requestRevision(Map param) throws Exception;
    public List<Map<String, Object>> getIcInformation(Map param) throws Exception;
    public List<Map<String, Object>> getRevisionHistory(Map param) throws Exception;
    public List<Map<String, Object>> getLaneRevisionStatus(Map param) throws Exception;
    public List<Map<String, Object>> getIcRevision(Map param) throws Exception;
    public int setIcRevisionBase(Map param) throws Exception; //기초정보
    public int setIcRevisionWorker(Map param) throws Exception; //근무자정보
    public int setIcRevisionFare(Map param) throws Exception; //요금정보
    public int setIcRevisionBlDiscount(Map param) throws Exception; //무효할인면제
    public int setIcRevisionHoliday(Map param) throws Exception; //휴일일자
    public int setIcRevisionCommute(Map param) throws Exception; //출퇴근할인
    public int setIcRevisionBlEcard(Map param) throws Exception; //무효전자카드
    public int setIcRevisionContinuousDiscount(Map param) throws Exception; //연속통행할인
    public int setIcRevisionBlTcard(Map param) throws Exception; //무효교통카드
    public int setIcRevisionHourDiscount(Map param) throws Exception; //시간별할인
    public int setIcRevisionBlObu(Map param) throws Exception; //무효OBU
    public int setIcRevisionHolidayException(Map param) throws Exception; //명절(특정일)면제
    public int setIcRevisionPlEcard(Map param) throws Exception; //전자카드면제
    public int setIcRevisionDiffFare(Map param) throws Exception; //차등요금제
    public int addIcRevisionHistory(Map param) throws Exception;
    public int setIcRevisionHistoryResult(Map param) throws Exception;
    public List<Map<String, Object>> getLaneList(Map param) throws Exception;
    public int setLane(Map param) throws Exception;
    public List<Map<String, Object>> getFareInfo(Map param) throws Exception;
    public int setFareInfo(Map param) throws Exception;
    public int addFareInfoHistory(Map param) throws Exception;
    public List<Map<String, Object>> getDiffFare(Map param) throws Exception;
    public int setDiffFare(Map param) throws Exception;
    public int addDiffFareHistory(Map param) throws Exception;
    public List<Map<String, Object>> getWorkerList(Map param) throws Exception;
    public List<Map<String, Object>> getAvailableWorkerNo(Map param) throws Exception;
    public int addWorkerInfo(Map param) throws Exception;
    public int setWorkerPassword(Map param) throws Exception;
    public int setWorkerInfo(Map param) throws Exception;
    public int setWorkerResign(Map param) throws Exception;
    public int setWorkerProfileImage(Map param) throws Exception;
    public int removeWorker(Map param) throws Exception;
    public int addIcInformationHistory(Map param) throws Exception;
    public int setIcInformation(Map param) throws Exception;
    public List<Map<String, Object>> getProgramList(Map param) throws Exception;
    public int setProgram(Map param) throws Exception;
    public List<Map<String, Object>> getReportImageInformation(Map param) throws Exception;
    public int setReportImageInformation(Map param) throws Exception;
    public List<Map<String, Object>> getContinuousPassIcList(Map param) throws Exception;
    public List<Map<String, Object>> getEcardDiscountCarType(Map param) throws Exception;
    public int addEcardDiscountCarTypeHistory(Map param) throws Exception;
    public int setEcardDiscountCarType(Map param) throws Exception;
    public List<Map<String, Object>> getEcardDiscount(Map param) throws Exception;
    public int setEcardDiscount(Map param) throws Exception;
    public List<Map<String, Object>> getHourDiscount(Map param) throws Exception;
    public int addHourDiscountHistory(Map param) throws Exception;
    public int setHourDiscount(Map param) throws Exception;
    public List<Map<String, Object>> getDiscountExtra(Map param) throws Exception;
    public int addDiscountExtraHistory(Map param) throws Exception;
    public int setDiscountExtra(Map param) throws Exception;
    public List<Map<String, Object>> getChargeExtra(Map param) throws Exception;
    public int addChargeExtraHistory(Map param) throws Exception;
    public int setChargeExtra(Map param) throws Exception;
    public List<Map<String, Object>> getEcardChargeLimit(Map param) throws Exception;
    public int setEcardChargeLimit(Map param) throws Exception;
    public List<Map<String, Object>> getObuDiscount(Map param) throws Exception;
    public int addObuDiscountHistory(Map param) throws Exception;
    public int setObuDiscount(Map param) throws Exception;
    public List<Map<String, Object>> getContinuousDiscount(Map param) throws Exception;
    public List<Map<String, Object>> getBaseDateForCalendar(Map param) throws Exception;
    public List<Map<String, Object>> getSaveTermDate(Map param) throws Exception;
    public List<Map<String, Object>> getBaseDateInfo(Map param) throws Exception;
    public List<Map<String, Object>> getYearToDate(Map param) throws Exception;
    public int saveBaseDate(Map param) throws Exception;
    public int removeBaseDate(Map param) throws Exception;
    public int createDateList(Map param) throws Exception;
    public List<Map<String, Object>> getDateList(Map param) throws Exception;
    public int setDate(Map param) throws Exception;
    public List<Map<String, Object>> getVehicleManage(Map param) throws Exception;

    // 차적DB 엑셀
    String processVehicleData(List<Map<String, String>> vehicleDataList) throws Exception;

    // 차적DB 단건 추가
    String addVehicleInfo(Map<String, String> param);

    // 차적DB 단건 삭제
    String deleteVehicleInfo(Map<String, String> param);

    // 차적DB 단건 차종 수정
    String oneUpdateVehicleInfo(Map<String, String> param);
}
