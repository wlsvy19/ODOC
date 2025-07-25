package gdtcs.maint.service;

import java.util.List;
import java.util.Map;

public interface MaintService {

    List<Map<String, Object>> getEquipErrorList(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getEquipErrorCum(Map<String, Object> param)throws Exception;
    List<Map<String, Object>> getTableSpaceMonitor(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getCarLineMonitor(Map<String, Object> param)throws Exception;

}
