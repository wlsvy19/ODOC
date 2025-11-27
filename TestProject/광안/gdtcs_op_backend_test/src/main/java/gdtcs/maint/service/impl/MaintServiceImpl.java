package gdtcs.maint.service.impl;

import gdtcs.maint.mapper.MaintMapper;
import gdtcs.maint.service.MaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Service("maintService")
public class MaintServiceImpl implements MaintService {

	 @Resource(name="maintMapper")
    private MaintMapper maintMapper;
    
    @Override
    public List<Map<String, Object>> getEquipErrorList(Map<String, Object> param) throws Exception {
    	return maintMapper.getEquipErrorList(param);
    }

    @Override
    public List<Map<String, Object>> getEquipErrorCum(Map<String, Object> param) throws Exception {
    
    	return maintMapper.getEquipErrorCum(param);
    }
    @Override
    public List<Map<String, Object>> getCarLineMonitor(Map<String, Object> param) throws Exception {
    
    	return maintMapper.getCarLineMonitor(param);
    }
    @Override
    public List<Map<String, Object>> getTableSpaceMonitor(Map<String, Object> param) throws Exception {
    
    	return maintMapper.getTableSpaceMonitor(param);
    }

}
