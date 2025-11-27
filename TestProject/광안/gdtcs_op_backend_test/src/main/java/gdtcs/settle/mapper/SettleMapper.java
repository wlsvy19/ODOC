package gdtcs.settle.mapper;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

@Mapper("settleMapper")
public interface SettleMapper {
	/* 환불이체내역-도로공사 */
	List<Map<String, Object>> getRefundRequestList(Map<String, Object> param);
	List<Map<String, Object>> getRefundResponseList(Map<String, Object> param);

	/* 환불 명세 조회 */
	List<Map<String, Object>> selectRefundDetailList(Map<String, Object> param);
	Map<String, Object> selectRefundInfoDetail(Map<String, Object> param);
	void updateRefundInfoBatch(Map<String, Object> param);
	
	/* 환불 명세 관리 - 목록조회 */
	List<Map<String, Object>> selectRefundManagementList(Map param);
	/* 환불 명세 관리 - 차량별 조회 */
	List<Map<String, Object>> selectRefundManagementDetailList(Map param);
}
