package gdtcs.unpaid.service.impl;

import gdtcs.unpaid.mapper.UnPaidMapper;
import gdtcs.unpaid.service.UnPaidService;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("unPaidService")
public class UnPaidServiceImpl extends EgovAbstractServiceImpl implements UnPaidService {

    @Resource(name="unPaidMapper")
    private UnPaidMapper unPaidMapper;
    
    @Override
    public List<Map<String, Object>> getUnPaidCar(Map<String, Object> param) throws Exception {
        return unPaidMapper.getUnPaidCar(param);
    }

    @Override
    public List<Map<String, Object>> getUnPaidCollect(Map<String, Object> param) throws Exception {
        return unPaidMapper.getUnPaidCollect(param);
    }


    @Override
    public List<Map<String, Object>> getLineCtrlOfficePayList(Map<String, Object> param) throws Exception {
        return unPaidMapper.getLineCtrlOfficePayList(param);
    }
}
