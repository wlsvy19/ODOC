package gdtcs.base.mapper;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

@Mapper("baseMapper")
public interface BaseMapper {

    List<Map<String, Object>> selectMenuInfo(Map<String, Object> param)throws Exception;
    List<Map<String, Object>> selectSystemCodeList(Map param);
    int updateSystemLargeCode(Map param);
    int insertSystemLargeCode(Map param);
    int deleteSystemLargeCode(Map param);
    List<Map<String, Object>> selectSystemSmallCode(Map param);
    int updateSystemSmallCode(Map param);
    int insertSystemSmallCode(Map param);
    int deleteSystemSmallCode(Map param);
    int countLargeCode(Map param);
    int countSmallCode(Map param);
    int countLargeCodeChild(Map param);
    public int updateIcRevisionHistoryResult(Map param);
    List<Map<String, Object>> selectIcInformation(Map param);
    List<Map<String, Object>> selectRevisionHistory(Map param);
    List<Map<String, Object>> selectLaneRevisionStatus(Map param);
    List<Map<String, Object>> selectIcRevision(Map param);
    int updateIcRevisionBase(Map param); //기초정보
    int updateIcRevisionWorker(Map param); //근무자정보
    int updateIcRevisionFare(Map param); //요금정보
    int updateIcRevisionBlDiscount(Map param); //무효할인면제
    int updateIcRevisionHoliday(Map param); //휴일일자
    int updateIcRevisionCommute(Map param); //출퇴근할인
    int updateIcRevisionBlEcard(Map param); //무효전자카드
    int updateIcRevisionContinuousDiscount(Map param); //연속통행할인
    int updateIcRevisionBlTcard(Map param); //무효교통카드
    int updateIcRevisionHourDiscount(Map param); //시간별할인
    int updateIcRevisionBlObu(Map param); //무효OBU
    int updateIcRevisionHolidayException(Map param); //명절(특정일)면제
    int updateIcRevisionPlEcard(Map param); //전자카드면제
    int updateIcRevisionDiffFare(Map param); //차등요금제
    int insertIcRevisionHistory(Map param);
    List<Map<String, Object>> selectLaneList(Map param);
    int updateLane(Map param);
    List<Map<String, Object>> selectFareInfo(Map param);
    int updateFareInfo(Map param);
    int insertFareInfoHistory(Map param);
    List<Map<String, Object>> selectDiffFare(Map param);
    int updateDiffFare(Map param);
    int insertDiffFareHistory(Map param);
    List<Map<String, Object>> selectWorkerList(Map param);
    int isMatchPassword(Map param);
    List<Map<String, Object>> selectAvailableWorkerNo(Map param);
    int insertWorkerInfo(Map param);
    int updateWorkerPassword(Map param);
    int updateWorkerInfo(Map param);
    int updateWorkerResign(Map param);
    int updateWorkerProfileImage(Map param);
    int deleteWorker(Map param);
    int insertIcInformationHistory(Map param);
    int updateIcInformation(Map param);
    List<Map<String, Object>> selectProgramList(Map param);
    int updateProgram(Map param);
    List<Map<String, Object>> selectReportImageInformation(Map param);
    int mergeReportImageInformation(Map param);
    List<Map<String, Object>> selectContinuousPassIcList(Map param);
    List<Map<String, Object>> selectEcardDiscountCarType(Map param);
    int insertEcardDiscountCarTypeHistory(Map param);
    int updateEcardDiscountCarType(Map param);
    List<Map<String, Object>> selectEcardDiscount(Map param);
    int updateEcardDiscount(Map param);
    List<Map<String, Object>> selectHourDiscount(Map param);
    int insertHourDiscountHistory(Map param);
    int updateHourDiscount(Map param);
    List<Map<String, Object>> selectDiscountExtra(Map param);
    int insertDiscountExtraHistory(Map param);
    int updateDiscountExtra(Map param);
    List<Map<String, Object>> selectChargeExtra(Map param);
    int insertChargeExtraHistory(Map param);
    int updateChargeExtra(Map param);
    List<Map<String, Object>> selectEcardChargeLimit(Map param);
    int updateEcardChargeLimit(Map param);
    List<Map<String, Object>> selectObuDiscount(Map param);
    int insertObuDiscountHistory(Map param);
    int updateObuDiscount(Map param);
    List<Map<String, Object>> selectContinuousDiscount(Map param);
    List<Map<String, Object>> selectBaseDateForCalendar(Map param);
    List<Map<String, Object>> selectBaseDateInfo(Map param);
    List<Map<String, Object>> selectYearToDate(Map param);
    int updateBaseDate(Map param);
    int updateBaseHolyDay(Map param);
    int deleteBaseDate(Map param);
    int deleteBaseHolyDay(Map param);
    List<Map<String, Object>> selectSaveTermDate(Map param);
    List<Map<String, Object>> selectDateList(Map param);
    int updateDate(Map param);
    int mergeHolyday(Map param);
    int deleteHolyday(Map param);
    List<Map<String, Object>> selectVehicleManage(Map param);

    int checkVehicleExists(String carNo);

    void insertVehicleInfo(Map<String, String> carNo);

    int updateVehicleInfo(Map<String, String> carNo);

    int addVehicleInfo(Map<String, String> carNo);

    int deleteVehicleInfo(Map<String, String> param);

    int oneUpdateVehicleInfo(Map<String, String> param);
}
