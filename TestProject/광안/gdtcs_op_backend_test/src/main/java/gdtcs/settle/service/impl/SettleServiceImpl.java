package gdtcs.settle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import gdtcs.settle.service.SettleService;
import gdtcs.settle.mapper.SettleMapper;

@Service("SettleService")
public class SettleServiceImpl extends EgovAbstractServiceImpl implements SettleService {

    @Resource(name = "settleMapper")
    private SettleMapper settleMapper;

    @Override
    public List<Map<String, Object>> getRefundRequestList(Map<String, Object> param){
    	return settleMapper.getRefundRequestList(param);
    }
    @Override
    public List<Map<String, Object>> getRefundResponseList(Map<String, Object> param){
    	return settleMapper.getRefundResponseList(param);
    }
    
    /* 환불 명세 조회 - 목록조회 */
    @Override
    public List<Map<String, Object>> getRefundDetailList(Map<String, Object> param){
    	return settleMapper.selectRefundDetailList(param);
    }

    /* 환불 명세 조회 - 상세조회 */
	@Override
	public Map<String, Object> getRefundInfoDetail(Map<String, Object> param) throws Exception {
		return settleMapper.selectRefundInfoDetail(param);
	}

    /* 환불 명세 조회 - 저장처리 */
	@Override
	public Map<String, Object> setRefundInfoBatch(Map<String, Object> param) throws Exception {
		Map<String, Object> res = new HashMap<String, Object>();
		settleMapper.updateRefundInfoBatch(param);
		int successCount = (int) param.get("successCount");
		res.put("successCount", successCount);
		return res;
	}
	
	/* 환불 명세 관리 - 목록조회 */
    @Override
    public List<Map<String, Object>> getRefundManagementList(Map param) throws Exception{
        return settleMapper.selectRefundManagementList(param);
    }
    
    /* 환불 명세 관리 - 차량별 조회 */
    @SuppressWarnings("unchecked")
	@Override
    public List<Map<String, Object>> getRefundManagementDetailList(Map param) throws Exception{
        return settleMapper.selectRefundManagementDetailList(param);
    }
}
