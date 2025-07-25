package gdtcs.settle.service;

import java.util.List;
import java.util.Map;

public interface SettleService {
	List<Map<String, Object>> getRefundRequestList(Map<String, Object> param) throws Exception;
	List<Map<String, Object>> getRefundResponseList (Map<String, Object> param) throws Exception;

	/* 환불 명세 조회*/
	List<Map<String, Object>> getRefundDetailList (Map<String, Object> param) throws Exception;
	Map<String, Object> getRefundInfoDetail (Map<String, Object> param) throws Exception;
	public Map<String, Object> setRefundInfoBatch(Map<String, Object> param) throws Exception;

	/* 환불 명세 관리 - 목록조회 */
	public List<Map<String, Object>> getRefundManagementList(Map param) throws Exception;
	/* 환불 명세 관리 - 차량별 조회 */
	public List<Map<String, Object>> getRefundManagementDetailList(Map param) throws Exception;
}
