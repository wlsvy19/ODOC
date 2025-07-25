package gdtcs.office.mapper;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

@Mapper("officeMapper")
public interface OfficeMapper {
	int updateTablePrimHandcarno(Map<String, Object> param);	/* 심사처리 - PRIM_HANDCARNO 처리차량번호, 비고 업데이트 */
	int selectTableProcDayfininfo(Map<String, Object> param); 		/* 일마감 체크 - 0: 마감안됨 */
	
	List<Map<String, Object>> selectViolationList(Map<String, Object> param);	/* 위반심사 조회 */
	List<Map<String, Object>> selectViolationHistList(Map<String, Object> param);	/* 위반심사이력 조회 */
	
	Map<String, Object> checkDayFin(List<Map<String, Object>> param);		/* 일마감 처리 여부 확인 */
	List<Map<String, Object>> selectLcarPl(List<Map<String, Object>> param);		/* 경차PL 목록조회 */
	void updateViolationAudit(Map<String, Object> param);		/* 위반심사처리 */
	List<Map<String, Object>> selectEcardBl(Map<String, Object> param);		/* 위반심사-전자카드PL 조회 */
	List<Map<String, Object>> selectExemptPl(Map<String, Object> param);	/* 위반심사-면제마스터PL 조회 */
	List<Map<String, Object>> selectMemberInfo(Map<String, Object> param);	/* 위반심사-고객정보 조회 */
	Map<String, Object> selectLocationSearchResultDetail(Map<String, Object> param); 		/* 위반심사 - 위치정보조회(단 건) */
	int updateLocationCorrectionBs (Map<String, Object> param); 							/* 위반심사 - 위치정보처리부산 */

	List<Map<String, Object>> selectImageList(Map<String, Object> param);					/* 영상심사 - 목록조회 */
	Map<String, Object> selectImageDetail(Map<String, Object> param);						/* 영상심사 - 내역조회(단 건) */
	List<Map<String, Object>> selectImageHistList(Map<String, Object> param);				/* 영상심사 - 심사이력 조회 */
	int insertImageHist (Map<String, Object> param); 										/* 영상심사 - 내역저장 */
	void updateImageAudit(Map<String, Object> param); 										/* 영상심사 - 심사처리 */
	List<Map<String, Object>> selectImageCorrectionResultList(Map<String, Object> param); 	/* 영상심사 - 심사결과 조회*/
	
	List<Map<String, Object>> selectProcessList(Map<String, Object> param); 			/* 차량처리내역(영상) 조회 */
	
	List<Map<String, Object>> selectReductionExEcardList(Map<String, Object> param); 	/* 감면내역심사-도로공사(면제카드) - 목록조회 */
	List<Map<String, Object>> selectReductionExObuList(Map<String, Object> param); 		/* 감면내역심사-도로공사(단말기) - 목록조회 */
	List<Map<String, Object>> selectReductionExWelfareList(Map<String, Object> param); 	/* 감면내역심사-도로공사(통복) - 목록조회 */
	void updateReductionExCorrection(Map<String, Object> param); 						/* 감면내역심사-도로공사(면제카드/단말기) - 심사처리 */
	void updateReductionExCorrectionWelfare(Map<String, Object> param); 				/* 감면내역심사-도로공사(통복) - 심사처리 */
	
	List<Map<String, Object>> selectCheatReductionExList(Map<String, Object> param);	/* 감면부정사용내역-도로공사 조회 */
	
	List<Map<String, Object>> selectReductionBsList(Map<String, Object> param);			/* 감면내역심사-부산시 목록조회 */
	Map<String, Object> selectReductionBsDetail(Map<String, Object> param);				/* 감면내역심사-부산시 내역조회(단 건) */
	void updateReductionBsCorrection(Map<String, Object> param); 						/* 감면내역심사-부산시 심사처리 */
	int insertReductionBsAdditionalProcessTaxi(Map<String, Object> param); 				/* 감면내역심사-부산시 심사처리(택시 추가처리) */
	int deleteReductionBsAdditionalProcessTaxi(Map<String, Object> param); 				/* 감면내역심사-부산시 심사처리(택시 추가처리) */
	
	List<Map<String, Object>> selectCheatReductionBsList(Map<String, Object> param);	/* 감면부정사용내역-부산시 조회 */
	
	List<Map<String, Object>> selectPayCorrectionList(Map<String, Object> param);	/* 후불보정내역 조회 */
	
	List<Map<String, Object>> selectPreRegistrationList(Map<String, Object> param);	/* 사전등록처리내역 조회 */
	
	List<Map<String, Object>> selectEtcIncomeList(Map<String, Object> param);	/* 기타수입관리 조회 */
	List<Map<String, Object>> selectWorkerList(Map<String, Object> param);		/*근무자목록 조회*/
	int checkEtcIncome(Map<String, Object> param);
	int insertEtcIncome(Map<String, Object> param);
	int deleteEtcIncome(List<Map<String, Object>> param);
	int updateEtcIncome(Map<String, Object> param);
	
	List<Map<String, Object>> selectWorkNo(Map<String, Object> param);		/*근무목록 조회*/
	List<Map<String, Object>> selectLaneNo(Map<String, Object> param);		/*차로목록 조회*/

	List<Map<String, Object>> selectViolationState(Map<String, Object> param);			/* 위반차량 현황조회 */

	List<Map<String, Object>> selectCorrectionCount(Map<String, Object> param);
}
