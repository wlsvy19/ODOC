package gdtcs.maint.mapper;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;
import java.util.Map;

@Mapper("maintMapper")
public interface MaintMapper {
	
    List<Map<String, Object>> getEquipErrorList(Map<String, Object> param);
    List<Map<String, Object>> getEquipErrorCum(Map<String, Object> param);
    List<Map<String, Object>> getCarLineMonitor(Map<String, Object> param);
    List<Map<String, Object>> getTableSpaceMonitor(Map<String, Object> param);
    
}
