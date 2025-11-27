package gdtcs.office.service;

import java.util.List;
import java.util.Map;

public interface OfficeService {

	public List<Map<String, Object>> getViolationList(Map<String, Object> param) throws Exception; /* 위반심사 조회 */

	public List<Map<String, Object>> getViolationHistList(Map<String, Object> param) throws Exception; /* 위반심사 조회 */
	
	public Map<String, Object> chkDayFin(List<Map<String, Object>> param) throws Exception; /* 일마감여부 확인 */
	
	public List<Map<String, Object>> getLcarPl(List<Map<String, Object>> param) throws Exception; /* 경차PL 목록 확인 */
	
	public Map<String, Object> setViolationAudit(Map<String, Object> param) throws Exception; /* 위반심사 단건처리 */
	
	public Map<String, Object> setViolationAuditBatch(List<Map<String, Object>> param) throws Exception; /* 위반심사 일괄처리 */
	
	public List<Map<String, Object>> getEcardBl(Map<String, Object> param) throws Exception; /* 위반심사-전자카드PL 조회 */
	
	public List<Map<String, Object>> getExemptPl(Map<String, Object> param) throws Exception; /* 위반심사-면제마스터PL 조회 */
	
	public List<Map<String, Object>> getMemberInfo(Map<String, Object> param) throws Exception; /* 위반심사-고객정보 조회 */

	public Map<String, Object> getLocationSearchResultDetail(Map<String, Object> param) throws Exception; /* 영상심사 - 내역조회 */
	
	public List<Map<String, Object>> getImageList(Map<String, Object> param) throws Exception; /* 영상심사 - 목록조회 */

	public Map<String, Object> getImageDetail(Map<String, Object> param) throws Exception; /* 영상심사 - 내역조회 */

	public List<Map<String, Object>> getImageHistList(Map<String, Object> param) throws Exception; /* 영상심사 - 심사이력 조회 */

	public Map<String, Object> setImageAudit(Map<String, Object> param) throws Exception; /* 영상심사 - 심사처리 */

	public List<Map<String, Object>> getImageCorrectionResultList(Map<String, Object> param) throws Exception; /* 영상심사 - 심사이력 조회 */

	public List<Map<String, Object>> getProcessList(Map<String, Object> param) throws Exception; /* 차량처리내역(영상) 조회 */

	public List<Map<String, Object>> getReductionExEcardList(Map<String, Object> param)
			throws Exception; /* 감면내역심사-도로공사(면제카드) - 목록조회 */

	public List<Map<String, Object>> getReductionExObuList(Map<String, Object> param)
			throws Exception; /* 감면내역심사-도로공사(단말기) - 목록조회 */

	public List<Map<String, Object>> getReductionExWelfareList(Map<String, Object> param)
			throws Exception; /* 감면내역심사-도로공사(통복) - 목록조회 */
	
	public Map<String, Object> setReductionExCorrection(Map<String, Object>param)
			throws Exception; /* 감면내역심사-도로공사(면제카드/단말기) - 심사처리 */

	public Map<String, Object> setReductionExCorrectionWelfare(Map<String, Object> param)
			throws Exception; /* 감면내역심사-도로공사(통복) - 심사처리 */

	public List<Map<String, Object>> getCheatReductionExList(Map<String, Object> param)
			throws Exception; /* 감면부정사용내역-도로공사 - 목록조회 */
	
	public List<Map<String, Object>> getReductionBsList(Map<String, Object> param) throws Exception; /* 감면내역심사-부산시 조회 */

	public Map<String, Object> getReductionBsDetail(Map<String, Object> param) throws Exception; /* 감면내역심사-부산시 조회 */

	public Map<String, Object> setReductionBsCorrection(Map<String, Object>param)
			throws Exception; /* 감면내역심사-부산시 심사처리 */

	public Map<String, Object> setReductionBsCorrectionTaxi(Map<String, Object>param)
			throws Exception; /* 감면내역심사-부산시 택시심사처리 */

	public Map<String, Object> setReductionBsCorrectionTaxiBatch(Map<String, Object>param)
			throws Exception; /* 감면내역심사-부산시 택시심사처리 */

	public List<Map<String, Object>> getCheatReductionBsList(Map<String, Object> param)
			throws Exception; /* 감면부정사용내역-부산시 조회 */

	public List<Map<String, Object>> getPayCorrectionList(Map<String, Object> param) throws Exception; /* 후붛보정내역 조회 */
	
	public List<Map<String, Object>> getPreRegistrationList(Map<String, Object> param) throws Exception; /* 사전등록처리내역 조회 */

	public List<Map<String, Object>> getEtcIncomeList(Map<String, Object> param) throws Exception; /* 기타수입관리 조회 */

	public List<Map<String, Object>> getWorkerList(Map<String, Object> param) throws Exception; /* 근무자목록 조회 */
	
	public int chkEtcIncome(Map<String, Object> param) throws Exception; /* 기타수입관리 중복데이터 확인 */

	public int addEtcIncome(Map<String, Object> param) throws Exception; /* 기타수입관리 추가 */

	public int delEtcIncome(List<Map<String, Object>> param) throws Exception; /* 기타수입관리 삭제 */
	
	public int setEtcIncome(Map<String, Object> param) throws Exception; /* 기타수입관리 삭제 */

	public List<Map<String, Object>> getWorkNo(Map<String, Object> param) throws Exception; /* 근무목록 조회 */

	public List<Map<String, Object>> getLaneNo(Map<String, Object> param) throws Exception; /* 차로목록 조회 */

	public List<Map<String, Object>> getViolationState(Map<String, Object> param) throws Exception; /* 위반차량 현황조회 */

    public List<Map<String, Object>> getCorrectionCount(Map<String, Object> param);
}
